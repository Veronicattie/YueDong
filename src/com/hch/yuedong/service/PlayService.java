package com.hch.yuedong.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.hch.yuedong.R;
import com.hch.yuedong.entity.Music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class PlayService extends Service {

	public static final String MUSIC_COMPLETED = "com.hch.yuedong.music_completed";
	public static final String MUSIC_PLAYING = "com.hch.yuedong.music_playing";
	private static final String TAG = "PlayService";
	private MediaPlayer mp;
	private Music currentMusic = null;
	private int currentPlayTimeFromAC = 0;
	private int currentPlayTime = 0;
	ArrayList<Music> list = new ArrayList<Music>();

	@Override
	public void onCreate() {
		super.onCreate();
		mp = new MediaPlayer();
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		boolean playing = intent.getBooleanExtra("playing", false);
		currentMusic = intent.getParcelableExtra("music");
		list = intent.getParcelableArrayListExtra("list");
		currentPlayTimeFromAC = intent.getIntExtra("currentPlayTime", 0);
		Log.i(TAG, "list.size():"+list.size());
		if (playing) {
			// 现在在播就暂停
			currentPlayTime=mp.getCurrentPosition();
			mp.pause();
		} else {
			playMusic(currentMusic.getUrl());
		}

		return Service.START_REDELIVER_INTENT;
	}

	/**
	 * 播放音乐
	 * 
	 * @param musicPath
	 *            音乐文件所在的路径
	 */
	private void playMusic(String musicPath) {
		if(currentPlayTimeFromAC!=0){
			//重新播放已暂停的歌曲
			Log.e("重新播放", currentPlayTime+"");
			//mp.seekTo(currentPlayTime);
			mp.start();
		}else{
		try {
			// 重置mediaPlayer实例，reset之后处于空闲状态
			mp.reset();
			// 设置需要播放的音乐文件的路径，只有设置了文件路径之后才能调用prepare
			mp.setDataSource(musicPath);
			// 准备播放，只有调用了prepare之后才能调用start
			mp.prepareAsync();
			mp.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
				}
			});
			mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					Intent intent = new Intent(PlayService.MUSIC_COMPLETED);
					sendBroadcast(intent);
				}
			});
			mp.setOnErrorListener(new errorListener());
			// 使用唤醒锁，使得设备进入休眠状态时，MediaPlayer仍能播放，如果音乐是从网上下载，则还需要WIFI锁
			mp.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
		} catch (Exception e) {
			Log.e(TAG, "playMusic error " + e);
		}
		}
	}
	
	private void playCurrent(){
		while (true) {
//			int currentDuration = mp.getCurrentPosition();
//				sendBroadcast(new Intent(
//						MediaPlayerManager.BROADCASTRECEVIER_ACTON)
//						.putExtra("flag",
//								MediaPlayerManager.FLAG_CHANGED)
//						.putExtra("currentPosition", currentDuration)
//						.putExtra("duration", mPlayer.getDuration()));
//				updateWidget(false,currentDuration,mPlayer.getDuration());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	private class errorListener implements OnErrorListener {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// The MediaPlayer has moved to the Error state, must be reset!
			return false;
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mp.release();
	}

}
