package com.hch.yuedong.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.hch.yuedong.R;
import com.hch.yuedong.adapter.TabPagerAdapter;
import com.hch.yuedong.db.MusicDB;
import com.hch.yuedong.entity.Music;
import com.hch.yuedong.widget.LocalMusicFragment;
import com.hch.yuedong.widget.MenuFragment;
import com.hch.yuedong.widget.MySlidView;
import com.hch.yuedong.widget.PlayListFragment;
import com.hch.yuedong.widget.SlidFragment;
import com.hch.yuedong.widget.YueDongFragment;

import com.viewpagerindicator.TabPageIndicator;


public class MainActivity extends FragmentActivity {
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
		FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.viewPager);
        pager.setAdapter(adapter);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
        ImageView iv_slidmenu = (ImageView) this.findViewById(R.id.headerbar_iv_slidmenu);
        iv_slidmenu.setOnClickListener(new SlidMenuClickListener());
	}
	

	private class SlidMenuClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			MySlidView mSlidView = (MySlidView)findViewById(R.id.slid_view);
			
			mSlidView.setMenuView(getLayoutInflater().inflate(R.layout.menu_fragment, null));
			mSlidView.setSlidView(getLayoutInflater().inflate(R.layout.slid_fragment, null));

			FragmentTransaction ft = MainActivity.this.getSupportFragmentManager().beginTransaction();
			
			MenuFragment menuFragment = new MenuFragment();
			ft.replace(R.id.menu_fragment, menuFragment);
			
			SlidFragment slidFragment = new SlidFragment();
			ft.replace(R.id.slid_fragment, slidFragment);
			
			ft.commit();
		}
		
	}
	
	/**
	 * ViewPager适配器
	 * @author len
	 *
	 */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	//新建一个Fragment来展示ViewPager item的内容，并传递参数
        	Fragment fragment = mFragmentList.get(position);
            Bundle args = new Bundle();  
            args.putString("arg", TITLE[position]);  
            fragment.setArguments(args);  
        	
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position % TITLE.length];
        }

        @Override
        public int getCount() {
            return TITLE.length;
        }
    }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(YueDongFragment.receiver);
	}

}
