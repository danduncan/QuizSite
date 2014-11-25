package Quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.jsp.JspWriter;

import quizsite.DatabaseConnection;
import quizsite.FormatDateTime;

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
	public Integer numtaken;
	public ArrayList<Question> questions = new ArrayList<Question>();
	private QuizConnection quizconnection;
	
	public Quiz(boolean randomOrder, boolean manyPages, boolean immediateCorrection, int quizid){
		this.randomorder = randomOrder;
		this.multipage = manyPages;
		this.immediatecorrection = immediateCorrection;
		this.id = quizid;
		questions = new ArrayList<Question>();
	}
	
	public Quiz(boolean randomOrder, boolean manyPages, boolean immediateCorrection){
		this.randomorder = randomOrder;
		this.multipage = manyPages;
		this.immediatecorrection = immediateCorrection;
		questions = new ArrayList<Question>();
	}
	//creating new quiz
	public Quiz(Integer ID, Integer AuthorID,  String Name, String Description, Integer PracticeMode, boolean MultiPage,
			boolean RandomOrder, boolean ImmediateCorrection, QuizConnection qc){
		id = ID;
		authorid = AuthorID;
		datemade = FormatDateTime.getCurrentSystemDate();
		name = Name;
		description = Description;
		practicemode = 0;
		multipage = MultiPage;
		randomorder = RandomOrder;
		immediatecorrection = ImmediateCorrection;
		questions = new ArrayList<Question>();
		numtaken = 0;	
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
		practicemode = Integer.parseInt((String)quizconnection.getAttribute("practicemode", id));
		// the booleans are stored in database as strings which are 1 or 0 it seems since boolean.parse... was not working
		multipage = ((String)quizconnection.getAttribute("multipage", id)).equals("1");
		randomorder = ((String)quizconnection.getAttribute("randomorder", id)).equals("1");
		immediatecorrection = ((String) quizconnection.getAttribute("immediatecorrection", id)).equals("1");
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
	
	public int numPointsPossible(){
		int total = 0;
		for(int i = 0; i<questions.size(); i++){
			total += questions.get(i).numAttempted();
		}
		return total;
	}
	
	public void printEntireQuizToJSP(JspWriter out) throws IOException{
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
	
	public void printQuizPageToJSP(JspWriter out, int i) throws IOException{
		out.println("<h1>Question "+ (i+1) +"</h1>");
		Question q = questions.get(i);
		q.printToJSP(out, i);
		out.println("<br>");
	}
	
	//static method to pull name from database based on id
	public static String getName(Integer ID, DatabaseConnection dc) throws SQLException{
		String query = "SELECT name FROM quizzes WHERE (id = "+ID+")";
		ResultSet rs = dc.executeQuery(query);
	
		String fieldvalue = "";
		while(rs.next()) {
			fieldvalue = rs.getString("name");
		}

		return fieldvalue;
	}
}
