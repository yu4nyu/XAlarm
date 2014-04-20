package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.RealTimeProvider;
import com.yuanyu.upwardalarm.sensor.MovementAnalysor;
import com.yuanyu.upwardalarm.ui.AlarmItemAnimator;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;

public class AlarmGoOffActivity extends Activity implements MovementAnalysor.MovementListener {

	private static final String TAG = "AlarmGoOffActivity";

	private static final String ARGS_KEY_LABEL = "label";
	private static final String ARGS_KEY_VIBRATE = "vibrate";
	private static final String ARGS_KEY_RINGTONE_URI = "ringtone";

	private String mLabel;
	private boolean mVibrate;
	private String mRingtoneUri;

	private AlarmGoOffDialog mDialog;
	private boolean isDestroyed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		setContentView(R.layout.activity_test);

		Intent intent = getIntent();
		mLabel = intent.getStringExtra(AlarmBroadcastReceiver.EXTRA_ALARM_LABEL);
		mVibrate = intent.getBooleanExtra(AlarmBroadcastReceiver.EXTRA_IS_VIBRATE, false);
		mRingtoneUri = intent.getStringExtra(AlarmBroadcastReceiver.EXTRA_RINGTONE_URI);

		MovementAnalysor.INSTANCE.addMovementListener(this);
		isDestroyed = false;

		mDialog = new AlarmGoOffDialog();
		Bundle args = new Bundle();
		args.putString(ARGS_KEY_LABEL, mLabel);
		args.putBoolean(ARGS_KEY_VIBRATE, mVibrate);
		args.putString(ARGS_KEY_RINGTONE_URI, mRingtoneUri);
		mDialog.setArguments(args);
		mDialog.show(getFragmentManager(), "alarmGoOff");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isDestroyed = true;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return true; // Disable all keys, but does not work for home key and power key
	}

	@Override
	public void onUpwardDetected() {
		if(!isFinishing() && !isDestroyed) {
			mDialog.release();
			mDialog.dismiss();
			finish();
			Log.d(TAG, "finish()");
		}
	}

	public static class AlarmGoOffDialog extends DialogFragment {

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

			ImageView icon = (ImageView) view.findViewById(R.id.dialog_alarm_go_off_icon);
			AlarmItemAnimator.shakeForever(getActivity(), icon);

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(view);

			// Start the ringtone and vibrate service
			AlarmGoOffService.startService(getActivity(), mIsVibrate, mRingtoneUri);

			return builder.create();
		}

		public void release() {
			mTimeProvider.stop();
		}
	}
}
