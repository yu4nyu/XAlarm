package com.yuanyu.xalarm.ui;

import com.yuanyu.xalarm.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

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
	
	private void createFloatingView(final Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		mView = inflater.inflate(R.layout.floating_toast_view, null);
		
		mWinLayoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		mWindowManager.addView(mView, mWinLayoutParams);
		
		Button buyButton = (Button) mView.findViewById(R.id.floating_toast_view_buy_button);
		buyButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				final String appPackageName = context.getPackageName() + "pro";
				try {
					context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
				}
			}
		});
    }
}
