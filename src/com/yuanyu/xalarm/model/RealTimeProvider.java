package com.yuanyu.xalarm.model;

import java.util.Calendar;

import android.os.Handler;
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
		mHandler = null;
	}
	
	private void updateTime() {
		if(mText == null) return;
		
		long time = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		String result = addZeroIfLessThanTen(hour) + ":" + addZeroIfLessThanTen(minute) + ":" + addZeroIfLessThanTen(second);
		mText.setText(result);
		
		mHandler.postDelayed(mRunnable, 1000);
	}
	
	private String addZeroIfLessThanTen(int number) {
		if(number < 10) {
			return "0" + number;
		}
		return "" + number;
	}
}
