package users;

import quizsite.FormatDateTime;

public class QuizTaken {
	public Integer id;
	public Integer quizid;
	public Integer userid;
	public String datetaken;
	public Integer score;
	public Integer time;
	
	public QuizTaken(Integer ID, Integer QuizID, Integer UserID, String DateTaken,Integer Score,  Integer Time){
		id = ID;
		quizid = QuizID;
		userid = UserID;
		score = Score;
		datetaken = DateTaken;
		time = Time;		
	}
	
	public QuizTaken(Integer ID, Integer QuizID, Integer UserID, Integer Score,  Integer Time){
		id = ID;
		quizid = QuizID;
		userid = UserID;
		score = Score;
		datetaken = FormatDateTime.getCurrentSystemDate();
		time = Time;		
	}
	
	public String toString(){
		return "Quiz ID:     " + quizid + "      Date Taken:  " +datetaken
			+ "      Score:     " +score + "      Time to Completion:     " + time + "s";
	}
}
