package com.yuanyu.upwardalarm.model;

import java.io.Serializable;

import android.content.Context;

public class Alarm implements Serializable {

	private static final long serialVersionUID = 1L;

	private int mId;

	private String mLabel;

	private boolean mEnable;

	private int mHour;
	private int mMinute;

	private String mRingtoneUri;

	private boolean mVibrate;

	private boolean mRepeat;
	private boolean[] mWeekRepeat = new boolean[7]; // Begin with Sunday

	private Alarm() {
		mLabel = "";
		mEnable = false;
		mHour = 8;
		mMinute = 0;
		mRingtoneUri = "";
		mVibrate = true;
		mRepeat = false;
		for(int i = 0; i < mWeekRepeat.length; i++) {
			mWeekRepeat[i] = true;
		}
	}

	private Alarm(Alarm alarm) {
		mId = alarm.mId;
		mLabel = alarm.mLabel;
		mEnable = alarm.mEnable;
		mHour = alarm.mHour;
		mMinute = alarm.mMinute;
		mRingtoneUri = alarm.mRingtoneUri;
		mVibrate = alarm.mVibrate;
		mRepeat = alarm.mRepeat;
		for(int i = 0; i < mWeekRepeat.length; i++) {
			mWeekRepeat[i] = alarm.mWeekRepeat[i];
		}
	}
	
	public static Builder newBuilder(Context context) {
		return new Builder(context);
	}

	public static Builder getBuilder(Alarm alarm) {
		Builder builder = new Builder();
		builder.mAlarm = new Alarm(alarm);
		return builder;
	}
	
	public void setEnabled(boolean enabled) {
		mEnable = enabled;
	}

	public int getId() {
		return mId;
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

	public String getRingtoneUri() {
		return mRingtoneUri;
	}

	public boolean getVibrateEnable() {
		return mVibrate;
	}
	
	public boolean[] getWeekRepeat() {
		return mWeekRepeat;
	}

	public boolean isSundayRepeat() {
		return mWeekRepeat[0];
	}

	public boolean isMondayRepeat() {
		return mWeekRepeat[1];
	}

	public boolean isTuesdayRepeat() {
		return mWeekRepeat[2];
	}

	public boolean isWednesdayRepeat() {
		return mWeekRepeat[3];
	}

	public boolean isThursdayRepeat() {
		return mWeekRepeat[4];
	}

	public boolean isFridayRepeat() {
		return mWeekRepeat[5];
	}

	public boolean isSaturdayRepeat() {
		return mWeekRepeat[6];
	}

	public boolean isRepeat() {
		return mRepeat;
	}

	/**
	 * @return false if isRepeat() returns false,
	 * then it determines if repeat everyday in a whole week.
	 */
	public boolean isRepeatWholeWeek() {
		if(!mRepeat) return false;
		for(int i = 0; i < mWeekRepeat.length; i++) {
			if(!mWeekRepeat[i]) {
				return false;
			}
		}
		return true;
	}

	public static class Builder {

		Alarm mAlarm;

		private Builder() {
			// Do nothing here
		}

		private Builder(Context context) {
			mAlarm = new Alarm();
			mAlarm.mId = Manager.INSTANCE.getUniqueId(context);
		}

		public Builder setLable(String label) {
			mAlarm.mLabel = label;
			return this;
		}

		public Builder setEnable(boolean enable) {
			mAlarm.mEnable = enable;
			return this;
		}

		public Builder setHour(int hour) {
			mAlarm.mHour = hour;
			return this;
		}

		public Builder setMinute(int minute) {
			mAlarm.mMinute = minute;
			return this;
		}

		public Builder setRingtoneUri(String ringtoneUri) {
			mAlarm.mRingtoneUri = ringtoneUri;
			return this;
		}

		public Builder setVibrateEnable(boolean enable) {
			mAlarm.mVibrate = enable;
			return this;
		}

		public Builder enableRepeat(boolean enable) {
			mAlarm.mRepeat = enable;
			return this;
		}

		/**
		 * This only works if enableRepeat(true) set
		 */
		public Builder setWeekRepeat(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat) {
			mAlarm.mWeekRepeat[0] = sun;
			mAlarm.mWeekRepeat[1] = mon;
			mAlarm.mWeekRepeat[2] = tue;
			mAlarm.mWeekRepeat[3] = wed;
			mAlarm.mWeekRepeat[4] = thu;
			mAlarm.mWeekRepeat[5] = fri;
			mAlarm.mWeekRepeat[6] = sat;
			return this;
		}

		/**
		 * This only works if enableRepeat(true) set
		 */
		public Builder setRepeatEveryday() {
			for(int i = 0; i < mAlarm.mWeekRepeat.length; i++) {
				mAlarm.mWeekRepeat[i] = true;
			}
			return this;
		}

		public Alarm build() {
			return new Alarm(mAlarm);
		}
	}
}
