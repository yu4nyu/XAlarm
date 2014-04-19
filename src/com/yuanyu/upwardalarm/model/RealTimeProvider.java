package com.yuanyu.upwardalarm.model;

import java.util.Calendar;

import android.os.Handler;
import android.os.SystemClock;
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
		long uptime = SystemClock.uptimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.MILLISECOND, 0);
		
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		String result = addZeroIfLessThanTen(hour) + ":" + addZeroIfLessThanTen(minute) + ":" + addZeroIfLessThanTen(second);
		mText.setText(result);
		
		long nextTime = uptime / 1000 * 1000; // Set the last 3 numbers to 0
		mHandler.postAtTime(mRunnable, nextTime);
	}
	
	private String addZeroIfLessThanTen(int number) {
		if(number < 10) {
			return "0" + number;
		}
		return "" + number;
	}
}
