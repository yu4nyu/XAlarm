package com.yuanyu.xalarm.model;

public class Constants {

	public static final int STOP_WAY_TEST = -1; // For debug test only
	public static final int STOP_WAY_BUTTON = 0;
	public static final int STOP_WAY_UPWARD = 1;
	public static final int STOP_WAY_TAP = 2;
	public static final int STOP_WAY_SHAKE = 3;
	public static final int STOP_WAY_SHOUT = 4;
	public static final int STOP_WAY_TURN_OVER = 5;

	public static final int LEVEL_EASY = 0;
	public static final int LEVEL_MODERATE = 1;
	public static final int LEVEL_HARD = 2;

	public static boolean isNeedAccelerometer(int stopWay) {
		switch(stopWay) {
		case STOP_WAY_TEST:
		case STOP_WAY_UPWARD:
		case STOP_WAY_TAP:
		case STOP_WAY_SHAKE:
		case STOP_WAY_TURN_OVER:
			return true;
		}
		return false;
	}
	
	public static boolean isNeedMicrophone(int stopWay) {
		if(stopWay == STOP_WAY_SHOUT) {
			return true;
		}
		return false;
	}
	
	public static boolean isForProVersion(int stopWay) {
		if(stopWay == STOP_WAY_TURN_OVER) {
			return true;
		}
		return false;
	}
	
	public static String GOOGLE_PLAY_URI = "https://play.google.com/store/apps/details?id=com.yuanyu.upwardalarm";
}