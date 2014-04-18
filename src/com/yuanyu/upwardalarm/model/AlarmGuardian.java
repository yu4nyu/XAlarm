package com.yuanyu.upwardalarm.model;

import android.content.Context;
import android.content.SharedPreferences;

public class AlarmGuardian {

	private final static String PREFS_IS_GOT_OFF_KEY = "is_got_off";
	private final static String PREFS_IS_VIBRATE_KEY = "is_vibrate";
	private final static String PREFS_RINGTONE_URI_KEY = "ringtone_uri";
	
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
}
