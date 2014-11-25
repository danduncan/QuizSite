package Quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.QuizConnection;
import connection.UserConnection;

import quizsite.DatabaseConnection;
import quizsite.ScoreManager;
import users.User;

@WebServlet("/QuizHomepageServlet")
public class QuizHomepageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public QuizHomepageServlet() {}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer ID = Integer.parseInt((String)request.getParameter("quizid"));
		if (ID != null){
			ServletContext sc = request.getServletContext();
			DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
			ArrayList<QuestionType> questiontypes =  (ArrayList<QuestionType>) sc.getAttribute("questiontypes"); 
			Quiz quiz = new Quiz(ID, new QuizConnection(dc,questiontypes));
			request.getSession().setAttribute(ShowQuizServlet.QUIZ, quiz);
		}
		
		RequestDispatcher dispatch = request.getRequestDispatcher("quizhomepage.jsp");
		dispatch.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

}
