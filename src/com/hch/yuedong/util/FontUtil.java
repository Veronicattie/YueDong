package com.hch.yuedong.util;

import com.hch.yuedong.ui.MyApplication;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 第一次使用FontUtil时设置screenWith和screenHeight以后都不需要再设置
 * @author hechenghuan
 *
 */
public class FontUtil {
	// 测试机的宽高
	private static final int testWidth = 768;
	private static final int testHeight = 1184;
	public static int screenWidth, screenHeight;
	private float ratio = 0;
	
	public FontUtil(){
		ratio = getRatio();
	}

	public FontUtil(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		ratio = getRatio();
	}

	public float getRatio() {
		float ratioWidth = (float) screenWidth / testWidth;
		float ratioHeight = (float) screenHeight / testHeight;
		float ratio = Math.min(ratioWidth, ratioHeight);
		float OFFSET_LEFT = 0;
		float OFFSET_TOP = 0;
		float tmp = 0;
		if (ratioWidth != ratioHeight) {
			if (ratio == ratioWidth) {
				OFFSET_LEFT = 0;
				tmp = (screenHeight - testHeight * ratio) / (screenWidth-80);
				OFFSET_TOP = Math
						.round((screenHeight - testHeight * ratio) / 2);
			} else {
				tmp = (screenWidth - testWidth * ratio) / (screenHeight - 80);
				OFFSET_LEFT = Math.round((screenWidth - testWidth * ratio) / 2);
				OFFSET_TOP = 0;
			}
		}
		// float tmp =
		// (OFFSET_LEFT==0)?OFFSET_TOP/screenHeight:OFFSET_LEFT/screenWidth;
		Log.i("ratio------->", ratio + "");
		Log.i("tmp----->", tmp + "");
		ratio = ratio + tmp;
		return ratio;
	}

	public int adjustScreen(float textSize) {
		return Math.round(textSize * ratio);
	}

	public void changeViewSize(ViewGroup viewGroup) {// 传入Activity顶层Layout,屏幕宽,屏幕高
		// int adjustFontSize = adjustFontSize(screenWidth,screenHeight);
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View v = viewGroup.getChildAt(i);
			if (v instanceof ViewGroup) {
				changeViewSize((ViewGroup) v);
			} else if (v instanceof TextView || v instanceof Button) {
				TextView tv = (TextView) v;
				int textSize = adjustScreen(tv.getTextSize());
				tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			}
		}
	}

	public void changeViewSize(TextView tv) {// 传入Activity顶层Layout,屏幕宽,屏幕高
		int textSize = adjustScreen(tv.getTextSize());
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
	}
}
