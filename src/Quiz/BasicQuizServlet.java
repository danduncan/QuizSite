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
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		Quiz quiz = new Quiz(true, true, true);
		quiz.addQuestion(new FillBlankQuestion("What", "the", "fuck"));
		quiz.addQuestion(new PictureResponseQuestion("http://events.stanford.edu/events/252/25201/Memchu_small.jpg", "Memchu"));
		quiz.addQuestion(new QuestionResponse("What are you doing?", "IDK"));
		String answers[] = {"a", "b", "shit"};
		quiz.addQuestion(new MultipleChoiceQuestion("Favorite letter?", answers, answers[2]));
		
		request.getSession().setAttribute(QUIZ, quiz);
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
			
			if(quiz.getQuizSize()>quizPage){
				showQuiz(request, response);
			}else{
				showResults(request, response);
			}
		}else{
			for(int i = 0; i< quiz.getQuizSize(); i++){
				checkAnswer(request, i);
			}
			showResults(request,response);
		}
	}
	
	private void showAnswer(HttpServletResponse response, HttpServletRequest request) throws IOException{
		int quizPage = (Integer) request.getSession().getAttribute(QUIZ_PAGE);
		Quiz quiz = (Quiz) request.getSession().getAttribute(QUIZ);
		String answer = request.getParameter("Answer"+quizPage);
		Question q = quiz.getQuestion(quizPage);
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>Basic Quiz</title>");
		out.println("</head>");
		out.println("<body>");
		if(q.isCorrect(answer)){
			out.println("<h1>Correct!</h1>");
		}else{
			out.println("<h1>Inorrect!</h1>");
			out.println("<p> The correct answers are: "+ q.getAnswerStr()+"</p>");
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
		String answer = request.getParameter("Answer"+i);
		Question q = quiz.getQuestion(i);
		if(answer!= null && q.isCorrect(answer)){
			request.getSession().setAttribute(NUM_CORRECT, numCorrect+1);
		}
		request.getSession().setAttribute(NUM_ATTEMPTED, numAttempted+1);
	}
}
