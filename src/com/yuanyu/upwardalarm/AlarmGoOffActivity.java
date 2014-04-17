package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.RealTimeProvider;
import com.yuanyu.upwardalarm.model.Utils;
import com.yuanyu.upwardalarm.sensor.MovementAnalysor;
import com.yuanyu.upwardalarm.sensor.MovementTracker;

import android.media.Ringtone;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;

public class AlarmGoOffActivity extends Activity {

	private static final String ARGS_KEY_LABEL = "label";
	private static final String ARGS_KEY_VIBRATE = "vibrate";
	private static final String ARGS_KEY_RINGTONE_URI = "ringtone";
	
	private String mLabel;
	private boolean mIsVibrate;
	private String mRingtoneUri; // Absolute path of ringtone file, may be null
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_test);
		
		// TODO savedInstanceState
		
		Intent intent = getIntent();
		mLabel = intent.getStringExtra(AlarmBroadcastReceiver.EXTRA_ALARM_LABEL);
		mIsVibrate = intent.getBooleanExtra(AlarmBroadcastReceiver.EXTRA_IS_VIBRATE, false);
		mRingtoneUri = intent.getStringExtra(AlarmBroadcastReceiver.EXTRA_RINGTONE_URI);
		
		AlarmGoOffDialog dialog = new AlarmGoOffDialog();
		Bundle args = new Bundle();
		args.putString(ARGS_KEY_LABEL, mLabel);
		args.putBoolean(ARGS_KEY_VIBRATE, mIsVibrate);
		args.putString(ARGS_KEY_RINGTONE_URI, mRingtoneUri);
		dialog.setArguments(args);
		dialog.show(getFragmentManager(), "alarmGoOff");
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return true; // Disable all keys, but does not work for home key and power key
	}
	
	public static class AlarmGoOffDialog extends DialogFragment implements MovementAnalysor.MovementListener {
		
		private MovementTracker mTracker;
		private RealTimeProvider mTimeProvider;
		
		private String mLabel;
		private Ringtone mRingtone;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setCancelable(false);
			
			Bundle args = getArguments();
			
			boolean isVibrate = args.getBoolean(ARGS_KEY_VIBRATE, false);
			if(isVibrate) {
				startVibration();
			}
			
			String ringtoneUri = args.getString(ARGS_KEY_RINGTONE_URI);
			mRingtone = Utils.getRingtoneByUriString(getActivity(), ringtoneUri);
			if(mRingtone != null) {
				startRingtone(mRingtone);
			}
			
			mLabel = args.getString(ARGS_KEY_LABEL);
			
			mTracker = new MovementTracker(getActivity());
			mTracker.start();
			MovementAnalysor.INSTANCE.setMovementListener(this);
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			View view = inflater.inflate(R.layout.dialog_alarm_go_off, null);
			
			TextView labelView = (TextView) view.findViewById(R.id.dialog_alarm_go_off_label);
			if(mLabel!= null && !mLabel.isEmpty()) {
				labelView.setText(mLabel);
				labelView.setVisibility(View.VISIBLE);
			}
			
			TextView timeText = (TextView) view.findViewById(R.id.dialog_alarm_go_off_time);
			mTimeProvider = new RealTimeProvider();
			mTimeProvider.start(timeText);
			
			// TODO show animation
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(view);
			return builder.create();
		}
		
		@Override
		public void onDestroyView() {
			release();
			super.onDestroyView();
		}

		@Override
		public void onDestroy() {
			release();
			super.onDestroy();
		}
		
		private void release() {
			mTracker.stop();
			mTimeProvider.stop();
		}

		private void startRingtone(Ringtone ringtone) {
			if(ringtone != null) {
				ringtone.play();
			}
		}
		
		private void startVibration() {
			Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
			
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
		
		private void stopRingtone(Ringtone ringtone) {
			if(ringtone != null) {
				ringtone.stop();
			}
		}
		
		private void stopVibration() {
			Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
			vibrator.cancel();
		}

		@Override
		public void onUpwardDetected() {
			MovementAnalysor.INSTANCE.removeMovementListener();
			stopRingtone(mRingtone);
			stopVibration();
			dismiss();
			getActivity().finish();
		}
	}
}
