package Quiz;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Quiz {
	private List<Question> questions;
	private boolean randomOrder;
	private boolean manyPages;
	private boolean immediateCorrection;
	
	public Quiz(boolean randomOrder, boolean manyPages, boolean immediateCorrection){
		this.randomOrder = randomOrder;
		this.manyPages = manyPages;
		this.immediateCorrection = immediateCorrection;
		questions = new LinkedList<Question>();
	}
	
	public void addQuestion(Question q){
		questions.add(q);
		if(randomOrder){
			Collections.shuffle(questions);
		}
	}
	
	public boolean isManyPages(){
		return manyPages;
	}
	
	public boolean wantsImmediateCorrection(){
		return immediateCorrection;
	}
	
	public Question getQuestion(int i){
		return questions.get(i);
	}
	
	public int getQuizSize(){
		return questions.size();
	}
	
	public void printEntireQuizToJSP(PrintWriter out){
		out.println("<h1>Basic Quiz</h1>");
		out.println("<ol>");
		for(int i = 0; i<questions.size(); i++){
			Question q = questions.get(i);
			out.println("<li>");
			q.printToJSP(out, i);
			out.println("</li>");
		}
		out.println("</ol>");
	}
	
	public void printQuizPageToJSP(PrintWriter out, int i){
		out.println("<h1>Question "+ (i+1) +"</h1>");
		Question q = questions.get(i);
		q.printToJSP(out, i);
		out.println("<br>");
	}
}
