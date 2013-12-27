package com.hch.yuedong.manage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.hch.yuedong.entity.Music;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.renderscript.Long2;
import android.text.format.DateFormat;

public class MusicDB {
	public static ArrayList<Music> scanAllAudioFiles(Context context) {
		ArrayList<Music> mylist = new ArrayList<Music>();
		// 查询媒体数据库
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		// 遍历媒体数据库
		if (cursor.moveToFirst()) {

			while (!cursor.isAfterLast()) {

				// 歌曲编号
				int id = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
				// 歌曲标题
				String title = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				// 歌曲的专辑名：MediaStore.Audio.Media.ALBUM
				String album = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
				// 歌曲的歌手名： MediaStore.Audio.Media.ARTIST
				String artist = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				if("<unknown>".equals(artist)){
					artist="未知艺术家";
				}
				// 歌曲文件的路径 ：MediaStore.Audio.Media.DATA
				String url = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				// 歌曲的总播放时长：MediaStore.Audio.Media.DURATION
				int duration = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
				String str_duration = long2Date(duration);
				// 歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
				Long size = cursor.getLong(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

				if (size > 1024 * 800) {// 大于800K
					Music music = new Music();
					music.setId(id);
					music.setName(title);
					music.setAlbum(album);
					music.setArtist(artist);
					music.setUrl(url);
					music.setDuration(duration);
					music.setStr_duration(str_duration);
					music.setSize(size);
					mylist.add(music);
				}
				cursor.moveToNext();
			}
		}
		return mylist;
	}
	
	public static String long2Date(long value){
		SimpleDateFormat dateformat = new SimpleDateFormat("mm:ss");
		Date date = new Date(value);
		String result = dateformat.format(date);
		return result;
	}
}
