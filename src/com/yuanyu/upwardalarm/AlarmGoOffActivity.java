package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.RealTimeProvider;

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
	
	private String mLabel;
	
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
		
		AlarmGoOffDialog dialog = new AlarmGoOffDialog();
		Bundle args = new Bundle();
		args.putString(ARGS_KEY_LABEL, mLabel);
		dialog.setArguments(args);
		dialog.show(getFragmentManager(), "alarmGoOff");
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return true; // Disable all keys, but does not work for home key and power key
	}
	
	public static class AlarmGoOffDialog extends DialogFragment {

		private RealTimeProvider mTimeProvider;
		private String mLabel;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setCancelable(false);
			
			Bundle args = getArguments();
			mLabel = args.getString(ARGS_KEY_LABEL);
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
	}
}
