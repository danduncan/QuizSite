package connection;

import java.sql.ResultSet;
import quizsite.MyDBInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import Quiz.*;

import quizsite.DatabaseConnection;

//this class retrieves and updates quiz information in database
public class QuizConnection {
	private String stringdelimiter = "&&&";
	private String arraydelimiter = "@";
	private DatabaseConnection db;
	public QuestionConnection qc;
	private ArrayList<QuestionType> questiontypes;
	
	//ultimately want to make string and array inputs from site manager
	public QuizConnection(DatabaseConnection DB, ArrayList<QuestionType> QuestionTypes){
		db = DB;
		questiontypes = QuestionTypes;
		qc = new QuestionConnection(db,questiontypes);
		
	}
	//this function will retrieve the specified object from the database based on the quiz id
	public Object getAttribute(String field, Integer ID){
		String query;
		try{
			//check what we are retrieving
			if (field.equals("questions")){
				//if questions, we need to construct list of questions for quiz
				return qc.getQuestions(ID);
				
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
		return null;
	}
	public void storeQuiz(Quiz quiz, boolean newQuiz){
		//new quiz is insert
		if (newQuiz){
			//insert quiz info
			String insert = "INSERT INTO " + MyDBInfo.QUIZZESTABLE + " VALUES("+quiz.id+","+quiz.authorid+", \"" + quiz.datemade + "\",\""+quiz.name +"\" ,\"" + quiz.description + "\"," + quiz.practicemode + ", " + quiz.multipage
            + ", " + quiz.randomorder + "," + quiz.immediatecorrection + ","+quiz.questions.size()+ ","+ quiz.numtaken +")";   			
			
			db.executeUpdate(insert);
			
			qc.insertQuestions(quiz);
			
		
		} else {
			
		}
	}
}
