package Quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import users.*;
import quizsite.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.UserConnection;

@WebServlet("/ShowQuizServlet")
public class ShowQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static final String START_TIME = "Start Time";
	public static final String QUIZ = "Quiz";
	private static final String QUIZ_PAGE = "Quiz Page";
	private static final String QUIZ_ANSWERS = "Quiz Answers";
	private static final String NUM_CORRECT = "Number Correct";
	private static final String NUM_ATTEMPTED = "Number Attempted";
	private static final String SHOW_ANSWER = "Show Answer Boolean";
	private static final String PAGE_TITLE = "Quiz";
	
    public ShowQuizServlet() {}
    
    private void makeTestQuiz(HttpServletRequest request){
    	Quiz quiz = (Quiz)request.getSession().getAttribute(CreateQuizServlet.QUIZ_CREATED);
    	User user = (User)request.getSession().getAttribute("user");
    	
    	if(user == null){
			ServletContext sc = request.getServletContext();
			DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
			user = new User(0, new UserConnection(dc));
    	}
    	request.getSession().setAttribute("user", user);
    	
    	if(quiz == null){
        	quiz = new Quiz(true, false, false, 3);
        	String[] ans1 = new String[]{"Packers", "packers"};
        	String[] ans2 = new String[]{"Badgers", "badgers"};
        	String[] ans3 = new String[]{"bears"};
        	quiz.addQuestion(new MultiAnswerQuestion("Best sports teams?", new String[][]{ans1, ans2, ans3}, false));
        	quiz.addQuestion(new FillBlankQuestion("What", "the", "fuck"));
        	quiz.addQuestion(new PictureResponseQuestion("What is the name of this building?","http://events.stanford.edu/events/252/25201/Memchu_small.jpg", "Memchu"));
        	quiz.addQuestion(new QuestionResponse("What are you doing?", new String[]{"IDK", "big things"}));
        	quiz.addQuestion(new MultipleChoiceQuestion("Favorite letter?", new String[] {"a", "b duh", "a shit"}, "a shit"));
        	quiz.addQuestion(new MultiChoiceMultiAnswerQuestion("Favorite letter?", new String[] {"a", "poop man", "shit bro"}, new String[]{"poop man","shit bro"}));	
    	}
    	request.getSession().setAttribute(QUIZ, quiz);
    }
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		makeTestQuiz(request);
		
		List<String[]> quizAnswers = new LinkedList<String[]>();
		request.getSession().setAttribute(QUIZ_ANSWERS, quizAnswers);
		request.getSession().setAttribute(QUIZ_PAGE, 0);
		request.getSession().setAttribute(NUM_ATTEMPTED, 0);
		request.getSession().setAttribute(SHOW_ANSWER, false);
		request.getSession().setAttribute(NUM_CORRECT, 0);
    	request.getSession().setAttribute(START_TIME, System.currentTimeMillis());
		showQuiz(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Quiz quiz = (Quiz) request.getSession().getAttribute(QUIZ);
		int quizPage = (Integer) request.getSession().getAttribute(QUIZ_PAGE);
		boolean showAnswer = (Boolean) request.getSession().getAttribute(SHOW_ANSWER);
		
		if(quiz.isManyPages()){
			if(quiz.wantsImmediateCorrection()){
				if(showAnswer){
					checkAnswer(request, quizPage);
					showAnswer(response, request);
					request.getSession().setAttribute(QUIZ_PAGE, quizPage+1);
					return;
				}
			}else{
				checkAnswer(request, quizPage);
				request.getSession().setAttribute(QUIZ_PAGE, quizPage+1);
			}
			//the quiz page may or may not have just been incremented
			quizPage = (Integer) request.getSession().getAttribute(QUIZ_PAGE);
			if(quiz.getQuizSize()>quizPage){
				showQuiz(request, response);
			}else{
				showResults(request, response);
			}
			
		}else{
			for(int i = 0; i< quiz.getQuizSize(); i++) checkAnswer(request, i);
			showResults(request,response);
		}
	}
	
	private PrintWriter writeHeader(HttpServletResponse response, String title) throws IOException{
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>"+title+"</title>");
		out.println("</head>");
		out.println("<body>");
		return out;
	}
	
	private void showAnswer(HttpServletResponse response, HttpServletRequest request) throws IOException{
		int quizPage = (Integer) request.getSession().getAttribute(QUIZ_PAGE);
		Quiz quiz = (Quiz) request.getSession().getAttribute(QUIZ);
		Question q = quiz.getQuestion(quizPage);
		String[] answer = q.getResponses(request, quizPage);
		
		PrintWriter out = writeHeader(response, PAGE_TITLE);
		if(q.isCorrect(answer)){
			out.println("<h1>That is correct!</h1>");
			out.println("<img src=\"http://media0.giphy.com/media/PS7d4tm1Hq6Sk/giphy.gif\">");
		}else{
			out.println("<h1>That is wrong.</h1>");
			out.println("<img src=\"http://www.reactiongifs.com/r/wrong-gif.gif\" width=\"300\" height=\"250\"><br>");
			printAnswers(out, q);
		}
		out.println("<form action=\"ShowQuizServlet\" method=\"post\">");
		out.println("<input type=\"submit\" value=\"Continue\"></form>");
		out.println("</body></html>");
		request.getSession().setAttribute(SHOW_ANSWER, false);
	}

	private void showQuiz(HttpServletRequest request, HttpServletResponse response) throws IOException{
		int quizPage = (Integer) request.getSession().getAttribute(QUIZ_PAGE);
		Quiz quiz = (Quiz) request.getSession().getAttribute(QUIZ);
		PrintWriter out = writeHeader(response, PAGE_TITLE);
		out.println("<form action=\"ShowQuizServlet\" method=\"post\">");
		if(quiz.isManyPages()){
			if(quiz.wantsImmediateCorrection()){
				request.getSession().setAttribute(SHOW_ANSWER, true);
			}
			quiz.printQuizPageToJSP(out, quizPage);
			out.println("<input type=\"submit\" value=\"Submit Answer\">");
		}else{
			quiz.printEntireQuizToJSP(out);
			out.println("<input type=\"submit\" value=\"Submit Quiz\">");
		}
		out.println("</form></body></html>");
	}
	
	private void printAnswers(PrintWriter out, Question q){
		String[] answers = q.getAnswerStrs();
		if(answers.length == 1){
			out.println("The answers are: "+ answers[0]+"<br>");
		}else{
			out.println("The answers are:");
			out.println("<ul>");
			for(int i = 0; i<answers.length; i++){
				out.println("<li>");
				char c = (char) ('a'+i);
				out.println(c+".) "+ answers[i]+"<br>");
				out.println("</li>");
			}
			out.println("</ul>");
		}
	}
	
	private void printResponses(PrintWriter out, String[] responses, Question q){
		if(q.numAttempted() == 1){
			String responseStr = "";
			for(int i = 0; i<responses.length; i++){
				responseStr+= responses[i];
				if(i != responses.length-1) responseStr+=", ";
			}
			out.println("Your responses were: "+ responseStr+"<br>");
		}else{
			out.println("Your responses were:");
			out.println("<ul>");
			for(int i = 0; i<responses.length; i++){
				out.println("<li>");
				char c = (char) ('a'+i);
				out.println(c+".) "+ responses[i]+"<br>");
				out.println("</li>");
			}
			out.println("</ul>");
		}
	}
	
	
	private void showResults(HttpServletRequest request, HttpServletResponse response) throws IOException{
		int numAttempted = (Integer) request.getSession().getAttribute(NUM_ATTEMPTED);
		int numCorrect = (Integer) request.getSession().getAttribute(NUM_CORRECT);
		long startTime = (Long) request.getSession().getAttribute(START_TIME);
		double secondsElapsed = (double)(System.currentTimeMillis()-startTime)/1000;
		Quiz quiz = (Quiz) request.getSession().getAttribute(QUIZ);
		List<String[]> quizAnswers = (List<String[]>) request.getSession().getAttribute(QUIZ_ANSWERS);
		User user = (User) request.getSession().getAttribute("user");
		ScoreManager scoreManager = (ScoreManager) request.getServletContext().getAttribute("ScoreManager");
					
		Score quizScore = new Score(user.id, quiz.id, numCorrect, (int)secondsElapsed, FormatDateTime.getCurrentSystemDate());
		int rank = scoreManager.addScore(quizScore);
		
		PrintWriter out = writeHeader(response, PAGE_TITLE);
		out.println("<h1>Quiz Results</h1>");
		out.println("Points earned: "+numCorrect+"<br>");
		out.println("Points possible: "+numAttempted+"<br>");
		out.println("Percent score: "+100*((double)numCorrect/(double)numAttempted)+"%<br>");
		out.println("Time: "+secondsElapsed+" seconds<br>");
		if(rank != 0){
			out.println("High score!!!<br>");
			out.println("All-time score rank: "+rank+"<br>");
		}
		
		out.println("<h1>Answers</h1>");
		out.println("<ul style=\"list-style-type:square\">");
		for(int i = 0; i<quiz.getQuizSize(); i++){
			out.println("<li>");
			out.println("Question " + (i+1)+":<br>");
			printResponses(out, quizAnswers.get(i), quiz.getQuestion(i));
			printAnswers(out, quiz.getQuestion(i));
			out.println("<br>");
			out.println("</li>");
		}
		out.println("</ul></body></html>");
	}
	
	private void checkAnswer(HttpServletRequest request, int i){
		Quiz quiz = (Quiz) request.getSession().getAttribute(QUIZ);
		int numAttempted = (Integer) request.getSession().getAttribute(NUM_ATTEMPTED);
		int numCorrect = (Integer) request.getSession().getAttribute(NUM_CORRECT);
		List<String[]> quizAnswers = (List<String[]>) request.getSession().getAttribute(QUIZ_ANSWERS);
		
		Question q = quiz.getQuestion(i);
		String[] answer = q.getResponses(request, i);
		quizAnswers.add(answer);
		
		request.getSession().setAttribute(QUIZ_ANSWERS, quizAnswers);
		int correct = q.numCorrect(answer);
		request.getSession().setAttribute(NUM_CORRECT, numCorrect+correct);
		int attempted = q.numAttempted();
		request.getSession().setAttribute(NUM_ATTEMPTED, numAttempted+attempted);
	}
}
