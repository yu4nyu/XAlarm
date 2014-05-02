package com.yuanyu.upwardalarm.sensor;

import java.util.ArrayList;
import java.util.List;

import com.yuanyu.upwardalarm.model.Constants;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;

public class MovementTracker implements SensorEventListener {

	private final static int TRACK_RATE = SensorManager.SENSOR_DELAY_NORMAL;
	
	private final static int THRESHOLD_NUMBER_TO_ANALYSE = 20;

	private SensorManager mSensorManager;
	private Sensor mAcceleroMeter;
	
	private int mMovementType;
	
	public static class Sample {
		public float x;
		public float y;
		public float z;
		
		public double independentValue() {
			return Math.sqrt(x*x + y*y + z*z);
		}
		
		public float sumValue() {
			return x + y + z;
		}
	}
	
	private List<Sample> mData = new ArrayList<Sample>();

	public MovementTracker(Context context) {
		mSensorManager = (SensorManager) context.getSystemService(Service.SENSOR_SERVICE);
		mAcceleroMeter = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	/**
	 * Do not forget to call stop() if you do not need to track the movement any more
	 */
	public void start(int movementType, int movementLevel) {
		mMovementType = movementType;
		MovementAnalysor.INSTANCE.initMovement(movementType, movementLevel);
		if(Constants.isNeedAccelerometer(movementType)) {
			mSensorManager.registerListener(this, mAcceleroMeter, TRACK_RATE);
		}
		if(Constants.isNeedMicrophone(movementType)) {
			
		}
	}

	public void stop() {
		if(Constants.isNeedAccelerometer(mMovementType)) {
			mSensorManager.unregisterListener(this);
		}
		if(Constants.isNeedMicrophone(mMovementType)) {
			
		}
	}
	
	public void clearData() {
		mData.clear();
	}
	
	public List<Sample> getData() {
		return mData;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do nothing here
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Sample sample = new Sample();
		sample.x = event.values[0];
		sample.y = event.values[1];
		sample.z = event.values[2];
		mData.add(sample);
		
		if(mData.size() >= THRESHOLD_NUMBER_TO_ANALYSE) {
			MovementAnalysor.INSTANCE.analyse(mData);
		}
	}
}
