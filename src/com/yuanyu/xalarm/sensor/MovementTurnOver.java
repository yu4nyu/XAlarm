package com.yuanyu.xalarm.sensor;

import java.util.List;

import com.yuanyu.xalarm.model.Constants;
import com.yuanyu.xalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.xalarm.sensor.MovementTracker.Sample;

public class MovementTurnOver extends Movement {
	
	// If z value is greater than this threshold means the telephone is keep flat in one side
	private final static float FLAT_THRESHOLD = 9.0f;
	private final static long TIME_THRESHOLD_EASY = 2048;
	private final static long TIME_THRESHOLD_MODERATE = 1020;
	private final static long TIME_THRESHOLD_HARD = 256;
	
	private boolean mIsPositive; // true if z value is positive, false otherwise
	private boolean mIsFlatDetected = false;
	
	private long mTimeThreshold;
	private long mOtherSideLastTime;

	public MovementTurnOver(List<MovementListener> listeners, int movementLevel) {
		super(listeners, movementLevel);
		
		switch(mMovementLevel) {
		case Constants.LEVEL_EASY:
			mTimeThreshold = TIME_THRESHOLD_EASY;
			break;
		case Constants.LEVEL_MODERATE:
			mTimeThreshold = TIME_THRESHOLD_MODERATE;
			break;
		case Constants.LEVEL_HARD:
			mTimeThreshold = TIME_THRESHOLD_HARD;
			break;
		default:
			mTimeThreshold = TIME_THRESHOLD_EASY;
			break;
		}
	}

	@Override
	void detectMovement(List<Sample> samples) {
		for(Sample sample : samples) {
			if(!mIsFlatDetected) {
				if(Math.abs(sample.z) > FLAT_THRESHOLD) { // Detected first time
					mIsFlatDetected = true;
					mIsPositive = sample.z > 0;
					mOtherSideLastTime = System.currentTimeMillis();
				}
			}
			else {
				if(Math.abs(sample.z) > FLAT_THRESHOLD) {
					boolean isPositive = sample.z > 0; // Current state
					if(mIsPositive != isPositive) { // Turned over
						mIsPositive = isPositive;
						if(System.currentTimeMillis() - mOtherSideLastTime < mTimeThreshold) {
							notifyMovementDetected();
						}
					}
					else {
						mOtherSideLastTime = System.currentTimeMillis();
					}
				}
			}
		}
	}
}
