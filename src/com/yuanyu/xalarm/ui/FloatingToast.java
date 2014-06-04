package com.yuanyu.xalarm.ui;

import com.yuanyu.xalarm.R;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public enum FloatingToast {

	INSTANCE;
	
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWinLayoutParams;
	
	private View mView;
	private IBinder mToken;
	
	public void create(Context context, IBinder token) {
		mToken = token;
		initLayoutParams(context);
		createFloatingView(context);
	}
	
	public void destroy() {
		if(mWindowManager != null) {
			mWindowManager.removeView(mView);
			mWindowManager = null;
			mWinLayoutParams = null;
		}
	}
	
	public void setVisibility(boolean visibility) {
		if(mView == null) {
			return;
		}
		if(visibility) {
			mView.setVisibility(View.VISIBLE);
		}
		else {
			mView.setVisibility(View.INVISIBLE);
		}
	}
	
	private void initLayoutParams(Context context){
		mWindowManager = (WindowManager)context.getApplicationContext().getSystemService("window");
	    DisplayMetrics display = new DisplayMetrics();
	    mWindowManager.getDefaultDisplay().getMetrics(display);
	    mWinLayoutParams = new WindowManager.LayoutParams();

	    mWinLayoutParams.type = LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
	    mWinLayoutParams.token = mToken;
	    mWinLayoutParams.format = PixelFormat.RGBA_8888;
	    mWinLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;

	    mWinLayoutParams.x = 0;
	    mWinLayoutParams.y = 0;
	    mWinLayoutParams.width = LayoutParams.MATCH_PARENT;
	    mWinLayoutParams.height = LayoutParams.WRAP_CONTENT;
	}
	
	private void createFloatingView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		mView = inflater.inflate(R.layout.floating_toast_view, null);
		
		mWinLayoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		mWindowManager.addView(mView, mWinLayoutParams);
    }
}
