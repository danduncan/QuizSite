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

	public SearchForQuizzes() {
	}
	
	/**
	 * Given a user string, find all users whose first name, last name, or username match one or more of the words in the query
	 * Query should read: SELECT * FROM users WHERE username="query1" OR 
	 */
	public static ResultSet basicSearch(quizsite.DatabaseConnection dc, String searchQuery) {
		String query = buildBasicQuery(searchQuery);
		System.out.println("\tSearchForQuizzes.basicSearch(): Input query = \"" + searchQuery + "\";");
		System.out.println("\tSearchForQuizzes.basicSearch(): Database query = \"" + query + "\";");
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
		
		
		// Construct query
		String colNames = quizTable + ".id AS quizid," + quizTable + ".authorid," + userTable + ".username," + quizTable + ".datemade," +
						  quizTable + ".name," + quizTable + ".description," + quizTable + ".practicemode," + quizTable + ".numquestions," +
						  quizTable + ".numtaken";
		String tableName = "(" + userTable + " INNER JOIN " + quizTable + " ON " + userTable + ".id=" + quizTable + ".authorid)";
		String baseQuery = "SELECT " + colNames + " FROM " + tableName;

		// Separate the query into one-word substrings
		String[] querySplit = searchQuery.split("\\s+");

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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String query = "test";
		quizsite.DatabaseConnection dc = new quizsite.DatabaseConnection();
		ResultSet rs = basicSearch(dc,query);
		if (rs != null) System.out.println("Search yielded results!");
	}

}
