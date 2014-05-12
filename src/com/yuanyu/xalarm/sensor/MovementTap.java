package com.yuanyu.xalarm.sensor;

import java.util.List;

import com.yuanyu.xalarm.model.Constants;
import com.yuanyu.xalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.xalarm.sensor.MovementTracker.Sample;

public class MovementTap extends Movement {

	private final static float VALUE_THRESHOLD_EASY = 2.0f;
	private final static float VALUE_THRESHOLD_MODERATE = 4.0f;
	private final static float VALUE_THRESHOLD_HARD = 6.0f;
	
	private final static float VALUT_MAX_INDEPENDENT_THRESHOLD = 15.0f;
	
	private float mValueThreshold;
	
	public MovementTap(List<MovementListener> listeners, int movementLevel) {
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
		float maxX, maxY, maxZ;
		maxX = maxY = maxZ = Float.MIN_VALUE;
		float minX, minY, minZ;
		minX = minY = minZ = Float.MAX_VALUE;
		double maxI = Double.MIN_VALUE;
		double minI = Double.MAX_VALUE;
		
		for(Sample sample : samples) {
			if(sample.x > maxX) {
				maxX = sample.x;
			}
			if(sample.x < minX) {
				minX = sample.x;
			}
			
			if(sample.y > maxY) {
				maxY = sample.y;
			}
			if(sample.y < minY) {
				minY = sample.y;
			}
			
			if(sample.z > maxZ) {
				maxZ = sample.z;
			}
			if(sample.z < minZ) {
				minZ = sample.z;
			}
			
			double independent = sample.independentValue();
			if(independent > maxI) {
				maxI = independent;
			}
			if(independent < minI) {
				minI = independent;
			}
		}
		
		float deltaX = Math.abs(maxX - minX);
		float deltaY = Math.abs(maxY - minY);
		float deltaZ = Math.abs(maxZ - minZ);
		double deltaI = Math.abs(maxI - minI);
		
		if((deltaX > mValueThreshold || deltaY > mValueThreshold || deltaZ > mValueThreshold)
				&& deltaI < VALUT_MAX_INDEPENDENT_THRESHOLD) {
			notifyMovementDetected();
		}
	}
}
