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
	private String[] answerStrs;
	
	public Question(int type, String questionStr, String answerStr){
		this(type, questionStr, new String[]{answerStr});
	}
	
	public Question(int type, String questionStr, String[] answerStrs){
		this.type = type;
		this.questionStr = questionStr;
		this.answerStrs = answerStrs;
	}
	
	
	public boolean isCorrect(String responseStr){
		if(responseStr == null) return false;
		for(int i = 0 ; i<answerStrs.length; i++){
			if(responseStr.equals(answerStrs[i])) return true;
		}
		return false;
	}
	
	public String getQuestionStr(){
		return questionStr;
	}
	
	public String getAnswerStr(){
		String answerStr = "";
		for(int i = 0; i<answerStrs.length; i++){
			answerStr += answerStrs[i];
			if(i < answerStrs.length-1) answerStr += ", ";
		}
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
