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
 * Servlet implementation class CheckLoginCredentialsServlet
 */
@WebServlet("/CheckLoginCredentialsServlet")
public class CheckLoginCredentialsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public static final Integer KSUCCESS = 0; // Username is available
	public static final Integer KFAILURE = 1; // Username is taken
	public static final Integer KERROR = 2; // Database or other system error
    public static final String table = MyDBInfo.USERTABLE;   
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckLoginCredentialsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("CheckLoginCredentialsServlet GET was called. Forwarding to POST...");
		doPost(request,response);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println("CheckLoginCredentialsServlet: username=" + username + " and password=" + password);
		
		ServletContext sc = getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		if (username == null || username.isEmpty() || password == null || password.isEmpty() || dc == null) {
			System.out.println("CheckLoginCredentialsServlet.doPost(): Check failed due to null inputs");
			setResponse(response,KERROR);
			return;
		}
		
		Integer status = checkCredentials(username,password,dc);
		setResponse(response,status);
		System.out.println("CheckLoginCredentialsServlet: Status code: " + status);
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

	private static Integer checkCredentials(String username, String password, DatabaseConnection dc) {
		if (username == null || username.isEmpty() || password == null || password.isEmpty() || dc == null) return KERROR;
		password = HashPassword.getHash(password);
		String query = "SELECT username FROM " + table + " WHERE username=\"" + username + "\" AND password=\"" + password + "\" LIMIT 1;";
		System.out.println(query);
		ResultSet rs = dc.executeQuery(query);
		try {
			if (rs.first()) {
				// rs contains results. This means the credentials were valid
				System.out.println("CheckLoginCredentialsServlet: Correct credentials");
				return KSUCCESS;
			} else {
				// rs is empty. The credentials are invalid
				System.out.println("CheckLoginCredentialsServlet: Incorrect credentials");
				return KFAILURE;
			}
		} catch (SQLException e) {
			System.out.println("\tCheckLoginCredentialsServlet: SQL error");
			return KERROR;
		}
	}
}

