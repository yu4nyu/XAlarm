package com.yuanyu.xalarm.model;

import java.util.List;

import com.yuanyu.xalarm.AlarmBroadcastReceiver;
import com.yuanyu.xalarm.BootReceiver;

import android.content.Context;
import android.util.Log;

public class BroadcastEnabler {

	private final static String TAG = "BroadcastEnabler";
	
	private static boolean hasEnabledAlarm(List<Alarm> allAlarms) {
		for(Alarm alarm : allAlarms) {
			if(alarm.getEnable()) {
				return true;
			}
		}
		return false;
	}
	
	public static void determine(Context context, List<Alarm> allAlarms) {
		if(hasEnabledAlarm(allAlarms)) {
			AlarmBroadcastReceiver.setEnabled(context, true);
			BootReceiver.setEnabled(context, true);
			Log.d(TAG, "Enable all broadcasts");
		}
		else {
			AlarmBroadcastReceiver.setEnabled(context, false);
			BootReceiver.setEnabled(context, false);
			Log.d(TAG, "Disable all broadcasts");
		}
	}
	
	private BroadcastEnabler() {} // No instance
}
