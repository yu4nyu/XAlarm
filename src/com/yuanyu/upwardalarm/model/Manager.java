package com.yuanyu.upwardalarm.model;

import android.content.Context;
import android.content.SharedPreferences;

public enum Manager {
	
	INSTANCE;
	
	public final static int GET_UNIQUE_ID_ERROR = -1;
	
	private final static String PREFS_KEY = "prefs";
	private final static String PREFS_UNIQUE_ID_KEY = "unique_id";
	
	int getUniqueId(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
		int result = sp.getInt(PREFS_UNIQUE_ID_KEY, -1);
		
		if(result < 0) {
			result = 0;
			SharedPreferences.Editor editor = sp.edit();
			editor.putInt(PREFS_UNIQUE_ID_KEY, result + 1); // Error will occur if result+1 > Integer.MAX. But... :)
			editor.apply();
		}
		
		return result;
	}
}
