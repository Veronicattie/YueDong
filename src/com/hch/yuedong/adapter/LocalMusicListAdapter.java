package com.hch.yuedong.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hch.yuedong.R;
import com.hch.yuedong.entity.Music;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LocalMusicListAdapter extends BaseAdapter{

	List<Music> list = new ArrayList<Music>();
	Context context = null;
	
	public LocalMusicListAdapter(Context context,List<Music> list){
		this.list = list;
		context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Music getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		Log.e("创建一行", "创建一行");
		if(convertView!=null){
			viewHolder = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.listitem_localmusiclist, null);
			
			viewHolder.name = (TextView) convertView.findViewById(R.id.item_tv_musicname);
			viewHolder.artist = (TextView) convertView.findViewById(R.id.item_tv_artist);
			viewHolder.duration = (TextView) convertView.findViewById(R.id.item_tv_duration);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Music music = getItem(position);
		if(music!=null){
			viewHolder.name.setText(music.getName());
			viewHolder.artist.setText(music.getArtist());
			viewHolder.duration.setText(music.getDuration());
		}
		return null;
	}
	
	private static class ViewHolder{
		TextView name;
		TextView artist;
		TextView duration;
		
	}

}
