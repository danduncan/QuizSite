package Quiz;

import java.io.PrintWriter;

public class PictureResponseQuestion extends Question {
	private String picURL;
	
	public PictureResponseQuestion(String picURL, String answer){
		this(picURL, new String[]{answer});
	}
	
	public PictureResponseQuestion(String picURL, String[] answers){
		super(Question.PICTURE_RESPONSE, picURL, answers);
		this.picURL = picURL;
	}
	
	public void printToJSP(PrintWriter out, int i){
		out.println("<img src="+picURL+" alt="+picURL+"><br>");
		out.println("<input type=\"text\" name=\"Answer"+i+"\">");
	}
	

}
