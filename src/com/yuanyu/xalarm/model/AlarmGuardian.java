package com.yuanyu.xalarm.model;

import android.content.Context;
import android.content.SharedPreferences;

public class AlarmGuardian {

	private final static String PREFS_IS_GOT_OFF_KEY = "is_got_off";
	private final static String PREFS_IS_VIBRATE_KEY = "is_vibrate";
	private final static String PREFS_RINGTONE_URI_KEY = "ringtone_uri";
	
	private final static String PREFS_STOP_WAY = "stop_way";
	private final static String PREFS_STOP_LEVEL = "stop_level";
	private final static String PREFS_STOP_TIMES = "stop_times";
	private final static String PREFS_STOP_TIMES_COUNT = "times_count";
	
	public static void markGotOff(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(PREFS_IS_GOT_OFF_KEY, true);
		editor.apply();
	}
	
	public static void markStopped(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(PREFS_IS_GOT_OFF_KEY, false);
		editor.apply();
	}
	
	public static boolean isGotOff(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		return sp.getBoolean(PREFS_IS_GOT_OFF_KEY, false);
	}
	
	public static void saveIsVibrate(Context context, boolean isVibrate) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(PREFS_IS_VIBRATE_KEY, isVibrate);
		editor.apply();
	}
	
	public static boolean getIsVibrate(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		return sp.getBoolean(PREFS_IS_VIBRATE_KEY, false);
	}
	
	public static void saveRingtoneUri(Context context, String uri) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(PREFS_RINGTONE_URI_KEY, uri);
		editor.apply();
	}
	
	public static String getRingtoneUri(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		return sp.getString(PREFS_RINGTONE_URI_KEY, null);
	}
	
	public static void saveStopWay(Context context, int way) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(PREFS_STOP_WAY, way);
		editor.apply();
	}
	
	public static int getStopWay(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		return sp.getInt(PREFS_STOP_WAY, Constants.STOP_WAY_BUTTON);
	}
	
	public static void saveStopLevel(Context context, int level) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(PREFS_STOP_LEVEL, level);
		editor.apply();
	}
	
	public static int getStopLevel(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		return sp.getInt(PREFS_STOP_LEVEL, Constants.LEVEL_EASY);
	}
	
	public static void saveStopTimes(Context context, int times) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(PREFS_STOP_TIMES, times);
		editor.apply();
	}
	
	public static int getStopTimes(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		return sp.getInt(PREFS_STOP_TIMES, 1);
	}
	
	public static void saveStopTimesCount(Context context, int count) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(PREFS_STOP_TIMES_COUNT, count);
		editor.apply();
	}
	
	public static int getStopTimesCount(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Manager.PREFS_KEY, Context.MODE_PRIVATE);
		return sp.getInt(PREFS_STOP_TIMES_COUNT, 0);
	}
}
