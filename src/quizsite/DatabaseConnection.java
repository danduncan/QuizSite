package quizsite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import quizsite.MyDBInfo;

public class DatabaseConnection {
	// Static constants 
	private static final String account = MyDBInfo.MYSQL_USERNAME;
	private static final String password = MyDBInfo.MYSQL_PASSWORD;
	private static final String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	private static final String database = MyDBInfo.MYSQL_DATABASE_NAME;
	
	// ivars 
	private Connection con; 
	private Statement stmt;
	
	// SQL Exception error codes
	private static final Integer SQL_NoDatabaseSelected = 1046;
	private static final Integer SQL_InvalidTable = 1146;
	private static final Integer SQL_InvalidColumn = 1054;
	private static final Integer SQL_GeneralSyntaxError = 1064; // Many other error codes for specific syntax errors
	private static final Integer SQL_ConnectionFailed = null; // Don't know this number yet
	
	
	public DatabaseConnection() {
		this.openConnection();
	}
	
	
	/**
	 * Connect to a MySQL database using the details specified in the MyDBInfo class
	 */
	private void openConnection() {
		try {
			// Open connection to database
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection( "jdbc:mysql://" + server, account ,password);
			stmt = con.createStatement();
			stmt.executeQuery("USE " + database);
						
		} catch (SQLException e) {
			System.out.println("openConnection(): SQLException encountered; error code=" + e.getErrorCode());
			e.printStackTrace();
			//System.exit(0);
			return;
		} catch (ClassNotFoundException e) {
			System.out.println("openConnection(): ClassNotFoundException encountered");
			e.printStackTrace();
			//System.exit(0);
			return;
		} catch (Exception e) {
			System.out.println("openConnection(): General Exception encountered");
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Close the connection to the database
	 */
	public void closeConnection() {
		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Restart a database connection
	 */
	public void restartConnection() {
		closeConnection();
		openConnection();
	}
	
	/**
	 * Display a SQL error message
	 */
	private static void printException(SQLException e, String query) {
		String err = "\tDatabaseConnection: SQLException (error code " + e.getErrorCode() + "): \"" + e.getMessage() + "\";";
		System.out.println(err);
		if (query != null && !query.isEmpty()) {
			System.out.println("\t\tQuery: \"" + query +"\";");
		}
	}

	// Query database. Returns a ResultSet on success, null on failure
	/**
	 * Queries a MySQL database and stores the results in the MetroTable 
	 * @param query is a properly formatted MySQL query
	 * @return a pointer to the current MetroTable
	 */
	public ResultSet executeQuery(String query) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			printException(e, query);
			return rs;
		}

		// Result data received. Now process rs into a table and store in the table ivar
		return rs;
	}
	
	// Execute a table update. Return true if successful.
	/**
	 * Adds an entry to a MySQL database specified by the input String
	 * @param update is a properly-formatted MySQL INSERT command
	 * @return a pointer to this MetroTable
	 */
	public boolean executeUpdate(String update) {
		try {
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			printException(e, update);
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	// Execute simultaneous queries
	/**
	 * This is slower than executeQuery, but it is necessary if you ever want to have two or more ResultSets simultaneously
	 */
	public ResultSet executeSimultaneousQuery(String query) {
		ResultSet rs = null;
		Statement stmtNew = null;
		
		try {
			// Open connection to database
			//Connection conNew = DriverManager.getConnection( "jdbc:mysql://" + server, account ,password);
			stmtNew = con.createStatement();
			stmtNew.executeQuery("USE " + database);						
		} catch (SQLException e) {
			System.out.println("DatabaseConnection.executeSimultaneousQuery() SQLException: Too many active connections. Don't forget to call rs.close() as soon as you are done with a simultaneous ResultSet");
			return null;
		} catch (Exception e) {
			System.out.println("DatabaseConnection.executeSimultaenousQuery(): General Exception encountered");
			return null;
		}
		if(stmtNew == null) return null;
		
		try {
			rs = stmtNew.executeQuery(query);
		} catch (SQLException e) {
			printException(e, query);
			return rs;
		}

		// Result data received. Now process rs into a table and store in the table ivar
		return rs;
	}

}
