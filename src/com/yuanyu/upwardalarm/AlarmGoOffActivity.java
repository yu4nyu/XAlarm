package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.RealTimeProvider;
import com.yuanyu.upwardalarm.sensor.MovementAnalysor;

import android.os.Bundle;
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
	private boolean mVibrate;
	private String mRingtoneUri;
	
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
		mVibrate = intent.getBooleanExtra(AlarmBroadcastReceiver.EXTRA_IS_VIBRATE, false);
		mRingtoneUri = intent.getStringExtra(AlarmBroadcastReceiver.EXTRA_RINGTONE_URI);
		
		AlarmGoOffDialog dialog = new AlarmGoOffDialog();
		Bundle args = new Bundle();
		args.putString(ARGS_KEY_LABEL, mLabel);
		args.putBoolean(ARGS_KEY_VIBRATE, mVibrate);
		args.putString(ARGS_KEY_RINGTONE_URI, mRingtoneUri);
		dialog.setArguments(args);
		dialog.show(getFragmentManager(), "alarmGoOff");
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return true; // Disable all keys, but does not work for home key and power key
	}
	
	public static class AlarmGoOffDialog extends DialogFragment implements MovementAnalysor.MovementListener {

		private RealTimeProvider mTimeProvider;
		private String mLabel;
		private boolean mIsVibrate;
		private String mRingtoneUri;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setCancelable(false);
			
			Bundle args = getArguments();
			mLabel = args.getString(ARGS_KEY_LABEL);
			mIsVibrate = args.getBoolean(ARGS_KEY_VIBRATE);
			mRingtoneUri = args.getString(ARGS_KEY_RINGTONE_URI);
			
			MovementAnalysor.INSTANCE.addMovementListener(this);
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
			
			startService(); // Start the ringtone and vibrate service
			
			return builder.create();
		}

		@Override
		public void onUpwardDetected() {
			dismiss();
			getActivity().finish();
		}
		
		private void startService() {
			Intent i = new Intent(getActivity(), AlarmGoOffService.class);
			i.putExtra(AlarmBroadcastReceiver.EXTRA_IS_VIBRATE, mIsVibrate);
			i.putExtra(AlarmBroadcastReceiver.EXTRA_RINGTONE_URI, mRingtoneUri);
			getActivity().startService(i);
		}
	}
}
