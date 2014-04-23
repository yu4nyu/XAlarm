package com.yuanyu.upwardalarm.sensor;

import java.util.List;

import com.yuanyu.upwardalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.upwardalarm.sensor.MovementTracker.Sample;

abstract class Movement {

	List<MovementListener> mMovementListeners;
	
	protected int mMovementLevel;
	protected int mMovementTimes;
	
	public Movement(List<MovementListener> listeners, int movementLevel, int movementTimes) {
		mMovementListeners = listeners;
		mMovementLevel = movementLevel;
		mMovementTimes = movementTimes;
	}

	abstract void detectMovement(List<Sample> samples);
	
	protected void notifyMovementDetected() {
		if(mMovementListeners != null) {
			for(MovementListener listener : mMovementListeners) {
				listener.onMovementDetected();
			}
		}
	}
}
