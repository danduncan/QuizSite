package Quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
		System.out.println(ID);
		ServletContext sc = request.getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		ArrayList<QuestionType> questiontypes =  (ArrayList<QuestionType>) sc.getAttribute("questiontypes"); 
		if (ID != null){
			Quiz quiz = new Quiz(ID, new QuizConnection(dc,questiontypes));
			System.out.println(quiz);
			request.getSession().setAttribute(ShowQuizServlet.QUIZ, quiz);
		}
		
		ScoreManager scoreManager = (ScoreManager) sc.getAttribute("ScoreManager");
		Quiz quiz = (Quiz) request.getSession().getAttribute(ShowQuizServlet.QUIZ);
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\" />");
		out.println("<title>"+"Quiz Homepage"+"</title>");
		out.println("</head>");
		out.println("<body>");
		
		out.println("<h1>"+"Quiz Overview"+"</h1>");
		out.println("Name: "+ quiz.name+"<br>");
		out.println("Description: "+quiz.description+"<br>");
		User user = new User(quiz.authorid, new UserConnection(dc));
		out.println("Creator: "+ user.firstname + " " + user.lastname+"<br>");
		out.println("Date Created: "+quiz.datemade + "<br>");
		out.println("<a href=\"ShowQuizServlet\">Take this quiz!</a><br>");
		
		
		
		out.println("<h1>"+"High scorers"+"</h1>");
		ResultSet rs = scoreManager.getHighScores(quiz.id);
		try {
			out.println("<ol>");
			while(rs.next()){
				int userID = rs.getInt("userid");
				int score = rs.getInt("score");
				int time = rs.getInt("time");
				user = new User(userID, new UserConnection(dc));
				out.println("<li>"+ user.firstname +" "+user.lastname + " scored " + score+ " in " + time+" seconds</li>");
			}
			out.println("</ol>");
		} catch (SQLException ignored){}
		out.println("</body></html>");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
