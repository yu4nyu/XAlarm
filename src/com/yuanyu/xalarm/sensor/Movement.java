package com.yuanyu.xalarm.sensor;

import java.util.List;

import com.yuanyu.xalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.xalarm.sensor.MovementTracker.Sample;

abstract class Movement {

	List<MovementListener> mMovementListeners;
	
	protected int mMovementLevel;
	
	public Movement(List<MovementListener> listeners, int movementLevel) {
		mMovementListeners = listeners;
		mMovementLevel = movementLevel;
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
