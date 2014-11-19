package users; 

import quizsite.FormatDateTime;

public class QuizMade {
	public Integer userid;
	public Integer quizid;
	public String date;
	
	public QuizMade(Integer UserID, Integer QuizID, String Date){
		userid = UserID;
		quizid = QuizID;
		date = Date;	
	}
	
	public QuizMade(Integer UserID, Integer QuizID){
		userid = UserID;
		quizid = QuizID;
		date = FormatDateTime.getCurrentSystemDate();	
	}
	
	public String toString(){
		return "QuizID:   " + quizid + "    Date Created: " + date;
	}
}
