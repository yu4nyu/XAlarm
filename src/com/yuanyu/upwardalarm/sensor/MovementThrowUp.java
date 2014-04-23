package com.yuanyu.upwardalarm.sensor;

import java.util.List;

import com.yuanyu.upwardalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.upwardalarm.sensor.MovementTracker.Sample;

public class MovementThrowUp extends Movement {

	private final static int SUCCESSIVE_THRESHOLD = 5;
	private final static float VALUE_THRESHOLD = 2.0f;
	
	private int mSuccessiveCount = 0;
	
	public MovementThrowUp(List<MovementListener> listeners) {
		super(listeners);
	}

	@Override
	public void detectMovement(List<Sample> samples) {
		for(Sample sample : samples) {
			if(sample.independentValue() <= VALUE_THRESHOLD) {
				mSuccessiveCount++;
			}
			if(mSuccessiveCount >= SUCCESSIVE_THRESHOLD) {
				notifyMovementDetected();
				mSuccessiveCount = 0;
			}
		}
	}
}
