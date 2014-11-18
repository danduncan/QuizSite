package Quiz;

import java.io.PrintWriter;

public class PictureResponseQuestion extends Question {
	public String picURL;
	private String question;
	private String[] answer;
	
	public PictureResponseQuestion(Integer ID, Integer QuizID, Integer QNumber, Integer PageNumber, Integer QuestionTime, Integer NumAttempted, Integer NumCorrect, String Type,String Question, String[] Answer, String url){
		super(ID,QuizID,QNumber,PageNumber,QuestionTime,NumAttempted,NumCorrect, Type);
		answer = Answer;
		question = Question;
		picURL = url;
	}
	
	public PictureResponseQuestion(String question, String picURL, String answer){
		this(question, picURL, new String[]{answer});
	}
	
	public PictureResponseQuestion(String question, String picURL, String[] answers){
		super(Question.PICTURE_RESPONSE, question, answers);
		this.picURL = picURL;
	}
	
	public void printToJSP(PrintWriter out, int i){
		out.println(questionStr + "<br>");
		out.println("<img src="+picURL+" alt="+picURL+"><br>");
		out.println("<input type=\"text\" name=\""+Question.ANSWER+i+""+0+"\">");
	}
	

}
