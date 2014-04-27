package com.yuanyu.upwardalarm.sensor;

import java.util.List;

import com.yuanyu.upwardalarm.model.Constants;
import com.yuanyu.upwardalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.upwardalarm.sensor.MovementTracker.Sample;

public class MovementShake extends Movement {

	private final static float VALUE_THRESHOLD_EASY = 30.0f;
	private final static float VALUE_THRESHOLD_MODERATE = 35.0f;
	private final static float VALUE_THRESHOLD_HARD = 40.0f;
	private final static float FILTER_ALPHA = 0.1f;
	private final static long SHAKE_INTERVAL = 300;
	
	private float mValueThreshold;
	private float mLowX,mLowY,mLowZ;
	
	private float mCurrentMin = Float.MAX_VALUE;
	private float mCurrentMax = Float.MIN_VALUE;
	private long mLastDetectedTime = 0;
	
	public MovementShake(List<MovementListener> listeners, int movementLevel) {
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
			if(System.currentTimeMillis() - mLastDetectedTime < SHAKE_INTERVAL) {
				continue;
			}
			
			float x = sample.x;
			float y = sample.y;
			float z = sample.z;
			
			//Low-Pass Filter
			mLowX = x * FILTER_ALPHA + mLowX * (1.0f - FILTER_ALPHA);
			mLowY = y * FILTER_ALPHA + mLowY * (1.0f - FILTER_ALPHA);
			mLowZ = z * FILTER_ALPHA + mLowZ * (1.0f - FILTER_ALPHA);
			
			//High-pass filter
			float highX = x - mLowX;
			float highY = y - mLowY;
			float highZ = z - mLowZ;
			
			float max = Math.max(highX, Math.max(highY, highZ));
			
			if(max > mCurrentMax) {
				mCurrentMax = max;
			}
			if(max < mCurrentMin) {
				mCurrentMin = max;
			}
			if(mCurrentMax - mCurrentMin > mValueThreshold) {
				notifyMovementDetected();
				mCurrentMax = Float.MIN_VALUE;
				mCurrentMin = Float.MAX_VALUE;
				mLastDetectedTime = System.currentTimeMillis();
			}
		}
	}
}
