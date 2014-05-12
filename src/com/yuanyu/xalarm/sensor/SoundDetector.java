package com.yuanyu.xalarm.sensor;

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Handler;

public enum SoundDetector {

	INSTANCE;

	private static final long SCHEDULE_PERIODE = 200;
	
	public static interface SoundAmplitudeListener {
		void onRegularSoundDetection(double amplitude);
	}
	private SoundAmplitudeListener mSoundAmplitudeListener;

	private MediaRecorder mMediaRecorder;
	
	private Handler mHandler = new Handler();
	private Runnable mTimerSchedule = new Runnable() {
		@Override
		public void run() {
			notifyListener();
			mHandler.postDelayed(this, SCHEDULE_PERIODE);
		}
	};
	
	private void notifyListener() {
		if(mSoundAmplitudeListener != null) {
			mSoundAmplitudeListener.onRegularSoundDetection(getDecibel());
		}
	}

	public void start(SoundAmplitudeListener listener) {
		mSoundAmplitudeListener = listener;
		
		mMediaRecorder = new MediaRecorder();
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mMediaRecorder.setAudioEncodingBitRate(44100);
		mMediaRecorder.setAudioSamplingRate(16);
		mMediaRecorder.setOutputFile("/dev/null");

		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mMediaRecorder.start();
		
		mHandler.postDelayed(mTimerSchedule, SCHEDULE_PERIODE);
	}

	public void stop() {
		mSoundAmplitudeListener = null;
		
		if(mMediaRecorder != null) {
			mMediaRecorder.stop();
			mMediaRecorder.release();
			mMediaRecorder = null;
		}
		
		mHandler.removeCallbacksAndMessages(null);
	}

	private double getAmplitude() {
		if (mMediaRecorder != null) {
			return mMediaRecorder.getMaxAmplitude();
		}
		else {
			return 0;
		}
	}
	
	private double getDecibel() {
		return 20 * Math.log10(getAmplitude());
	}
}
