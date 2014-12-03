package quizsite;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;

import javax.servlet.jsp.JspWriter;

import quizsite.*;

public class ScoreManager {
	// Connection to database
	private DatabaseConnection dc = null;
	private SiteManager sm = null;
	private static final String table = MyDBInfo.QUIZZESTAKENTABLE;
	private static final int NUMHIGHSCORES = 5; // Number of high scores to track for a given quiz
	private static final int NUMRECENTSCORES = 5; // Number of recent scores to track for a given quiz
	private static final int NUMUSERSCORES = 5; //Number of scores to track for a user
	
	public ScoreManager(DatabaseConnection dc, SiteManager sm) {
		// Database Connection
		this.dc = dc;
		this.sm = sm;
	}
	
	// Get all scores associated with a particular quiz
	public ResultSet getAllScores(int quizid) {
		String query = "SELECT * FROM " + table + " WHERE quizid = " + quizid + " ORDER BY score DESC, time ASC;";
		ResultSet rs = dc.executeQuery(query);
		return rs;
	}
	
	// Get only the top scores for a particular quiz
	public ResultSet getHighScores(int quizid) {
		String query = "SELECT * FROM " + table + " WHERE quizid = " + quizid + " ORDER BY score DESC, time ASC LIMIT " + this.NUMHIGHSCORES + ";";
		ResultSet rs = dc.executeQuery(query);
		return rs;
	}
	
	// Get only the top scores for a particular quiz today
	public ResultSet getHighScoresToday(int quizid) {
		String date = FormatDateTime.getSystemDate(new Date());
		String query = "SELECT * FROM " + table + " WHERE quizid = " + quizid + " AND datetaken LIKE \"" +date + "%\" ORDER BY score DESC, time ASC LIMIT " + this.NUMHIGHSCORES + ";";
		ResultSet rs = dc.executeQuery(query);
		return rs;
	}
	
	// Get only the lowest scores for a particular quiz
	public ResultSet getLowScores(int quizid) {
		String query = "SELECT * FROM " + table + " WHERE quizid = " + quizid + " ORDER BY score ASC, time DESC LIMIT " + this.NUMHIGHSCORES + ";";
		ResultSet rs = dc.executeQuery(query);
		return rs;
	}
	
	// Get only the most recent scores for a particular quiz
	public ResultSet getRecentScores(int quizid) {
		String query = "SELECT * FROM " + table + " WHERE quizid = " + quizid + " ORDER BY datetaken DESC LIMIT " + this.NUMRECENTSCORES + ";";
		ResultSet rs = dc.executeQuery(query);
		return rs;
	}
	
	// Get only the high scores for the user in question
	public ResultSet getUserScores(int quizid, int userid) {
		String query = "SELECT * FROM " + table + " WHERE quizid = " + quizid + " AND userid = " + userid + " ORDER BY score DESC, time ASC LIMIT " + this.NUMUSERSCORES + ";";
		ResultSet rs = dc.executeQuery(query);
		return rs;
	}
	
	//returns high score, low score, average score in an array
	public double[] getSummaryStats(int quizid) throws SQLException{
		ResultSet rs = getAllScores(quizid);
		int total = 0;
		int highScore = -1;
		int lowScore = -1;
		int counter = 0;
		while(rs.next()){
			counter++;
			int score = rs.getInt("score");
			total += score;
			if(lowScore == -1 || score<lowScore) lowScore = score;
			if(highScore == -1 || score>highScore) highScore = score;
		}
		double average = -1;
		if(counter>0) average = ((double) total)/counter;
		return new double[]{average, (double)lowScore, (double)highScore};
	}
	
	/**
	 * This method is called within JSPs to print scores  of a quiz in a table with the 
	 * following columns: UserID, Score, Time, DateTaken. Possible points is the possible 
	 * points of the quiz in question.
	 */
	public static void printScoresToJSP(JspWriter out, ResultSet rs, int possiblePoints) throws IOException, SQLException{
		if(rs == null){
			out.println("Oops! Something went wrong! <br>");
			return;
		}
		rs.last();
		int numScores = rs.getRow();
		rs.beforeFirst();
		if(numScores>0){
			DecimalFormat df = new DecimalFormat("#.00");
			String[] columnNames = new String[]{"User ID", "Score", "Time", "Date Taken"};
			String[][] table = new String[numScores][columnNames.length];
			int rowInd = 0;
			while(rs.next()){
				try{
					int id = rs.getInt("userid");
					table[rowInd][0] = "<a href=\"/QuizSite/user?userid="+id+"\">"+ id+"</a>";
					int score = rs.getInt("score");
					double percent = 100*((double)score/(double)possiblePoints);
					table[rowInd][1] = df.format(percent) + "%";
					table[rowInd][2] = rs.getInt("time") + "s";
					table[rowInd][3] = FormatDateTime.getUserDateTime(rs.getString("datetaken"))[0];
					rowInd++;
				} catch (SQLException ignored){}
			}
			out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(table, columnNames));
		}else{
			out.println("These statistics have not yet been populated! <br>");
		}
	}
	
	
	/**
	 * Add a new quiz taken. Returns rank if the user got a high score (i.e. 1 for first place, 10 for 10th). Returns 0 if the user did not.
	 * @param score // Score object stores all necessary fields except the next quizTaken ID
	 * @return
	 */
	public int addScore(Quiz.Score score) {
		// Get an ID for the new score
		int id = this.sm.popNextQuizTakenID();
		
		// Create update statement for databse
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO " + this.table + " ");
		sb.append("(id, quizid, userid, datetaken, score, time) ");
		sb.append("VALUES "); //(1, 2, 3, "20141225", 100, 5);");
		sb.append("(" + id + ", " + score.quizid + ", " + score.userid + ", " + score.date + ", " + score.score + ", " + score.time + ");");
		
		// Insert row into table
		dc.executeUpdate(sb.toString());
		
		// Check to see if the user got a high score
		ResultSet rs = this.getHighScores(score.quizid);
		try {
			// Make sure rs isn't empty (this could only happen if there is an error)
			if(!rs.first()) return 0;
			
			// Iterate over rs
			for (int i = 1; i < this.NUMHIGHSCORES; i++) {
				int curid = rs.getInt("id");
				if (curid == id) return i;
				if (rs.isLast()) break;
			}
		} catch (SQLException ignored) {};
		
		return 0;
	}
	
	
}
