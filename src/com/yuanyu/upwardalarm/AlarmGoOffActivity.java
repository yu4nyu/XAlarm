package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.RealTimeProvider;
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
				startRingtone();
			}
			
			mLabel = args.getString(ARGS_KEY_LABEL);
			
			mTracker = new MovementTracker(getActivity());
			mTracker.start();
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
