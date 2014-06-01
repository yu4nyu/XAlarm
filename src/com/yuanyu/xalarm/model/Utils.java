package com.yuanyu.xalarm.model;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.yuanyu.xalarm.R;

import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.text.Html;
import android.text.Spanned;

public class Utils {

	/**
	 * Determine if today's given hour and minute has passed or not
	 * @return
	 */
	@SuppressWarnings("unused")
	private static boolean isNextTimeToday(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		if(calendar.getTimeInMillis() > System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	/**
	 * Get the next time after certain days in millisecond with the given hour and minute
	 * @param hour
	 * @param minute
	 * @param daysAfter must be > 0
	 * @return
	 */
	static long getNextTimeMillisDaysAfter(int hour, int minute, int daysAfter) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// Do not check if is today as daysAfter must be > 0
		//if(calendar.getTimeInMillis() > System.currentTimeMillis()) { // Next time is in today
		//return calendar.getTimeInMillis();
		//}

		// Otherwise the next time is in tomorrow
		GregorianCalendar cal = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		boolean isLeapYear = cal.isLeapYear(year);
		int dayNumberOfYear = isLeapYear ? 366 : 365;
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		if(day + daysAfter - 1 < dayNumberOfYear) { // Today is not last day of this year
			calendar.set(Calendar.DAY_OF_YEAR, day + daysAfter);
		}
		else { // Today is the last day of this year
			calendar.set(Calendar.YEAR, year + 1);
			calendar.set(Calendar.DAY_OF_YEAR, daysAfter);
		}
		return calendar.getTimeInMillis();
	}

	/**
	 * Get the next time in millisecond with the given hour and minute
	 * @param hour
	 * @param minute
	 * @return The time of today if not passed yet or tomorrow otherwise
	 */
	private static long getNextTimeMillis(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		if(calendar.getTimeInMillis() > System.currentTimeMillis()) { // Next time is in today
			return calendar.getTimeInMillis();
		}

		// Otherwise the next time is in tomorrow
		GregorianCalendar cal = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		boolean isLeapYear = cal.isLeapYear(year);
		int dayNumberOfYear = isLeapYear ? 366 : 365;
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		if(day < dayNumberOfYear) { // Today is not last day of this year
			calendar.set(Calendar.DAY_OF_YEAR, day + 1);
		}
		else { // Today is the last day of this year
			calendar.set(Calendar.YEAR, year + 1);
			calendar.set(Calendar.DAY_OF_YEAR, 1);
		}
		return calendar.getTimeInMillis();
	}

	/**
	 * Get the next time when the alarm goes off by taking account the week repetition
	 * @return 0 if can't get the time
	 */
	public static long getGoOffTimeMillis(Alarm alarm) {		
		if(!alarm.isRepeat()) {
			return getNextTimeMillis(alarm.getHour(), alarm.getMinute());
		}

		Calendar todayCal = Calendar.getInstance();
		int today = todayCal.get(Calendar.DAY_OF_WEEK) - 1;

		Calendar alarmCal = Calendar.getInstance();
		alarmCal.set(Calendar.HOUR_OF_DAY, alarm.getHour());
		alarmCal.set(Calendar.MINUTE, alarm.getMinute());
		alarmCal.set(Calendar.SECOND, 0);
		alarmCal.set(Calendar.MILLISECOND, 0);

		boolean[] weekRepeat = alarm.getWeekRepeat();
		if(weekRepeat[today]) {
			if(alarmCal.after(todayCal)) {
				return alarmCal.getTimeInMillis();
			}
		}

		int dayAfter = 0;
		for(int i = today + 1; i < 7; i++) {
			dayAfter++;
			if(weekRepeat[i]) {
				return getNextTimeMillisDaysAfter(alarm.getHour(), alarm.getMinute(), dayAfter);
			}
		}
		for(int i = 0; i <= today; i++) {
			dayAfter++;
			if(weekRepeat[i]) {
				return getNextTimeMillisDaysAfter(alarm.getHour(), alarm.getMinute(), dayAfter);
			}
		}

		return 0;
	}

	/**
	 * Get the text of time displayed on items of alarm list
	 */
	public static Spanned getTimeText(int hour, int minute) {
		String hourZero = "";
		if(hour < 10) {
			hourZero = "0";
		}
		String minuteZero = "";
		if(minute < 10) {
			minuteZero = "0";
		}
		String html = "<b>" + hourZero + hour + "</b>" + ":" + minuteZero + minute;
		return Html.fromHtml(html);
	}

	public static Spanned getLabelSpannedText(Context context, Alarm alarm) {
		String html = "";

		// Set the label
		String label = alarm.getLabel();
		if(label != null && !label.trim().isEmpty()) {
			html += "<b>" + label + "</b> ";
		}

		return Html.fromHtml(html);
	}

	/**
	 * Get the text of label and repetition displayed on items of alarm list
	 */
	public static Spanned getRepeatSpannedText(Context context, Alarm alarm) {

		String html = "";
		if(alarm.isRepeat()) {
			// Set the repetition
			if(alarm.isRepeatWholeWeek()) {
				html += context.getString(R.string.every_day);
			}
			else {
				if(alarm.isMondayRepeat()) {
					html += context.getString(R.string.monday) + ",";
				}
				if(alarm.isTuesdayRepeat()) {
					html += context.getString(R.string.tuesday) + ",";
				}
				if(alarm.isWednesdayRepeat()) {
					html += context.getString(R.string.wednesday) + ",";
				}
				if(alarm.isThursdayRepeat()) {
					html += context.getString(R.string.thursday) + ",";
				}
				if(alarm.isFridayRepeat()) {
					html += context.getString(R.string.friday) + ",";
				}
				if(alarm.isSaturdayRepeat()) {
					html += context.getString(R.string.saturday) + ",";
				}
				if(alarm.isSundayRepeat()) {
					html += context.getString(R.string.sunday) + ",";
				}
			}

			// Remove last ',' if exists
			int index = html.lastIndexOf(",");
			if(index != -1 && index == html.length() - 1) {
				html = html.substring(0, html.length() - 1);
			}
		}

		return Html.fromHtml(html);
	}

	public static String getTextTimeBeforeGoOff(Context context, Alarm alarm) {
		String format = context.getString(R.string.time_before_go_off_text) + " ";
		long time = getGoOffTimeMillis(alarm);
		if(time == 0) {
			return "";
		}
		long timeBefore = time - System.currentTimeMillis();
		long minuteCount = TimeUnit.MILLISECONDS.toMinutes(timeBefore);
		int hour = (int) minuteCount / 60;
		int minute = (int) minuteCount % 60;

		String timeText = "";
		if(hour > 0) {
			timeText = hour + " " + context.getResources().getQuantityString(R.plurals.hour, hour);
		}
		if(minute > 0) {
			timeText += " " + minute + " " + context.getResources().getQuantityString(R.plurals.minute, minute);
		}
		if(hour == 0 && minute == 0) {
			timeText += context.getString(R.string.less_than_one_minute);
		}

		return String.format(format, timeText);
	}
	
	public static String getStopWayText(Context context, int way, int level, int times) {
		String result = context.getResources().getStringArray(R.array.stop_selections)[way];
		if(way != Constants.STOP_WAY_BUTTON) {
			result += " (" + context.getResources().getStringArray(R.array.level)[level] + ")";
			result += "  " + times + " ";
			if(times == 1) {
				result += context.getString(R.string.times_lowercase_singular);
			}
			else {
				result += context.getString(R.string.times_lowercase);
			}
		}
		return result;
	}

	/**
	 * Returns whether the SDK is KitKat or later
	 */
	public static boolean isKitKatOrLater() {
		return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2;
	}

	public static Ringtone getRingtoneByUriString(Context context, String uriString) {
		Uri uri = Uri.parse(uriString);
		return RingtoneManager.getRingtone(context, uri);
	}

	public static void startAlarm(Context context, MediaPlayer player) throws IOException {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		// Do not play alarms if stream volume is 0 (typically because ringer mode is silent).
		if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
			player.setAudioStreamType(AudioManager.STREAM_ALARM);
			player.setLooping(true);
			player.prepare();
			audioManager.requestAudioFocus(null,
					AudioManager.STREAM_ALARM, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
			player.start();
		}
	}
	
	public static String getVersionName(Context context) {
		try {
			return context.getPackageManager()
				    .getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static Set<WeakReference<Context>> sVibrationContexts = new HashSet<WeakReference<Context>>();
	
	public static void startVibration(Context context) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);

		int dot = 200;      // Length of a Morse Code "dot" in milliseconds
		int dash = 500;     // Length of a Morse Code "dash" in milliseconds
		int short_gap = 200;    // Length of Gap Between dots/dashes
		int medium_gap = 500;   // Length of Gap Between Letters
		int long_gap = 1000;    // Length of Gap Between Words
		long[] pattern = {
				0,  // Start immediately
				dash, short_gap, dot, short_gap, dash, short_gap, dash, // Y
				medium_gap,
				dash, short_gap, dot, short_gap, dash, short_gap, dash, // Y
				long_gap
		};
		vibrator.vibrate(pattern, 0);
		
		WeakReference<Context> reference = new WeakReference<Context>(context);
		sVibrationContexts.add(reference);
	}
	
	public static void stopVibration() {
		Iterator<WeakReference<Context>> iterator = sVibrationContexts.iterator();
		while(iterator.hasNext()) {
			Context c = iterator.next().get();
			if(c == null) {
				iterator.remove();
			}
			else {
				((Vibrator)c.getSystemService(Service.VIBRATOR_SERVICE)).cancel();
			}
		}
	}
}
