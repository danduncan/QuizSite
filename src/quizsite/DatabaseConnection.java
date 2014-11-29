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
			System.out.println("openConnection(): SQLException encountered");
			e.printStackTrace();
			System.exit(0);
			return;
		} catch (ClassNotFoundException e) {
			System.out.println("openConnection(): ClassNotFoundException encountered");
			System.exit(0);
			e.printStackTrace();
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
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
			System.out.println("Exception: Invalid query: \"" + query + "\"");
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
			System.out.println("Exception: Invalid update: \"" + update + "\"");
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
			Connection conNew = DriverManager.getConnection( "jdbc:mysql://" + server, account ,password);
			stmtNew = conNew.createStatement();
			stmtNew.executeQuery("USE " + database);						
		} catch (SQLException e) {
			System.out.println("DatabaseConnection.executeSimultaenousQuery(): SQLException encountered");
			e.printStackTrace();
			System.exit(0);
			return null;
		} catch (Exception e) {
			System.out.println("DatabaseConnection.executeSimultaenousQuery(): General Exception encountered");
			e.printStackTrace();
			return null;
		}
		if(stmtNew == null) return null;
		
		try {
			rs = stmtNew.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("Exception: Invalid query: \"" + query + "\"");
			return rs;
		}

		// Result data received. Now process rs into a table and store in the table ivar
		return rs;
	}

}
