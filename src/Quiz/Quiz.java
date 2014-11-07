package Quiz;

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
	
}
