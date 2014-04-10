package com.yuanyu.upwardalarm.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public enum Manager {

	INSTANCE;

	public final static int GET_UNIQUE_ID_ERROR = -1;

	private final static String PREFS_KEY = "prefs";
	private final static String PREFS_UNIQUE_ID_KEY = "unique_id";

	private final static String ALARM_DATA_FILE_PREFIX = "alarm_data_";

	int getUniqueId(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
		int result = sp.getInt(PREFS_UNIQUE_ID_KEY, -1);

		if(result < 0) {
			result = 0;
		}

		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(PREFS_UNIQUE_ID_KEY, result + 1); // Error will occur if result+1 > Integer.MAX. But... :)
		editor.apply();

		return result;
	}

	/**
	 * Save alarm to file system
	 */
	// TODO maybe use a AsyncTask ?
	void saveAlarm(Context context, Alarm alarm) {
		try {
			FileOutputStream fos;
			fos = context.openFileOutput(ALARM_DATA_FILE_PREFIX + alarm.getId(), Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(alarm);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// TODO maybe use a AsyncTask ?
	public List<Alarm> getSavedAlarms(Context context) {

		File dir = context.getFilesDir();
		String[] files = dir.list();
		List<Alarm> result = new ArrayList<Alarm>();

		for(String str : files) {
			Log.d("YY", "file name = " + str);
			try {
				FileInputStream fis = context.openFileInput(str);
				ObjectInputStream is = new ObjectInputStream(fis);
				Alarm alarm = (Alarm) is.readObject();
				result.add(alarm);
				is.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public Alarm getSavedAlarmById(Context context, int id) {
		Alarm result = null;
		String file = ALARM_DATA_FILE_PREFIX + id;
		try {
			FileInputStream fis = context.openFileInput(file);
			ObjectInputStream is = new ObjectInputStream(fis);
			result = (Alarm) is.readObject();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public void onApplicationExit() {
		// TODO Delete the deleted alarm files
	}
}
