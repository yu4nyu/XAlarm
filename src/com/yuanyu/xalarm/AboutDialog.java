package com.yuanyu.xalarm;

import com.yuanyu.xalarm.R;
import com.yuanyu.xalarm.model.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class AboutDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		String versionName = Utils.getVersionName(getActivity());
		String message = getString(R.string.app_name) + " " + versionName;
		builder
			.setTitle(R.string.action_about)
			.setMessage(message)
			.setPositiveButton(android.R.string.ok, null);
		return builder.create();
	}
}
