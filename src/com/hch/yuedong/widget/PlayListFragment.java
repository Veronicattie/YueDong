package com.hch.yuedong.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.hch.yuedong.R;
import com.hch.yuedong.adapter.LocalMusicListAdapter;
import com.hch.yuedong.adapter.PlayListAdapter;
import com.hch.yuedong.entity.Music;
import com.hch.yuedong.entity.PlayList;
import com.hch.yuedong.manage.MusicDB;
import com.hch.yuedong.manage.PlaylistDB;
import com.hch.yuedong.util.FontUtil;

public class PlayListFragment extends SherlockFragment implements OnClickListener{
	
	List<PlayList> list  = new ArrayList<PlayList>();
	ListView lv_list = null;
	ImageView iv_empty = null;
	ProgressBar pb_loading = null;
	LinearLayout linear_newplaylist = null;
	Context context =  null;
	PlaylistDB playlistDB = null;
	BaseAdapter adapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contextView = inflater.inflate(R.layout.main_tab_playlist, container, false);
		//没有找到这个参数就返回null
		context = contextView.getContext();
		playlistDB = new PlaylistDB(context);
		list = playlistDB.queryAll2();
		initView(contextView);
		
		return contextView;
	}
	
	public void initView(View view){
		linear_newplaylist = (LinearLayout) view.findViewById(R.id.playlist_linear_newplaylist);
		linear_newplaylist.setOnClickListener(this);
		lv_list = (ListView) view.findViewById(R.id.playlist_lv_list);
		adapter = new PlayListAdapter(context, list);
		lv_list.setAdapter(adapter);
		iv_empty = (ImageView) view.findViewById(R.id.playlist_iv_empty);
		pb_loading = (ProgressBar) view.findViewById(R.id.playlist_pb_loading);
		pb_loading.setVisibility(View.INVISIBLE);
		if(list.size()==0){
			iv_empty.setVisibility(View.VISIBLE);
		}else{
			lv_list.setVisibility(View.VISIBLE);
		}
		FontUtil fontUtil = new FontUtil();
		fontUtil.changeViewSize((ViewGroup)view);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.playlist_linear_newplaylist:
			newPlayListDialog();
			break;

		}
	}
	
	private void newPlayListDialog(){
		final EditText et = new EditText(context);
		new AlertDialog.Builder(context)
			.setTitle("请输入列表名称")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setView(et)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String name = et.getText().toString();
                    PlaylistDB db = new PlaylistDB(context);
                    long result = db.insertPlaylist(name);
                    if(result>0){
                    	dialog.dismiss();
                    	list.add(db.selectPlaylistById2((int)result));
                    	adapter.notifyDataSetChanged();
                    }
                }
			})
			.setNegativeButton("取消",  new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
			})
			.show();
	}

}
