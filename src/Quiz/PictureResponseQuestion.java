package Quiz;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.jsp.JspWriter;

public class PictureResponseQuestion extends Question {
	public String picURL;

	public PictureResponseQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime, 
								Integer NumAttempted, Integer NumCorrect, String Type,String question, String[] Answer, String url){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,NumAttempted,NumCorrect, Type, question, Answer, Question.PICTURE_RESPONSE);
		picURL = url;
	}
	public PictureResponseQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber,
									Integer QuestionTime, String Type,String question, String[] Answer, String url){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,Type, question, Answer, Question.PICTURE_RESPONSE);
		picURL = url;
	}
	
	public void printToJSP(JspWriter out, int i) throws IOException{
		out.println(questionStr + "<br>");
		out.println("<img src="+picURL+" alt="+picURL+"><br>");
		out.println("<input type=\"text\" name=\""+Question.ANSWER+i+""+0+"\">");
	}
	

}
