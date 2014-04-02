package com.yuanyu.upwardalarm;

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

	private final static String TAG = "AlarmDefineActivity";
	
	private final static int ACTIVITY_RINGTONE_PICKER = 0;
	
	public final static String EXTRA_LABEL = "label"; // String extra
	public final static String EXTRA_ENABLE = "enable"; // Boolean extra
	public final static String EXTRA_HOUR = "hour"; // Integer extra
	public final static String EXTRA_MINUTE = "minute"; // Integer extra
	public final static String EXTRA_RINGTONE = "ringtone"; // String extra, absolute path of ringtone file
	public final static String EXTRA_VIBRATE = "vibrate"; // Boolean extra
	public final static String EXTRA_REPEAT_SUNDAY = "sunday"; // Boolean extra;
	public final static String EXTRA_REPEAT_MONDAY = "monday"; // Boolean extra;
	public final static String EXTRA_REPEAT_TUESDAY = "tuesday"; // Boolean extra;
	public final static String EXTRA_REPEAT_WEDNESDAY = "wednesday"; // Boolean extra;
	public final static String EXTRA_REPEAT_THURSDAY = "thursday"; // Boolean extra;
	public final static String EXTRA_REPEAT_FRIDAY = "friday"; // Boolean extra;
	public final static String EXTRA_REPEAT_SATURDAY = "saturday"; // Boolean extra;
	
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
		// TODO just put a serializable Alarm object as extra
		Intent intent = new Intent();
		intent.putExtra(EXTRA_LABEL, mLabel);
		intent.putExtra(EXTRA_ENABLE, mSwitch.isChecked());
		intent.putExtra(EXTRA_HOUR, mTimePicker.getCurrentHour());
		intent.putExtra(EXTRA_MINUTE, mTimePicker.getCurrentMinute());
		intent.putExtra(EXTRA_RINGTONE, mRingtone);
		intent.putExtra(EXTRA_VIBRATE, mVibrateCheck.isChecked());
		intent.putExtra(EXTRA_REPEAT_SUNDAY, mSunday.isChecked());
		intent.putExtra(EXTRA_REPEAT_MONDAY, mMonday.isChecked());
		intent.putExtra(EXTRA_REPEAT_TUESDAY, mTuesday.isChecked());
		intent.putExtra(EXTRA_REPEAT_WEDNESDAY, mWednesday.isChecked());
		intent.putExtra(EXTRA_REPEAT_THURSDAY, mThursday.isChecked());
		intent.putExtra(EXTRA_REPEAT_FRIDAY, mFriday.isChecked());
		intent.putExtra(EXTRA_REPEAT_SATURDAY, mSaturday.isChecked());
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
