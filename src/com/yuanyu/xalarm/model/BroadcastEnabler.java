package com.yuanyu.xalarm.model;

import java.util.List;

import com.yuanyu.xalarm.AlarmBroadcastReceiver;
import com.yuanyu.xalarm.BootReceiver;

import android.content.Context;

public class BroadcastEnabler {

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
		}
		else {
			AlarmBroadcastReceiver.setEnabled(context, false);
			BootReceiver.setEnabled(context, false);
		}
	}
	
	private BroadcastEnabler() {} // No instance
}
