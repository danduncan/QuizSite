/**
 * This is a static class that handles sanitizing of user inputs and parsing of strings into substring arrays
 * Any kind fo generic string parsing or formatting can go here
 */

package quizsite;

public class FormatString {
	// These are regular expressions that define the legal characters for different user inputs
	// Feel free to add your own
	public static final String ALLLEGALCHARS = "a-zA-Z0-9!@#$%^&*()?,.'<> -";
	public static final String NAMELEGALCHARS = "a-zA-Z' -";
	public static final String EMAILLEGALCHARS = "a-zA-Z0-9@. -";
	
	public FormatString() {
	}
	
	/**
	 * Given a regex of all legal characters, eliminate illegal characters from a string
	 * @param str
	 * @param legalChars
	 * @return
	 */
	public static String sanitizeString(String str, String legalChars) {
		if (str==null || legalChars==null) return null;
		// Eliminate illegal characters
		String illegalChars = "[^" + legalChars + "]";
		return str.replaceAll(illegalChars,"");
	}
	
	/**
	 * Parse a given string into an array of substrings delimited by any whitespace
	 */
	public static String[] parseStringByWhitespace(String str) {
		if (str==null) return null;
		return str.split("\\s+");
	}
	
	// Testing only
	public static void main(String[] args) {
		String test = "Dan-i\\e;l D'un,ca/n+= d\"a'nduncan2010,;@gmail.com?!?!?! !@#$%^&*()-_=+[]{}\\|;:'\",.<>/?`~";
		System.out.println("Original string: \"" + test + "\"");
		System.out.println("All legal chars: \"" + sanitizeString(test,ALLLEGALCHARS) + "\"");
		System.out.println("All name chars: \"" + sanitizeString(test,NAMELEGALCHARS) + "\"");
		System.out.println("All email chars: \"" + sanitizeString(test,EMAILLEGALCHARS) + "\"");
		
	}
}
