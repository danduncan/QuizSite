package Quiz;
import java.sql.ResultSet;

public class SearchForQuizzes {
	private static final String colQuizName = "name";
	private static final String colQuizDescription = "description";
	private static final String colUsername = "username";
	private static final String colFirstName = "firstname";
	private static final String colLastName = "lastname";
	private static final String colEmail = "email";
	public static final String allLegalChars = "a-zA-Z0-9!@#$%^&*()?,.'<> -";
	public static final String nameLegalChars = "a-zA-Z' -";
	public static final String emailLegalChars = "a-zA-Z0-9@. -";
	private static final String userTable = quizsite.MyDBInfo.USERTABLE;
	private static final String quizTable = quizsite.MyDBInfo.QUIZZESTABLE;
	private static final String colNames = quizTable + ".id AS quizid," + quizTable + ".authorid," + userTable + ".username," + quizTable + ".datemade," +
			  quizTable + ".name," + quizTable + ".description," + quizTable + ".practicemode," + quizTable + ".numquestions," +
			  quizTable + ".numtaken";
	private static final String tableName = "(" + userTable + " INNER JOIN " + quizTable + " ON " + userTable + ".id=" + quizTable + ".authorid)";
	private static final String baseQuery = "SELECT " + colNames + " FROM " + tableName;

	public SearchForQuizzes() {
	}
	
	/**
	 * Given a user string, find all users whose first name, last name, or username match one or more of the words in the query
	 * Query should read: SELECT * FROM users WHERE username="query1" OR 
	 */
	public static ResultSet basicSearch(quizsite.DatabaseConnection dc, String searchQuery) {
		String query = buildBasicQuery(searchQuery);
		//System.out.println("\tSearchForQuizzes.basicSearch(): Input query = \"" + searchQuery + "\";");
		//System.out.println("\tSearchForQuizzes.basicSearch(): Database query = \"" + query + "\";");
		return dc.executeQuery(query);
	}
	
	/**
	 * Given the user's search terms, construct a MySQL query that tests for each term in each of the firstname, lastname, username, and email columns
	 * @param searchQuery
	 * @return
	 */
	protected static String buildBasicQuery(String searchQuery) {
		
		
		// Sanitize the user query
		searchQuery = quizsite.FormatString.sanitizeString(searchQuery,quizsite.FormatString.ALLLEGALCHARS);

		// Separate the query into one-word substrings
		String[] querySplit = searchQuery.split("\\s+");

		// Construct query
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < querySplit.length; i++) {
			if(querySplit[i] == null || querySplit[i].isEmpty()) continue;
			if (sb.length() > 0) {
				sb.append(" OR ");
			}
			sb.append(colQuizName + " LIKE \"%" + querySplit[i] + "%\" OR ");
			sb.append(colQuizDescription + " LIKE \"%" + querySplit[i] + "%\" OR ");
			sb.append(colUsername + " LIKE \"%" + querySplit[i] + "%\"");
		}
		String criteria = sb.toString();
		if(criteria.isEmpty()) return "";
		
		return baseQuery + " WHERE " + sb.toString() + " ORDER BY numtaken DESC;";
	}
	
	/**
	 * Given strings for quizName, username, and/or quizDescription, find all matches
	 * Note: null's or empty strings may be provided for any field you do not wish to search
	 * If andTerms == false, search for term1 OR term2 OR term3 ...
	 * If andTerms == true, search for term1 AND term2 AND term3 ...
	 */
	public static ResultSet advancedSearch(quizsite.DatabaseConnection dc, String quizName, String username, String quizDescription, boolean andTerms) {
		if(dc==null) return null;
		String query = buildAdvancedQuery(quizName,username,quizDescription,andTerms);
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
	protected static String buildAdvancedQuery(String quizName, String username, String quizDescription, boolean andTerms) {	
		// Check that at least one input was provided
		if (quizName==null && username==null && quizDescription==null) {
			return null;
		}
		
		// Sanitize all inputs or create dummies for null inputs
		if (username == null || username.isEmpty()) {
			username = "";
		} else {
			username = quizsite.FormatString.sanitizeString(username,quizsite.FormatString.NAMELEGALCHARS);
		}
		if (quizName == null || quizName.isEmpty()) {
			quizName = "";
		} else {
			quizName = quizsite.FormatString.sanitizeString(quizName,quizsite.FormatString.ALLLEGALCHARS);
		}
		if (quizDescription == null || quizDescription.isEmpty()) {
			quizDescription = "";
		} else {
			quizDescription = quizsite.FormatString.sanitizeString(quizDescription,quizsite.FormatString.ALLLEGALCHARS);
		}
		
		// Check that at least one input was valid
		if (username.isEmpty() && quizName.isEmpty() && quizDescription.isEmpty()) {
			return null;
		}
		
		String strBw = "";
		// Check whether we are ANDing or ORing
		if (andTerms) {
			strBw = "AND";
		}
		else {
			strBw = "OR";
		}
		strBw = " " + strBw + " ";
		
		// Build the query from the sanitized inputs
		StringBuilder sb = new StringBuilder();
		sb.append(baseQuery + " WHERE ");
		boolean one = false; // Tracks whether OR's need to be added
		if(!quizName.isEmpty()) {
			one = true;
			sb.append(colQuizName + " LIKE \"%" + quizName.trim() + "%\"");
		}
		if(!username.isEmpty()) {
			if (one) {
				sb.append(strBw);
			}
			one = true;
			sb.append(colUsername + " LIKE \"%" + username.trim() + "%\"");
		}

		if(!quizDescription.isEmpty()) {
			if(one) {
				sb.append(strBw);
			}
			one = true;
			sb.append(colQuizDescription + " LIKE \"%" + quizDescription.trim() + "%\"");
		}
		sb.append(" ORDER BY numtaken DESC;");
		
		// Return the completed query
		return sb.toString();
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String query = "test";
		quizsite.DatabaseConnection dc = new quizsite.DatabaseConnection();
		ResultSet rs = basicSearch(dc,query);
		if (rs != null) System.out.println("Search yielded results!");
	}

}
