package Quiz;

import java.io.PrintWriter;

public class PictureResponseQuestion extends Question {
	private String picURL;
	private String answer;
	
	public PictureResponseQuestion(String picURL, String answer){
		super(Question.PICTURE_RESPONSE, picURL, answer);
		this.picURL = picURL;
		this.answer = answer;
	}
	
	public void printToJSP(PrintWriter out, int i){
		out.println("<img src="+picURL+" alt="+picURL+"><br>");
		out.println("<input type=\"text\" name=\"Answer"+i+"\">");
	}
	

}
