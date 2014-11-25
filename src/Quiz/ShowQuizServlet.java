package Quiz;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;


@WebServlet("/ShowQuizServlet")
public class ShowQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public static final String START_TIME = "Start Time";
	public static final String QUIZ = "Quiz";
	public static final String QUIZ_PAGE = "Quiz Page";
	public static final String QUIZ_ANSWERS = "Quiz Answers";
	public static final String NUM_CORRECT = "Number Correct";
	public static final String NUM_ATTEMPTED = "Number Attempted";
	public static final String SHOW_ANSWER = "Show Answer Boolean";
	public static final String PAGE_TITLE = "Quiz";
	
    public ShowQuizServlet() {}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {				
		request.getSession().setAttribute(QUIZ_ANSWERS, new LinkedList<String[]>());
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
				forwardRequest(request, response, "quizresults.jsp");
			}
			
		}else{
			for(int i = 0; i< quiz.getQuizSize(); i++) checkAnswer(request, i);
			forwardRequest(request, response, "quizresults.jsp");
		}
	}
		
	private void showAnswer(HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException{
		int quizPage = (Integer) request.getSession().getAttribute(QUIZ_PAGE);
		Quiz quiz = (Quiz) request.getSession().getAttribute(QUIZ);
		Question q = quiz.getQuestion(quizPage);
		String[] answer = q.getResponses(request, quizPage);
		
		if(q.isCorrect(answer)){
			forwardRequest(request, response, "correctanswer.jsp");
		}else{
			forwardRequest(request, response, "wronganswer.jsp");
		}
		request.getSession().setAttribute(SHOW_ANSWER, false);
	}

	private void showQuiz(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		Quiz quiz = (Quiz) request.getSession().getAttribute(QUIZ);
		
		if(quiz.isManyPages()){
			if(quiz.wantsImmediateCorrection()){
				request.getSession().setAttribute(SHOW_ANSWER, true);
			}
			forwardRequest(request, response, "showquizpage.jsp");
		}else{
			forwardRequest(request, response, "showentirequiz.jsp");
		}
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
	
	public static void printAnswers(JspWriter out, Question q) throws IOException{
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
	
	public static void printResponses(JspWriter out, String[] responses, Question q) throws IOException{
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
	
	
	private void forwardRequest(HttpServletRequest request, HttpServletResponse response, String dest) throws IOException, ServletException{
		RequestDispatcher dispatch = request.getRequestDispatcher(dest);
		dispatch.forward(request, response);
	}
	
}
