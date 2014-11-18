package quizsite;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;


/**
 * Static class to convert between system and user-friendly date formats
 * @author Dan
 *
 */
public class FormatDateTime {
	// Changing these will change how all user and system values are formatted
	// in methods below
	private static final String DATEUSERPATTERN = "MM/dd/yyyy";
	private static final String TIMEUSERPATTERN = "hh:mm:ss";

	private static final String DATESYSTEMPATTERN = "yyyyMMdd";
	private static final String TIMESYSTEMPATTERN = "hhmmss";
	
	
	// Takes a date object and returns a String formatted for system use
	public static String getSystemDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATESYSTEMPATTERN);
		return sdf.format(date);
	}
	
	// Takes a date object and returns a String formatted for the user
	public static String getUserDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATEUSERPATTERN);
		return sdf.format(date);
	}

	// Take a String formatted for the system and convert it to a String formatted for the user
	public static String getUserDate(String systemDate) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATESYSTEMPATTERN);
			date = sdf.parse(systemDate);

		} catch (java.text.ParseException e) {
			return systemDate;
		}
		return getUserDate(date);
	}

	// Take a String formatted for the user and convert it to a String formatted for the system
	public static String getSystemDate(String userDate) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATEUSERPATTERN);
			date = sdf.parse(userDate);

		} catch (java.text.ParseException e) {
			return userDate;
		}
		return getSystemDate(date);
	}
	
	// Takes a date object and returns a String formatted for system use
	public static String getSystemTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(TIMESYSTEMPATTERN);
		return sdf.format(date);
	}

	// Takes a date object and returns a String formatted for the user
	public static String getUserTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(TIMEUSERPATTERN);
		return sdf.format(date);
	}

	// Take a String formatted for the system and convert it to a String formatted for the user
	public static String getUserTime(String systemTime) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIMESYSTEMPATTERN);
			date = sdf.parse(systemTime);

		} catch (java.text.ParseException e) {
			return systemTime;
		}
		return getUserTime(date);
	}

	// Take a String formatted for the user and convert it to a String formatted for the system
	public static String getSystemTime(String userTime) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEUSERPATTERN);
			date = sdf.parse(userTime);

		} catch (java.text.ParseException e) {
			return userTime;
		}
		return getSystemTime(date);
	}
	
	public static String getCurrentSystemTime() {
		return getSystemTime(new Date());
	}
	public static String getCurrentUserTime() {
		return getUserTime(new Date());
	}
	public static String getCurrentSystemDate() {
		return getSystemDate(new Date());
	}
	public static String getCurrentUserDate() {
		return getUserDate(new Date());
	}
	
	// For testing:
	public static void main(String[] args) {
		System.out.println(FormatDateTime.getCurrentSystemDate());
		System.out.println(FormatDateTime.getCurrentSystemTime());
		System.out.println(FormatDateTime.getCurrentUserDate());
		System.out.println(FormatDateTime.getCurrentUserTime());
	}
}
