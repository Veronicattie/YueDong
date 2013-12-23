package com.hch.yuedong.entity;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

public class Music implements Parcelable{

	public int id;
	public String name;
	public String album;
	public String artist;
	public String url;
	//歌曲总播放时长
	public int duration;
	public String str_duration;
	public Long size;
	//默认没有播放列表
	public int playlistId=-1;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getStr_duration() {
		return str_duration;
	}
	public void setStr_duration(String str_duration) {
		this.str_duration = str_duration;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public int getPlaylistId() {
		return playlistId;
	}
	public void setPlaylistId(int playlistId) {
		this.playlistId = playlistId;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		//必须按成员变量声明的顺序封装数据，不然会出现获取数据出错
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(album);
		dest.writeString(artist);
		dest.writeString(url);
		dest.writeInt(duration);
		dest.writeString(str_duration);
		dest.writeLong(size);
		dest.writeInt(playlistId);
	}
	
	public static final Parcelable.Creator<Music> CREATOR = new Creator<Music>(){
		public Music createFromParcel(Parcel source) {
			//必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			Music music = new Music();
			music.setId(source.readInt());
			music.setName(source.readString());
			music.setAlbum(source.readString());
			music.setArtist(source.readString());
			music.setUrl(source.readString());
			music.setDuration(source.readInt());
			music.setStr_duration(source.readString());
			music.setSize(source.readLong());
			music.setPlaylistId(source.readInt());
			return music;
		}

		@Override
		public Music[] newArray(int size) {
			return new Music[size];
		}
		
	};
	@Override
	public String toString() {
		return "Music [id=" + id + ", name=" + name + ", album=" + album
				+ ", artist=" + artist + ", url=" + url + ", duration="
				+ duration + ", str_duration=" + str_duration + ", size="
				+ size + ", playlistId=" + playlistId + "]";
	}
	
	
	
}
