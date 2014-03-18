package com.yuanyu.upwardalarm;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ListActivity {

	private final static int ACTIVITY_ALARM_DEFINE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == ACTIVITY_ALARM_DEFINE) {
			if(resultCode == Activity.RESULT_OK) {
				// TODO
			}
		}
	}
}
