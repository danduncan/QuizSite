package Quiz;

import java.io.PrintWriter;


public class QuestionResponse extends Question {
	public QuestionResponse(String questionStr, String answerStr){
		this(questionStr, new String[]{answerStr});
	}
	
	public QuestionResponse(String questionStr, String[] answerStrs){
		super(Question.QUESTION_RESPONSE, questionStr, answerStrs);
	}
	
	public void printToJSP(PrintWriter out, int i){
		out.println(getQuestionStr() + " <input type=\"text\" name=\"Answer"+i+"\">");
	}
	
}
