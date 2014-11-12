package connection;

import java.sql.ResultSet;
import java.util.ArrayList;

import Quiz.QuestionType;

import quizsite.DatabaseConnection;

//this class retrieves and updates quiz information in database
public class QuizConnection {
	private String stringdelimiter = "&&&";
	private String arraydelimiter = "@";
	private DatabaseConnection db;
	private ArrayList<QuestionType> questiontypes = new ArrayList<QuestionType>();
	
	//ultimately want to make string and array inputs from site manager
	public QuizConnection(DatabaseConnection DB, ArrayList<QuestionType> QuestionTypes){
		db = DB;
		questiontypes = QuestionTypes;
	}
	//this function will retrieve the specified object from the database based on the quiz id
	public Object getAttribute(String field, Integer ID){
		String query;
		try{
			//check what we are retrieving
			if (field.equals("questions")){
				for (int i = 0; i < questiontypes.size(); i++){
					query = "SELECT * FROM "+questiontypes.get(i).tableName +" WHERE (id = "+ID+")";
					System.out.print(query);
					
					
				
				}
				 
			} else {
				query = "SELECT " + field + "  FROM quizzes WHERE (id = "+ID+")";
				ResultSet rs = db.executeQuery(query);
			
				String fieldvalue = "";
				while(rs.next()) {
					fieldvalue = rs.getString(field);
			}

			return fieldvalue;
		}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	
}
