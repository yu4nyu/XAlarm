package com.yuanyu.xalarm;

import com.yuanyu.xalarm.model.Utils;

import android.os.Bundle;
import android.app.Activity;

public class KeepVibrationActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Utils.startVibration(this);
		Utils.keepVibrationContextReference(this);
		
		finish();
	}
}