package com.hch.yuedong.ui;

import java.util.Stack;

import com.hch.yuedong.db.CurrentInfoManage;
import com.hch.yuedong.util.FontUtil;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View.OnCreateContextMenuListener;

public class MyApplication extends Application{

	private static MyApplication mInstance = null;
	private static Stack<Activity> activityStack;
	
	public int height;
	public int width;
	public float density;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("MyApplication", "onCreate");
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		height = displayMetrics.heightPixels;
		width = displayMetrics.widthPixels;
		density = displayMetrics.density;
		mInstance = this;
	}


	public static MyApplication getInstance() {
		return mInstance;
	}
	
	

	/****************** 控制应用中activity的声明周期 *****************/

	/**
	 * add Activity 添加Activity到栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * get current Activity 获取当前Activity（栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		removeActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void removeActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				removeActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit() {
		try {
			finishAllActivity();
		} catch (Exception e) {
		}
	}
}
