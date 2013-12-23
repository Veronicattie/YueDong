package com.hch.yuedong.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	private List<Fragment> list;
//	//将要分页显示的View装入数组中
//    LayoutInflater mLi = LayoutInflater.from(this);
//    View view1 = mLi.inflate(R.layout.main_tab_weixin, null);
//    View view2 = mLi.inflate(R.layout.main_tab_address, null);
//    View view3 = mLi.inflate(R.layout.main_tab_friends, null);
//    View view4 = mLi.inflate(R.layout.main_tab_settings, null);
//    
//  //每个页面的view数据
//    final ArrayList<View> views = new ArrayList<View>();
//    views.add(view1);
//    views.add(view2);
//    views.add(view3);
//    views.add(view4);
	//构造函数
	public TabPagerAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
