package Quiz;

import java.io.PrintWriter;

public class PictureResponseQuestion extends Question {
	private String picURL;
	
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
