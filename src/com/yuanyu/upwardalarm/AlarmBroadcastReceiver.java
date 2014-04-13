package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.Alarm;
import com.yuanyu.upwardalarm.model.Manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

	public final static String EXTRA_ALARM_ID = "alarm_id"; // int extra
	public final static String EXTRA_IS_VIBRATE = "is_vibrate"; // boolean extra
	public final static String EXTRA_RINGTONE_URI = "ringtone"; // String extra
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		int id = intent.getIntExtra(EXTRA_ALARM_ID, -1);
		if(id >= 0) {
			Alarm alarm = Manager.INSTANCE.getSavedAlarmById(context, id);
			resetAlarmIfRepeat(context, alarm);
		}
		
		Intent i = new Intent(context, AlarmGoOffActivity.class);
		i.putExtra(EXTRA_IS_VIBRATE, intent.getBooleanExtra(EXTRA_IS_VIBRATE, false));
		i.putExtra(EXTRA_RINGTONE_URI, intent.getStringExtra(EXTRA_RINGTONE_URI));
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
	
	private void resetAlarmIfRepeat(Context context, Alarm alarm) {
		Manager.INSTANCE.resetIfRepeat(context, alarm);
	}
}
