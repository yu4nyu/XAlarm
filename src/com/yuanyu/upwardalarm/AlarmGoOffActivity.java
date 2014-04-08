package com.yuanyu.upwardalarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;

public class AlarmGoOffActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alarm_go_off);
		
		AlarmGoOffDialog dialog = new AlarmGoOffDialog();
		dialog.show(getFragmentManager(), "alarmGoOff");
	}
	
	public static class AlarmGoOffDialog extends DialogFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setCancelable(false);
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			View view = inflater.inflate(R.layout.dialog_alarm_go_off, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(view);
			return builder.create();
		}
	}
}
