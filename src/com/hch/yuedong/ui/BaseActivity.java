package com.hch.yuedong.ui;


import com.hch.yuedong.util.FontUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;


public class BaseActivity extends Activity{
	private ProgressDialog dialog;
	public FontUtil fontUtil;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		fontUtil = new FontUtil( MyApplication.getInstance().width, 
				MyApplication.getInstance().height);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getInstance().removeActivity(this);
	}
	
	
	public void showDialog(){
		dialog = ProgressDialog.show(this, null,
				"请求数据中...", true, true);
	}
	
	public void showDialog(String s){
		dialog = ProgressDialog.show(this, null,
				s, true, true);
	}
	
	public void dialogDismiss(){
		if(dialog!=null){
			dialog.dismiss();
		}
	}
}
