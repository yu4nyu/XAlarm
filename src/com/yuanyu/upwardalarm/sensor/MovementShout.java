package com.yuanyu.upwardalarm.sensor;

import java.util.List;

import com.yuanyu.upwardalarm.model.Constants;
import com.yuanyu.upwardalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.upwardalarm.sensor.MovementTracker.Sample;

public class MovementShout extends Movement {

	// TODO
	private final static float VALUE_THRESHOLD_EASY = 2.0f;
	private final static float VALUE_THRESHOLD_MODERATE = 4.0f;
	private final static float VALUE_THRESHOLD_HARD = 6.0f;
	
	private float mValueThreshold;
	
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
		for(Sample sample : samples) { // TODO maybe use average value
			if(sample.x > mValueThreshold) {
				notifyMovementDetected();
				break;
			}
		}
	}
}
