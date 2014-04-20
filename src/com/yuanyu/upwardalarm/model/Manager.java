package com.yuanyu.upwardalarm.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.yuanyu.upwardalarm.AlarmBroadcastReceiver;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

public enum Manager {

	INSTANCE;
	
	private final static String TAG = "Manager";

	final static String PREFS_KEY = "prefs";
	private final static String INTENT_DATA_PREFIX = "com.yuanyu.upwardalarm:";
	private final static String PREFS_UNIQUE_ID_KEY = "unique_id";

	private final static String ALARM_DATA_FILE_PREFIX = "alarm_data_";
	
	private PowerManager.WakeLock mCpuWakeLock;

	private class AlarmComparator implements Comparator<Alarm> {
		@Override
		public int compare(Alarm lhs, Alarm rhs) {
			int o1 = lhs.getHour() * 60 + lhs.getMinute();
            int o2 = rhs.getHour() * 60 + rhs.getMinute();
            return o1 < o2 ? -1 : (o1 == o2 ? 0 : 1);
		}
	}
	
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

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void registerForKitKatOrLater(AlarmManager alarmManager, long timeInMillis, PendingIntent alarmPending) {
		alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, alarmPending);
		
		Date date = new Date(timeInMillis);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
		Log.d(TAG, "set exact alarm at " + format.format(date));
	}

	/**
	 * Register the alarm to android system with given time
	 */
	void register(Context context, Alarm alarm, long timeInMillis) {
		if(timeInMillis == 0) {
			Log.d(TAG, "Error, register time is 0");
		}
		
		Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
		intent.setData(Uri.parse(INTENT_DATA_PREFIX + alarm.getId()));
		intent.putExtra(AlarmBroadcastReceiver.EXTRA_ALARM_ID, alarm.getId());
		intent.putExtra(AlarmBroadcastReceiver.EXTRA_ALARM_LABEL, alarm.getLabel());
		intent.putExtra(AlarmBroadcastReceiver.EXTRA_IS_VIBRATE, alarm.getVibrateEnable());
		intent.putExtra(AlarmBroadcastReceiver.EXTRA_RINGTONE_URI, alarm.getRingtoneUri());
		PendingIntent alarmPending = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
		if(Utils.isKitKatOrLater()) {
			registerForKitKatOrLater(alarmManager, timeInMillis, alarmPending);
		}
		else {
			alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, alarmPending);
			
			Date date = new Date(timeInMillis);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
			Log.d(TAG, "set alarm at " + format.format(date));
		}
	}

	/**
	 * Register the alarm to android system
	 */
	public void register(Context context, Alarm alarm) {
		register(context, alarm, Utils.getGoOffTimeMillis(alarm));
	}

	/**
	 * Unregister the alarm to android system
	 */
	public void unregister(Context context, int alarmId) {
		Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
		intent.setData(Uri.parse(INTENT_DATA_PREFIX + alarmId));
		PendingIntent alarmPending = PendingIntent.getBroadcast(context, alarmId, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
		alarmManager.cancel(alarmPending);
	}
	
	public void resetForNextTime(Context context, Alarm alarm) {
		if(alarm.isRepeat()) {
			long time = Utils.getGoOffTimeMillis(alarm);
			if(time != 0) {
				register(context, alarm, time);
			}
		}
	}

	/**
	 * Register the alarm to android system for the next time.
	 */
	@Deprecated
	public void resetIfRepeat(Context context, Alarm alarm) {
		if(alarm.isRepeat()) {
			if(alarm.isRepeatWholeWeek()) {
				register(context, alarm);
			}
			else {
				Calendar calendar = Calendar.getInstance();
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // Sunday is 1
				int daysAfter = 0;
				boolean isFound = false;
				boolean[] weekRepeat = alarm.getWeekRepeat();
				for(int i = dayOfWeek; i < weekRepeat.length; i++) {
					if(!weekRepeat[i]) {
						daysAfter++;
					}
					else {
						isFound = true;
						break;
					}
				}
				if(!isFound) {
					for(int i = 0; i < dayOfWeek; i++) {
						if(!weekRepeat[i]) {
							daysAfter++;
						}
						else {
							isFound = true;
							break;
						}
					}
				}
				if(isFound) {
					long time = Utils.getNextTimeMillisDaysAfter(alarm.getHour(), alarm.getMinute(), daysAfter);
					Manager.INSTANCE.register(context, alarm, time);
				}
			}
		}
	}

	/**
	 * Save alarm to file system
	 */
	// TODO maybe use a AsyncTask ?
	public void saveAlarm(Context context, Alarm alarm) {
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
	public void deleteAlarmFile(Context context, Alarm alarm) {
		File file = new File(context.getFilesDir(), ALARM_DATA_FILE_PREFIX + alarm.getId());
		file.delete(); // TODO take account the return value ?
	}

	/**
	 * @return null if can't get the valid Ringtone object
	 */
	public Ringtone getRingtone(Context context, String ringtoneUri) {
		if(ringtoneUri == null || ringtoneUri.isEmpty()) {
			return null;
		}
		Uri uri = Uri.parse(ringtoneUri);
		if(uri == null) {
			return null;
		}
		return RingtoneManager.getRingtone(context, uri);
	}

	// TODO maybe use a AsyncTask ?
	public List<Alarm> getSavedAlarms(Context context) {

		File dir = context.getFilesDir();
		String[] files = dir.list();
		List<Alarm> result = new ArrayList<Alarm>();

		if(files != null) {
			for(int i = files.length - 1; i >= 0; i--) {
				try {
					FileInputStream fis = context.openFileInput(files[i]);
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
		}
		
		Collections.sort(result, new AlarmComparator());
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
	
	public void requireWakeLock(Context context) {
		if (mCpuWakeLock != null) {
            return;
        }
		
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		mCpuWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Upward Alarm");
		mCpuWakeLock.acquire();
	}
	
	public void releaseWakeLock() {
		if(mCpuWakeLock == null) {
			return;
		}
		
		mCpuWakeLock.release();
		mCpuWakeLock = null;
	}
}
