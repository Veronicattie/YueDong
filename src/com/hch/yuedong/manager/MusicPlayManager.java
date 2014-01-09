package com.hch.yuedong.manager;

import java.util.ArrayList;
import java.util.Timer;

import com.hch.yuedong.R;
import com.hch.yuedong.entity.Music;
import com.hch.yuedong.service.PlayService;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MusicPlayManager {
	public static final int MODE_LIST_ONCE = 0;
	public static final int MODE_LOOP_LIST = 1;
	public static final int MODE_SHUFFLE = 2;
	public static final int MODE_SINGLE = 3;
	ArrayList<Music> list = new ArrayList<Music>();
	// 默认播放模式为顺序播放
	private int play_mode = MODE_LIST_ONCE;
	// 默认播放列表中的第一首歌
	private Music currentMusic = null;
	private boolean playing = false;
	private int currentPlayTime=0;
	private int currentDuration = 0;
	private Timer timer;
	private Context context;
	public MusicPlayManager(){
		
	}
	public MusicPlayManager(Context context){
		this.context = context;
	}
	// 上一曲
	private void playLastMusic() {
		int currentMusicIndex = list.indexOf(currentMusic);
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
	
	private void play() {
		
		Intent serviceIntent = new Intent(context, PlayService.class);
		serviceIntent.putExtra("music", currentMusic);
		serviceIntent.putExtra("playing", playing);
		serviceIntent.putExtra("currentPlayTime", currentPlayTime);
		serviceIntent.putParcelableArrayListExtra("list", list);
		context.startService(serviceIntent);
		if (playing) {// 当前是正在播放的,歌曲将暂停，图标将变成使音乐播放
			playing = false;
			//iv_play.setImageResource(R.drawable.selector_play);
			if(timer!=null){timer.cancel();}
		} else {// 当前是暂停状态的，歌曲将播放，图标将变成使音乐暂停
			playing = true;
			//iv_play.setImageResource(R.drawable.selector_stop);
			//startAutoPlay();
		}
		//setMusicData(currentMusic);
	}
}
