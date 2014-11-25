package Quiz;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import users.QuizMade;
import users.User;

@WebServlet("/SaveQuizServlet")
public class SaveQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SaveQuizServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Quiz quiz = (Quiz) request.getSession().getAttribute(ShowQuizServlet.QUIZ);
		User user = (User) request.getSession().getAttribute("user");
		quiz.updateDatabase(true, quiz.id);
		user.quizzesmade.add(new QuizMade(user.id,quiz.id));
		
		RequestDispatcher dispatch = request.getRequestDispatcher("savequiz.jsp");
		dispatch.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

}
