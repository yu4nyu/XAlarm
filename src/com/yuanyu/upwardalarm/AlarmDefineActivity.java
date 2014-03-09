package com.yuanyu.upwardalarm;

import android.os.Bundle;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AlarmDefineActivity extends Activity {

	private TextView mLabelTxt;
	private TextView mHourTxt;
	private TextView mMinuteTxt;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_define);
		
		initViews();
		drawCircles(mLeftCircle, mRightCircle);
	}

	private void initViews() {
		mLabelTxt = (TextView) findViewById(R.id.activity_alarm_define_label);
		mHourTxt = (TextView) findViewById(R.id.activity_alarm_define_hour);
		mMinuteTxt = (TextView) findViewById(R.id.activity_alarm_define_minute);
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
	
	private void drawCircles(final ImageView left, final ImageView right) {
		left.post(new Runnable(){
			@Override
			public void run() {
				Bitmap circle = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
				int radius = circle.getWidth() / 2;
				
				// Draw left image
				//int width = left.getWidth();
				int height = left.getHeight();
				int cropWidth = (int) ( radius - Math.sqrt(radius*radius - height*height/4));
				int x = 2*radius - cropWidth;
				int y = radius - height/2;
				Bitmap leftBitmap = Bitmap.createBitmap(circle, x, y, cropWidth, height);
				left.setImageBitmap(leftBitmap);
				
				// Draw right image
				Bitmap rightBitmap = Bitmap.createBitmap(circle, 0, y, cropWidth, height);
				right.setImageBitmap(rightBitmap);
			}
		});
	}
}
