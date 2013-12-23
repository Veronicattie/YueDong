package com.hch.yuedong.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import com.hch.yuedong.R;
import com.hch.yuedong.adapter.TabPagerAdapter;
import com.hch.yuedong.entity.Music;
import com.hch.yuedong.manage.MusicDB;
import com.hch.yuedong.widget.LocalMusicFragment;
import com.hch.yuedong.widget.PlayListFragment;
import com.hch.yuedong.widget.YueDongFragment;

public class MainActivity extends SherlockFragmentActivity implements
		ActionBar.TabListener, OnPageChangeListener, BaseActivity {
	/**
	 * 顶部Tab的title
	 */
	private String[] mTabTitles;

	/**
	 * ViewPager对象的引用
	 */
	private ViewPager mViewPager;

	/**
	 * 装载Fragment的容器，我们的每一个界面都是一个Fragment
	 */
	private List<Fragment> mFragmentList;

	/**
	 * ActionBar对象的引用
	 */
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_main);
		init();
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

		// 将list放入Adapter中，用到ViewPager里去显示
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(),
				mFragmentList));
		mViewPager.setOnPageChangeListener(this);
		
		
		//设置ActionBar
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// 为ActionBar添加Tab并设置TabListener
		mTabTitles = getResources().getStringArray(R.array.tab_title);
		for (int i = 0; i < mTabTitles.length; i++) {
			ActionBar.Tab tab = mActionBar.newTab();
			tab.setText(mTabTitles[i]);
			tab.setTabListener(this);
			mActionBar.addTab(tab, i);
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// 点击ActionBar Tab的时候切换不同的Fragment界面
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		// 滑动ViewPager的时候设置相对应的ActionBar Tab被选中
		mActionBar.setSelectedNavigationItem(arg0);
	}

}
