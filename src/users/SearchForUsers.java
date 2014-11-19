/**
 * This class implements the SEARCH function to find users in the table matching basic search criteria
 * This is a static class and requires only a DatabaseConnection and a string of search criteria to work
 */

package users;

import java.sql.ResultSet;

public class SearchForUsers {
	private quizsite.DatabaseConnection dc;
	private static final String colUsername = "username";
	private static final String colFirstName = "firstname";
	private static final String colLastName = "lastname";
	private static final String colEmail = "email";
	public static final String allLegalChars = "a-zA-Z0-9!@#$%^&*()?,.'<> -";
	public static final String nameLegalChars = "a-zA-Z' -";
	public static final String emailLegalChars = "a-zA-Z0-9@. -";
	private static final String SEARCHTABLE = quizsite.MyDBInfo.USERTABLE;
	
	public SearchForUsers(quizsite.DatabaseConnection dc) {
		this.dc = dc;
	}
	
	/**
	 * Given a user string, find all users whose first name, last name, or username match one or more of the words in the query
	 * Query should read: SELECT * FROM users WHERE username="query1" OR 
	 */
	public static ResultSet basicSearch(quizsite.DatabaseConnection dc, String searchQuery) {
		String query = buildBasicQuery(searchQuery);
		return dc.executeQuery(query);
	}
	
	
	/**
	 * Given the user's search terms, construct a MySQL query that tests for each term in each of the firstname, lastname, username, and email columns
	 * @param searchQuery
	 * @return
	 */
	protected static String buildBasicQuery(String searchQuery) {
		// Sanitize the user query
		String nameQuery = sanitizeString(searchQuery,nameLegalChars);
		String emailQuery = sanitizeString(searchQuery,emailLegalChars);
		
		// Separate the query into one-word substrings
		String[] nameSplit = nameQuery.split("\\s+");
		String[] emailSplit = emailQuery.split("\\s+");
		
		// Construct query
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM " + SEARCHTABLE + " WHERE ");
		for (int i = 0; i < nameSplit.length; i++) {
			if (i > 0) {
				sb.append(" OR ");
			}
			sb.append(colUsername + " LIKE \"%" + nameSplit[i] + "%\" OR ");
			sb.append(colFirstName + " LIKE \"%" + nameSplit[i] + "%\" OR ");
			sb.append(colLastName + " LIKE \"%" + nameSplit[i] + "%\"");
		}
		for (int i = 0; i < emailSplit.length; i++) {
			sb.append(" OR ");
			sb.append(colEmail + " LIKE \"%" + emailSplit[i] + "%\"");
		}
		sb.append(";");
		
		return sb.toString();
	}

	
	/**
	 * Given a user string, sanitize it by removing all characters not specified in the legalChars regex
	 * @param searchQuery
	 * @param legal
	 * @return
	 */
	protected static String sanitizeString(String searchQuery, String legalChars) {
		// Eliminate illegal characters
		String illegalChars = "[^" + legalChars + "]";
		searchQuery = searchQuery.replaceAll(illegalChars,"");
		return searchQuery;
	}
	
	public static void main(String[] args) {
		String test = "Dan-iel D'uncan dan\\dun/can2010@\"gmail.com?!?!";
		System.out.println("Original: " + test);
		System.out.println("All legal chars: " + sanitizeString(test, allLegalChars));
		System.out.println("Name formatted: " + sanitizeString(test, nameLegalChars));
		System.out.println("Email formatted: " + sanitizeString(test, emailLegalChars));
		
		System.out.println("Basic MySQL Query:");
		System.out.println("\t" + buildBasicQuery(test));
		
	}

}
