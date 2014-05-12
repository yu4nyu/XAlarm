package com.yuanyu.xalarm.sensor;

import java.util.List;

import com.yuanyu.xalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.xalarm.sensor.MovementTracker.Sample;

public class MovementTurnOver extends Movement {
	
	// If z value is greater than this threshold means the telephone is keep flat in one side
	private final static float FLAT_THRESHOLD = 7.0f; // TODO verify this
	
	private boolean mIsPositive; // true if z value is positive, false otherwise
	private boolean mIsFlatDetected = false;

	public MovementTurnOver(List<MovementListener> listeners, int movementLevel) {
		super(listeners, movementLevel);
	}

	@Override
	void detectMovement(List<Sample> samples) {
		for(Sample sample : samples) {
			if(!mIsFlatDetected) {
				if(Math.abs(sample.z) > FLAT_THRESHOLD) { // Detected first time
					mIsFlatDetected = true;
					mIsPositive = sample.z > 0;
				}
			}
			else {
				if(Math.abs(sample.z) > FLAT_THRESHOLD) {
					boolean isPositive = sample.z > 0; // Current state
					if(mIsPositive != isPositive) { // Turned over
						mIsPositive = isPositive;
						notifyMovementDetected();
					}
				}
			}
		}
	}
}
