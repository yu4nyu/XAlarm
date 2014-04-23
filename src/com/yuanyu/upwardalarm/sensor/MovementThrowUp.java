package com.yuanyu.upwardalarm.sensor;

import java.util.List;

import com.yuanyu.upwardalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.upwardalarm.sensor.MovementTracker.Sample;

public class MovementThrowUp extends Movement {

	private final static float VALUE_THRESHOLD = 2.0f;
	private final static int SUCCESSIVE_THRESHOLD_EASY = 5;
	private final static int SUCCESSIVE_THRESHOLD_MODERATE = 10;
	private final static int SUCCESSIVE_THRESHOLD_HARD = 15;
	
	private int mSuccessiveCount = 0;
	
	public MovementThrowUp(List<MovementListener> listeners, int movementLevel, int movementTimes) {
		super(listeners, movementLevel, movementTimes);
	}

	@Override
	public void detectMovement(List<Sample> samples) {
		// TODO level and times
		for(Sample sample : samples) {
			if(sample.independentValue() <= VALUE_THRESHOLD) {
				mSuccessiveCount++;
			}
			if(mSuccessiveCount >= SUCCESSIVE_THRESHOLD_EASY) {
				notifyMovementDetected();
				mSuccessiveCount = 0;
			}
		}
	}
}
