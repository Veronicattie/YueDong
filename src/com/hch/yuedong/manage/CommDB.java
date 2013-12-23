package com.hch.yuedong.manage;

import android.database.sqlite.SQLiteDatabase;


public final class CommDB {

	public static final String DBNAME = "yuedong_databse.db";
	
	public static void createTable(SQLiteDatabase db)
	{
		db.execSQL("create table "+PlaylistDB.TABLE_NAME+"(" +
				PlaylistDB.FILED_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
						PlaylistDB.FIELD_NAME+" varchar(20) NOT NULL UNIQUE,"+PlaylistDB.FIELD_TOTALMUSIC+" INTEGER DEFAULT 0)");
		
	}
}
