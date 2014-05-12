package com.yuanyu.xalarm.sensor;

import java.util.ArrayList;
import java.util.List;

import com.yuanyu.xalarm.model.Constants;
import com.yuanyu.xalarm.sensor.MovementTracker.Sample;

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
		case Constants.STOP_WAY_TEST:
			mMovement = new MovementTest(new ArrayList<MovementListener>(), movementLevel);
			break;
		case Constants.STOP_WAY_UPWARD:
			mMovement = new MovementThrowUp(mMovementListeners, movementLevel);
			break;
		case Constants.STOP_WAY_TAP:
			mMovement = new MovementTap(mMovementListeners, movementLevel);
			break;
		case Constants.STOP_WAY_SHAKE:
			mMovement = new MovementShake(mMovementListeners, movementLevel);
			break;
		case Constants.STOP_WAY_SHOUT:
			mMovement = new MovementShout(mMovementListeners, movementLevel);
			break;
		case Constants.STOP_WAY_TURN_OVER:
			mMovement = new MovementTurnOver(mMovementListeners, movementLevel);
			break;
		}
	}

	void analyse(List<Sample> samples) {
		mMovement.detectMovement(samples);
		if(!(mMovement instanceof MovementTest)) {
			samples.clear();
		}
	}
}
