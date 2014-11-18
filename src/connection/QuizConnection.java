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

				
				for (int i = 0; i < questiontypes.size(); i++){
					String type = questiontypes.get(i).tableName;
					query = "SELECT * FROM "+type +" WHERE (quizid = "+ID+")";
					System.out.print(query);
					
					//get all quiz questions from each question type table
					ResultSet rs = db.executeQuery(query);
					
					if (type.equals(MyDBInfo.QUESTIONRESPONSE)){
						while (rs.next()){
							//answer can be an array of strings
							String formattedString = rs.getString("answer");
							String[] answer = formattedString.split(stringdelimiter);
							//need to format answer
							questions.add(new QuestionResponse(rs.getInt("id"),rs.getInt("quizid"),rs.getInt("qnumber"),rs.getInt("pagenumber"),rs.getInt("questiontime"), rs.getInt("numattemped"), rs.getInt("numcorrect"), MyDBInfo.QUESTIONRESPONSE, rs.getString("question"),answer));
						}
					} else if (type.equals(MyDBInfo.QUESTIONFILLBLANK)){
						while (rs.next()){
							//answer can be string array, and the question can be two strings as well
							String formattedString = rs.getString("answer");
							String[] answer = formattedString.split(stringdelimiter);
							//split question string if needed
							String formattedQ = rs.getString("question");
							String[] question = formattedQ.split(stringdelimiter, -1);
							questions.add(new FillBlankQuestion(rs.getInt("id"),rs.getInt("quizid"),rs.getInt("qnumber"),rs.getInt("pagenumber"),rs.getInt("questiontime"), rs.getInt("numattemped"), rs.getInt("numcorrect"), MyDBInfo.QUESTIONFILLBLANK, question[0], question[1],answer));
						}
					} else if (type.equals(MyDBInfo.QUESTIONMULTIPLECHOICE)){
						while (rs.next()){
							//need to get answer choices
							String formAnsChoices = rs.getString("answerChoices");
							String[] answerChoices = formAnsChoices.split(stringdelimiter);
							questions.add(new MultipleChoiceQuestion(rs.getInt("id"),rs.getInt("quizid"),rs.getInt("qnumber"),rs.getInt("pagenumber"),rs.getInt("questiontime"), rs.getInt("numattemped"), rs.getInt("numcorrect"), MyDBInfo.QUESTIONMULTIPLECHOICE,rs.getString("question"), rs.getString("answer"),answerChoices, rs.getBoolean("randomizeAnswers")));
						}
					} else if (type.equals(MyDBInfo.QUESTIONPICTURE)){
						while (rs.next()){
							//need to get answers string
							String formattedString = rs.getString("answer");
							String[] answers = formattedString.split(stringdelimiter);
							questions.add(new PictureResponseQuestion(rs.getInt("id"),rs.getInt("quizid"),rs.getInt("qnumber"),rs.getInt("pagenumber"),rs.getInt("questiontime"), rs.getInt("numattemped"), rs.getInt("numcorrect"), MyDBInfo.QUESTIONPICTURE, rs.getString("question"), answers, rs.getString("url")));
						}
					} else if (type.equals(MyDBInfo.QUESTIONMULTIPLEANSWER)){
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
							questions.add(new MultiAnswerQuestion(rs.getInt("id"),rs.getInt("quizid"),rs.getInt("qnumber"),rs.getInt("pagenumber"),rs.getInt("questiontime"), rs.getInt("numattemped"), rs.getInt("numcorrect"), MyDBInfo.QUESTIONMULTIPLEANSWER, rs.getString("question"), answers, rs.getBoolean("ordered")));
						} 
					} else if (type.equals(MyDBInfo.QUESTIONMULTIPLECHOICEMULTIPLEANSWER)){
						while (rs.next()){
							//need to get answers string
							String formattedString = rs.getString("answer");
							String[] answers = formattedString.split(stringdelimiter);
							String formattedChoices = rs.getString("answerChoices");
							String[] choices = formattedChoices.split(stringdelimiter);
							
							questions.add(new MultiChoiceMultiAnswerQuestion(rs.getInt("id"),rs.getInt("quizid"),rs.getInt("qnumber"),rs.getInt("pagenumber"),rs.getInt("questiontime"), rs.getInt("numattemped"), rs.getInt("numcorrect"), MyDBInfo.QUESTIONMULTIPLECHOICEMULTIPLEANSWER, rs.getString("question"), answers, choices, rs.getBoolean("randomizeAnswers")));
						}
					}
				
					
				}
				//want array list to be sorted by question number
				Collections.sort(questions, new Comparator<Question>() {
			        @Override public int compare(Question q1, Question q2) {
			            return q2.qnumber - q1.qnumber; // Descending
			        }

			    });
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
	public void storeQuiz(Quiz quiz, boolean newQuiz){
		//new quiz is insert
		if (newQuiz){
			//insert quiz info
			String insert = "INSERT INTO " + MyDBInfo.QUIZZESTABLE + " VALUES("+quiz.id+","+quiz.authorid+", \"" + quiz.datemade + "\", \"" + quiz.description + "\"," + quiz.practicemode + ", " + quiz.multipage
            + ", " + quiz.randomorder + "," + quiz.immediatecorrection + ","+quiz.numquestions+ ","+ quiz.numtaken +")";   
		
			db.executeUpdate(insert);
			
			//iterate through each question to store in proper table
			Iterator itr = quiz.questions.iterator();
			for(int i = 0; i < quiz.questions.size(); i++){		
				//get type of question
				String type = quiz.questions.get(i).typeQ; 				
				if (type.equals(MyDBInfo.QUESTIONRESPONSE)){
					QuestionResponse Q = (QuestionResponse) quiz.questions.get(i); 
					//need to convert Answer String array to formatted string
					String[] Answer = Q.getAnswerStrs();
					String formattedAnswer = Answer[0];
					
					for(int j = 1; j < Answer.length; j++){
						formattedAnswer += stringdelimiter+Answer[j];
					}
					
					String insertQ = "INSERT INTO " +type + " VALUES("+Q.id+","+Q.quizid+","+Q.qnumber+","+Q.pagenumber+","+Q.numcorrect+",\""+ Q.getQuestionStr()+"\",\""+formattedAnswer+"\")";
					db.executeUpdate(insertQ);
					
				} else if (type.equals(MyDBInfo.QUESTIONFILLBLANK)){
					FillBlankQuestion Q = (FillBlankQuestion) quiz.questions.get(i); 
					//need to convert Answer and Question String array to formatted string
					String[] Answer = Q.answer;
					String formattedAnswer = Answer[0];
					for(int j = 1; j < Answer.length; j++){
						formattedAnswer += stringdelimiter+Answer[j];
					}
					//format question
					String q = Q.qStr1 + stringdelimiter + Q.qStr2;
					
					String insertQ = "INSERT INTO " +type + " VALUES("+Q.id+","+Q.quizid+","+Q.qnumber+","+Q.pagenumber+","+Q.numcorrect+",\""+ q +"\",\""+formattedAnswer+"\")";
					db.executeUpdate(insertQ);		
					
				} else if (type.equals(MyDBInfo.QUESTIONMULTIPLECHOICE)){
					MultipleChoiceQuestion Q = (MultipleChoiceQuestion) quiz.questions.get(i); 
					//need to convert answer choices String array to formatted string
					String[] answerChoices = Q.answerChoices;
					String formattedChoices = answerChoices[0];
					for(int j = 1; j < answerChoices.length; j++){
						formattedChoices += stringdelimiter+answerChoices[j];
					}
					
					String insertQ = "INSERT INTO " +type + " VALUES("+Q.id+","+Q.quizid+","+Q.qnumber+","+Q.pagenumber+","+Q.numcorrect+",\""+ Q.getQuestionStr() +"\",\""+Q.getAnswerStrs()+"\",\""+formattedChoices+"\","+Q.randomizeAnswers+")";
					db.executeUpdate(insertQ);		

				} else if (type.equals(MyDBInfo.QUESTIONPICTURE)){
					PictureResponseQuestion Q = (PictureResponseQuestion) quiz.questions.get(i); 
					//need to convert Answer array to formatted string
					String[] Answer = Q.getAnswerStrs();
					String formattedAnswer = Answer[0];
					for(int j = 1; j < Answer.length; j++){
						formattedAnswer += stringdelimiter+Answer[j];
					}
					
					String insertQ = "INSERT INTO " +type + " VALUES("+Q.id+","+Q.quizid+","+Q.qnumber+","+Q.pagenumber+","+Q.numcorrect+",\""+ Q.getQuestionStr() +"\",\""+formattedAnswer+"\",\""+Q.picURL+"\")";
					db.executeUpdate(insertQ);
					
				} else if (type.equals(MyDBInfo.QUESTIONMULTIPLEANSWER)){
					MultiAnswerQuestion Q = (MultiAnswerQuestion) quiz.questions.get(i); 
					//need to convert Answer and Question String array to formatted string
					String[][] Answer = Q.answer;
					String formattedAnswer = Answer[0][0];
					for(int j = 0; j < Answer.length; j++){
						for(int k = 0; k < Answer)
						formattedAnswer += stringdelimiter+Answer[j];
					}
					
					String insertQ = "INSERT INTO " +type + " VALUES("+Q.id+","+Q.quizid+","+Q.qnumber+","+Q.pagenumber+","+Q.numcorrect+",\""+ Q.getQuestionStr() +"\",\""+formattedAnswer+"\",\""+Q.picURL+"\")";
					db.executeUpdate(insertQ);
					
					
				} else if (type.equals(MyDBInfo.QUESTIONMULTIPLECHOICEMULTIPLEANSWER)){
				
				}
				
				//String insertQ = "INSERT INTO " +question.typeQ + " VALUES(";
			}
			
		
		} else {
			
		}
	}
	
}
