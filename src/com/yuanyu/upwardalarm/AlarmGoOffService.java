package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.RealTimeProvider;
import com.yuanyu.upwardalarm.model.Utils;
import com.yuanyu.upwardalarm.sensor.MovementAnalysor;
import com.yuanyu.upwardalarm.sensor.MovementTracker;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.os.IBinder;
import android.os.Vibrator;

public class AlarmGoOffService extends Service implements MovementAnalysor.MovementListener {

	private MovementTracker mTracker;
	private RealTimeProvider mTimeProvider;

	private Ringtone mRingtone;

	@Override
	public void onCreate() {
		super.onCreate();

		mTracker = new MovementTracker(this);
		mTracker.start();
		MovementAnalysor.INSTANCE.setMovementListener(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		boolean isVibrate = intent.getBooleanExtra(AlarmBroadcastReceiver.EXTRA_IS_VIBRATE, false);
		if(isVibrate) {
			startVibration();
		}

		String uri = intent.getStringExtra(AlarmBroadcastReceiver.EXTRA_RINGTONE_URI);
		mRingtone = Utils.getRingtoneByUriString(this, uri);
		if(mRingtone != null) {
			startRingtone();
		}

		return  START_STICKY ;
	}

	@Override
	public void onDestroy() {
		mTracker.stop();
		mTimeProvider.stop();
		super.onDestroy();
	}

	private void startRingtone() {
		if(mRingtone != null) {
			mRingtone.play();
		}
	}

	private void startVibration() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		int dot = 200;      // Length of a Morse Code "dot" in milliseconds
		int dash = 500;     // Length of a Morse Code "dash" in milliseconds
		int short_gap = 200;    // Length of Gap Between dots/dashes
		int medium_gap = 500;   // Length of Gap Between Letters
		int long_gap = 1000;    // Length of Gap Between Words
		long[] pattern = {
				0,  // Start immediately
				dash, short_gap, dot, short_gap, dash, short_gap, dash, // Y
				medium_gap,
				dash, short_gap, dot, short_gap, dash, short_gap, dash, // Y
				long_gap
		};
		vibrator.vibrate(pattern, 0);
	}

	private void stopRingtone() {
		if(mRingtone != null) {
			mRingtone.stop();
		}
	}

	private void stopVibration() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.cancel();
	}

	@Override
	public void onUpwardDetected() {
		MovementAnalysor.INSTANCE.removeMovementListener();
		stopRingtone();
		stopVibration();
		stopSelf();
	}
}
