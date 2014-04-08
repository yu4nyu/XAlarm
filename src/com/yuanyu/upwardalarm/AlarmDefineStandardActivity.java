package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.Alarm;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class AlarmDefineStandardActivity extends Activity implements View.OnClickListener {
	
	private final static int ACTIVITY_RINGTONE_PICKER = 0;
	
	public final static String EXTRA_ALARM = "alarm";
	
	private TextView mLabelTxt;
	private Switch mSwitch;
	private TimePicker mTimePicker;
	private TextView mRingtoneTxt;
	private CheckBox mVibrateCheck;
	
	private ToggleButton mSunday;
	private ToggleButton mMonday;
	private ToggleButton mTuesday;
	private ToggleButton mWednesday;
	private ToggleButton mThursday;
	private ToggleButton mFriday;
	private ToggleButton mSaturday;
	
	private Button mDoneBtn;
	
	private String mLabel = "";
	private String mRingtone = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_define_standard);
		
		initViews();
		setOnClickListeners();
	}

	private void initViews() {
		mLabelTxt = (TextView) findViewById(R.id.activity_alarm_define_label);
		mSwitch = (Switch) findViewById(R.id.activity_alarm_define_switch);
		mTimePicker = (TimePicker) findViewById(R.id.activity_alarm_define_time_picker);
		mRingtoneTxt = (TextView) findViewById(R.id.activity_alarm_define_ringtone);
		mVibrateCheck = (CheckBox) findViewById(R.id.activity_alarm_define_vibrate);
		
		mSunday = (ToggleButton) findViewById(R.id.activity_alarm_define_sunday_toggle);
		mMonday = (ToggleButton) findViewById(R.id.activity_alarm_define_monday_toggle);
		mTuesday = (ToggleButton) findViewById(R.id.activity_alarm_define_tuesday_toggle);
		mWednesday = (ToggleButton) findViewById(R.id.activity_alarm_define_wednesday_toggle);
		mThursday = (ToggleButton) findViewById(R.id.activity_alarm_define_thursday_toggle);
		mFriday = (ToggleButton) findViewById(R.id.activity_alarm_define_friday_toggle);
		mSaturday = (ToggleButton) findViewById(R.id.activity_alarm_define_saturday_toggle);
		
		mDoneBtn = (Button) findViewById(R.id.activity_alarm_define_done_btn);
	}
	
	private void setOnClickListeners() {
		mLabelTxt.setOnClickListener(this);
		mRingtoneTxt.setOnClickListener(this);
		mDoneBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.activity_alarm_define_label:
			showTitleDefineDialog();
			break;
		case R.id.activity_alarm_define_ringtone:
			showRingtonePicker();
			break;
		case R.id.activity_alarm_define_done_btn:
			done();
			break;
		}
	}
	
	private void showTitleDefineDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final EditText edit = new EditText(this);
		builder.setTitle(R.string.label_define_dialog_title)
			.setView(edit)
			.setNegativeButton(android.R.string.cancel, null)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mLabel = edit.getEditableText().toString();
					mLabelTxt.setText(mLabel);
				}
			});
		builder.show();
	}
	
	private void showRingtonePicker() {
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.ringtone_picker_title));
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
		startActivityForResult(intent, ACTIVITY_RINGTONE_PICKER);
	}
	
	private void done() {
		
		Alarm.Builder builder = new Alarm.Builder(this);
		builder.setLable(mLabel)
			.setEnable(mSwitch.isChecked())
			.setHour(mTimePicker.getCurrentHour())
			.setMinute(mTimePicker.getCurrentMinute())
			.setRingtone(mRingtone)
			.setVibrateEnable(mVibrateCheck.isChecked())
			.enableRepeat(true) // TODO attach to a checkbox
			.setWeekRepeat(mSunday.isChecked(),
					mMonday.isChecked(),
					mTuesday.isChecked(),
					mWednesday.isChecked(),
					mThursday.isChecked(),
					mFriday.isChecked(),
					mSaturday.isChecked())
			;
		Alarm alarm = builder.build();
		
		Intent intent = new Intent();
		intent.putExtra(EXTRA_ALARM, alarm);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == ACTIVITY_RINGTONE_PICKER && resultCode == Activity.RESULT_OK) {
			Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			if(uri != null) {
				Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
				mRingtone = ringtone.getTitle(this);
				
				int start = 0;
				int lastSlash = mRingtone.lastIndexOf("/");
				if(lastSlash != -1) {
					start = lastSlash + 1;
				}
				int end = mRingtone.length();
				int lastPoint = mRingtone.lastIndexOf(".");
				if(lastPoint != -1) {
					end = lastPoint;
				}
				String fileName = mRingtone.substring(start, end);
				mRingtoneTxt.setText(fileName);
			}
		}
	}
}
