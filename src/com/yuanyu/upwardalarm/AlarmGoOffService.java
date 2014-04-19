package com.yuanyu.upwardalarm;

import java.io.IOException;

import com.yuanyu.upwardalarm.model.AlarmGuardian;
import com.yuanyu.upwardalarm.model.Utils;
import com.yuanyu.upwardalarm.sensor.MovementAnalysor;
import com.yuanyu.upwardalarm.sensor.MovementTracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;

public class AlarmGoOffService extends Service implements MovementAnalysor.MovementListener {

	private static final String TAG = "AlarmGoOffService";
	
	// Volume suggested by media team for in-call alarms.
	private static final float IN_CALL_VOLUME = 0.125f;

	private MovementTracker mTracker;
	private Ringtone mRingtone; // TODO delete this
	
	private MediaPlayer mMediaPlayer;
	private TelephonyManager mTelephonyManager;

	public static void startService(Context context, boolean isVibrate, String ringtoneUri) {
		Intent i = new Intent(context, AlarmGoOffService.class);
		i.putExtra(AlarmBroadcastReceiver.EXTRA_IS_VIBRATE, isVibrate);
		i.putExtra(AlarmBroadcastReceiver.EXTRA_RINGTONE_URI, ringtoneUri);
		context.startService(i);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();

		mTracker = new MovementTracker(this);
		mTracker.start();
		MovementAnalysor.INSTANCE.addMovementListener(this);
		
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		boolean isVibrate = false;
		String uri = null;
		if(intent != null) { // The service was killed but restarted by the system
			isVibrate = intent.getBooleanExtra(AlarmBroadcastReceiver.EXTRA_IS_VIBRATE, false);
			uri = intent.getStringExtra(AlarmBroadcastReceiver.EXTRA_RINGTONE_URI);

			AlarmGuardian.markGotOff(this);
			AlarmGuardian.saveIsVibrate(this, isVibrate);
			AlarmGuardian.saveRingtoneUri(this, uri);
		}
		else {
			if(AlarmGuardian.isGotOff(this)) {
				isVibrate = AlarmGuardian.getIsVibrate(this);
				uri = AlarmGuardian.getRingtoneUri(this);
			}
		}

		if(isVibrate) {
			startVibration();
		}

		Log.d("YY", "uri = " + uri);
		mRingtone = Utils.getRingtoneByUriString(this, uri);
		Log.d("YY", "ringtone = " + mRingtone);
		if(mRingtone != null) {
			startRingtone();
			startAlarmNoise(uri, false);
		}

		return  START_STICKY ;
	}

	@Override
	public void onDestroy() {
		mTracker.stop();
		super.onDestroy();
	}

	private void startRingtone() {
		if(mRingtone != null) {
			mRingtone.play();
		}
	}

	private void startAlarmNoise(String uriString, boolean inTelephoneCall) {
		if(uriString == null || uriString.isEmpty()) {
			return;
		}

		Uri alarmNoise = Uri.parse(uriString);
		// Fall back on the default alarm if the database does not have an alarm stored.
		if (alarmNoise == null) {
			alarmNoise = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		}

		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				stopAlarmNoise();
				return true;
			}
		});

		try {
			// Check if we are in a call. If we are, use the in-call alarm
			// resource at a low volume to not disrupt the call.
			if (inTelephoneCall) {
				Log.v(TAG, "Using the in-call alarm");
				mMediaPlayer.setVolume(IN_CALL_VOLUME, IN_CALL_VOLUME);
				setDataSourceFromResource(this, mMediaPlayer, R.raw.beep);
			} else {
				mMediaPlayer.setDataSource(this, alarmNoise);
			}
			Utils.startAlarm(this, mMediaPlayer);
		} catch (Exception ex) {
			Log.v(TAG, "Using the fallback ringtone");
			// The alarmNoise may be on the sd card which could be busy right
			// now. Use the fallback ringtone.
			try {
				// Must reset the media player to clear the error state.
				mMediaPlayer.reset();
				setDataSourceFromResource(this, mMediaPlayer, R.raw.beep);
				Utils.startAlarm(this, mMediaPlayer);
			} catch (Exception ex2) {
				// At this point we just don't play anything.
				Log.e(TAG, "Failed to play fallback ringtone", ex2);
			}
		}
	}
	
	private void stopAlarmNoise() {
		if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.abandonAudioFocus(null);
            mMediaPlayer.release();
            mMediaPlayer = null;
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
		stopAlarmNoise();
		stopVibration();
		stopSelf();
		AlarmGuardian.markStopped(this);
	}
	
	private static void setDataSourceFromResource(Context context, MediaPlayer player, int res)
            throws IOException {
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(res);
        if (afd != null) {
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
        }
    }
}
