/**
 * This static class implements the SEARCH function to find users in the table matching basic search criteria
 * This requires only a DatabaseConnection and 1 or more strings of search criteria to work
 */

package users;

import java.sql.ResultSet;

public class SearchForUsers {
	private static final String colUsername = "username";
	private static final String colFirstName = "firstname";
	private static final String colLastName = "lastname";
	private static final String colEmail = "email";
	public static final String allLegalChars = "a-zA-Z0-9!@#$%^&*()?,.'<> -";
	public static final String nameLegalChars = "a-zA-Z' -";
	public static final String emailLegalChars = "a-zA-Z0-9@. -";
	private static final String SEARCHTABLE = quizsite.MyDBInfo.USERTABLE;
	
	public SearchForUsers() {
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
	 * Given strings for username, firstname, lastname, and/or email, find all matches
	 * Note: null's or empty strings may be provided for any field you do not wish to search
	 */
	public static ResultSet advancedSearch(quizsite.DatabaseConnection dc, String username, String firstname, String lastname, String email) {
		if(dc==null) return null;
		String query = buildAdvancedQuery(username,firstname,lastname,email);
		if(query == null || query.isEmpty()) return null;
		ResultSet rs = dc.executeQuery(query);
		return rs;
	}
	
	/**
	 * Given the strings for search criteria, construct a search query
	 * @param username
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @return
	 */
	protected static String buildAdvancedQuery(String username, String firstname, String lastname, String email) {	
		// Check that at least one input was provided
		if (username==null && firstname==null && lastname==null && email==null) {
			return null;
		}
		
		// Sanitize all inputs or create dummies for null inputs
		if (username == null) {
			username = "";
		} else {
			username = quizsite.FormatString.sanitizeString(username,quizsite.FormatString.NAMELEGALCHARS);
		}
		if (firstname == null) {
			firstname = "";
		} else {
			firstname = quizsite.FormatString.sanitizeString(firstname,quizsite.FormatString.NAMELEGALCHARS);
		}
		if (lastname == null) {
			lastname = "";
		} else {
			lastname = quizsite.FormatString.sanitizeString(lastname,quizsite.FormatString.NAMELEGALCHARS);
		}
		if (email == null) {
			email = "";
		} else {
			email = quizsite.FormatString.sanitizeString(email,quizsite.FormatString.EMAILLEGALCHARS);
		}
		
		// Check that at least one input was valid
		if (username.isEmpty() && firstname.isEmpty() && lastname.isEmpty() && email.isEmpty()) {
			return null;
		}
		
		// Build the query from the sanitized inputs
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM " + SEARCHTABLE + " WHERE ");
		boolean one = false; // Tracks whether OR's need to be added
		if(!username.isEmpty()) {
			one = true;
			sb.append(colUsername + " LIKE \"%" + username + "%\"");
		}

		if(!firstname.isEmpty()) {
			if(one) {
				sb.append(" OR ");
			}
			one = true;
			sb.append(colFirstName + " LIKE \"%" + firstname + "%\"");
		}
		if(!lastname.isEmpty()) {
			if(one) {
				sb.append(" OR ");
			}
			one = true;
			sb.append(colLastName + " LIKE \"%" + lastname + "%\"");
		}
		if(!email.isEmpty()) {
			if(one) {
				sb.append(" OR ");
			}
			one = true;
			sb.append(colEmail + " LIKE \"%" + email + "%\"");
		}
		sb.append(";");
		
		// Return the completed query
		return sb.toString();
	}
	
	
	/**
	 * Given the user's search terms, construct a MySQL query that tests for each term in each of the firstname, lastname, username, and email columns
	 * @param searchQuery
	 * @return
	 */
	protected static String buildBasicQuery(String searchQuery) {
		// Sanitize the user query
		String nameQuery = quizsite.FormatString.sanitizeString(searchQuery,quizsite.FormatString.NAMELEGALCHARS);
		String emailQuery = quizsite.FormatString.sanitizeString(searchQuery,quizsite.FormatString.EMAILLEGALCHARS);
		
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
//	protected static String sanitizeString(String searchQuery, String legalChars) {
//		// Eliminate illegal characters
//		String illegalChars = "[^" + legalChars + "]";
//		searchQuery = searchQuery.replaceAll(illegalChars,"");
//		return searchQuery;
//	}
	
	public static void main(String[] args) {
		String test = "Dan-iel D'uncan dan\\dun/can2010@\"gmail.com?!?!";
		System.out.println("Original: " + test);
		System.out.println("Basic MySQL Query:");
		System.out.println("\t" + buildBasicQuery(test));
		
		String user = "d\\u\"n_c^><><,.and";
		String first = ":::Dan-iel:::";
		String last = "D'uncan";
		String email = "dan\\duncan/2010!@#gma%^&il.com";
		System.out.println("Unformatted advanced query: " + user + " " + first + " " + last + " " + email);
		System.out.println("query(user,first,last,email): ");
		System.out.println("\t" + buildAdvancedQuery(user,first,last,email));
		System.out.println("query(null,first,last,email): ");
		System.out.println("\t" + buildAdvancedQuery(null,first,last,email));
		System.out.println("query(null,null,last,\"\"): ");
		System.out.println("\t" + buildAdvancedQuery(null,null,last,""));
		System.out.println("query(null,null,null,null): ");
		if(buildAdvancedQuery(null,null,null,null) == null ) {
			System.out.println("\tSuccess: Result was null");
		} else if (buildAdvancedQuery(null,null,null,null).isEmpty() ) {
			System.out.println("\tResult was empty string");
		} else {
			System.out.println("\tResult was: " + buildAdvancedQuery(null,null,null,null));
		}
		System.out.println("query(\"\",\"\",\"\",\"\"): ");
		if(buildAdvancedQuery("","","","") == null ) {
			System.out.println("\tSuccess: Result was null");
		} else if (buildAdvancedQuery("","","","").isEmpty() ) {
			System.out.println("\tResult was empty string");
		} else {
			System.out.println("\tResult was: " + buildAdvancedQuery("","","",""));
		}
		
		
		
	}

}
