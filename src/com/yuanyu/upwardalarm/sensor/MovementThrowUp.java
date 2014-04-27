package com.yuanyu.upwardalarm.sensor;

import java.util.List;

import com.yuanyu.upwardalarm.model.Constants;
import com.yuanyu.upwardalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.upwardalarm.sensor.MovementTracker.Sample;

public class MovementThrowUp extends Movement {

	private final static float VALUE_THRESHOLD = 2.0f;
	private final static int SUCCESSIVE_THRESHOLD_EASY = 5;
	private final static int SUCCESSIVE_THRESHOLD_MODERATE = 10;
	private final static int SUCCESSIVE_THRESHOLD_HARD = 15;
	private final static long THROW_INTERVAL = 2000;

	private int mSuccessiveCount = 0;
	private boolean mNewMovementStarted = true;
	private boolean mLastValueDetected = false;
	private final int mSuccessiveThreshold;

	private long mLastDetectedTime = 0;

	public MovementThrowUp(List<MovementListener> listeners, int movementLevel) {
		super(listeners, movementLevel);

		switch(mMovementLevel) {
		case Constants.LEVEL_EASY:
			mSuccessiveThreshold = SUCCESSIVE_THRESHOLD_EASY;
			break;
		case Constants.LEVEL_MODERATE:
			mSuccessiveThreshold = SUCCESSIVE_THRESHOLD_MODERATE;
			break;
		case Constants.LEVEL_HARD:
			mSuccessiveThreshold = SUCCESSIVE_THRESHOLD_HARD;
			break;
		default:
			mSuccessiveThreshold = SUCCESSIVE_THRESHOLD_EASY;
			break;
		}
	}

	@Override
	public void detectMovement(List<Sample> samples) {
		for(Sample sample : samples) {
			if(System.currentTimeMillis() - mLastDetectedTime < THROW_INTERVAL) {
				continue;
			}
			
			if(sample.independentValue() <= VALUE_THRESHOLD && mNewMovementStarted) {
				if(mSuccessiveCount == 0) {
					mSuccessiveCount++;
					mLastValueDetected = true;
				}
				else {
					if(mLastValueDetected) {
						mSuccessiveCount++;
					}
					else {
						mSuccessiveCount = 1;
						mLastValueDetected = true;
					}
				}
			}
			else {
				mNewMovementStarted = true;
				mLastValueDetected = false;
			}
			
			if(mSuccessiveCount >= mSuccessiveThreshold) {
				mSuccessiveCount = 0;
				notifyMovementDetected();
				mNewMovementStarted = false;
				mLastDetectedTime = System.currentTimeMillis();
			}
		}
	}
}
