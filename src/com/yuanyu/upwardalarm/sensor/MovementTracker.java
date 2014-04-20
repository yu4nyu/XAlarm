package com.yuanyu.upwardalarm.sensor;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MovementTracker implements SensorEventListener {

	private final static int TRACK_RATE = SensorManager.SENSOR_DELAY_NORMAL; // TODO verify this
	
	private final static int THRESHOLD_NUMBER_TO_ANALYSE = 20;

	private SensorManager mSensorManager;
	private Sensor mAcceleroMeter;
	
	public static class Sample {
		float x;
		float y;
		float z;
		
		public double independentValue() {
			return Math.sqrt(x*x + y*y + z*z);
		}
	}
	
	private List<Sample> mData = new ArrayList<Sample>();

	public MovementTracker(Context context) {
		mSensorManager = (SensorManager) context.getSystemService(Service.SENSOR_SERVICE);
		mAcceleroMeter = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	/**
	 * Do not forget to call stop() if you do not need to track the movement
	 */
	public void start() {
		mSensorManager.registerListener(this, mAcceleroMeter, TRACK_RATE);
	}

	public void stop() {
		mSensorManager.unregisterListener(this);
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
