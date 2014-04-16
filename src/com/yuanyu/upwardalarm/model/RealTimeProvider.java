package com.yuanyu.upwardalarm.model;

import java.util.Calendar;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

public class RealTimeProvider {

	private TextView mText;
	private Handler mHandler = new Handler();
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			updateTime();
		}
	};
	
	public void start(TextView textView) {
		mText = textView;
		updateTime();
	}
	
	public void stop() {
		mText = null;
		mHandler.removeCallbacksAndMessages(null);
	}
	
	// TODO use SystemClock.uptimeMillis()
	private void updateTime() {
		Log.d("YY", "updateTime");
		Log.d("YY", "start time system clock = " + SystemClock.uptimeMillis());
		Log.d("YY", "System.currentTimeMillis() = " + System.currentTimeMillis());
		if(mText == null) return;
		
		long time = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.MILLISECOND, 0);
		
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		String result = addZeroIfLessThanTen(hour) + ":" + addZeroIfLessThanTen(minute) + ":" + addZeroIfLessThanTen(second);
		mText.setText(result);
		
		Log.d("YY", "set time = " + calendar.getTimeInMillis());
		boolean r = mHandler.postAtTime(mRunnable, calendar.getTimeInMillis() + 1000);
		Log.d("YY", "" + r);
	}
	
	private String addZeroIfLessThanTen(int number) {
		if(number < 10) {
			return "0" + number;
		}
		return "" + number;
	}
}
