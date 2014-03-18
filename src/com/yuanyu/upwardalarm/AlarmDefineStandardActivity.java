package com.yuanyu.upwardalarm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.app.Activity;

public class AlarmDefineStandardActivity extends Activity implements View.OnClickListener {

	private final static String TAG = "AlarmDefineActivity";
	
	private TextView mLabelTxt;
	private Switch mSwitch;
	
	private ToggleButton mSunday;
	private ToggleButton mMonday;
	private ToggleButton mTuesday;
	private ToggleButton mWednesday;
	private ToggleButton mThursday;
	private ToggleButton mFriday;
	private ToggleButton mSaturday;
	
	private TextView mRingtoneTxt;
	private CheckBox mVibrateCheck;
	
	private Button mDoneBtn;
	
	private int mCurrentHour;
	private int mCurrentMinute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_define);
		
		initViews();
		setOnClickListeners();
	}

	private void initViews() {
		mLabelTxt = (TextView) findViewById(R.id.activity_alarm_define_label);
		mSwitch = (Switch) findViewById(R.id.activity_alarm_define_switch);
		
		mSunday = (ToggleButton) findViewById(R.id.activity_alarm_define_sunday_toggle);
		mMonday = (ToggleButton) findViewById(R.id.activity_alarm_define_monday_toggle);
		mTuesday = (ToggleButton) findViewById(R.id.activity_alarm_define_tuesday_toggle);
		mWednesday = (ToggleButton) findViewById(R.id.activity_alarm_define_wednesday_toggle);
		mThursday = (ToggleButton) findViewById(R.id.activity_alarm_define_thursday_toggle);
		mFriday = (ToggleButton) findViewById(R.id.activity_alarm_define_friday_toggle);
		mSaturday = (ToggleButton) findViewById(R.id.activity_alarm_define_saturday_toggle);
		
		mRingtoneTxt = (TextView) findViewById(R.id.activity_alarm_define_ringtone);
		mVibrateCheck = (CheckBox) findViewById(R.id.activity_alarm_define_vibrate);
		
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
			// TODO
			break;
		case R.id.activity_alarm_define_ringtone:
			// TODO
			break;
		case R.id.activity_alarm_define_done_btn:
			// TODO
			break;
		}
	}
}
