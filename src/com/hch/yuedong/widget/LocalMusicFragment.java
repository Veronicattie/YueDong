package com.hch.yuedong.widget;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.hch.yuedong.R;
import com.hch.yuedong.adapter.LocalMusicListAdapter;
import com.hch.yuedong.entity.Music;
import com.hch.yuedong.util.MusicUtil;

public class LocalMusicFragment extends SherlockFragment{
	
	List<Music> list  = new ArrayList<Music>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contextView = inflater.inflate(R.layout.main_tab_localmusic, container, false);
		list =MusicUtil.scanAllAudioFiles(contextView.getContext());
		initTotalMusicView(contextView);
		return contextView;
	}
	
	public void initTotalMusicView(View contextView){
		TextView totalmusic = (TextView) contextView.findViewById(R.id.localmusic_tv_totalmusic);
		int size = list.size();
		String text = totalmusic.getText().toString();
		totalmusic.setText(String.format(text, size));
	}
	
	public void initListView(View contextView){
		ListView musiclist = (ListView) contextView.findViewById(R.id.localmusic_lv_musiclist);
		musiclist.setAdapter(new LocalMusicListAdapter(contextView.getContext(),list));
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
