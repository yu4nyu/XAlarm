package com.yuanyu.upwardalarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class AlarmStopConfigDialog extends DialogFragment {
	
	public static interface OnAlarmStopConfiguredListener {
		void onAlarmStopConfigured(int type, int level, int times);
	}
	
	private OnAlarmStopConfiguredListener mOnAlarmStopConfiguredListener;
	
	private Spinner mSelectionSpinner;
	private Spinner mLevelSpinner;
	private SeekBar mTimesSeekBar;
	private TextView mTimesText;

	public void setOnAlarmStopConfiguredListener(OnAlarmStopConfiguredListener listener) {
		mOnAlarmStopConfiguredListener = listener;
	}
	
	private void notifyOnAlarmStopConfiguredListener(int type, int level, int times) {
		if(mOnAlarmStopConfiguredListener != null) {
			mOnAlarmStopConfiguredListener.onAlarmStopConfigured(type, level, times);
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.dialog_alarm_stop_config, null);
		
		mSelectionSpinner = (Spinner) view.findViewById(R.id.dialog_alarm_stop_config_selection_spinner);
		ArrayAdapter<CharSequence> selectionAdapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.stop_selections, android.R.layout.simple_spinner_item);
		selectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSelectionSpinner.setAdapter(selectionAdapter);
		
		mLevelSpinner = (Spinner) view.findViewById(R.id.dialog_alarm_stop_config_level_spinner);
		ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.level, android.R.layout.simple_spinner_item);
		levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mLevelSpinner.setAdapter(levelAdapter);
		
		mTimesText = (TextView) view.findViewById(R.id.dialog_alarm_stop_config_times_text);
		mTimesText.setText(getString(R.string.times) + " x1");
		
		mTimesSeekBar = (SeekBar) view.findViewById(R.id.dialog_alarm_stop_config_seekbar);
		mTimesSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mTimesText.setText(getString(R.string.times) + " x" + (progress + 1));
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(view)
			.setNegativeButton(android.R.string.cancel, null)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					int type = mSelectionSpinner.getSelectedItemPosition();
					int level = mLevelSpinner.getSelectedItemPosition();
					int times = mTimesSeekBar.getProgress();
					notifyOnAlarmStopConfiguredListener(type, level, times);
					mOnAlarmStopConfiguredListener = null;
				}
			});

		return builder.create();
	}
}
