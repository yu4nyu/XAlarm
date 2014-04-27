package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.Alarm;
import com.yuanyu.upwardalarm.model.Constants;
import com.yuanyu.upwardalarm.model.Manager;
import com.yuanyu.upwardalarm.model.Utils;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class AlarmDefineActivity extends Activity implements View.OnClickListener,
	CompoundButton.OnCheckedChangeListener, AlarmStopConfigDialog.OnAlarmStopConfiguredListener {
	
	private final static String TAG = "AlarmDefineStandardActivity";
	
	private final static int ACTIVITY_RINGTONE_PICKER = 0;
	
	public final static String EXTRA_ALARM = "alarm";
	public final static String EXTRA_POSITION = "position";
	
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
	
	private View mDefineAlarmStopLayout;
	
	private Button mDoneBtn;
	private Button mCancelBtn;
	
	private String mRingtoneUri = "";
	
	private int mStopWay = Constants.STOP_WAY_BUTTON;
	private int mStopLevel = Constants.LEVEL_EASY;
	private int mStopTimes = 1;
	private TextView mStopWayText;
	
	private int mEnabledColor;
	private int mDisabledColor;
	
	// Used for edition mode
	private int mPosition; // Just keep the position and return it to MainActivity
	private boolean mInitEnabled = false;
	Alarm mAlarm = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_define_standard);
		
		mEnabledColor = getResources().getColor(R.color.black);
		mDisabledColor = getResources().getColor(R.color.gray);
		
		initViews();
		setOnClickListeners();
		updateStopWayText();
		
		Object extra = getIntent().getSerializableExtra(EXTRA_ALARM);
		if(extra != null) { // Edit
			setTitle(R.string.title_edit_alarm);
			
			mAlarm = (Alarm)extra;
			initStatus(mAlarm);
			mPosition =  getIntent().getIntExtra(EXTRA_POSITION, -1);
		}
		else { // Add a new alarm
			setTitle(R.string.title_add_alarm);
		}
		
		if(savedInstanceState != null) {
			Alarm alarm = (Alarm) savedInstanceState.getSerializable("alarm");
			initStatus(alarm);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.alarm_define_standard, menu);
		MenuItem item = menu.findItem(R.id.action_switch);
		mSwitch = (Switch) item.getActionView().findViewById(R.id.alarm_define_action_bar_view_switch);
		mSwitch.setChecked(mInitEnabled);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("alarm", generateAlarmObject());
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.shift_in_from_left, R.anim.shift_out_to_right);
	}

	private void initViews() {
		mLabelLayout = findViewById(R.id.activity_alarm_define_label_layout);
		mLabelTxt = (TextView) findViewById(R.id.activity_alarm_define_label);
		
		mTimePicker = (TimePicker) findViewById(R.id.activity_alarm_define_time_picker);
		mTimePicker.setCurrentHour(8);
		mTimePicker.setCurrentMinute(0);
		
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
		
		mDefineAlarmStopLayout = (View) findViewById(R.id.activity_alarm_define_stop_layout);
		mStopWayText = (TextView) findViewById(R.id.activity_alarm_define_stop_text);
		
		mDoneBtn = (Button) findViewById(R.id.activity_alarm_define_done_btn);
		mCancelBtn = (Button) findViewById(R.id.activity_alarm_define_cancel_btn);
	}
	
	private void setOnClickListeners() {
		mLabelLayout.setOnClickListener(this);
		mRingtoneLayout.setOnClickListener(this);
		mDefineAlarmStopLayout.setOnClickListener(this);
		mDoneBtn.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);
		
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
	
	private void initStatus(Alarm alarm) {
		mInitEnabled = alarm.getEnable(); // Record here, will be used in onCreateOptionsMenu()
		setLabelText(alarm.getLabel());
		mTimePicker.setCurrentHour(alarm.getHour());
		mTimePicker.setCurrentMinute(alarm.getMinute());
		Ringtone ringtone = Manager.INSTANCE.getRingtone(this, alarm.getRingtoneUri());
		if(ringtone != null) {
			mRingtoneTxt.setText(ringtone.getTitle(this));
			mRingtoneTxt.setTextColor(mEnabledColor);
			mRingtoneUri = alarm.getRingtoneUri();
		}
		else {
			mRingtoneTxt.setText(""); // It will the default hint text
			mRingtoneTxt.setTextColor(mDisabledColor);
		}
		mVibrateCheck.setChecked(alarm.getVibrateEnable());
		updateVibrateTextColor(alarm.getVibrateEnable());
		mRepeatCheck.setChecked(alarm.isRepeat());
		if(alarm.isRepeat()) {
			boolean isCheck = alarm.isSundayRepeat();
			mSunday.setChecked(isCheck);
			mSunday.setTextColor(isCheck ? mEnabledColor : mDisabledColor);
			isCheck = alarm.isMondayRepeat();
			mMonday.setChecked(isCheck);
			mMonday.setTextColor(isCheck ? mEnabledColor : mDisabledColor);
			isCheck = alarm.isTuesdayRepeat();
			mTuesday.setChecked(isCheck);
			mTuesday.setTextColor(isCheck ? mEnabledColor : mDisabledColor);
			isCheck = alarm.isWednesdayRepeat();
			mWednesday.setChecked(isCheck);
			mWednesday.setTextColor(isCheck ? mEnabledColor : mDisabledColor);
			isCheck = alarm.isThursdayRepeat();
			mThursday.setChecked(isCheck);
			mThursday.setTextColor(isCheck ? mEnabledColor : mDisabledColor);
			isCheck = alarm.isFridayRepeat();
			mFriday.setChecked(isCheck);
			mFriday.setTextColor(isCheck ? mEnabledColor : mDisabledColor);
			isCheck = alarm.isSaturdayRepeat();
			mSaturday.setChecked(isCheck);
			mSaturday.setTextColor(isCheck ? mEnabledColor : mDisabledColor);
		}
		
		mStopWay = alarm.getStopWay();
		mStopLevel = alarm.getStopLevel();
		mStopTimes = alarm.getStopTimes();
		updateStopWayText();
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
		case R.id.activity_alarm_define_stop_layout:
			startStopWayConfigActivity();
			break;
		case R.id.activity_alarm_define_done_btn:
			done();
			break;
		case R.id.activity_alarm_define_cancel_btn:
			finish();
			overridePendingTransition(R.anim.shift_in_from_left, R.anim.shift_out_to_right);
			break;
		}
	}
	
	private void startStopWayConfigActivity() {
		AlarmStopConfigDialog dialog = new AlarmStopConfigDialog();
		dialog.setOnAlarmStopConfiguredListener(this);
		Bundle args = new Bundle();
		args.putInt(AlarmStopConfigDialog.ARGS_STOP_WAY, mStopWay);
		args.putInt(AlarmStopConfigDialog.ARGS_STOP_LEVEL, mStopLevel);
		args.putInt(AlarmStopConfigDialog.ARGS_STOP_TIMES, mStopTimes);
		dialog.setArguments(args);
		dialog.show(getFragmentManager(), "Stop Way Config");
	}
	
	private void updateStopWayText() {
		mStopWayText.setText(Utils.getStopWayText(this, mStopWay, mStopLevel, mStopTimes));
	}
	
	@Override
	public void onAlarmStopConfigured(int type, int level, int times) {
		mStopWay = type;
		mStopLevel = level;
		mStopTimes = times;
		updateStopWayText();
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
					setLabelText(edit.getEditableText().toString());
				}
			});
		
		// Set the default text of edit text
		edit.setText(mLabelTxt.getText());
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
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
		if(mRingtoneUri != null && !mRingtoneUri.isEmpty()) {
			Uri defaultUri = Uri.parse(mRingtoneUri);
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, defaultUri);
		}
		startActivityForResult(intent, ACTIVITY_RINGTONE_PICKER);
	}
	
	private Alarm generateAlarmObject() {
		Alarm.Builder builder;
		if(mAlarm == null) {
			builder = Alarm.newBuilder(this);
		}
		else {
			builder = Alarm.getBuilder(mAlarm);
		}
		
		builder.setLable(mLabelTxt.getText().toString())
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
		.setStopWay(mStopWay)
		.setStopLevel(mStopLevel)
		.setStopTimes(mStopTimes);
		
		return builder.build();
	}
	
	private void done() {
		Alarm alarm = generateAlarmObject();
		Intent intent = new Intent();
		intent.putExtra(EXTRA_ALARM, alarm);
		intent.putExtra(EXTRA_POSITION, mPosition);
		setResult(Activity.RESULT_OK, intent);
		finish();
		overridePendingTransition(R.anim.shift_in_from_left, R.anim.shift_out_to_right);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == ACTIVITY_RINGTONE_PICKER && resultCode == Activity.RESULT_OK) {
			Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			if(uri != null) {
				mRingtoneUri = uri.toString();
				Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
				mRingtoneTxt.setText(ringtone.getTitle(this));
				mRingtoneTxt.setTextColor(mEnabledColor);
			}
			else {
				mRingtoneUri = "";
				mRingtoneTxt.setText(""); // It will the default hint text
				mRingtoneTxt.setTextColor(mDisabledColor);
			}
			
			Log.d(TAG, "mRingtoneUri = " + mRingtoneUri);
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
				updateVibrateTextColor(isChecked);
			break;
		case R.id.activity_alarm_define_sunday_toggle:
			updateToggleTextColor(mSunday, isChecked);
			break;
		case R.id.activity_alarm_define_monday_toggle:
			updateToggleTextColor(mMonday, isChecked);
			break;
		case R.id.activity_alarm_define_tuesday_toggle:
			updateToggleTextColor(mTuesday, isChecked);
			break;
		case R.id.activity_alarm_define_wednesday_toggle:
			updateToggleTextColor(mWednesday, isChecked);
			break;
		case R.id.activity_alarm_define_thursday_toggle:
			updateToggleTextColor(mThursday, isChecked);
			break;
		case R.id.activity_alarm_define_friday_toggle:
			updateToggleTextColor(mFriday, isChecked);
			break;
		case R.id.activity_alarm_define_saturday_toggle:
			updateToggleTextColor(mSaturday, isChecked);
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
	
	private void setLabelText(String label) {
		mLabelTxt.setText(label.trim());
		if(label.trim().isEmpty()) {
			mLabelTxt.setTextColor(mDisabledColor);
		}
		else {
			mLabelTxt.setTextColor(mEnabledColor);
		}
	}
	
	private void updateVibrateTextColor(boolean checked) {
		mVibrateCheck.setTextColor(checked ? mEnabledColor : mDisabledColor);
	}
	
	private void updateToggleTextColor(ToggleButton toggle, boolean checked) {
		toggle.setTextColor(checked ? mEnabledColor : mDisabledColor);
		if(isAllWeekToggleUnchecked()) {
			mRepeatCheck.setChecked(false);
		}
	}
}