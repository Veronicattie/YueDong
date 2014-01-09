package com.hch.yuedong.ui;

import java.util.ArrayList;

import com.hch.yuedong.R;
import com.hch.yuedong.ui.MainActivity.TabPageIndicatorAdapter;
import com.hch.yuedong.widget.LocalMusicFragment;
import com.hch.yuedong.widget.PlayListFragment;
import com.hch.yuedong.widget.YueDongFragment;
import com.viewpagerindicator.TabPageIndicator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;

public class ContentActivity extends FragmentActivity {

	/**
	 * Tab标题
	 */
	private static final String[] TITLE = new String[] { "本地音乐", "网络音乐", "下载管理" };
	
	
	private ArrayList<Fragment> mFragmentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_main);
		init();
		MyApplication.getInstance().addActivity(this);
	}
	
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.exit_title);
			builder.setMessage(R.string.exit_info);
			builder.setPositiveButton(R.string.confirm,
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							MyApplication.getInstance().AppExit();
						}
					});
			builder.setNegativeButton(R.string.cancel,
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			Dialog noticeDialog = builder.create();
			noticeDialog.show();
		}
		return false;
	}
	
	public void init() {
		// 将Fragment加入List中
		mFragmentList = new ArrayList<Fragment>();
		Fragment localMusicFragment = new LocalMusicFragment();
		Fragment playListFragment = new PlayListFragment();
		Fragment yueDongFragmetn = new YueDongFragment();	
		mFragmentList.add(yueDongFragmetn);
		mFragmentList.add(playListFragment);
		mFragmentList.add(localMusicFragment);
		
		//ViewPager的adapter
//		FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
//        ViewPager pager = (ViewPager)findViewById(R.id.viewPager);
//        pager.setAdapter(adapter);
//
//        //实例化TabPageIndicator然后设置ViewPager与之关联
//        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
//        indicator.setViewPager(pager);
        
	}
	

	
}
