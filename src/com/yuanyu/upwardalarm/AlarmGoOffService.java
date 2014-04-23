package com.yuanyu.upwardalarm;

import java.io.IOException;

import com.yuanyu.upwardalarm.model.AlarmGuardian;
import com.yuanyu.upwardalarm.model.Constants;
import com.yuanyu.upwardalarm.model.Manager;
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
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class AlarmGoOffService extends Service implements MovementAnalysor.MovementListener {

	private static final String TAG = "AlarmGoOffService";

	private MovementTracker mTracker;
	
	private int mStopWay;
	private int mStopLevel;
	private int mStopTimes;
	
	private boolean mIsVibrate;
	private String mRingtoneUri;
	
	private MediaPlayer mMediaPlayer;
	private TelephonyManager mTelephonyManager;
	private int mlastPhoneState;
	
	private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String ignored) {
        	if(state == mlastPhoneState) return;
            if (state == TelephonyManager.CALL_STATE_IDLE) { // Call finished
            	Log.d(TAG, "Phone state changed to IDLE");
            	startAlarm();
            }
            else { // Call started
            	Log.d(TAG, "Phone state changed to BUSY");
            	mlastPhoneState = state;
            	stopAlarm();
            }
        }
    };

	public static void startService(Context context, boolean isVibrate, String ringtoneUri,
			int stopWay, int stopLevel, int stopTimes) {
		Intent i = new Intent(context, AlarmGoOffService.class);
		i.putExtra(AlarmBroadcastReceiver.EXTRA_IS_VIBRATE, isVibrate);
		i.putExtra(AlarmBroadcastReceiver.EXTRA_RINGTONE_URI, ringtoneUri);
		i.putExtra(AlarmBroadcastReceiver.EXTRA_STOP_WAY, stopWay);
		i.putExtra(AlarmBroadcastReceiver.EXTRA_STOP_LEVEL, stopLevel);
		i.putExtra(AlarmBroadcastReceiver.EXTRA_STOP_TIMES, stopTimes);
		Manager.INSTANCE.requireWakeLock(context);
		context.startService(i);
	}
	
	public static void stopService(Context context) {
		Intent i = new Intent(context, AlarmGoOffService.class);
		Manager.INSTANCE.releaseWakeLock();
		// TODO need to release other resources here ???
		context.stopService(i);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();

		mTracker = new MovementTracker(this);
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

		Log.d(TAG, "onStartCommand");
		
		boolean started = false;
		mIsVibrate = false;
		mRingtoneUri = null;
		if(intent != null) { // The service was killed but restarted by the system
			mIsVibrate = intent.getBooleanExtra(AlarmBroadcastReceiver.EXTRA_IS_VIBRATE, false);
			mRingtoneUri = intent.getStringExtra(AlarmBroadcastReceiver.EXTRA_RINGTONE_URI);
			mStopWay = intent.getIntExtra(AlarmBroadcastReceiver.EXTRA_STOP_WAY, Constants.STOP_WAY_BUTTON);
			mStopLevel = intent.getIntExtra(AlarmBroadcastReceiver.EXTRA_STOP_LEVEL, Constants.LEVEL_EASY);
			mStopTimes = intent.getIntExtra(AlarmBroadcastReceiver.EXTRA_STOP_TIMES, 1);

			AlarmGuardian.markGotOff(this);
			AlarmGuardian.saveIsVibrate(this, mIsVibrate);
			AlarmGuardian.saveRingtoneUri(this, mRingtoneUri);
			// TODO save stop way, stop level, stop times
			
			started = true;
		}
		else {
			if(AlarmGuardian.isGotOff(this)) {
				mIsVibrate = AlarmGuardian.getIsVibrate(this);
				mRingtoneUri = AlarmGuardian.getRingtoneUri(this);
				// TODO get stop way
				started = true;
			}
		}
		
		if(started) {
			if(mStopWay != Constants.STOP_WAY_BUTTON) {
				mTracker.start(mStopWay, mStopLevel, mStopTimes);
			}
			else {
				MovementAnalysor.INSTANCE.removeMovementListener(this);
			}
			startAlarm();
		}
		else {
			Manager.INSTANCE.releaseWakeLock();
		}

		return  START_STICKY ;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		mTracker.stop();
		mTracker.clearData();
		super.onDestroy();
	}
	
	private void startAlarm() {
		Manager.INSTANCE.requireWakeLock(this); // Make sure the device is awake
		
		mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		mlastPhoneState = mTelephonyManager.getCallState();
		boolean isInCall = mlastPhoneState != TelephonyManager.CALL_STATE_IDLE;
		if(!isInCall) {
			if(mIsVibrate && !isInCall) {
				startVibration();
			}
			if(mRingtoneUri != null && !mRingtoneUri.isEmpty()) {
				startAlarmNoise(mRingtoneUri);
			}
		}
	}
	
	private void stopAlarm() {
		stopAlarmNoise();
		stopVibration();
		Manager.INSTANCE.releaseWakeLock();
	}

	private void startAlarmNoise(String uriString) {
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
			mMediaPlayer.setDataSource(this, alarmNoise);
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

	private void stopVibration() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.cancel();
	}

	@Override
	public void onMovementDetected() {
		Log.d(TAG, "onUpwardDetected");
		mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		stopAlarm();
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
