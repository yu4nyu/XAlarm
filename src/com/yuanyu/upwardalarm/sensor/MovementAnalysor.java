package com.yuanyu.upwardalarm.sensor;

import java.util.ArrayList;
import java.util.List;

import com.yuanyu.upwardalarm.model.Constants;
import com.yuanyu.upwardalarm.sensor.MovementTracker.Sample;

public enum MovementAnalysor {

	INSTANCE;

	public static interface MovementListener {
		
		/**
		 * Movement detected one time
		 */
		void onMovementDetected();
	}

	private List<MovementListener> mMovementListeners = new ArrayList<MovementListener>();
	private Movement mMovement;

	public void addMovementListener(MovementListener listener) {
		mMovementListeners.add(listener);
	}

	public void removeMovementListener(MovementListener listener) {
		mMovementListeners.remove(listener);
	}

	void initMovement(int movementType, int movementLevel) {
		switch(movementType) {
		case Constants.STOP_WAY_UPWARD:
			mMovement = new MovementThrowUp(mMovementListeners, movementLevel);
			break;
		case Constants.STOP_WAY_TAP:
			// TODO
			break;
		case Constants.STOP_WAY_SHAKE:
			// TODO
			break;
		}
	}

	void analyse(List<Sample> samples) {
		mMovement.detectMovement(samples);
		samples.clear();
	}
}
