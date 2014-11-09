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
    
	
	// these should end up not as private instance vars
	private Quiz quiz;
	private int quizPage;
	private int numCorrect;
	private int numAttempted;
	private boolean showAnswer;
	private long quizStartTime;
	private long quizEndTime;
	
    public BasicQuizServlet() {
		quiz = new Quiz(true, false, false);
		quiz.addQuestion(new FillBlankQuestion("What", "the", "fuck"));
		quiz.addQuestion(new PictureResponseQuestion("http://events.stanford.edu/events/252/25201/Memchu_small.jpg", "Memchu"));
		quiz.addQuestion(new QuestionResponse("What are you doing?", "IDK"));
		String answers[] = {"a", "b", "shit"};
		quiz.addQuestion(new MultipleChoiceQuestion("Favorite letter?", answers, answers[2]));
        
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
        quizPage = 0;
        numCorrect = 0;
        numAttempted = 0;
        showAnswer = false;
    	quizStartTime = System.currentTimeMillis();
		showQuiz(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(quiz.isManyPages()){
			if(quiz.wantsImmediateCorrection()){
				if(showAnswer){
					checkAnswer(request, quizPage);
					showAnswer(response, request);
					quizPage++;
					return;
				}
			}else{
				checkAnswer(request, quizPage);
				quizPage++;
			}
			
			if(quiz.getQuizSize()>quizPage){
				showQuiz(request, response);
			}else{
				showResults(response);
			}
		}else{
			for(int i = 0; i< quiz.getQuizSize(); i++){
				checkAnswer(request, i);
			}
			showResults(response);
		}
	}
	
	private void showAnswer(HttpServletResponse response, HttpServletRequest request) throws IOException{
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
			out.println("<p> The correct answer is: "+q.getAnswer()+"</p>");
		}
		out.println("<form action=\"BasicQuizServlet\" method=\"post\">");
		out.println("<input type=\"submit\" value=\"Continue\"></form>");
		out.println("</body>");
		out.println("</html>");
		showAnswer = false;
	}

	private void showQuiz(HttpServletRequest request, HttpServletResponse response) throws IOException{
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
				showAnswer = true;	
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
	
	private void showResults(HttpServletResponse response) throws IOException{
		quizEndTime = System.currentTimeMillis();
		double secondsElapsed = (double)(quizEndTime-quizStartTime)/1000;
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
		String answer = request.getParameter("Answer"+i);
		Question q = quiz.getQuestion(i);
		if(answer!= null && q.isCorrect(answer)) numCorrect++;
		numAttempted++;
	}
}
