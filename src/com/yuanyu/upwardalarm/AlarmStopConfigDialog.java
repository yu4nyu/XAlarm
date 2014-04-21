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
import android.widget.Spinner;

public class AlarmStopConfigDialog extends DialogFragment {
	
	private Spinner mSelectionSpinner;
	private Spinner mLevelSpinner;
	private SeekBar mTimesSeekBar;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.dialog_alarm_stop_config, null);
		
		mSelectionSpinner = (Spinner) view.findViewById(R.id.dialog_alarm_stop_config_selection_spinner);
		String[] selections = getActivity().getResources().getStringArray(R.array.stop_selections);
		ArrayAdapter<String> selectionAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_dropdown_item, selections);
		mSelectionSpinner.setAdapter(selectionAdapter);
		
		mLevelSpinner = (Spinner) view.findViewById(R.id.dialog_alarm_stop_config_level_spinner);
		String[] levels = getActivity().getResources().getStringArray(R.array.level);
		ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_dropdown_item, levels);
		mLevelSpinner.setAdapter(levelAdapter);
		
		mTimesSeekBar = (SeekBar) view.findViewById(R.id.dialog_alarm_stop_config_seekbar);
		// TODO
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(view)
			.setNegativeButton(android.R.string.cancel, null)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO
				}
			});

		return builder.create();
	}
}
