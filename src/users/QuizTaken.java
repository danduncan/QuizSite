package users;

public class QuizTaken {
	public Integer id;
	public Integer quizid;
	public Integer userid;
	public String datetaken;
	public Integer score;
	public Integer time;
	
	public void QuizTaken(Integer ID, Integer QuizID, Integer UserID, Integer Score, String DateTaken, Integer Time){
		id = ID;
		quizid = QuizID;
		userid = UserID;
		score = Score;
		datetaken = DateTaken;
		time = Time;		
	}
}
