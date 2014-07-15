package com.yuanyu.xalarm;

import java.util.List;

import com.yuanyu.xalarm.model.Alarm;
import com.yuanyu.xalarm.model.Manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            List<Alarm> alarms = Manager.INSTANCE.getSavedAlarms(context);
            for(Alarm alarm : alarms) {
            	if(alarm.getEnable()) {
            		Manager.INSTANCE.register(context, alarm);
            	}
            }
        }
	}
}
