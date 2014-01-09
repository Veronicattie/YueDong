package com.hch.yuedong.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final int DBVERSION = 1;
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DBHelper(Context context,String name)
	{  
		this(context,name,DBVERSION);
	}
	
	public DBHelper(Context context,String name,int version)
	{
		
		this(context,name,null,version);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建表
		CommDB.createTable(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//System.out.println("database upgrade");
	}

}
