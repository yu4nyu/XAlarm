package com.yuanyu.upwardalarm.sensor;

import java.util.List;

import com.yuanyu.upwardalarm.sensor.MovementTracker.Sample;

public enum MovementAnalysor {
	
	INSTANCE;

	public static interface MovementListener {
		void onUpwardDetected();
	}
	
	// TODO verify these values
	private final static int SUCCESSIVE_THRESHOLD = 5;
	private final static float VALUE_THRESHOLD = 2.0f;
	
	private int mSuccessiveCount = 0;
	
	private MovementListener mMovementListener;
	
	public void setMovementListener(MovementListener listener) {
		mMovementListener = listener;
	}
	
	public void removeMovementListener() {
		mMovementListener = null;
	}
	
	void analyse(List<Sample> samples) {
		for(Sample sample : samples) {
			if(sample.independentValue() <= VALUE_THRESHOLD) {
				mSuccessiveCount++;
			}
			if(mSuccessiveCount >= SUCCESSIVE_THRESHOLD) {
				notifyMovementListener();
				mSuccessiveCount = 0;
			}
		}
	}
	
	private void notifyMovementListener() {
		if(mMovementListener != null) {
			mMovementListener.onUpwardDetected();
		}
	}
}
