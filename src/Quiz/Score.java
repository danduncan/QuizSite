package Quiz;

public class Score {
	public int userid = 0;
	public int quizid = 0;
	public String date = null;
	public int score = 0;
	public int timeInSeconds = 0;
	
	public Score(int userid, int quizid, int score, int timeInSeconds, String date) {
		this.userid = userid;
		this.quizid = quizid;
		this.score = score;
		this.timeInSeconds = timeInSeconds;
		this.date = date;
		
	}
}
