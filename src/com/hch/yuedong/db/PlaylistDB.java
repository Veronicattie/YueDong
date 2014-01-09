package com.hch.yuedong.db;

import java.util.ArrayList;
import java.util.List;

import com.hch.yuedong.entity.PlayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;
import android.util.Log;

public class PlaylistDB{
	private DBHelper dbHelper=null;
	private SQLiteDatabase sqldatabase;
	public final static String TABLE_NAME="playlist";
	public final static String FILED_ID="_id";
	public final static String FIELD_NAME="name";
	public final static String FIELD_TOTALMUSIC="totalmusic";
	
	
	public PlaylistDB(Context context){
		dbHelper=new DBHelper(context,CommDB.DBNAME);
		sqldatabase=dbHelper.getWritableDatabase();
	}
	
	//插入成功返回该行的id值，不成功则返回-1
	public long insertPlaylist(String name)
	{
		long rowNum = -1;
		try
		{
			ContentValues cv=new ContentValues();
			cv.put(FIELD_NAME,name);
			cv.put(FIELD_TOTALMUSIC, 0);
			rowNum = sqldatabase.insert(TABLE_NAME,null,cv);
		}catch(Exception ex)
		{
		  Log.e("insertPlaylist(int id, String name)", ex.getMessage());
		  return -1;
		}
		return rowNum;
	}

	
	//通过Playlist Id 获取 Cursor
	public Cursor selectPlaylistById1(int id)
	{
		Cursor cursor=sqldatabase.query(TABLE_NAME,null, FILED_ID+"=?",new String[]{""+id+""}, null, null, null);
		return cursor;
	}
	
	public PlayList selectPlaylistById2(int id){
		Cursor cursor = selectPlaylistById1(id);
		PlayList playList = new PlayList();
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				String name = cursor.getString(cursor.getColumnIndexOrThrow(FIELD_NAME));
				int count = cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_TOTALMUSIC));
				playList.setId(id);
				playList.setName(name);
				playList.setTotalmusic(count);
				
				cursor.moveToNext();
			}
		}
		return playList;
	}
	
	//通过播放列表 name 获取 Cursor
	public Cursor selectPlaylistByName(String name)
	{
		Cursor cursor=sqldatabase.query(TABLE_NAME,null, FIELD_NAME+"=?",new String[]{""+name+""}, null, null, null);
		return cursor;
	}
	
	//获取所有的播放列表
	public Cursor queryAll1()
	{
		Cursor cursor=sqldatabase.query(TABLE_NAME,null,null,null, null, null, null);
		return cursor;
	}
	
	public List<PlayList> queryAll2(){
		List<PlayList> list = new ArrayList<PlayList>();
		Cursor cursor = queryAll1();
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				PlayList playList = new PlayList();
				int id = cursor.getInt(cursor.getColumnIndexOrThrow(FILED_ID));
				String name = cursor.getString(cursor.getColumnIndexOrThrow(FIELD_NAME));
				int count = cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_TOTALMUSIC));
				playList.setId(id);
				playList.setName(name);
				playList.setTotalmusic(count);
				list.add(playList);
				cursor.moveToNext();
			}
		}
		return list;
	}


}
