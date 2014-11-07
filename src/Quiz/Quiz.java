package Quiz;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class Quiz {
	private List<Question> questions;
	private int quizID;
	
	public Quiz(){
		questions = new LinkedList<Question>();
	}
	
	public void addQuestions(Question q){
		questions.add(q);
	}
	
	public List<Question> getQuestions(){
		return questions;
	}
	
	public int getQuizSize(){
		return questions.size();
	}
	
	public void printToJSP(PrintWriter out){
		out.println("<ol>");
		for(int i = 0; i<questions.size(); i++){
			Question q = questions.get(i);
			out.println("<li>");
			q.printToJSP(out, i);
			out.println("</li>");
		}
		out.println("</ol>");
	}
}
