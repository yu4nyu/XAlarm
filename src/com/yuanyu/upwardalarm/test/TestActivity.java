package com.yuanyu.upwardalarm.test;

import java.util.List;

import com.yuanyu.upwardalarm.R;
import com.yuanyu.upwardalarm.sensor.MovementAnalysor;
import com.yuanyu.upwardalarm.sensor.MovementTracker;
import com.yuanyu.upwardalarm.sensor.MovementTracker.Sample;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import android.content.DialogInterface;

public class TestActivity extends Activity {

	private TextView mText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setContentView(R.layout.activity_test);
		mText = (TextView) findViewById(R.id.activity_alarm_go_off_text);
		mText.setMovementMethod(new ScrollingMovementMethod());

		AlarmGoOffDialog dialog = new AlarmGoOffDialog();
		dialog.show(getFragmentManager(), "alarmGoOff");
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return true;
	}

	public static class AlarmGoOffDialog extends DialogFragment {

		private MovementTracker mTracker;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setCancelable(false);

			mTracker = new MovementTracker(getActivity());
			mTracker.start();
			MovementAnalysor.INSTANCE.setMovementListener(null);
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			View view = inflater.inflate(R.layout.dialog_alarm_go_off, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(view);

			// For test
			final TestActivity activity = (TestActivity) getActivity();
			builder.setPositiveButton("Stop", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mTracker.stop();
					activity.showData(mTracker.getData());
				}
			});

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
	}

	private void showData(List<Sample> data) {
		StringBuilder builder = new StringBuilder();
		for(Sample s : data) {
			builder.append(s.independentValue() + "\n");
		}
		mText.setText(builder.toString());
	}
}
