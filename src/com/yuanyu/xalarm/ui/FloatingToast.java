package com.yuanyu.xalarm.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public enum FloatingToast {

	INSTANCE;
	
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWinLayoutParams;
	
	private TextView mTextView;
	
	public void create(Context context, String text) {
		initLayoutParams(context);
		createFloatingView(context, text);
	}
	
	public void create(Context context, int textResId) {
		create(context, context.getString(textResId));
	}
	
	public void destroy() {
		if(mWindowManager != null) {
			mWindowManager.removeView(mTextView);
			mWindowManager = null;
			mWinLayoutParams = null;
		}
	}
	
	public void setVisibility(boolean visibility) {
		if(mTextView == null) {
			return;
		}
		if(visibility) {
			mTextView.setVisibility(View.VISIBLE);
		}
		else {
			mTextView.setVisibility(View.INVISIBLE);
		}
	}
	
	private void initLayoutParams(Context context){
		mWindowManager = (WindowManager)context.getApplicationContext().getSystemService("window");
	    DisplayMetrics display = new DisplayMetrics();
	    mWindowManager.getDefaultDisplay().getMetrics(display);
	    mWinLayoutParams = new WindowManager.LayoutParams();

	    mWinLayoutParams.type = LayoutParams.TYPE_PHONE;
	    mWinLayoutParams.format = PixelFormat.RGBA_8888;
	    mWinLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;

	    mWinLayoutParams.x = 0;
	    mWinLayoutParams.y = 0;
	    mWinLayoutParams.width = LayoutParams.MATCH_PARENT;
	    mWinLayoutParams.height = LayoutParams.WRAP_CONTENT;
	}
	
	private void createFloatingView(Context context, String text){
		mTextView = new TextView(context);
		mTextView.setText(text);
		mTextView.setVisibility(View.INVISIBLE);
		mTextView.setPadding(5, 5, 5, 5);
		mTextView.setGravity(Gravity.CENTER);
		mTextView.setBackgroundResource(android.R.color.background_light);
		mWinLayoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		mWindowManager.addView(mTextView, mWinLayoutParams);
    }
}
