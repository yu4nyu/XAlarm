package com.yuanyu.upwardalarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public class AboutDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		String versionName = "";
		try {
			versionName = getActivity().getPackageManager()
				    .getPackageInfo(getActivity().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String message = getString(R.string.app_name) + " " + versionName;
		builder
			.setTitle(R.string.action_about)
			.setMessage(message)
			.setPositiveButton(android.R.string.ok, null);
		return builder.create();
	}
}
