package connection;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Quiz.Question;
import Quiz.QuestionResponse;
import Quiz.QuestionType;
import Quiz.*;

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
				//if questions, we need to construct list of questions for quiz
				ArrayList<Question> questions = new ArrayList<Question>();
				//want array list to be sorted by question number
				Collections.sort(questions, new Comparator<Question>() {
			        @Override public int compare(Question q1, Question q2) {
			            return q2.qNumber - q1.qNumber; // Descending
			        }

			    });
				
				for (int i = 0; i < questiontypes.size(); i++){
					String type = questiontypes.get(i).tableName;
					query = "SELECT * FROM "+type +" WHERE (quizid = "+ID+")";
					System.out.print(query);
					
					//get all quiz questions from each question type table
					ResultSet rs = db.executeQuery(query);
					
					if (type.equals("qResponse")){
						while (rs.next()){
							//answer can be an array of strings
							String formattedString = rs.getString("answer");
							String[] answer = formattedString.split(stringdelimiter);
							//need to format answer
							questions.add(new QuestionResponse(rs.getInt("id"),rs.getInt("quizid"),rs.getInt("qnumber"),rs.getInt("pagenumber"),rs.getInt("questiontime"), rs.getInt("numattemped"), rs.getInt("numcorrect"), rs.getString("question"),answer));
						}
					} else if (type.equals("qFillBlank")){
						while (rs.next()){
							//answer can be string array, and the question can be two strings as well
							String formattedString = rs.getString("answer");
							String[] answer = formattedString.split(stringdelimiter);
							//split question string if needed
							String formattedQ = rs.getString("question");
							String[] question = formattedQ.split(stringdelimiter, -1);
							questions.add(new FillBlankQuestion(rs.getInt("id"),rs.getInt("quizid"),rs.getInt("qnumber"),rs.getInt("pagenumber"),rs.getInt("questiontime"), rs.getInt("numattemped"), rs.getInt("numcorrect"), question[0], question[1],answer));
						}
					} else if (type.equals("qMultipleChoice")){
						while (rs.next()){
							//need to get answer choices
							String formAnsChoices = rs.getString("answerChoices");
							String[] answerChoices = formAnsChoices.split(stringdelimiter);
							questions.add(new MultipleChoiceQuestion(rs.getInt("id"),rs.getInt("quizid"),rs.getInt("qnumber"),rs.getInt("pagenumber"),rs.getInt("questiontime"), rs.getInt("numattemped"), rs.getInt("numcorrect"), rs.getString("question"), rs.getString("answer"),answerChoices, rs.getBoolean("randomizeAnswers")));
						}
					} else if (type.equals("qPicture")){
						while (rs.next()){
							//need to get answers string
							String formattedString = rs.getString("answer");
							String[] answers = formattedString.split(stringdelimiter);
							questions.add(new PictureResponseQuestion(rs.getInt("id"),rs.getInt("quizid"),rs.getInt("qnumber"),rs.getInt("pagenumber"),rs.getInt("questiontime"), rs.getInt("numattemped"), rs.getInt("numcorrect"), rs.getString("question"), answers, rs.getString("url")));
						}
					} else if (type.equals("qMultipleAnswer")){
						while (rs.next()){
							//String [][] answers = null;
							//need to get answers string
							String formattedString = rs.getString("answer");
							String[] numAnswers = formattedString.split(arraydelimiter);
							//number of answers
							int numAns = numAnswers.length;
							//determine max num of answers for one question
							int max = 0;
							for (int j = 0; j < numAns; j++){
								int sameanswers = numAnswers[j].split(stringdelimiter).length;
								if( sameanswers > max){
									max = sameanswers;
								}
							}
							//we now know size of 2D string array needed
							String[][] answers = new String[numAns][max];
 							//construct 2D array of answers
							for (int j = 0; j < numAns; j++){
								answers[j] = numAnswers[j].split(stringdelimiter);
							}
							questions.add(new MultiAnswerQuestion(rs.getInt("id"),rs.getInt("quizid"),rs.getInt("qnumber"),rs.getInt("pagenumber"),rs.getInt("questiontime"), rs.getInt("numattemped"), rs.getInt("numcorrect"), rs.getString("question"), answers, rs.getBoolean("ordered")));
						} 
					} else if (type.equals("qMultipleChoiceMultipleAnswer")){
						while (rs.next()){
							//need to get answers string
							String formattedString = rs.getString("answer");
							String[] answers = formattedString.split(stringdelimiter);
							String formattedChoices = rs.getString("answerChoices");
							String[] choices = formattedChoices.split(stringdelimiter);
			
							questions.add(new MultiChoiceMultiAnswerQuestion(rs.getInt("id"),rs.getInt("quizid"),rs.getInt("qnumber"),rs.getInt("pagenumber"),rs.getInt("questiontime"), rs.getInt("numattemped"), rs.getInt("numcorrect"), rs.getString("question"), answers, choices, rs.getBoolean("randomizeAnswers")));
						}
					}
				
					
				}
				return questions; 
				
			} else {
				query = "SELECT " + field + "  FROM quizzes WHERE (quizid = "+ID+")";
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

	
}
