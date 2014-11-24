package Quiz;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.jsp.JspWriter;

public class FillBlankQuestion extends Question{
	public String qStr1;
	public String qStr2;
	
	public FillBlankQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime,
							Integer NumAttempted, Integer NumCorrect, String Type,String QStr1, String QStr2, String[] Answer){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,NumAttempted,NumCorrect, Type, QStr1 + Question.STRING_DELIM + QStr2, Answer, Question.FILL_IN_BLANK);
		qStr1 = QStr1;
		qStr2 = QStr2;
	}
	public FillBlankQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber,
						Integer QuestionTime,  String Type,String QStr1, String QStr2, String[] Answer){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,Type, QStr1 + Question.STRING_DELIM + QStr2, Answer, Question.FILL_IN_BLANK);
		qStr1 = QStr1;
		qStr2 = QStr2;
	}
	
	public void printToJSP(JspWriter out, int i) throws IOException{
		out.println(qStr1 + " <input type=\"text\" name=\""+Question.ANSWER+i+""+0+"\"> " + qStr2);
	}
}
