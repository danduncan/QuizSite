package Quiz;

import java.io.PrintWriter;

public class Question {
	public static int QUESTION_RESPONSE = 0;
	public static int FILL_IN_BLANK = 1;
	public static int MULTIPLE_CHOICE = 2;
	public static int PICTURE_RESPONSE = 3;
	public static String DELIM = "&&&";
	
	private int type;
	private String questionStr;
	private String answerStr;
	
	public Question(int type, String questionStr, String answerStr){
		this.type = type;
		this.questionStr = questionStr;
		this.answerStr = answerStr;
	}
	
	public boolean isCorrect(String responseStr){
		if(responseStr == null) return false;
		return responseStr.equals(answerStr);
	}
	
	public String getQuestionStr(){
		return questionStr;
	}
	
	public String getAnswer(){
		return answerStr;
	}
	
	public int getType(){
		return type;
	}
	
	public void printToJSP(PrintWriter out, int i){
		if(type == Question.QUESTION_RESPONSE){
			QuestionResponse qr = (QuestionResponse)this;
			qr.printToJSP(out, i);
		}else if(type == Question.FILL_IN_BLANK){
			FillBlankQuestion qfb = (FillBlankQuestion)this;
			qfb.printToJSP(out, i);
		}else if(type == Question.PICTURE_RESPONSE){
			PictureResponseQuestion qpr = (PictureResponseQuestion)this;
			qpr.printToJSP(out, i);
		}else if(type == Question.MULTIPLE_CHOICE){
			MultipleChoiceQuestion qmc = (MultipleChoiceQuestion)this;
			qmc.printToJSP(out, i);
		}
	}
	
}
