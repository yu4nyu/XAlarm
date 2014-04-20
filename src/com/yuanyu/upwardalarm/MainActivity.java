package com.yuanyu.upwardalarm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.yuanyu.upwardalarm.model.Alarm;
import com.yuanyu.upwardalarm.model.Manager;
import com.yuanyu.upwardalarm.model.Utils;
import com.yuanyu.upwardalarm.test.TestActivity;
import com.yuanyu.upwardalarm.ui.AlarmItemsManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final static int ACTIVITY_ALARM_DEFINE = 0;
	public final static int ACTIVITY_ALARM_EDIT = 1;

	private AlarmItemsManager mManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		List<Alarm> data = new ArrayList<Alarm>();
		data.addAll(Manager.INSTANCE.getSavedAlarms(this));
		mManager = new AlarmItemsManager(this, data);

		ViewGroup scrollable = (ViewGroup) findViewById(R.id.activity_main_scroll_view);
		mManager.fillAlarmList(scrollable);

		// Show overflow option on every devices
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_add) {
			Intent intent = new Intent(MainActivity.this, AlarmDefineStandardActivity.class);
			startActivityForResult(intent, ACTIVITY_ALARM_DEFINE);
			overridePendingTransition(R.anim.shift_in_from_right, R.anim.shift_out_to_left);
		}
		else if(item.getItemId() == R.id.action_test) {
			// TODO
			Intent intent = new Intent(MainActivity.this, TestActivity.class);
			startActivity(intent);
			//TestActivity.startGoOffActivity(this);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case ACTIVITY_ALARM_DEFINE:
			if(resultCode == Activity.RESULT_OK) {
				Alarm alarm = (Alarm) data.getSerializableExtra(AlarmDefineStandardActivity.EXTRA_ALARM);
				mManager.add(alarm);
				registerAlarm(alarm);
			}
			break;
		case ACTIVITY_ALARM_EDIT:
			if(resultCode == Activity.RESULT_OK) {
				Alarm alarm = (Alarm) data.getSerializableExtra(AlarmDefineStandardActivity.EXTRA_ALARM);
				int position = data.getIntExtra(AlarmDefineStandardActivity.EXTRA_POSITION, -1);
				if(mManager.update(position, alarm)) { // Update succeeded
					registerAlarm(alarm); // The existed alarm will be replaced

					if(alarm.getEnable()) {
						String message = Utils.getTextTimeBeforeGoOff(this, alarm);
						if(!message.isEmpty()) {
							Toast.makeText(this, message, Toast.LENGTH_LONG).show();
						}
					}
				}
			}
			break;
		}
	}

	/**
	 * Register the alarm to android system
	 */
	private void registerAlarm(Alarm alarm) {
		if(alarm.getEnable()) {
			Manager.INSTANCE.register(this, alarm);
		}
		Manager.INSTANCE.saveAlarm(this, alarm);
	}
}
