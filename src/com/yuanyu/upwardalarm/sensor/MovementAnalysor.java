package com.yuanyu.upwardalarm.sensor;

import java.util.List;

import com.yuanyu.upwardalarm.sensor.MovementTracker.Sample;

public enum MovementAnalysor {
	
	INSTANCE;

	private final static int SUCCESSIVE_THRESHOLD = 5;
	private final static float VALUE_THRESHOLD = 2.0f;
	
	private int mSuccessiveCount = 0;
	
	public boolean analyse(List<Sample> samples) {
		return false; // TODO
	}
}
