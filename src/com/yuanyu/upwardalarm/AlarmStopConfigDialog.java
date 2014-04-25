package com.yuanyu.upwardalarm;

import com.yuanyu.upwardalarm.model.Constants;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class AlarmStopConfigDialog extends DialogFragment {
	
	public static final String ARGS_STOP_WAY = "stop_way";
	public static final String ARGS_STOP_LEVEL = "stop_level";
	public static final String ARGS_STOP_TIMES = "stop_times";
	
	public static interface OnAlarmStopConfiguredListener {
		void onAlarmStopConfigured(int type, int level, int times);
	}
	
	private OnAlarmStopConfiguredListener mOnAlarmStopConfiguredListener;
	
	private Spinner mSelectionSpinner;
	private Spinner mLevelSpinner;
	private SeekBar mTimesSeekBar;
	private TextView mTimesText;
	
	private boolean mIsTestSensor;

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
		
		Bundle args = getArguments();
		if(args != null) { // Define alarm
			mIsTestSensor = false;
			
			ArrayAdapter<CharSequence> selectionAdapter = ArrayAdapter.createFromResource(
					getActivity(), R.array.stop_selections, android.R.layout.simple_spinner_item);
			selectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSelectionSpinner.setAdapter(selectionAdapter);
			
			mSelectionSpinner.setSelection(args.getInt(ARGS_STOP_WAY));
			mLevelSpinner.setSelection(args.getInt(ARGS_STOP_LEVEL));
			mTimesSeekBar.setProgress(args.getInt(ARGS_STOP_TIMES) - 1);
		}
		else { // Test sensor
			mIsTestSensor = true;
			
			// Do not show first selection "Click button"
			String[] selections = getResources().getStringArray(R.array.stop_selections);
			String[] newSelections = new String[selections.length - 1];
			System.arraycopy(selections, 1, newSelections, 0, selections.length - 1);
			ArrayAdapter<String> selectionAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, newSelections);
			selectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSelectionSpinner.setAdapter(selectionAdapter);
		}
		
		mSelectionSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(mIsTestSensor) {
					return;
				}
				
				if(position == Constants.STOP_WAY_BUTTON) {
					mLevelSpinner.setEnabled(false);
					mTimesSeekBar.setEnabled(false);
				}
				else {
					mLevelSpinner.setEnabled(true);
					mTimesSeekBar.setEnabled(true);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(view)
			.setNegativeButton(android.R.string.cancel, null)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					int type = mSelectionSpinner.getSelectedItemPosition();
					if(mIsTestSensor) {
						type++;
					}
					int level = mLevelSpinner.getSelectedItemPosition();
					int times = mTimesSeekBar.getProgress() + 1;
					notifyOnAlarmStopConfiguredListener(type, level, times);
					mOnAlarmStopConfiguredListener = null;
				}
			});

		return builder.create();
	}
}
