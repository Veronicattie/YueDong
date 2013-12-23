package com.hch.yuedong.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hch.yuedong.R;
import com.hch.yuedong.entity.Music;
import com.hch.yuedong.entity.PlayList;
import com.hch.yuedong.util.FontUtil;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayListAdapter extends BaseAdapter{

	List<PlayList> list = new ArrayList<PlayList>();
	Context context = null;
	FontUtil fontUtil;
	
	public PlayListAdapter(Context context,List<PlayList> list){
		this.list = list;
		this.context = context;
		fontUtil = new FontUtil();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public PlayList getItem(int arg0) {
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
		if(convertView==null){
			viewHolder = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.listitem_playlist, null);
			
			viewHolder.name = (TextView) convertView.findViewById(R.id.item_tv_playlistname);
			viewHolder.totalmusic = (TextView) convertView.findViewById(R.id.item_tv_totalmusic);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PlayList playlist = getItem(position);
		if(playlist!=null){
			viewHolder.name.setText(playlist.getName());
			viewHolder.totalmusic.setText(playlist.getTotalmusic()+"");
		}
		
		fontUtil.changeViewSize((ViewGroup)convertView);
		return convertView;
	}
	
	private static class ViewHolder{
		TextView name;
		TextView totalmusic;
	}

}
