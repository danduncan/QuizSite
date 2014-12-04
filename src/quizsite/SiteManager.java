package quizsite;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SiteManager {
	// Connection to database
	private DatabaseConnection dc = null;
	private String table = MyDBInfo.SITEMANAGERTABLE;
	
	// Column labels for database
	private static String colNextUserID = "nextuserid";
	private static String colNextQuizID = "nextquizid";
	private static String colNextQuestionID = "nextquestionid";
	private static String colNextMessageID = "nextmessageid";
	private static String colNextQuizTakenID = "nextquiztakenid";
	
	private static String colTotalUsers = "totalusers";
	private static String colTotalQuizzes = "totalquizzes";
	private static String colTotalQuestions = "totalquestions";
	//private static String colTotalQuizzesTaken = "totalquizzestaken";
	private static String colTotalQuizzesPracticed = "totalquizzespracticed";
	private static String colTotalFriendships = "totalfriendships";
	//private static String colTotalMessages = "totalmessages";
	private static String colTotalQuizCategories = "totalquizcategories";
	private static String colTotalQuestionCategories = "totalquestioncategories";
	private static String colTotalAchievements = "totalachievements";
	private static String colTotalUniqueVisitors = "totaluniquevisitors";
	
	// ID numbers for the new items
	private int nextUserID = 0;
	private int nextQuizID = 0;
	private int nextQuestionID = 0;
	private int nextMessageID = 0;
	private int nextQuizTakenID = 0;
	
	// Collection of site metrics
	private int totalUsers = 0;
	private int totalQuizzes = 0;
	//private int totalQuizzesTaken = 0;
	private int totalQuizzesPracticed = 0;
	private int totalQuestions = 0;
	private int totalFriendships = 0;
	//private int totalMessages = 0;
	private int totalQuizCategories = 0;
	private int totalQuestionCategories = 0;
	private int totalAchievements = 0;
	private int totalUniqueVisitors = 0;
	
	public SiteManager(DatabaseConnection dc) {
		this.dc = dc;
		readTable();
	}
	
	private void readTable() {
		// Get data from table
		String query = "select * from " + this.table + ";";
		ResultSet rs = dc.executeQuery(query);
		
		// Go to start of table. Return if table is empty
		try {
			if (!rs.first()) return; // 
		} catch (SQLException ignored) {return;}
		
		// Parse data from table
		nextUserID = setIVar(rs, colNextUserID);
		nextQuizID = setIVar(rs, colNextQuizID);
		nextQuestionID = setIVar(rs, colNextQuestionID);
		nextMessageID = setIVar(rs, colNextMessageID);
		nextQuizTakenID = setIVar(rs, colNextQuizTakenID);
		
		totalUsers = setIVar(rs, colTotalUsers);
		totalQuizzes = setIVar(rs, colTotalQuizzes);
		//totalQuizzesTaken = setIVar(rs, colTotalQuizzesTaken);
		totalQuizzesPracticed = setIVar(rs, colTotalQuizzesPracticed);
		totalQuestions = setIVar(rs, colTotalQuestions);
		totalFriendships = setIVar(rs, colTotalFriendships);
		//totalMessages = setIVar(rs, colTotalMessages);
		totalQuizCategories = setIVar(rs, colTotalQuizCategories);
		totalQuestionCategories = setIVar(rs, colTotalQuestionCategories);
		totalAchievements = setIVar(rs, colTotalAchievements);
		totalUniqueVisitors = setIVar(rs, colTotalUniqueVisitors);
	}
	
	
	private int readTableColumn(String colName) {
		String query = "SELECT " + colName + " from " + this.table + ";";
		ResultSet rs = dc.executeQuery(query);
		try {
			// First check if rs is null or empty
			if (rs == null || !rs.first()) return -1;
			
			// Next extract the value from the desired column
			return setIVar(rs,colName);
			
		} catch (SQLException ignored) {return -1;}		
	}
	
	// Read an entry from the table. Enforce that entries are always >= 0
	private int setIVar(ResultSet rs, String colName) {
		int ivar = -1;
		try {
			int iv = rs.getInt(colName);
			
			if (iv < 0) {
				iv = 0;
				updateTableColumn(colName,iv);
			}
			
			ivar = iv;
		} catch (SQLException e) {
			System.out.println("SiteManager readTable(): Invalid column name: " + colName);
		}
		return ivar;
	}
	
	// Update an entry in the table. Enforce that no entry is < 0.
	private void updateTableColumn(String colName, int ivar) {
		if (ivar < 0) ivar = 0;
		String update = "UPDATE " + table + " SET " + colName + " = " + ivar + ";";
		dc.executeUpdate(update);
	}
	
	// Get-and-increment an IVar (i.e. the x++ operation)
	private int IVarPlusPlus(String colName) {
		int iv = readTableColumn(colName);
		updateTableColumn(colName, iv + 1);
		return iv;
	}
	
	// Get-and-decrement an IVar (i.e. the x-- operation)
	private int IVarMinusMinus(String colName) {
		int iv = readTableColumn(colName);
		updateTableColumn(colName, iv - 1);
		return iv;
	}

	// Increment-and-get an IVar (i.e. the ++x operation)
	private int PlusPlusIVar(String colName) {
		int iv = readTableColumn(colName) + 1;
		updateTableColumn(colName, iv);
		return iv;
	}

	// Decrement-and-get an IVar (i.e. the --x operation)
	private int MinusMinusIVar(String colName) {
		int iv = readTableColumn(colName) - 1;
		updateTableColumn(colName, iv);
		return iv;
	}
		
	
	
	/**
	 * GETTING an ID for X automatically increments the nextXID and totalX fields
	 * PEEKING an ID for X returns the current value for nextXID without changing anything
	 * @return int nextID
	 */
	public int popNextUserID() {
		// Need to increment two columns
		this.totalUsers = PlusPlusIVar(colTotalUsers);
		int id = IVarPlusPlus(colNextUserID);
		this.nextUserID = id + 1;
		return id;
	}
	public int popNextQuizID() {
		// Need to increment two columns
		this.totalQuizzes = PlusPlusIVar(colTotalQuizzes);
		int id = IVarPlusPlus(colNextQuizID);
		this.nextQuizID = id + 1;
		return id;
	}
	public int popNextQuestionID() {
		// Need to increment two columns
		this.totalQuestions = PlusPlusIVar(colTotalQuestions);
		int id = IVarPlusPlus(colNextQuestionID);
		this.nextQuestionID = id + 1;
		return id;
	}
	public int popNextMessageID() {
		int id = IVarPlusPlus(colNextMessageID);
		this.nextMessageID = id + 1;
		return id;
	}
	public int popNextQuizTakenID() {
		int id = IVarPlusPlus(colNextQuizTakenID);
		this.nextQuizTakenID = id + 1;
		return id;
	}
	/*
	public int getNextUserID() {
		return readTableColumn(colNextUserID);
	}
	public int getNextQuizID() {
		return readTableColumn(colNextQuizID);
	}
	public int getNextQuestionID() {
		return readTableColumn(colNextQuestionID);
	}
	public int getNextMessageID() {
		return readTableColumn(colNextMessageID);
	}*/
	public int getNextQuizTakenID() {
		return readTableColumn(colNextQuizTakenID);
	}
	
	
	// Getters for other major ivars. These do not modify any stored values
	/*
	public int gettotalUsers() {
		return readTableColumn(colTotalUsers);
	}
	public int getTotalQuizzes() {
		return readTableColumn(colTotalQuizzes);
	}
	public int getTotalQuizzesTaken() {
		return readTableColumn(colNextQuizTakenID);
	}
	public int getTotalQuizzesPracticed() {
		return readTableColumn(colTotalQuizzesPracticed);
	}
	public int getTotalQuestions() {
		return readTableColumn(colTotalQuestions);
	}
	public int getTotalFriendships() {
		return readTableColumn(colTotalFriendships);
	}
	public int getTotalMessages() {
		return readTableColumn(nextMessageID);
	}
	public int getTotalQuizCategories() {
		return readTableColumn(colTotalQuizCategories);
	}
	public int getTotalQuestionCategories() {
		return readTableColumn(colTotalQuestionCategories);
	}
	public int getTotalAchievements() {
		return readTableColumn(colTotalAchievements);
	}
	public int getTotalUniqueVisitors() {
		return readTableColumn(colTotalUniqueVisitors);
	}
	*/
	
	// Functions to add or delete site objects
	// NOTE: These adders are obsolete. Use "popNext___()" to add an object 
	/*
	public void addUser() {
		nextUserID++;
		totalUsers++;
	}
	public void addQuiz() {
		nextQuizID++;
		totalQuizzes++;
	}
	public void addQuestion() {
		nextQuestionID++;
		//totalQuestions++;
	}
	public void addMessage() {
		nextMessageID++;
		//totalMessages++;
	}
	public void addQuizTaken() {
		//totalQuizzesTaken++;
	}
	*/
	public void deleteUser() {
		totalUsers = MinusMinusIVar(colTotalUsers);
	}
	public void deleteQuiz() {
		totalQuizzes = MinusMinusIVar(colTotalQuizzes);
	}
	public void deleteQuestion() {
		totalQuestions = MinusMinusIVar(colTotalQuestions);
	}
	public void addQuizPracticed() {
		this.totalQuizzesPracticed = PlusPlusIVar(colTotalQuizzesPracticed);
	}
	public void addFriendship() {
		totalFriendships = PlusPlusIVar(colTotalFriendships);
	}
	public void addQuizCategory() {
		totalQuizCategories = PlusPlusIVar(colTotalQuizCategories);
	}
	public void addQuestionCategory() {
		totalQuestionCategories = PlusPlusIVar(colTotalQuestionCategories);
	}
	public void addAchievement() {
		totalAchievements = PlusPlusIVar(colTotalAchievements);
	}
	public void addUniqueVisitor() {
		totalUniqueVisitors = PlusPlusIVar(colTotalUniqueVisitors);
	}
	
	// Write all data to server
	// Current implementation of SiteManager always stores the most recent update in the database, so
	// this update could potentially corrupt the database by saving old information
	public void updateDatabase() {
		return;
//		updateTableColumn(colNextUserID,nextUserID);
//		updateTableColumn(colNextQuizID,nextQuizID);
//		updateTableColumn(colNextQuestionID,nextQuestionID);
//		updateTableColumn(colNextMessageID,nextMessageID);
//		updateTableColumn(colNextQuizTakenID,nextQuizTakenID);
//		updateTableColumn(colTotalUsers,totalUsers);
//		updateTableColumn(colTotalQuizzes,totalQuizzes);
//		updateTableColumn(colTotalQuizzesPracticed,totalQuizzesPracticed);
//		updateTableColumn(colTotalFriendships,totalFriendships);
//		updateTableColumn(colTotalQuizCategories,totalQuizCategories);
//		updateTableColumn(colTotalQuestionCategories,totalQuestionCategories);
//		updateTableColumn(colTotalAchievements,totalAchievements);
//		updateTableColumn(colTotalUniqueVisitors,totalUniqueVisitors);

		//updateTableColumn(colTotalQuestions,totalQuestions);
		//updateTableColumn(colTotalQuizzesTaken,totalQuizzesTaken);
		//updateTableColumn(colTotalMessages,totalMessages);
	}
		
	@Override
	public String toString() {
		String ls = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		sb.append("SiteManager:" + ls);
		sb.append("\tnextUserID: " + nextUserID + ls);
		sb.append("\tnextQuizID: " + nextQuizID + ls);
		sb.append("\tnextQuestionID: " + nextQuestionID + ls);
		sb.append("\tnextMessageID: " + nextMessageID + ls);
		sb.append("\tnextQuizTakenID " + nextQuizTakenID + ls);
		sb.append("\ttotalUsers: " + totalUsers + ls);
		sb.append("\ttotalQuizzes: " + totalQuizzes + ls);
		//sb.append("\ttotalQuizzesTaken: " + totalQuizzesTaken + ls);
		sb.append("\ttotalQuizzesPracticed: " + totalQuizzesPracticed + ls);
		sb.append("\ttotalQuestions: " + totalQuestions + ls);
		sb.append("\ttotalFriendships: " + totalFriendships + ls);
		//sb.append("\ttotalMessages: " + totalMessages + ls);
		sb.append("\ttotalQuizCategories: " + totalQuizCategories + ls);
		sb.append("\totalQuestionCategories: " + totalQuestionCategories + ls);
		sb.append("\ttotalAchievements: " + totalAchievements + ls);
		sb.append("\ttotalUniqueVisitors: " + totalUniqueVisitors + ls);
		
		return sb.toString();
	}
	
	// For testing only
	public static void main(String[] args) {
		DatabaseConnection dc = new DatabaseConnection();
		SiteManager sm = new SiteManager(dc);
		
		// Test that sm matches the database after many updates		
		System.out.println("Quiz ID: " + sm.popNextQuizID());
		System.out.println("Question ID 1: " + sm.popNextQuestionID());
		System.out.println("Question ID 2: " + sm.popNextQuestionID());
		System.out.println("Quiz Taken ID: " + sm.popNextQuizTakenID());
		System.out.println("User ID 1: " +  sm.popNextUserID());
		System.out.println("Message ID: " + sm.popNextMessageID());
		System.out.println("User ID 2: " +  sm.popNextUserID());
		System.out.println("Total users: " + sm.totalUsers + "; Now, delete two users...");
		sm.deleteUser();
		sm.deleteUser();
		System.out.println("Total users: " + sm.totalUsers);
		System.out.println("User ID 3: " +  sm.popNextUserID());
		sm.deleteQuiz(); sm.deleteQuestion(); sm.addQuizPracticed();
		sm.addFriendship(); sm.addFriendship(); sm.addFriendship(); 
		sm.addQuizCategory(); sm.addQuestionCategory();
		sm.addAchievement(); sm.addUniqueVisitor();
		sm.deleteQuiz(); sm.deleteQuestion(); sm.addQuizPracticed();
		sm.addFriendship(); sm.addFriendship(); sm.addFriendship(); 
		sm.addQuizCategory(); sm.addQuestionCategory();
		sm.addAchievement(); sm.addUniqueVisitor();
		
		System.out.println(sm);
		sm.readTable();
		System.out.println(sm);
	}
}
