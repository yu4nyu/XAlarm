package com.yuanyu.xalarm;

import java.util.List;

import com.yuanyu.xalarm.model.Alarm;
import com.yuanyu.xalarm.model.Manager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("YY", "onReceive() of BootReceiver");
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Log.d("YY", "ACTION_BOOT_COMPLETED");
            List<Alarm> alarms = Manager.INSTANCE.getSavedAlarms(context);
            for(Alarm alarm : alarms) {
            	Log.d("YY", "Alarm " + alarm.getId());
            	if(alarm.getEnable()) {
            		Log.d("YY", "is enabled");
            		Manager.INSTANCE.register(context, alarm);
            	}
            }
        }
	}
	
	// TODO use this and do the same thing for another receiver
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
