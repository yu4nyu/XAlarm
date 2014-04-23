package com.yuanyu.upwardalarm.sensor;

import java.util.List;

import com.yuanyu.upwardalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.upwardalarm.sensor.MovementTracker.Sample;

abstract class Movement {

	List<MovementListener> mMovementListeners;
	
	public Movement(List<MovementListener> listeners) {
		mMovementListeners = listeners;
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
