package Quiz;

public class Score {
	public int userid = 0;
	public int quizid = 0;
	public String date = null;
	public int score = 0;
	public int time = 0; // Completion time, in seconds
	
	public Score(int userid, int quizid, int score, int timeInSeconds, String date) {
		this.userid = userid;
		this.quizid = quizid;
		this.score = score;
		this.time = timeInSeconds;
		this.date = date;
		
	}
	
}
