package com.yuanyu.xalarm;

import java.util.List;

import com.yuanyu.xalarm.model.Alarm;
import com.yuanyu.xalarm.model.Manager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class BootReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            List<Alarm> alarms = Manager.INSTANCE.getSavedAlarms(context);
            for(Alarm alarm : alarms) {
            	if(alarm.getEnable()) {
            		Manager.INSTANCE.register(context, alarm);
            	}
            }
        }
	}
	
	public static void setEnabled(Context context, boolean enabled) {
		ComponentName receiver = new ComponentName(context, BootReceiver.class);
		PackageManager pm = context.getPackageManager();
		if(enabled) {
			pm.setComponentEnabledSetting(receiver,
			        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
			        PackageManager.DONT_KILL_APP);
		}
		else {
			pm.setComponentEnabledSetting(receiver,
			        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
			        PackageManager.DONT_KILL_APP);
		}
	}
}
