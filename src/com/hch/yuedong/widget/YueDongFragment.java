package com.hch.yuedong.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hch.yuedong.R;
import com.hch.yuedong.db.CurrentInfoManage;
import com.hch.yuedong.db.MusicDB;
import com.hch.yuedong.entity.CurrentInfo;
import com.hch.yuedong.entity.Music;
import com.hch.yuedong.service.PlayService;
import com.hch.yuedong.ui.MyApplication;
import com.hch.yuedong.util.FontUtil;

public class YueDongFragment extends Fragment implements
		OnClickListener {
	private static final String TAG = "YueDongFragment";
	public static final int MODE_LIST_ONCE = 0;
	public static final int MODE_LOOP_LIST = 1;
	public static final int MODE_SHUFFLE = 2;
	public static final int MODE_SINGLE = 3;

	ImageButton ib_set_as_favor = null;
	ImageView iv_play = null;
	ImageView iv_play_next = null;
	ImageView iv_play_last = null;
	ImageView iv_play_mode = null;
	ImageView iv_voice = null;
	ImageView iv_cd = null;
	TextView tv_music_name = null;
	TextView tv_artist = null;
	TextView tv_album = null;
	TextView tv_duration = null;
	TextView tv_current_time = null;
	SeekBar skb_progress = null;
	
	View contextView = null;
	Context context = null;
	MediaPlayer mediaPlayer = null;
	Intent serviceIntent = null;
	public static PlayReciever receiver = null;

	ArrayList<Music> list = new ArrayList<Music>();
	// 默认播放模式为顺序播放
	private int play_mode = MODE_LIST_ONCE;
	// 默认播放列表中的第一首歌
	private Music currentMusic = null;
	private boolean playing = false;
	private int currentPlayTime=0;
	private int currentDuration = 0;
	private int totalMusic = 0;
	private Timer timer;
	private Boolean clickNextOrLast = false;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(contextView == null){
			contextView = inflater.inflate(R.layout.main_tab_yuedong,
					container, false);
			initView(contextView);
			initData(contextView);
		}
		
		ViewGroup parent = (ViewGroup) contextView.getParent();
		if (parent != null) {
			parent.removeView(contextView);
		}
		
		receiver = new PlayReciever();
		//注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(PlayService.MUSIC_COMPLETED);
		this.getActivity().registerReceiver(receiver, filter);
		
		Log.i(TAG, "onCreateView");
		return contextView;
	}

	@Override
	public void onPause() {
		super.onPause();
		//skb_progress.
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}	
	@Override
	public void onDetach() {
		super.onDetach();
		Log.i(TAG, "onDetach");
	}

	
	public void initData(View contextView){
		context = contextView.getContext();
		mediaPlayer = new MediaPlayer();
		list = MusicDB.scanAllAudioFiles(contextView.getContext());
		currentMusic = list.get(0);
		currentDuration = list.get(0).getDuration();
		totalMusic = list.size();
		
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

	public void initView(View view) {
		ib_set_as_favor = (ImageButton) view
				.findViewById(R.id.yuedong_ib_set_as_favor);
		iv_play = (ImageView) view.findViewById(R.id.yuedong_iv_play);
		iv_play_next = (ImageView) view.findViewById(R.id.yuedong_iv_play_next);
		iv_play_last = (ImageView) view.findViewById(R.id.yuedong_iv_play_last);
		iv_voice = (ImageView) view.findViewById(R.id.yuedong_iv_voice);
		iv_play_mode = (ImageView) view.findViewById(R.id.yuedong_iv_play_mode);
		iv_cd = (ImageView) view.findViewById(R.id.yuedong_iv_cd);
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

	private void setMusicData(Music music) {
		tv_music_name.setText(currentMusic.getName());
		tv_artist.setText(currentMusic.getArtist());
		tv_album.setText(currentMusic.getAlbum());
		tv_duration.setText(currentMusic.getStr_duration());
		skb_progress.setMax(currentDuration);
	}

	private void discRotate(){
		Animation rotate = AnimationUtils.loadAnimation(context, R.anim.disc_rotate);
		 LinearInterpolator lin = new LinearInterpolator(); // 设置旋转速度 此处设置为匀速旋转
		 rotate.setInterpolator(lin);//将旋转速度配置给动画。
		 iv_cd.startAnimation(rotate);
	}
	
	private void stopRotate(){
		iv_cd.clearAnimation();
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.yuedong_iv_play_mode:
			changePlayMode();
			break;
		case R.id.yuedong_ib_set_as_favor:
			break;
		case R.id.yuedong_iv_play:
			play();
			break;
		case R.id.yuedong_iv_play_last:
			clickNextOrLast = true;
			playLastMusic();
			break;
		case R.id.yuedong_iv_play_next:
			clickNextOrLast = true;
			playNextMusic();
			break;
		}
	}
	
	private void changePlayMode(){
		if(play_mode==MODE_LIST_ONCE){
			iv_play_mode.setImageResource(R.drawable.play_mode_loop_list);
			play_mode=MODE_LOOP_LIST;
			Toast.makeText(context, "列表循环", Toast.LENGTH_SHORT).show();
		}else if(play_mode==MODE_LOOP_LIST){
			iv_play_mode.setImageResource(R.drawable.play_mode_shuffle);
			play_mode=MODE_SHUFFLE;
			Toast.makeText(context, "随机播放", Toast.LENGTH_SHORT).show();
		}else if(play_mode==MODE_SHUFFLE){
			iv_play_mode.setImageResource(R.drawable.play_mode_single);
			play_mode=MODE_SINGLE;
			Toast.makeText(context, "单曲循环", Toast.LENGTH_SHORT).show();
		}else if(play_mode ==MODE_SINGLE){
			iv_play_mode.setImageResource(R.drawable.play_mode_list_once);
			play_mode=MODE_LIST_ONCE;
			Toast.makeText(context, "列表播放", Toast.LENGTH_SHORT).show();
		}
	}
	private void play() {
		setMusicData(currentMusic);
		serviceIntent = new Intent(context, PlayService.class);
		serviceIntent.putExtra("music", currentMusic);
		serviceIntent.putExtra("playing", playing);
		serviceIntent.putExtra("currentPlayTime", currentPlayTime);
		serviceIntent.putParcelableArrayListExtra("list", list);
		context.startService(serviceIntent);
		if (playing) {// 当前是正在播放的,歌曲将暂停，图标将变成使音乐播放
			playing = false;
			stopRotate();
			if(timer!=null){timer.cancel();}
		} else {// 当前是暂停状态的，歌曲将播放，图标将变成使音乐暂停
			playing = true;
			discRotate();
			iv_play.setImageResource(R.drawable.selector_stop);
			
			startAutoPlay();
		}
		
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
		Log.i(TAG, currentPlayTime+"");
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
				clickNextOrLast = false;
				playNextMusic();
			}

		}
	}
	
	private void playAt(int position){
		resetPlayParam(position);
		play();
	}

	// 下一曲
	private void playNextMusic() {
		int currentMusicIndex = list.indexOf(currentMusic);
		switch (play_mode) {
		case MODE_LIST_ONCE:
			if (++currentMusicIndex >= list.size() && (!clickNextOrLast)) {
				//如果是最后一曲了，就暂停音乐
				play();
			}else{playNextPositionMusic(currentMusicIndex);}
			break;
		case MODE_LOOP_LIST:
			playNextPositionMusic(currentMusicIndex);
			break;
		case MODE_SHUFFLE:
			int position = (int)(Math.random()*totalMusic);
			playAt(position);
			break;
		case MODE_SINGLE:
			if(clickNextOrLast){playNextPositionMusic(currentMusicIndex);}
			else{playAt(currentMusicIndex);}
			break;
		}
	}
	
	private void playNextPositionMusic(int currentMusicIndex ){
		if (++currentMusicIndex >= list.size()) {
			currentMusicIndex = 0;
		}
		playAt(currentMusicIndex);
	}
	
	private void playLastPositionMusic(int currentMusicIndex){
		if (--currentMusicIndex < 0) {
			currentMusicIndex = list.size() - 1;
		}
		playAt(currentMusicIndex);
	}

	// 上一曲
	private void playLastMusic() {
		int currentMusicIndex = list.indexOf(currentMusic);
		switch (play_mode) {
		case MODE_LIST_ONCE:
			if (--currentMusicIndex < 0 && (!clickNextOrLast)) {
				play();
			}else{playLastPositionMusic(currentMusicIndex);}
			break;
		case MODE_LOOP_LIST:
			playLastPositionMusic(currentMusicIndex);
			break;
		case MODE_SHUFFLE:
			int position = (int)(Math.random()*totalMusic);
			playAt(position);
			break;
		case MODE_SINGLE:
			if(clickNextOrLast){playLastPositionMusic(currentMusicIndex);}
			else{playAt(currentMusicIndex);}
			break;
		}
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
