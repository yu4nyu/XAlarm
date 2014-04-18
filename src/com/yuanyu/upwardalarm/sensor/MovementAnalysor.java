package com.yuanyu.upwardalarm.sensor;

import java.util.ArrayList;
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
	
	private List<MovementListener> mMovementListeners = new ArrayList<MovementListener>();
	
	public void addMovementListener(MovementListener listener) {
		mMovementListeners.add(listener);
	}
	
	public void removeMovementListener(MovementListener listener) {
		mMovementListeners.remove(listener);
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
		samples.clear();
	}
	
	private void notifyMovementListener() {
		for(MovementListener listener : mMovementListeners) {
			listener.onUpwardDetected();
		}
		mMovementListeners.clear(); // TODO do not clear here if want to notify several times
	}
}
