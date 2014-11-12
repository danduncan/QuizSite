package Quiz;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BasicQuizServlet")
public class BasicQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static final String START_TIME = "Start Time";
	private static final String QUIZ = "Quiz";
	private static final String QUIZ_PAGE = "Quiz Page";
	private static final String NUM_CORRECT = "Number Correct";
	private static final String NUM_ATTEMPTED = "Number Attempted";
	private static final String SHOW_ANSWER = "Show Answer Boolean";
	
    public BasicQuizServlet() {}
    
    private void makeTestQuiz(HttpServletRequest request){
    	Quiz quiz = new Quiz(true, true, false);
    	String[] ans1 = new String[]{"Packers", "packers"};
    	String[] ans2 = new String[]{"Badgers", "badgers"};
    	String[] ans3 = new String[]{"bears", "Bears"};
    	quiz.addQuestion(new MultiAnswerQuestion("Best sports teams?", new String[][]{ans1, ans2, ans3}, false));
    	quiz.addQuestion(new FillBlankQuestion("What", "the", "fuck"));
    	quiz.addQuestion(new PictureResponseQuestion("What is the name of this building?","http://events.stanford.edu/events/252/25201/Memchu_small.jpg", "Memchu"));
    	quiz.addQuestion(new QuestionResponse("What are you doing?", new String[]{"IDK", "big things"}));
    	quiz.addQuestion(new MultipleChoiceQuestion("Favorite letter?", new String[] {"a", "b", "shit"}, "shit"));
    	quiz.addQuestion(new MultiChoiceMultiAnswerQuestion("Favorite letter?", new String[] {"a", "poop", "shit"}, new String[]{"poop","shit"}));
    	request.getSession().setAttribute(QUIZ, quiz);
    }
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		makeTestQuiz(request);
		
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
	
	private void showAnswer(HttpServletResponse response, HttpServletRequest request) throws IOException{
		int quizPage = (Integer) request.getSession().getAttribute(QUIZ_PAGE);
		Quiz quiz = (Quiz) request.getSession().getAttribute(QUIZ);
		
		Question q = quiz.getQuestion(quizPage);
		String[] answer = q.getResponses(request, quizPage);
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>Basic Quiz</title>");
		out.println("</head>");
		out.println("<body>");
		if(q.isCorrect(answer)){
			out.println("<h1>That is correct!</h1>");
			out.println("<img src=\"http://media0.giphy.com/media/PS7d4tm1Hq6Sk/giphy.gif\">");
		}else{
			out.println("<h1>That is wrong.</h1>");
			out.println("<img src=\"http://www.reactiongifs.com/r/wrong-gif.gif\" width=\"300\" height=\"250\">");
			String[] answers = q.getAnswerStrs();
			if(answers.length == 1){
				out.println("<p> The answers are: "+ answers[0]+"</p>");
			}else{
				out.println("<ul>");
				for(int i = 0; i<answers.length; i++){
					out.println("<li>");
					char c = (char) ('a'+i);
					out.println("<p> The answers to "+c+".) are: "+ answers[i] +"</p>");
					out.println("</li>");
				}
				out.println("</ul>");
			}
		}
		out.println("<form action=\"BasicQuizServlet\" method=\"post\">");
		out.println("<input type=\"submit\" value=\"Continue\"></form>");
		out.println("</body>");
		out.println("</html>");
		request.getSession().setAttribute(SHOW_ANSWER, false);
	}

	private void showQuiz(HttpServletRequest request, HttpServletResponse response) throws IOException{
		int quizPage = (Integer) request.getSession().getAttribute(QUIZ_PAGE);
		Quiz quiz = (Quiz) request.getSession().getAttribute(QUIZ);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>Basic Quiz</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<form action=\"BasicQuizServlet\" method=\"post\">");
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
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}
	
	private void showResults(HttpServletRequest request, HttpServletResponse response) throws IOException{
		int numAttempted = (Integer) request.getSession().getAttribute(NUM_ATTEMPTED);
		int numCorrect = (Integer) request.getSession().getAttribute(NUM_CORRECT);
		long startTime = (Long) request.getSession().getAttribute(START_TIME);
		double secondsElapsed = (double)(System.currentTimeMillis()-startTime)/1000;
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>Basic Quiz</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>You got "+numCorrect+" answers out of "+numAttempted+" correct in "+ secondsElapsed +" seconds </h1>");
		out.println("</body>");
		out.println("</html>");
	}
	
	private void checkAnswer(HttpServletRequest request, int i){
		Quiz quiz = (Quiz) request.getSession().getAttribute(QUIZ);
		int numAttempted = (Integer) request.getSession().getAttribute(NUM_ATTEMPTED);
		int numCorrect = (Integer) request.getSession().getAttribute(NUM_CORRECT);
		
		Question q = quiz.getQuestion(i);
		String[] answer = q.getResponses(request, i);
		
		int correct = q.numCorrect(answer);
		request.getSession().setAttribute(NUM_CORRECT, numCorrect+correct);
		int attempted = q.numAttempted();
		request.getSession().setAttribute(NUM_ATTEMPTED, numAttempted+attempted);
	}
}
