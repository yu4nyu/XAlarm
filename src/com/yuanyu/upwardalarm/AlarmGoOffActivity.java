package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.Utils;
import com.yuanyu.upwardalarm.sensor.MovementTracker;

import android.media.Ringtone;
import android.os.Bundle;
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
		getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD); // Disable home key TODO make it work
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

	public static class AlarmGoOffDialog extends DialogFragment {
		
		private MovementTracker mTracker;
		private String mLable;
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
				startRingtone();
			}
			
			mTracker = new MovementTracker(getActivity());
			mTracker.start();
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			View view = inflater.inflate(R.layout.dialog_alarm_go_off, null);
			TextView labelView = (TextView) view.findViewById(R.id.dialog_alarm_go_off_label);
			labelView.setText(mLable);
			
			// TODO show animation
			
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

		private void startRingtone() {
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
