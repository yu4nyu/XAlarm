package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.Alarm;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class AlarmDefineStandardActivity extends Activity implements View.OnClickListener,
	OnCheckedChangeListener {
	
	private final static int ACTIVITY_RINGTONE_PICKER = 0;
	
	public final static String EXTRA_ALARM = "alarm";
	
	private View mLabelLayout;
	private TextView mLabelTxt;
	
	private Switch mSwitch;
	private TimePicker mTimePicker;
	
	private View mRingtoneLayout;
	private TextView mRingtoneTxt;
	
	private CheckBox mVibrateCheck;
	
	private CheckBox mRepeatCheck;
	private View mRepetitionLayout;
	private ToggleButton mSunday;
	private ToggleButton mMonday;
	private ToggleButton mTuesday;
	private ToggleButton mWednesday;
	private ToggleButton mThursday;
	private ToggleButton mFriday;
	private ToggleButton mSaturday;
	
	private Button mDoneBtn;
	
	private String mLabel = "";
	private String mRingtoneUri = "";
	
	private int mEnabledColor;
	private int mDisabledColor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_define_standard);
		
		initViews();
		setOnClickListeners();
		
		mEnabledColor = getResources().getColor(R.color.black);
		mDisabledColor = getResources().getColor(R.color.gray);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.alarm_define_standard, menu);
		MenuItem item = menu.findItem(R.id.action_switch);
		mSwitch = (Switch) item.getActionView().findViewById(R.id.alarm_define_action_bar_view_switch);
		return true;
	}

	private void initViews() {
		mLabelLayout = findViewById(R.id.activity_alarm_define_label_layout);
		mLabelTxt = (TextView) findViewById(R.id.activity_alarm_define_label);
		
		mTimePicker = (TimePicker) findViewById(R.id.activity_alarm_define_time_picker);
		
		mRingtoneLayout = findViewById(R.id.activity_alarm_define_ringtone_layout);
		mRingtoneTxt = (TextView) findViewById(R.id.activity_alarm_define_ringtone);
		
		mVibrateCheck = (CheckBox) findViewById(R.id.activity_alarm_define_vibrate);
		
		mRepeatCheck = (CheckBox) findViewById(R.id.activity_alarm_define_repeat);
		mRepetitionLayout = findViewById(R.id.activity_alarm_define_repetition_layout);
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
		mLabelLayout.setOnClickListener(this);
		mRingtoneLayout.setOnClickListener(this);
		mDoneBtn.setOnClickListener(this);
		
		mRepeatCheck.setOnCheckedChangeListener(this);
		mVibrateCheck.setOnCheckedChangeListener(this);
		mSunday.setOnCheckedChangeListener(this);
		mMonday.setOnCheckedChangeListener(this);
		mTuesday.setOnCheckedChangeListener(this);
		mWednesday.setOnCheckedChangeListener(this);
		mThursday.setOnCheckedChangeListener(this);
		mFriday.setOnCheckedChangeListener(this);
		mSaturday.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.activity_alarm_define_label_layout:
			showTitleDefineDialog();
			break;
		case R.id.activity_alarm_define_ringtone_layout:
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
					mLabelTxt.setText(mLabel.trim());
					if(mLabel.trim().isEmpty()) {
						mLabelTxt.setTextColor(mDisabledColor);
					}
					else {
						mLabelTxt.setTextColor(mEnabledColor);
					}
				}
			});
		
		// Set the default text of edit text
		edit.setText(mLabel);
		edit.selectAll();
		
		// Make the keyboard show automatically when dialog opened
		final AlertDialog dialog = builder.create();
		edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus) {
		            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		        }
		    }
		});
		dialog.show();
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
			.setRingtoneUri(mRingtoneUri)
			.setVibrateEnable(mVibrateCheck.isChecked())
			.enableRepeat(mRepeatCheck.isChecked())
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
				mRingtoneUri = uri.toString();
				
				Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
				String title =  ringtone.getTitle(this);
				
				int start = 0;
				int lastSlash = title.lastIndexOf("/");
				if(lastSlash != -1) {
					start = lastSlash + 1;
				}
				int end = title.length();
				int lastPoint = title.lastIndexOf(".");
				if(lastPoint != -1) {
					end = lastPoint;
				}
				String fileName = title.substring(start, end);
				mRingtoneTxt.setText(fileName);
				mRingtoneTxt.setTextColor(mEnabledColor);
			}
			else {
				mRingtoneTxt.setText(""); // It will the default hint text
				mRingtoneTxt.setTextColor(mDisabledColor);
			}
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch(buttonView.getId()) {
		case R.id.activity_alarm_define_repeat:
			if(isChecked) {
				mRepetitionLayout.setVisibility(View.VISIBLE);
				mSaturday.setVisibility(View.VISIBLE);
				mSunday.setVisibility(View.VISIBLE);
				checkEveryday();
			}
			else {
				mRepetitionLayout.setVisibility(View.GONE);
				mSaturday.setVisibility(View.GONE);
				mSunday.setVisibility(View.GONE);
			}
			break;
		case R.id.activity_alarm_define_vibrate:
			if(isChecked) {
				mVibrateCheck.setTextColor(mEnabledColor);
			}
			else {
				mVibrateCheck.setTextColor(mDisabledColor);
			}
			break;
		case R.id.activity_alarm_define_sunday_toggle:
		case R.id.activity_alarm_define_monday_toggle:
		case R.id.activity_alarm_define_tuesday_toggle:
		case R.id.activity_alarm_define_wednesday_toggle:
		case R.id.activity_alarm_define_thursday_toggle:
		case R.id.activity_alarm_define_friday_toggle:
		case R.id.activity_alarm_define_saturday_toggle:
			if(isAllWeekToggleUnchecked()) {
				mRepeatCheck.setChecked(false);
			}
			break;
		}
	}
	
	private boolean isAllWeekToggleUnchecked() {
		boolean result = true;
		result &= !mSunday.isChecked();
		result &= !mMonday.isChecked();
		result &= !mTuesday.isChecked();
		result &= !mWednesday.isChecked();
		result &= !mThursday.isChecked();
		result &= !mFriday.isChecked();
		result &= !mSaturday.isChecked();
		return result;
	}
	
	private void checkEveryday () {
		mSunday.setChecked(true);
		mMonday.setChecked(true);
		mTuesday.setChecked(true);
		mWednesday.setChecked(true);
		mThursday.setChecked(true);
		mFriday.setChecked(true);
		mSaturday.setChecked(true);
	}
}
