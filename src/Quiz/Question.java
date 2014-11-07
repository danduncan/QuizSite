package Quiz;

import java.io.PrintWriter;

public class Question {
	public static int QUESTION_RESPONSE = 0;
	public static int FILL_IN_BLANK = 1;
	public static int MULTIPLE_CHOICE = 2;
	public static int PICTURE_RESPONSE = 3;
	public static String DELIM = "&&&";
	
	private int questionID;
	private int type;
	private String questionStr;
	private String answerStr;
	
	public Question(int type, String questionStr, String answerStr){
		this.type = type;
		this.questionStr = questionStr;
		this.answerStr = answerStr;
	}
	
	public boolean isCorrect(String responseStr){
		return responseStr.equals(answerStr);
	}
	
	public String getQuestionStr(){
		return questionStr;
	}
	
	public int getType(){
		return type;
	}
	
}
