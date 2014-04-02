package com.yuanyu.upwardalarm;

import java.io.File;
import java.io.Serializable;

public class Alarm implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mLabel;
	
	private boolean mEnable;
	
	private int mHour;
	private int mMinute;
	
	private String mRingtone;
	
	private boolean mVibrate;
	
	private boolean[] mRepeat = new boolean[7]; // Begin with Sunday
	
	private Alarm() {
		mLabel = "";
		mEnable = false;
		mHour = 8;
		mMinute = 0;
		mRingtone = "";
		mVibrate = true;
		for(int i = 0; i < mRepeat.length; i++) {
			mRepeat[i] = true;
		}
	}
	
	private Alarm(Alarm alarm) {
		mLabel = alarm.mLabel;
		mEnable = alarm.mEnable;
		mHour = alarm.mHour;
		mMinute = alarm.mMinute;
		mRingtone = alarm.mRingtone;
		mVibrate = alarm.mVibrate;
		for(int i = 0; i < mRepeat.length; i++) {
			mRepeat[i] = alarm.mRepeat[i];
		}
	}
	
	public String getLabel() {
		return mLabel;
	}
	
	public boolean getEnable() {
		return mEnable;
	}
	
	public int getHour() {
		return mHour;
	}
	
	public int getMinute() {
		return mMinute;
	}
	
	/**
	 * @return null if can't get the valid File object
	 */
	public File getRingtone() {
		if(mRingtone == null || mRingtone.isEmpty()) {
			return null;
		}
		File file = new File(mRingtone);
		if(!file.isFile() || !file.exists()) {
			return null;
		}
		return file;
	}
	
	public boolean getVibrateEnable() {
		return mVibrate;
	}
	
	public boolean isSundayRepeat() {
		return mRepeat[0];
	}
	
	public boolean isMondayRepeat() {
		return mRepeat[1];
	}
	
	public boolean isTuesdayRepeat() {
		return mRepeat[1];
	}
	
	public boolean isWednesdayRepeat() {
		return mRepeat[1];
	}
	
	public boolean isThursdayRepeat() {
		return mRepeat[1];
	}
	
	public boolean isFridatRepeat() {
		return mRepeat[1];
	}
	
	public boolean isSaturdayRepeat() {
		return mRepeat[1];
	}
	
	public boolean isRepeatEveryday() {
		for(int i = 0; i < mRepeat.length; i++) {
			if(!mRepeat[i]) {
				return false;
			}
		}
		return true;
	}
	
	public static class Builder {
		
		Alarm mAlarm;
		
		public Builder() {
			mAlarm = new Alarm();
		}
		
		public void setLable(String label) {
			mAlarm.mLabel = label;
		}
		
		public void setEnable(boolean enable) {
			mAlarm.mEnable = enable;
		}
		
		public void setHour(int hour) {
			mAlarm.mHour = hour;
		}
		
		public void setMinute(int minute) {
			mAlarm.mMinute = minute;
		}
		
		public void setRingtone(String ringtone) {
			mAlarm.mRingtone = ringtone;
		}
		
		public void setVibrateEnable(boolean enable) {
			mAlarm.mVibrate = enable;
		}
		
		public void setRepeat(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat) {
			mAlarm.mRepeat[0] = sun;
			mAlarm.mRepeat[1] = mon;
			mAlarm.mRepeat[2] = tue;
			mAlarm.mRepeat[3] = wed;
			mAlarm.mRepeat[4] = thu;
			mAlarm.mRepeat[5] = fri;
			mAlarm.mRepeat[6] = sat;
		}
		
		public void setRepeatEveryday() {
			for(int i = 0; i < mAlarm.mRepeat.length; i++) {
				mAlarm.mRepeat[i] = true;
			}
		}
		
		public Alarm build() {
			return new Alarm(mAlarm);
		}
	}
}
