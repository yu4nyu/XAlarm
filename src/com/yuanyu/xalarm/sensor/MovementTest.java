package com.yuanyu.xalarm.sensor;

import java.util.List;

import com.yuanyu.xalarm.sensor.MovementAnalysor.MovementListener;
import com.yuanyu.xalarm.sensor.MovementTracker.Sample;

public class MovementTest extends Movement {

	public MovementTest(List<MovementListener> listeners, int movementLevel) {
		super(listeners, movementLevel);
	}

	@Override
	void detectMovement(List<Sample> samples) {
		
	}
}
