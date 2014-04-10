package com.yuanyu.upwardalarm.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.text.Html;
import android.text.Spanned;

public class Utils {
	
	/**
	 * Determine if today's given hour and minute has passed or not
	 * @return
	 */
	static boolean isNextTimeToday(int hour, int minute) {
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
	 * @param daysAfter must be > 0
	 * @return The time of today if not passed yet or tomorrow otherwise
	 */
	static long getNextTimeMillis(int hour, int minute) {
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
}
