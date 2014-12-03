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
import users.Message;
import users.User;

@WebServlet({"/QuizHomepageServlet", "/quiz"})
public class QuizHomepageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public QuizHomepageServlet() {}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String IDStr = (String)request.getParameter("quizid");
		Integer ID = null;
		if (IDStr != null && !IDStr.isEmpty()) ID = Integer.parseInt(IDStr);
		
		// First, forward to homepage if no quiz or invalid quiz specified
		if (ID == null || ID < 0) {
			RequestDispatcher dispatch = request.getRequestDispatcher("index.jsp");
			dispatch.forward(request, response);
			return;
		}
		
		ServletContext sc = request.getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		
		// Next, forward to homepage if quiz does not exist
		if (dc == null || !quizExists(ID,dc)) {
			RequestDispatcher dispatch = request.getRequestDispatcher("index.jsp");
			dispatch.forward(request, response);
			return;
		}
		
		// User has requested a valid quiz. Return it
		ArrayList<QuestionType> questiontypes =  (ArrayList<QuestionType>) sc.getAttribute("questiontypes"); 
		Quiz quiz = new Quiz(ID, new QuizConnection(dc,questiontypes));
		request.getSession().setAttribute(ShowQuizServlet.QUIZ, quiz);
		RequestDispatcher dispatch = request.getRequestDispatcher("quizhomepage.jsp");
		dispatch.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String challenge = request.getParameter("challenge");
		String challengeID = request.getParameter("messagenum");
		User user = (User) request.getSession().getAttribute("user");
		ServletContext sc = request.getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		if(challenge != null && challengeID != null){
			Message.deleteMessage(user, Integer.parseInt(challengeID), dc);
			if (challenge.equals("decline")){
				RequestDispatcher dispatch = request.getRequestDispatcher("welcomepage.jsp");
				dispatch.forward(request, response);
			}
		}
		
		doGet(request,response);
	}
	
	private boolean quizExists(Integer quizid, DatabaseConnection dc) {
		if (quizid == null || quizid < 0 || dc == null) return false;
		String query = "SELECT * FROM quizzes WHERE id=" + quizid + " LIMIT 1";
		ResultSet rs = dc.executeSimultaneousQuery(query);
		try {
			if(rs.first()) return true; // Quiz exists
		} catch (SQLException ignored) {}
		return false;
	}

}
