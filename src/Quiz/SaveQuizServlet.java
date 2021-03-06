package Quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizsite.DatabaseConnection;

import users.*;

@WebServlet("/SaveQuizServlet")
public class SaveQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SaveQuizServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Quiz quiz = (Quiz) request.getSession().getAttribute(ShowQuizServlet.QUIZ);
		User user = (User) request.getSession().getAttribute("user");
		
		if(user == null){
			RequestDispatcher dispatch = request.getRequestDispatcher("home.jsp");
			dispatch.forward(request, response);
			return;
		}
		
		quiz.updateDatabase(true, quiz.id);
		user.quizzesmade.add(0, new QuizMade(user.id,quiz.id));
		//user.quizzesmade.add(new QuizMade(user.id,quiz.id));
		user.updateUserDatabase();
		request.getSession().setAttribute("user", user);
		RequestDispatcher dispatch = request.getRequestDispatcher("savequiz.jsp");
		dispatch.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

}
