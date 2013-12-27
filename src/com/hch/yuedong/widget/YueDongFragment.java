package com.hch.yuedong.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.Gson;
import com.hch.yuedong.R;
import com.hch.yuedong.entity.CurrentInfo;
import com.hch.yuedong.entity.Music;
import com.hch.yuedong.manage.CurrentInfoManage;
import com.hch.yuedong.manage.MusicDB;
import com.hch.yuedong.service.PlayService;
import com.hch.yuedong.ui.MyApplication;
import com.hch.yuedong.util.FontUtil;

public class YueDongFragment extends SherlockFragment implements
		OnClickListener {
	private static final String TAG = "YueDongFragment";
	public static final int SINGLE_CYCLE = 0;
	public static final int SEQUENCE_PLAY = 1;
	public static final int RANDOM_PLAY = 2;

	ImageButton ib_set_as_favor = null;
	ImageView iv_play = null;
	ImageView iv_play_next = null;
	ImageView iv_play_last = null;
	ImageView iv_play_mode = null;
	ImageView iv_voice = null;
	TextView tv_music_name = null;
	TextView tv_artist = null;
	TextView tv_album = null;
	TextView tv_duration = null;
	TextView tv_current_time = null;
	SeekBar skb_progress = null;
	

	Context context = null;
	MediaPlayer mediaPlayer = null;
	Intent serviceIntent = null;
	PlayReciever receiver = null;

	ArrayList<Music> list = new ArrayList<Music>();
	// 默认播放模式为顺序播放
	private int play_mode = 1;
	// 默认播放列表中的第一首歌
	private Music currentMusic = null;
	private boolean playing = false;
	private int currentPlayTime=0;
	private int currentDuration = 0;
	private Timer timer;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.main_tab_yuedong,
				container, false);
		initData(contextView);
		initView(contextView);
		initViewData();
		Log.i(TAG, "onCreateView");
		return contextView;
	}

	@Override
	public void onPause() {
		super.onPause();
		// 将当前音乐的信息记录到sp中去，因为用户可能就直接退出了
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(timer!=null)
		timer.cancel();
	}
	
	public void initData(View contextView){
		context = contextView.getContext();
		mediaPlayer = new MediaPlayer();
		list = MusicDB.scanAllAudioFiles(contextView.getContext());
		currentMusic = list.get(0);
		receiver = new PlayReciever();
		//注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(PlayService.MUSIC_COMPLETED);
		this.getActivity().registerReceiver(receiver, filter);
	}

	public void initView(View view) {
		ib_set_as_favor = (ImageButton) view
				.findViewById(R.id.yuedong_ib_set_as_favor);
		iv_play = (ImageView) view.findViewById(R.id.yuedong_iv_play);
		iv_play_next = (ImageView) view.findViewById(R.id.yuedong_iv_play_next);
		iv_play_last = (ImageView) view.findViewById(R.id.yuedong_iv_play_last);
		iv_voice = (ImageView) view.findViewById(R.id.yuedong_iv_voice);
		iv_play_mode = (ImageView) view.findViewById(R.id.yuedong_iv_play_mode);
		tv_music_name = (TextView) view
				.findViewById(R.id.yuedong_tv_music_name);
		tv_artist = (TextView) view.findViewById(R.id.yuedong_tv_artist);
		tv_album = (TextView) view.findViewById(R.id.yuedong_tv_album);
		tv_duration = (TextView) view.findViewById(R.id.yuedong_tv_duration);
		tv_current_time = (TextView) view.findViewById(R.id.yuedong_tv_current_time);
		skb_progress = (SeekBar) view.findViewById(R.id.yuedong_skb_progress);
		ib_set_as_favor.setOnClickListener(this);
		iv_play.setOnClickListener(this);
		iv_play_next.setOnClickListener(this);
		iv_play_last.setOnClickListener(this);
		iv_voice.setOnClickListener(this);
		iv_play_mode.setOnClickListener(this);
		
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		FontUtil.screenHeight = displayMetrics.heightPixels;
		FontUtil.screenWidth = displayMetrics.widthPixels;
		FontUtil fontUtil = new FontUtil();
		fontUtil.changeViewSize((ViewGroup)view);
	}

	public void initViewData() {
		// 每次先加载sp中的数据，显示到主页上，
		SharedPreferences sp = context.getSharedPreferences(
				CurrentInfoManage.SP_NAME, Context.MODE_PRIVATE);
		String info = sp.getString(CurrentInfoManage.SP_NAME, "");
		if (!info.equals("")) {
			Gson gson = new Gson();
			CurrentInfo currentInfo = gson.fromJson(info, CurrentInfo.class);
			play_mode = currentInfo.getPlay_mode();
			currentMusic = currentInfo.getCurrent_music();
			setMusicData(currentMusic);
		}
	}

	private void setMusicData(Music music) {
		tv_music_name.setText(currentMusic.getName());
		tv_artist.setText(currentMusic.getArtist());
		tv_album.setText(currentMusic.getAlbum());
		tv_duration.setText(currentMusic.getStr_duration());
		skb_progress.setMax(currentDuration);
	}

	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.yuedong_ib_set_as_favor:
			break;
		case R.id.yuedong_iv_play:
			play();
			break;
		case R.id.yuedong_iv_play_last:
			playLastMusic();
			break;
		case R.id.yuedong_iv_play_next:
			playNextMusic();
			break;
		}
	}

	private void play() {
		
		serviceIntent = new Intent(context, PlayService.class);
		serviceIntent.putExtra("music", currentMusic);
		serviceIntent.putExtra("playing", playing);
		serviceIntent.putExtra("currentPlayTime", currentPlayTime);
		serviceIntent.putParcelableArrayListExtra("list", list);
		context.startService(serviceIntent);
		if (playing) {// 当前是正在播放的,歌曲将暂停，图标将变成使音乐播放
			playing = false;
			iv_play.setImageResource(R.drawable.selector_play);
			if(timer!=null){timer.cancel();}
		} else {// 当前是暂停状态的，歌曲将播放，图标将变成使音乐暂停
			playing = true;
			iv_play.setImageResource(R.drawable.selector_stop);
			startAutoPlay();
		}
		setMusicData(currentMusic);
	}

	public void startAutoPlay() {
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(currentPlayTime!=currentDuration){
					currentPlayTime=currentPlayTime+1000;
					//设置进度条的当前时间
					handler.post(new Runnable() {
						@Override
						public void run() {
							
								setPlayingMusicData();
						}
					});
				}
			}
		}, 1000, 1000);
	}
	
	private void setPlayingMusicData(){
		skb_progress.setProgress(currentPlayTime);
		String time = MusicDB.long2Date(currentPlayTime);
		tv_current_time.setText(time);
	}
	
	private Handler handler = new Handler();

	public class PlayReciever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(PlayService.MUSIC_COMPLETED)) {
				if(timer!=null){timer.cancel();}
				currentPlayTime=0;
				// 当前歌曲播放完成，自动跳到下一曲
				playNextMusic();
			}

		}
	}

	// 下一曲
	private void playNextMusic() {
		int currentMusicIndex = list.indexOf(currentMusic);
		Log.i(TAG, "playNextMusic currentMusicIndex = " + currentMusicIndex);
		// 如果当前已经到最后一曲，则再下一曲就变为第一曲
		if (++currentMusicIndex >= list.size()) {
			currentMusicIndex = 0;
		}
		resetPlayParam(currentMusicIndex);
		play();
	}

	// 上一曲
	private void playLastMusic() {
		int currentMusicIndex = list.indexOf(currentMusic);
		Log.i(TAG, "playLastMusic currentMusicIndex = " + currentMusicIndex);
		// 如果当前是第一曲，则再上一曲就变为最后曲
		if (--currentMusicIndex < 0) {
			currentMusicIndex = list.size() - 1;
		}
		resetPlayParam(currentMusicIndex);
		play();
	}
	
	/**
	 * 根据当前应该播放的曲目位置重置播放参数，
	 */
	private void resetPlayParam(int currentMusicIndex){
		currentMusic = list.get(currentMusicIndex);
		playing = false;
		currentDuration = currentMusic.getDuration();
		currentPlayTime=0;
		if(timer!=null){timer.cancel();}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
