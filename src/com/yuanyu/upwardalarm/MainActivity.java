package com.yuanyu.upwardalarm;

import android.os.Bundle;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

public class MainActivity extends ListActivity {

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
			showTimePickerDialog();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showTimePickerDialog() {
		TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener(){
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO
			}
		};
		TimePickerDialog dialog = new TimePickerDialog(this, timeSetListener, 8, 0, true);
		dialog.show();
	}
}
