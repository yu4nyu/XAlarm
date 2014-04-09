package com.yuanyu.upwardalarm.sensor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MovementTracker implements SensorEventListener {

	// TODO add permission and feature requirement in manifest

	private final static int TRACK_RATE = SensorManager.SENSOR_DELAY_NORMAL; // TODO verify this

	private SensorManager mSensorManager;
	private Sensor mAcceleroMeter;
	
	public static class Sample {
		float x;
		float y;
		float z;
		
		// TODO delete this, for test only
		public String toString() {
			return "" + x + "  " + y + "  " + z + "  " + (x+y+z);
		}
		public String getSqrt() {
			double result = Math.sqrt(x*x + y*y + z*z);
			DecimalFormat form = new DecimalFormat("0.000");
			return form.format(result);
		}
		public String getCalibratedSqrt() {
			double sqrt = Math.sqrt(x*x + y*y + z*z);
			if(mLastSqrt != Double.MAX_VALUE) {
				double result = mLastSqrt * 0.9f + sqrt * 0.1f;
				DecimalFormat form = new DecimalFormat("0.000");
				return form.format(result);
			}
			mLastSqrt = sqrt;
			return "";
		}
	}
	
	private List<Sample> mData = new ArrayList<Sample>();
	private static double mLastSqrt = Double.MAX_VALUE;

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
	
	public List<Sample> getData() {
		return mData;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// In this example, alpha is calculated as t / (t + dT),
		// where t is the low-pass filter's time-constant and
		// dT is the event delivery rate.

		/*final float alpha = 0.8;

		// Isolate the force of gravity with the low-pass filter.
		gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
		gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
		gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

		// Remove the gravity contribution with the high-pass filter.
		linear_acceleration[0] = event.values[0] - gravity[0];
		linear_acceleration[1] = event.values[1] - gravity[1];
		linear_acceleration[2] = event.values[2] - gravity[2];*/

		Sample sample = new Sample();
		sample.x = event.values[0];
		sample.y = event.values[1];
		sample.z = event.values[2];
		
		DecimalFormat form = new DecimalFormat("0.000");
		sample.x = Float.parseFloat(form.format(sample.x));
		sample.y = Float.parseFloat(form.format(sample.y));
		sample.z = Float.parseFloat(form.format(sample.z));
		
		mData.add(sample);
	}
}
