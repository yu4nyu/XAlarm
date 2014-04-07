package com.yuanyu.upwardalarm;

import java.util.ArrayList;
import java.util.List;

import com.yuanyu.upwardalarm.model.Alarm;
import com.yuanyu.upwardalarm.ui.AlarmListAdapter;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ListActivity {

	private final static int ACTIVITY_ALARM_DEFINE = 0;
	
	private final static String INTENT_DATA_PREFIX = "com.yuanyu.upwardalarm:";
	
	private List<Alarm> mData = new ArrayList<Alarm>();
	private AlarmListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// TODO get alarm data from file

		mAdapter = new AlarmListAdapter(this, mData);
		setListAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_add) {
			Intent intent = new Intent(MainActivity.this, AlarmDefineStandardActivity.class);
			startActivityForResult(intent, ACTIVITY_ALARM_DEFINE);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == ACTIVITY_ALARM_DEFINE) {
			if(resultCode == Activity.RESULT_OK) {
				Alarm alarm = (Alarm) data.getSerializableExtra(AlarmDefineStandardActivity.EXTRA_ALARM);
				mData.add(alarm);
				mAdapter.notifyDataSetChanged();
				registerAlarm(alarm);
			}
		}
	}
	
	/**
	 * Register the alarm to android system
	 */
	private void registerAlarm(Alarm alarm) {
		Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
		intent.setData(Uri.parse(INTENT_DATA_PREFIX + alarm.getId()));
		PendingIntent alarmPending = PendingIntent.getBroadcast(this, alarm.getId(), intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTimeMillis(), alarmPending);
	}
	
	/**
	 * Unregister the alarm from android system
	 */
	private void unregisterAlarm(Alarm alarm) {
		Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
		intent.setData(Uri.parse(INTENT_DATA_PREFIX + alarm.getId()));
		PendingIntent alarmPending = PendingIntent.getBroadcast(this, alarm.getId(), intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(alarmPending);
	}
}
