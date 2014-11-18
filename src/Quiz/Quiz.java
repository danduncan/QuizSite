package Quiz;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import connection.QuizConnection;

public class Quiz {
	public Integer id;
	public Integer authorid;
	public String datemade;
	public String name;
	public String description;
	public Integer practicemode;
	public boolean multipage;
	public boolean randomorder;
	public boolean immediatecorrection;
	public Integer numquestions;
	public Integer numtaken;
	public ArrayList<Question> questions = new ArrayList<Question>();
	private QuizConnection quizconnection;
	
	public Quiz(boolean randomOrder, boolean manyPages, boolean immediateCorrection){
		this.randomorder = randomOrder;
		this.multipage = manyPages;
		this.immediatecorrection = immediateCorrection;
		questions = new ArrayList<Question>();
	}
	//creating new quiz
	public Quiz(Integer ID, Integer AuthorID, String DateMade, String Name, String Description, Integer PracticeMode, boolean MultiPage,
			boolean RandomOrder, boolean ImmediateCorrection, Integer NumQuestions, Integer NumTaken, ArrayList<Question> Questions, QuizConnection qc){
		id = ID;
		authorid = AuthorID;
		datemade = DateMade;
		name = Name;
		description = Description;
		practicemode = PracticeMode;
		multipage = MultiPage;
		randomorder = RandomOrder;
		immediatecorrection = ImmediateCorrection;
		numquestions = NumQuestions;
		numtaken = NumTaken;
		questions = Questions;
		quizconnection = qc;
	}
	
	//retrieving old quiz
	public Quiz(Integer ID, QuizConnection qc){
		id = ID;
		quizconnection = qc;
		description = (String) quizconnection.getAttribute("description", id);
		authorid = Integer.parseInt((String) quizconnection.getAttribute("authorid", id));
		datemade = (String) quizconnection.getAttribute("datemade", id);
		name = (String) quizconnection.getAttribute("name", id);
		practicemode = Integer.parseInt((String) quizconnection.getAttribute("practicemode", id));
		multipage = Boolean.parseBoolean((String) quizconnection.getAttribute("multipage", id)) ;
		randomorder = Boolean.parseBoolean((String) quizconnection.getAttribute("randomorder", id));
		immediatecorrection = Boolean.parseBoolean((String) quizconnection.getAttribute("immediatecorrection", id));
		numquestions = Integer.parseInt((String) quizconnection.getAttribute("numquestions", id));
		numtaken = Integer.parseInt((String) quizconnection.getAttribute("numtaken", id));
		questions = (ArrayList<Question>) quizconnection.getAttribute("questions", id);
		
	}
	public void updateDatabase(boolean newQuiz, Integer ID){
		quizconnection.storeQuiz(this, newQuiz);
	}
	
	public void addQuestion(Question q){
		questions.add(q);
		if(randomorder){
			Collections.shuffle(questions);
		}
	}
	
	public boolean isManyPages(){
		return multipage;
	}
	
	public boolean wantsImmediateCorrection(){
		return immediatecorrection;
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
