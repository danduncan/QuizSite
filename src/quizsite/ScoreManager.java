package quizsite;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreManager {
	// Connection to database
	private DatabaseConnection dc = null;
	private SiteManager sm = null;
	private static final String table = MyDBInfo.QUIZZESTAKENTABLE;
	private static final int NUMHIGHSCORES = 10; // Number of high scores to track for a given quiz
	
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
		return dc.executeQuery(query);
	}
	
	// Get only the lowest scores for a particular quiz
	public ResultSet getLowScores(int quizid) {
		String query = "SELECT * FROM " + table + " WHERE quizid = " + quizid + " ORDER BY score ASC, time DESC LIMIT " + this.NUMHIGHSCORES + ";";
		return dc.executeQuery(query);
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
