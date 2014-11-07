package Quiz;

import java.io.PrintWriter;


public class QuestionResponse extends Question {
	public QuestionResponse(String questionStr, String answerStr){
		super(Question.QUESTION_RESPONSE, questionStr, answerStr);
	}
	
	public void printToJSP(PrintWriter out, int i){
		out.println(getQuestionStr() + " <input type=\"text\" name=\"Answer"+i+"\">");
	}
	
}
