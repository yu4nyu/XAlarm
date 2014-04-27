package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.Alarm;
import com.yuanyu.upwardalarm.model.Constants;
import com.yuanyu.upwardalarm.model.Manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

	public final static String EXTRA_ALARM_ID = "alarm_id"; // Integer extra
	public final static String EXTRA_ALARM_LABEL = "alarm_label"; // String extra
	public final static String EXTRA_IS_VIBRATE = "is_vibrate"; // boolean extra
	public final static String EXTRA_RINGTONE_URI = "ringtone"; // String extra
	
	public final static String EXTRA_STOP_WAY = "movement_type";
	public final static String EXTRA_STOP_LEVEL = "stop_level";
	public final static String EXTRA_STOP_TIMES = "stop_times";
	
	public final static String BROADCAST_ACTION_UPDATE_ITEM = "com.yuanyu.upwardalarm.broadcastActionUpdateItem";
	public final static String BROADCAST_KEY_ALARM_ID = "broadcast_alarm_id";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		int id = intent.getIntExtra(EXTRA_ALARM_ID, -1);
		if(id < 0) {
			return;
		}
		
		Alarm alarm = Manager.INSTANCE.getSavedAlarmById(context, id);
		if(alarm == null) {
			return;
		}
		resetAlarmIfRepeat(context, alarm);
		
		Intent i = new Intent(context, AlarmGoOffActivity.class);
		i.putExtra(EXTRA_ALARM_LABEL, intent.getStringExtra(EXTRA_ALARM_LABEL));
		i.putExtra(EXTRA_IS_VIBRATE, intent.getBooleanExtra(EXTRA_IS_VIBRATE, false));
		i.putExtra(EXTRA_RINGTONE_URI, intent.getStringExtra(EXTRA_RINGTONE_URI));
		i.putExtra(EXTRA_STOP_WAY, intent.getIntExtra(EXTRA_STOP_WAY, Constants.STOP_WAY_BUTTON));
		i.putExtra(EXTRA_STOP_LEVEL, intent.getIntExtra(EXTRA_STOP_LEVEL, Constants.LEVEL_EASY));
		i.putExtra(EXTRA_STOP_TIMES, intent.getIntExtra(EXTRA_STOP_TIMES, 1));
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
	
	private void resetAlarmIfRepeat(Context context, Alarm alarm) {
		Manager.INSTANCE.resetForNextTime(context, alarm);
		if(!alarm.isRepeat()) {
			alarm.setEnabled(false);
			Manager.INSTANCE.saveAlarm(context, alarm);
			
			updateAlarmItem(context, alarm.getId());
		}
	}
	
	/**
	 * Send a local broadcast to update the alarm list
	 */
	private void updateAlarmItem(Context context, int alarmId) {
		LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(context);
		Intent intent = new Intent(BROADCAST_ACTION_UPDATE_ITEM);
		intent.putExtra(BROADCAST_KEY_ALARM_ID, alarmId);
		broadcaster.sendBroadcast(intent);
	}
}
