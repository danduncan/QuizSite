package Quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.UserConnection;

import quizsite.DatabaseConnection;
import quizsite.ScoreManager;
import users.User;

@WebServlet("/QuizHomepageServlet")
public class QuizHomepageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public QuizHomepageServlet() {}

    private void makeTestQuiz(HttpServletRequest request){
    	Quiz quiz = (Quiz)request.getSession().getAttribute(CreateQuizServlet.QUIZ_CREATED);
    	User user = (User)request.getSession().getAttribute("user");
    	
    	if(user == null){
			ServletContext sc = request.getServletContext();
			DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
			user = new User(0, new UserConnection(dc));
    	}
    	request.getSession().setAttribute("user", user);
    	
    	if(quiz == null){
        	quiz = new Quiz(true, true, true, 3);
        	String[] ans1 = new String[]{"Packers", "packers"};
        	String[] ans2 = new String[]{"Badgers", "badgers"};
        	String[] ans3 = new String[]{"bears"};
        	quiz.addQuestion(new MultiAnswerQuestion("Best sports teams?", new String[][]{ans1, ans2, ans3}, false));
        	quiz.addQuestion(new FillBlankQuestion("What", "the", "fuck"));
        	quiz.addQuestion(new PictureResponseQuestion("What is the name of this building?","http://events.stanford.edu/events/252/25201/Memchu_small.jpg", "Memchu"));
        	quiz.addQuestion(new QuestionResponse("What are you doing?", new String[]{"IDK", "big things"}));
        	quiz.addQuestion(new MultipleChoiceQuestion("Favorite letter?", new String[] {"a", "b", "shit"}, "shit"));
        	quiz.addQuestion(new MultiChoiceMultiAnswerQuestion("Favorite letter?", new String[] {"a", "poop", "shit"}, new String[]{"poop","shit"}));	
    	}
    	request.getSession().setAttribute(ShowQuizServlet.QUIZ, quiz);
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		makeTestQuiz(request);
		
		ServletContext sc = request.getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
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
		//out.println("Name: "+ quiz.name);
		//out.println("Description: "+quiz.description);
		//User user = new User(quiz.authorid, new UserConnection(dc));
		//out.println("Creator: "+ user.firstname + " " + user.lastname);
		//out.println("Date Created: "+quiz.datemade);
		out.println("<a href=\"ShowQuizServlet\">Take this quiz!</a>");
		
		
		
		out.println("<h1>"+"High scorers"+"</h1>");
		ResultSet rs = scoreManager.getHighScores(quiz.id);
		try {
			out.println("<ol>");
			while(rs.next()){
				int userID = rs.getInt("userid");
				int score = rs.getInt("score");
				int time = rs.getInt("time");
				User user = new User(userID, new UserConnection(dc));
				out.println("<li>"+ user.firstname +" "+user.lastname + " scored " + score+ " in " + time+" seconds</li>");
			}
			out.println("</ol>");
		} catch (SQLException ignored){}
		out.println("</body></html>");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
