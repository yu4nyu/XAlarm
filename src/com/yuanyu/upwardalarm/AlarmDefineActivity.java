package com.yuanyu.upwardalarm;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnTouchListener;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AlarmDefineActivity extends Activity {

	private final static String TAG = "AlarmDefineActivity";
	
	private TextView mLabelTxt;
	private TextView mHourTxt;
	private TextView mHourBeforTxt;
	private TextView mHourAfterTxt;
	private TextView mMinuteTxt;
	private TextView mMinuteBeforeTxt;
	private TextView mMinuteAfterTxt;
	private Switch mSwitch;
	
	private ToggleButton mSunday;
	private ToggleButton mMonday;
	private ToggleButton mTuesday;
	private ToggleButton mWednesday;
	private ToggleButton mThursday;
	private ToggleButton mFriday;
	private ToggleButton mSaturday;
	
	private ImageView mLeftCircle;
	private ImageView mRightCircle;
	private TextView mRingtoneTxt;
	private CheckedTextView mVibrateCheck;
	
	private int mCurrentHour;
	private int mCurrentMinute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_define);
		
		initViews();
		addTouchListeners();
	}

	private void initViews() {
		mLabelTxt = (TextView) findViewById(R.id.activity_alarm_define_label);
		mHourTxt = (TextView) findViewById(R.id.activity_alarm_define_hour);
		mHourBeforTxt = (TextView) findViewById(R.id.activity_alarm_define_hour_before);
		mHourAfterTxt = (TextView) findViewById(R.id.activity_alarm_define_hour_after);
		mMinuteTxt = (TextView) findViewById(R.id.activity_alarm_define_minute);
		mMinuteBeforeTxt = (TextView) findViewById(R.id.activity_alarm_define_minute_before);
		mMinuteAfterTxt = (TextView) findViewById(R.id.activity_alarm_define_minute_after);
		mSwitch = (Switch) findViewById(R.id.activity_alarm_define_switch);
		
		mSunday = (ToggleButton) findViewById(R.id.activity_alarm_define_sunday_toggle);
		mMonday = (ToggleButton) findViewById(R.id.activity_alarm_define_monday_toggle);
		mTuesday = (ToggleButton) findViewById(R.id.activity_alarm_define_tuesday_toggle);
		mWednesday = (ToggleButton) findViewById(R.id.activity_alarm_define_wednesday_toggle);
		mThursday = (ToggleButton) findViewById(R.id.activity_alarm_define_thursday_toggle);
		mFriday = (ToggleButton) findViewById(R.id.activity_alarm_define_friday_toggle);
		mSaturday = (ToggleButton) findViewById(R.id.activity_alarm_define_saturday_toggle);
		
		mLeftCircle = (ImageView) findViewById(R.id.activity_alarm_define_left_image);
		mRightCircle = (ImageView) findViewById(R.id.activity_alarm_define_right_image);
		mRingtoneTxt = (TextView) findViewById(R.id.activity_alarm_define_ringtone);
		mVibrateCheck = (CheckedTextView) findViewById(R.id.activity_alarm_define_vibrate);
	}
	
	private void addTouchListeners() {
		mLeftCircle.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int[] locations = new int[2];
				mLeftCircle.getLocationOnScreen(locations);
				float x = event.getRawX() - locations[0];
				float y = event.getRawY() - locations[1];
				//Log.d(TAG, "onTouch(), x = " + x + ", y = " + y);
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					TimePickerAnimation.INSTANCE.calculateRadius(mLeftCircle);
					TimePickerAnimation.INSTANCE.initHourPicker(mHourBeforTxt, mHourTxt, mHourAfterTxt, x, y);
					break;
				case MotionEvent.ACTION_MOVE:
					TimePickerAnimation.INSTANCE.hourPickerAnimation(mHourBeforTxt, mHourTxt, mHourAfterTxt, x, y);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return true;
			}
		});
	}
}
