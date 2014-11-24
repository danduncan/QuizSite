package users;

import quizsite.FormatDateTime;

public class QuizTaken {
	public Integer id;
	public Integer quizid;
	public Integer userid;
	public String datetaken;
	public Integer score;
	public double time;
	
	public QuizTaken(Integer ID, Integer QuizID, Integer UserID, String DateTaken,Integer Score,  double Time){
		id = ID;
		quizid = QuizID;
		userid = UserID;
		score = Score;
		datetaken = DateTaken;
		time = Time;		
	}
	
	public QuizTaken(Integer ID, Integer QuizID, Integer UserID, Integer Score,  double Time){
		id = ID;
		quizid = QuizID;
		userid = UserID;
		score = Score;
		datetaken = FormatDateTime.getCurrentSystemDate();
		time = Time;		
	}
	
	public String toString(){
		return "     Date Taken:  " +datetaken
			+ "      Score:     " +score + "      Time to Completion:     " + time + "s";
	}
}
