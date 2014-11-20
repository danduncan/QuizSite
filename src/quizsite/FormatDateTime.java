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
	
	
	// Given a timestamp formatted as "yyyyMMdd hhmmss", get the user-friendly version as a String[] array
	// Note that if the input is empty or null, null is returned
	// If the input has only one word, its length is checked. Length 8 = yyyyMMdd, 6 = hhmmss
	// The returned String[] array always has two entries: [0] = date, [1] = time
	// In the event of invalid formatting, an entry will revert to the empty string (e.g. [0] = "" or [1] = "")
	public static String[] getUserDateTime(String timestamp) {
		// This will be the array that gets returned
		String[] output = new String[2];
		output[0] = ""; output[1] = "";

		// Check for valid input
		if (timestamp == null) {
			return null;
		} else if (timestamp.isEmpty()) {
			return output;
		}
		
		// Find number of words in timestamp
		String[] parsed = FormatString.parseStringByWhitespace(timestamp);
		
		int n = parsed.length;
		
		if (n == 1) {
			// Only one word was provided. Check whether it is a date or a time
			if (parsed[0].length() == DATESYSTEMPATTERN.length()) {
				// The one word is a date. Convert it to user-format and store in output[0]
				output[0] = getUserDate(parsed[0]);
			} else if (parsed[0].length() == TIMESYSTEMPATTERN.length()) {
				output[1] = getUserTime(parsed[0]);
			}
		} else {
			// At least two words were provided. 
			// parsed[0] = date, parsed[1] = time
			output[0] = getUserDate(parsed[0]);
			output[1] = getUserTime(parsed[1]);
		}
		
		// Return the final string array
		return output;
	}
	
	// Format a user-formatted date and time, either as separate strings or a {date,time} string array, into a systme-formatted single string
	public static String getSystemDateTime(String userDate, String userTime) {
		return getSystemDate(userDate) + " " + getSystemTime(userTime);
	}
	public static String getSystemDateTime(String[] userDateTime) {
		if (userDateTime.length < 2) return null;
		return getSystemDateTime(userDateTime[0],userDateTime[1]);
	}
	
	// Get the current date-time timestamp in system format
	public static String getCurrentSystemDateTime() {
		return getCurrentSystemDate() + " " + getCurrentSystemTime();
	}
	
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
	
	// Take a string formatted to show elapsed time in seconds
	// Convert to a string in the format hh:mm:ss
	public static String getUserElapsedTime(String timeInSeconds) {
		// Check validity of input
		if (timeInSeconds == null || timeInSeconds.isEmpty()) return null;
		Integer time = Integer.parseInt(timeInSeconds);
		if (time == null || time < 0) return null;
		
		// Format output
		String formatStr = "%02d";
		StringBuilder sb = new StringBuilder();
		if (time >= 3600) {
			int numHours = time/3600;
			time = time % 3600;
			sb.append(String.format(formatStr,numHours) + ":");
		} else {
			sb.append("00:");
		}
		if (time >= 60) {
			int numMinutes = time/60;
			time = time % 60;
			sb.append(String.format(formatStr,numMinutes) + ":");
		} else {
			sb.append("00:");
		}
		sb.append(String.format(formatStr,time));
		
		return sb.toString();
		
	}
	
	// For testing:
	public static void main(String[] args) {
		System.out.println("Testing formatting of date and time:");
		System.out.println(FormatDateTime.getCurrentSystemDate());
		System.out.println(FormatDateTime.getCurrentSystemTime());
		System.out.println(FormatDateTime.getCurrentUserDate());
		System.out.println(FormatDateTime.getCurrentUserTime());
		System.out.println("Testing formatting of elapsed time:");
		System.out.println("Correct: 01:43:06");
		System.out.println(getUserElapsedTime("6186"));
		
		// Test conversion of more complex date-time strings to user format
		String[] dt0 = getUserDateTime("20141225 012345");
		String[] dt1 = getUserDateTime("20141226");
		String[] dt2 = getUserDateTime("040608");
		System.out.println("dt0: \"" + dt0[0] + "\" ; \"" + dt0[1] + "\"");
		System.out.println("dt1: \"" + dt1[0] + "\" ; \"" + dt1[1] + "\"");
		System.out.println("dt2: \"" + dt2[0] + "\" ; \"" + dt2[1] + "\"");
		
		// Test conversion of date-time strings to system format
		System.out.println("Current system timestamp: \"" + getCurrentSystemDateTime() + "\"");
		
		
	}
}
