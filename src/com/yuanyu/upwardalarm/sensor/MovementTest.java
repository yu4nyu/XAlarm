package com.yuanyu.upwardalarm.sensor;

import java.util.List;

import com.yuanyu.upwardalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.upwardalarm.sensor.MovementTracker.Sample;

public class MovementTest extends Movement {

	public MovementTest(List<MovementListener> listeners, int movementLevel) {
		super(listeners, movementLevel);
	}

	@Override
	void detectMovement(List<Sample> samples) {
		
	}
}
