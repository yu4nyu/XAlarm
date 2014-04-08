package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.Alarm;
import com.yuanyu.upwardalarm.model.Manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

	public final static String EXTRA_ALARM_ID = "alarm_id";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		int id = intent.getIntExtra(EXTRA_ALARM_ID, -1);
		if(id >= 0) {
			Alarm alarm = Manager.INSTANCE.getSavedAlarmById(context, id);
			resetAlarmIfRepeat(context, alarm);
		}
		
		Intent i = new Intent(context, AlarmGoOffActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
	
	private void resetAlarmIfRepeat(Context context, Alarm alarm) {
		alarm.resetIfRepeat(context);
	}
}
