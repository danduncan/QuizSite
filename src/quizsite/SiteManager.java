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
	private static String colTotalUsers = "totalusers";
	private static String colTotalQuizzes = "totalquizzes";
	private static String colTotalQuestions = "totalquestions";
	private static String colTotalQuizzesTaken = "totalquizzestaken";
	private static String colTotalQuizzesPracticed = "totalquizzespracticed";
	private static String colTotalFriendships = "totalfriendships";
	private static String colTotalMessages = "totalmessages";
	private static String colTotalQuizCategories = "totalquizcategories";
	private static String colTotalQuestionCategories = "totalquestioncategories";
	private static String colTotalAchievements = "totalachievements";
	private static String colTotalUniqueVisitors = "totaluniquevisitors";
	
	// ID numbers for the new items
	private int nextUserID = 0;
	private int nextQuizID = 0;
	private int nextQuestionID = 0;
	private int nextMessageID = 0;
	
	// Collection of site metrics
	private int totalUsers = 0;
	private int totalQuizzes = 0;
	private int totalQuizzesTaken = 0;
	private int totalQuizzesPracticed = 0;
	private int totalQuestions = 0;
	private int totalFriendships = 0;
	private int totalMessages = 0;
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
		totalUsers = setIVar(rs, colTotalUsers);
		totalQuizzes = setIVar(rs, colTotalQuizzes);
		totalQuizzesTaken = setIVar(rs, colTotalQuizzesTaken);
		totalQuizzesPracticed = setIVar(rs, colTotalQuizzesPracticed);
		totalQuestions = setIVar(rs, colTotalQuestions);
		totalFriendships = setIVar(rs, colTotalFriendships);
		totalMessages = setIVar(rs, colTotalMessages);
		totalQuizCategories = setIVar(rs, colTotalQuizCategories);
		totalQuestionCategories = setIVar(rs, colTotalQuestionCategories);
		totalAchievements = setIVar(rs, colTotalAchievements);
		totalUniqueVisitors = setIVar(rs, colTotalUniqueVisitors);
	}
	
	private int setIVar(ResultSet rs, String colName) {
		int ivar = 0;
		try {
			ivar = rs.getInt(colName);
		} catch (SQLException e) {
			System.out.println("SiteManager readTable(): Invalid column name: " + colName);
		}
		return ivar;
	}
	
	/**
	 * GETTING an ID for X automatically increments the nextXID and totalX fields
	 * PEEKING an ID for X returns the current value for nextXID without changing anything
	 * @return int nextID
	 */
	public int popNextUserID() {
		this.totalUsers++;
		return this.nextUserID++;
	}
	public int popNextQuizID() {
		this.totalQuizzes++;
		return this.nextQuizID++;
	}
	public int popNextQuestionID() {
		this.totalQuestions++;
		return this.nextQuestionID++;
	}
	public int popNextMessageID() {
		this.totalMessages++;
		return this.nextMessageID++;
	}
	public int popNextQuizTakenID() {
		return ++this.totalQuizzesTaken;
	}
	public int getNextUserID() {
		return this.nextUserID;
	}
	public int getNextQuizID() {
		return this.nextQuizID;
	}
	public int getNextQuestionID() {
		return this.nextQuestionID;
	}
	public int getNextMessageID() {
		return this.nextMessageID;
	}
	
	
	
	// Getters for other major ivars. These do not modify any stored values
	public int gettotalUsers() {
		return totalUsers;
	}
	public int getTotalQuizzes() {
		return totalQuizzes;
	}
	public int getTotalQuizzesTaken() {
		return totalQuizzesTaken;
	}
	public int getTotalQuizzesPracticed() {
		return totalQuizzesPracticed;
	}
	public int getTotalQuestions() {
		return totalQuestions;
	}
	public int getTotalFriendships() {
		return totalFriendships;
	}
	public int getTotalMessages() {
		return totalMessages;
	}
	public int getTotalQuizCategories() {
		return totalQuizCategories;
	}
	public int getTotalQuestionCategories() {
		return totalQuestionCategories;
	}
	public int getTotalAchievements() {
		return totalAchievements;
	}
	public int getTotalUniqueVisitors() {
		return totalUniqueVisitors;
	}
	
	// Functions to increment or decrement ivars
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
		totalQuestions++;
	}
	public void addMessage() {
		nextMessageID++;
		totalMessages++;
	}
	public void deleteUser() {
		totalUsers--;
	}
	public void deleteQuiz() {
		totalQuizzes--;
	}
	public void deleteQuestion() {
		totalQuestions--;
	}
	public void addQuizTaken() {
		totalQuizzesTaken++;
	}
	public void addQuizPracticed() {
		totalQuizzesPracticed++;
	}
	public void addFriendship() {
		totalFriendships++;
	}
	public void addQuizCategory() {
		totalQuizCategories++;
	}
	public void addQuestionCategory() {
		totalQuestionCategories++;
	}
	public void addAchievement() {
		totalAchievements++;
	}
	public void addUniqueVisitor() {
		totalUniqueVisitors++;
	}
	
	// Write all data to server
	public void updateDatabase() {
		updateIVar(colNextUserID,nextUserID);
		updateIVar(colNextQuizID,nextQuizID);
		updateIVar(colNextQuestionID,nextQuestionID);
		updateIVar(colNextMessageID,nextMessageID);
		updateIVar(colTotalUsers,totalUsers);
		updateIVar(colTotalQuizzes,totalQuizzes);
		updateIVar(colTotalQuestions,totalQuestions);
		updateIVar(colTotalQuizzesTaken,totalQuizzesTaken);
		updateIVar(colTotalQuizzesPracticed,totalQuizzesPracticed);
		updateIVar(colTotalFriendships,totalFriendships);
		updateIVar(colTotalMessages,totalMessages);
		updateIVar(colTotalQuizCategories,totalQuizCategories);
		updateIVar(colTotalQuestionCategories,totalQuestionCategories);
		updateIVar(colTotalAchievements,totalAchievements);
		updateIVar(colTotalUniqueVisitors,totalUniqueVisitors);
	}
	
	
	private void updateIVar(String colName, int ivar) {
		String update = "UPDATE " + table + " SET " + colName + " = " + ivar + ";";
		dc.executeUpdate(update);
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
		sb.append("\ttotalUsers: " + totalUsers + ls);
		sb.append("\ttotalQuizzes: " + totalQuizzes + ls);
		sb.append("\ttotalQuizzesTaken: " + totalQuizzesTaken + ls);
		sb.append("\ttotalQuizzesPracticed: " + totalQuizzesPracticed + ls);
		sb.append("\ttotalQuestions: " + totalQuestions + ls);
		sb.append("\ttotalFriendships: " + totalFriendships + ls);
		sb.append("\ttotalMessages: " + totalMessages + ls);
		sb.append("\ttotalQuizCategories: " + totalQuizCategories + ls);
		sb.append("\ttotalAchievements: " + totalAchievements + ls);
		sb.append("\ttotalUniqueVisitors: " + totalUniqueVisitors + ls);
		
		return sb.toString();
	}
}
