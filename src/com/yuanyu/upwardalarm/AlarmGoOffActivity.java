package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.Utils;
import com.yuanyu.upwardalarm.sensor.MovementTracker;

import android.media.Ringtone;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;

public class AlarmGoOffActivity extends Activity {

	private static final String ARGS_KEY_VIBRATE = "vibrate";
	private static final String ARGS_KEY_RINGTONE_URI = "ringtone";
	
	private boolean mIsVibrate;
	private String mRingtoneUri; // Absolute path of ringtone file, may be null
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_test);
		
		Intent intent = getIntent();
		mIsVibrate = intent.getBooleanExtra(AlarmBroadcastReceiver.EXTRA_IS_VIBRATE, false);
		mRingtoneUri = intent.getStringExtra(AlarmBroadcastReceiver.EXTRA_RINGTONE_URI);
		
		AlarmGoOffDialog dialog = new AlarmGoOffDialog();
		Bundle args = new Bundle();
		args.putBoolean(ARGS_KEY_VIBRATE, mIsVibrate);
		args.putString(ARGS_KEY_RINGTONE_URI, mRingtoneUri);
		dialog.setArguments(args);
		dialog.show(getFragmentManager(), "alarmGoOff");
	}
	
	public static class AlarmGoOffDialog extends DialogFragment {
		
		private MovementTracker mTracker;
		
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
			Ringtone ringtone = Utils.getRingtoneByUriString(getActivity(), ringtoneUri);
			if(ringtone != null) {
				startRingtone(ringtone);
			}
			
			mTracker = new MovementTracker(getActivity());
			mTracker.start();
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			View view = inflater.inflate(R.layout.dialog_alarm_go_off, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(view);
			return builder.create();
		}
		
		@Override
		public void onDestroyView() {
			mTracker.stop();
			super.onDestroyView();
		}

		@Override
		public void onDestroy() {
			mTracker.stop();
			super.onDestroy();
		}

		private void startRingtone(Ringtone ringtone) {
			// TODO
		}
		
		private void startVibration() {
			// TODO
		}
		
		private void stopRingtone() {
			// TODO
		}
		
		private void stopVibration() {
			// TODO
		}
	}
}
