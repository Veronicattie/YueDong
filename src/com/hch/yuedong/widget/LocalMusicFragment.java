package com.hch.yuedong.widget;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hch.yuedong.R;
import com.hch.yuedong.adapter.LocalMusicListAdapter;
import com.hch.yuedong.db.MusicDB;
import com.hch.yuedong.entity.Music;
import com.hch.yuedong.util.FontUtil;

public class LocalMusicFragment extends Fragment{
	
	List<Music> list  = new ArrayList<Music>();
	
	View contextView=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(contextView==null){
			contextView = inflater.inflate(R.layout.main_tab_localmusic, container, false);
			list =MusicDB.scanAllAudioFiles(contextView.getContext());
			initTotalMusicView(contextView);
			initListView(contextView);
		}
		
		ViewGroup parent = (ViewGroup) contextView.getParent();
		if (parent != null) {
			parent.removeView(contextView);
		}
		return contextView;
	}
	
	public void initTotalMusicView(View contextView){
		TextView totalmusic = (TextView) contextView.findViewById(R.id.localmusic_tv_totalmusic);
		int size = list.size();
		String text = totalmusic.getText().toString();
		totalmusic.setText(String.format(text, size));
		FontUtil fontUtil = new FontUtil();
		fontUtil.changeViewSize((ViewGroup)contextView);
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
