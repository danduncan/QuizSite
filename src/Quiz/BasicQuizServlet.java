package Quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BasicQuizServlet")
public class BasicQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Quiz quiz;
	
    public BasicQuizServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		quiz = new Quiz();
		quiz.addQuestions(new FillBlankQuestion("What", "the", "fuck"));
		quiz.addQuestions(new PictureResponseQuestion("http://events.stanford.edu/events/252/25201/Memchu_small.jpg", "Memchu"));
		quiz.addQuestions(new QuestionResponse("What are you doing?", "IDK"));
		String answers[] = {"a", "b", "shit"};
		quiz.addQuestions(new MultipleChoiceQuestion("Favorite letter?", answers, answers[2]));
		showQuiz(response, quiz);
	}

	private void showQuiz(HttpServletResponse response, Quiz quiz) throws IOException{
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>Basic Quiz</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Basic Quiz</h1>");
		out.println("<form action=\"BasicQuizServlet\" method=\"post\">");
		out.println("<ol>");
		List<Question> questions = quiz.getQuestions();
		for(int i = 0; i<questions.size(); i++){
			Question q = questions.get(i);
			out.println("<li>");
			showQuestion(q, out, i);
			out.println("</li>");
		}
		out.println("</ol>");
		out.println("<input type=\"submit\" value=\"Submit Quiz\">");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}
	
	private void showQuestion(Question q, PrintWriter out, int i){
		int type = q.getType();
		if(type == Question.QUESTION_RESPONSE){
			QuestionResponse qr = (QuestionResponse)q;
			qr.printToJSP(out, i);
		}else if(type == Question.FILL_IN_BLANK){
			FillBlankQuestion qfb = (FillBlankQuestion)q;
			qfb.printToJSP(out, i);
		}else if(type == Question.PICTURE_RESPONSE){
			PictureResponseQuestion qpr = (PictureResponseQuestion)q;
			qpr.printToJSP(out, i);
		}else if(type == Question.MULTIPLE_CHOICE){
			MultipleChoiceQuestion qmc = (MultipleChoiceQuestion)q;
			qmc.printToJSP(out, i);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int numCorrect = 0;
		List<Question> questions = quiz.getQuestions();
		for(int i = 0; i< questions.size(); i++){
			Question q = questions.get(i);
			String answer = request.getParameter("Answer"+i);
			if(q.isCorrect(answer)){
				numCorrect++;
			}
		}
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>Basic Quiz</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>You got "+numCorrect+" answers right!!</h1>");
		out.println("</body>");
		out.println("</html>");
	}

}
