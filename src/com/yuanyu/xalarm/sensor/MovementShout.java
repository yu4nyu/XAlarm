package com.yuanyu.xalarm.sensor;

import java.util.List;

import com.yuanyu.xalarm.model.Constants;
import com.yuanyu.xalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.xalarm.sensor.MovementTracker.Sample;

public class MovementShout extends Movement {

	private final static float VALUE_THRESHOLD_EASY = 80.0f;
	private final static float VALUE_THRESHOLD_MODERATE = 90.0f;
	private final static float VALUE_THRESHOLD_HARD = 100.0f;
	private final static long SHOUT_INTERVAL = 1000;
	
	private float mValueThreshold;
	
	private long mLastDetectedTime = 0;
	
	public MovementShout(List<MovementListener> listeners, int movementLevel) {
		super(listeners, movementLevel);

		switch(mMovementLevel) {
		case Constants.LEVEL_EASY:
			mValueThreshold = VALUE_THRESHOLD_EASY;
			break;
		case Constants.LEVEL_MODERATE:
			mValueThreshold = VALUE_THRESHOLD_MODERATE;
			break;
		case Constants.LEVEL_HARD:
			mValueThreshold = VALUE_THRESHOLD_HARD;
			break;
		default:
			mValueThreshold = VALUE_THRESHOLD_EASY;
			break;
		}
	}

	@Override
	void detectMovement(List<Sample> samples) {
		for(Sample sample : samples) {
			if(System.currentTimeMillis() - mLastDetectedTime < SHOUT_INTERVAL) {
				continue;
			}
			
			if(sample.x > mValueThreshold) {
				notifyMovementDetected();
				mLastDetectedTime = System.currentTimeMillis();
				break;
			}
		}
	}
}
