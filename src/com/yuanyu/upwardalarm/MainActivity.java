package com.yuanyu.upwardalarm;

import java.util.ArrayList;
import java.util.List;

import com.yuanyu.upwardalarm.model.Alarm;
import com.yuanyu.upwardalarm.test.TestActivity;
import com.yuanyu.upwardalarm.ui.AlarmListAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ListActivity {

	private final static int ACTIVITY_ALARM_DEFINE = 0;
	
	
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
		else if(item.getItemId() == R.id.action_test) {
			Intent intent = new Intent(MainActivity.this, TestActivity.class);
			startActivity(intent);
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
		if(alarm.getEnable()) {
			alarm.register(this);
		}
		alarm.saveToFile(this);
	}
	
	/**
	 * Unregister the alarm from android system
	 */
	private void unregisterAlarm(Alarm alarm) {
		alarm.unregister(this);
	}
}
