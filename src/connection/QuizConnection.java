package connection;

import quizsite.DatabaseConnection;

//this class retrieves and updates quiz information in database
public class QuizConnection {
	private String stringdelimiter = "&&&";
	private String arraydelimiter = "@";
	private DatabaseConnection db;
	
	//ultimately want to make string and array inputs from site manager
	public QuizConnection(DatabaseConnection DB){
		db = DB;
	}
	
	public void getAttribute(String field, Integer ID){
		
	}

	
}
