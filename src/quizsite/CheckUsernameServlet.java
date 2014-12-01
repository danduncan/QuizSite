package quizsite;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CheckUsernameServlet
 */
@WebServlet("/CheckUsernameServlet")
public class CheckUsernameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final Integer KAVAILABLE = 0; // Username is available
	public static final Integer KTAKEN = 1; // Username is taken
	public static final Integer KERROR = 3; // Database or other system error
    public static final String table = MyDBInfo.USERTABLE;   
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckUsernameServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("CheckUsernameServlet GET was called. Forwarding to POST...");
		doPost(request,response);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		ServletContext sc = getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		if (username == null || username.isEmpty() || dc == null) {
			System.out.println("CheckUsernameServlet.doPost(): Check failed due to null inputs");
			setResponse(response,KERROR);
			return;
		}
		
		// Check if username is taken
		Integer status = usernameTaken(username,dc);
		setResponse(response,status);
		return;
	}

	// Set the response for either success or failure
	private static void setResponse(HttpServletResponse response, Integer status) {
		response.setContentType("text/XML");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(status.toString());
		} catch (Exception ignored) {};
	}
	
	private static Integer usernameTaken(String username, DatabaseConnection dc) {
		if (username == null || username.isEmpty() || dc == null) return KERROR;
		String query = "SELECT username FROM " + table + " WHERE username=\"" + username + "\" LIMIT 1;";
		//System.out.println(query);
		ResultSet rs = dc.executeQuery(query);
		try {
			if (rs.first()) {
				// rs contains results. This means the username is taken
				//System.out.println("username "+ username +" is taken");
				return KTAKEN;
			} else {
				// rs is empty. The username is available
				//System.out.println("username "+ username +" is available");
				return KAVAILABLE;
			}
		} catch (SQLException e) {
			System.out.println("\tCheckUsernameServlet: SQL error");
			return KERROR;
		}
	}
}
