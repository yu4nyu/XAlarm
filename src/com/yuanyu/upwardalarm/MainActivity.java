package com.yuanyu.upwardalarm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.yuanyu.upwardalarm.model.Alarm;
import com.yuanyu.upwardalarm.model.Manager;
import com.yuanyu.upwardalarm.model.Utils;
import com.yuanyu.upwardalarm.ui.AlarmItemsManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;

public class MainActivity extends Activity implements AlarmStopConfigDialog.OnAlarmStopConfiguredListener {

	private final static int ACTIVITY_ALARM_DEFINE = 0;
	public final static int ACTIVITY_ALARM_EDIT = 1;

	private AlarmItemsManager mManager;
	ShareActionProvider mShareActionProvider;
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int alarmId = intent.getIntExtra(AlarmBroadcastReceiver.BROADCAST_KEY_ALARM_ID, -1);
            if(alarmId >= 0) {
            	mManager.disable(alarmId);
            }
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		View emptyText = findViewById(R.id.activity_main_empty_text);

		List<Alarm> data = new ArrayList<Alarm>();
		data.addAll(Manager.INSTANCE.getSavedAlarms(this));
		mManager = new AlarmItemsManager(this, data, emptyText);

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
	protected void onStart() {
		super.onStart();
		LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
				new IntentFilter(AlarmBroadcastReceiver.BROADCAST_ACTION_UPDATE_ITEM));
	}

	@Override
	protected void onStop() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		// TODO add google play address
		MenuItem shareItem = menu.findItem(R.id.action_share);
		mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
		String shareText = getString(R.string.share_text, getString(R.string.app_name));
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        mShareActionProvider.setShareIntent(shareIntent);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_add:
			Intent intent = new Intent(MainActivity.this, AlarmDefineActivity.class);
			startActivityForResult(intent, ACTIVITY_ALARM_DEFINE);
			overridePendingTransition(R.anim.shift_in_from_right, R.anim.shift_out_to_left);
			break;
		case R.id.action_test:
			AlarmStopConfigDialog stopCofig = new AlarmStopConfigDialog();
			stopCofig.setOnAlarmStopConfiguredListener(this);
			stopCofig.show(getFragmentManager(), "Test sensor");
			break;
		/*case R.id.action_test_debug: // For test only
			Intent i = new Intent(MainActivity.this, TestActivity.class);
			startActivity(i);
			break;*/
		case R.id.action_about:
			AboutDialog about = new AboutDialog();
			about.show(getFragmentManager(), "About");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case ACTIVITY_ALARM_DEFINE:
			if(resultCode == Activity.RESULT_OK) {
				Alarm alarm = (Alarm) data.getSerializableExtra(AlarmDefineActivity.EXTRA_ALARM);
				mManager.add(alarm);
				registerAlarm(alarm);
				showToastMessage(alarm);
			}
			break;
		case ACTIVITY_ALARM_EDIT:
			if(resultCode == Activity.RESULT_OK) {
				Alarm alarm = (Alarm) data.getSerializableExtra(AlarmDefineActivity.EXTRA_ALARM);
				int position = data.getIntExtra(AlarmDefineActivity.EXTRA_POSITION, -1);
				if(mManager.update(position, alarm)) { // Update succeeded
					registerAlarm(alarm); // The existed alarm will be replaced
					showToastMessage(alarm);
				}
			}
			break;
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(0, R.anim.exit_out_animation);
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

	private void showToastMessage(Alarm alarm) {
		if(alarm.getEnable()) {
			String message = Utils.getTextTimeBeforeGoOff(this, alarm);
			if(!message.isEmpty()) {
				Manager.INSTANCE.showToast(this, message);
			}
		}
	}

	@Override
	public void onAlarmStopConfigured(int type, int level, int times) {
		Intent i = new Intent(this, AlarmGoOffActivity.class);
		i.putExtra(AlarmBroadcastReceiver.EXTRA_ALARM_LABEL, getString(R.string.action_test));
		i.putExtra(AlarmBroadcastReceiver.EXTRA_IS_VIBRATE, true);
		i.putExtra(AlarmBroadcastReceiver.EXTRA_RINGTONE_URI, "");
		i.putExtra(AlarmBroadcastReceiver.EXTRA_STOP_WAY, type);
		i.putExtra(AlarmBroadcastReceiver.EXTRA_STOP_LEVEL, level);
		i.putExtra(AlarmBroadcastReceiver.EXTRA_STOP_TIMES, times);
		startActivity(i);
	}
}
