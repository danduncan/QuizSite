package quizsite;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import connection.UserConnection;

import users.User;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/CreateServlet")
public class CreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get database connection
		ServletContext sc = request.getServletContext();
		HttpSession session = request.getSession();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		
		//get entered username
		String name = request.getParameter("username");

		//determine if username is available
		if (openUsername(name, dc)){
			String pswd = request.getParameter("password");
			String firstname = request.getParameter("firstname");
			String lastname = request.getParameter("lastname");
			String email = request.getParameter("email");
			String profpic = request.getParameter("profilepicture");
			
			//todo need next userid, hashed password
			User user = new User(new UserConnection(dc));
			user.username = name;
			user.password = pswd;
			user.firstname = firstname;
			user.lastname = lastname;
			user.email = email;
			user.profilepicture = profpic;
			//randomly choose id
			Random generator = new Random(); 
			int i = generator.nextInt(1000) + 1;
			user.id = i;
			user.numcreated = 0;
			user.numtaken = 0;
			user.numtakenpractice = 0;
			user.highscores = 0;
			user.numfriends = 0;
			Date date = new Date();
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			user.datejoined = df.format(date);
			//create inserts into database as opposed to updates it
			user.create = true;
			user.updateUserDatabase();
			
			session.setAttribute("user", user);
			
			RequestDispatcher dispatch = 
				request.getRequestDispatcher("welcomepage.jsp");
				dispatch.forward(request, response);
			
			
		} else {
			RequestDispatcher dispatch = 
				request.getRequestDispatcher("nameinuse.jsp");
				dispatch.forward(request, response);
		}
		
		
	}
	//determine if username is open
	private boolean openUsername(String name, DatabaseConnection dc){
		ResultSet rs = dc.executeQuery("SELECT username FROM users WHERE ( username = \""+ name + "\")");		
		try {
			while(rs.next()){
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

}
