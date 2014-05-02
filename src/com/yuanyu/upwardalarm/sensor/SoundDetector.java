package com.yuanyu.upwardalarm.sensor;

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Handler;

public enum SoundDetector {

	INSTANCE;

	private static final double EMA_FILTER = 0.6;
	
	public static interface SoundAmplitudeListener {
		// TODO
	}

	private MediaRecorder mMediaRecorder;
	private double mEMA = 0.0;
	
	private Handler mHandler = new Handler();
	private Runnable mTimerSchedule = new Runnable() {
		@Override
		public void run() {
			
		}
	};

	public void start() {
		mMediaRecorder = new MediaRecorder();
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mMediaRecorder.setOutputFile("/tmp/audio");

		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mMediaRecorder.start();
		mEMA = 0.0;
	}

	public void stop() {
		if(mMediaRecorder != null) {
			mMediaRecorder.stop();
			mMediaRecorder.release();
		}
		
		// TODO remove temp audio file
	}

	public double getAmplitude() {
		if (mMediaRecorder != null) {
			return (mMediaRecorder.getMaxAmplitude() / 2700.0);
		}
		else {
			return 0;
		}
	}

	// TODO verify this
	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}
}
