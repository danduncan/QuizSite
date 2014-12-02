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
	private Connection conSim; // Used for simultaneous queries
	
	// SQL Exception error codes
	private static final Integer SQL_NoDatabaseSelected = 1046;
	private static final Integer SQL_InvalidTable = 1146;
	private static final Integer SQL_InvalidColumn = 1054;
	private static final Integer SQL_GeneralSyntaxError = 1064; // Many other error codes for specific syntax errors
	private static final Integer SQL_ConnectionFailed = 0; // Don't know this number yet
	
	
	public DatabaseConnection() {
		this.openConnection();
		this.openSimConnection();
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
			printException(e,null);
			//System.exit(0);
			return;
		} catch (ClassNotFoundException e) {
			System.out.println("DataBaseConnect.openConnection(): ClassNotFound Exception encountered");
			//System.exit(0);
			return;
		} catch (Exception e) {
			System.out.println("DataBaseConnect.openConnection(): General Exception encountered");
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
			printException(e,null);
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
	 * Simultaenous connections
	 */
	public void openSimConnection() {
		try {
			// Open connection to database
			Class.forName("com.mysql.jdbc.Driver");
			conSim = DriverManager.getConnection( "jdbc:mysql://" + server, account ,password);
//			Statement tmp = con.createStatement();
//			tmp.executeQuery("USE " + database);
						
		} catch (SQLException e) {
			printException(e,null);
			//System.exit(0);
			return;
		} catch (ClassNotFoundException e) {
			System.out.println("DataBaseConnect.openSimConnection(): ClassNotFound Exception encountered");
			//System.exit(0);
			return;
		} catch (Exception e) {
			System.out.println("DataBaseConnect.openSimConnection(): General Exception encountered");
			return;
		}
	}
	public void closeSimConnection() {
		try {
			conSim.close();
		} catch (SQLException e) {
			printException(e,null);
		}
	}
	public void restartSimConnection() {
		closeSimConnection();
		openSimConnection();
	}
	
	/**
	 * Display a SQL error message
	 */
	private static void printException(SQLException e, String query) {
		e.printStackTrace();
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
			// Check if error was due to communication failure
			if (e.getErrorCode() == SQL_ConnectionFailed) {
				System.out.println("\tDatabaseConnection: Connection failed. Restarting and trying again...");
				restartConnection();
				try {
					rs = stmt.executeQuery(query);
				} catch (SQLException e2) {
					printException(e2, query);
					return rs;
				}
			} else {
				printException(e, query);
				return rs;
			}
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
			// Check if error was due to communication failure
			if (e.getErrorCode() == SQL_ConnectionFailed) {
				System.out.println("\tDatabaseConnection.update(): Connection failed. Restarting and trying again...");
				restartConnection();
				try {
					stmt.executeUpdate(update);
				} catch (SQLException e2) {
					printException(e2, update);
					return false;
				}
			} else {
				printException(e, update);
				return false;
			}
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
			stmtNew = conSim.createStatement();
			stmtNew.executeQuery("USE " + database);						
		} catch (SQLException e) {
			System.out.println("DatabaseConnection.executeSimultaneousQuery() SQLException: Too many active connections. Don't forget to call rs.close() as soon as you are done with a simultaneous ResultSet");
			printException(e,null);
			return null;
		} catch (Exception e) {
			System.out.println("DatabaseConnection.executeSimultaenousQuery(): General Exception encountered");
			return null;
		}
		if(stmtNew == null) return null;
		
		try {
			rs = stmtNew.executeQuery(query);
		} catch (SQLException e) {
			// Check if error was due to communication failure
			if (e.getErrorCode() == SQL_ConnectionFailed) {
				System.out.println("\tDatabaseConnection: Simultaneous Connection failed. Restarting and trying again...");
				restartSimConnection();
				try {
					stmtNew = conSim.createStatement();
					stmtNew.executeQuery("USE " + database);
					rs = stmtNew.executeQuery(query);
				} catch (SQLException e2) {
					printException(e2, query);
					return rs;
				}
			} else {
				printException(e, query);
				return rs;
			}
		}

		// Result data received. Now process rs into a table and store in the table ivar
		return rs;
	}

}
