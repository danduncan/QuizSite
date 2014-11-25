package quizsite;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import users.*;

import connection.UserConnection;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get requests are invalid. Forward client to homepage.
		// TODO Add address of homepage
		String homePage = "home.jsp";
		RequestDispatcher dispatch = request.getRequestDispatcher(homePage);
		dispatch.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// We need to hash the user's submitted password, then compare the username and password to the database
		Integer userid = getUserID(request);
		
		if (userid == null) {
			// An invalid username/password was submitted. Reject request.
			
			// Count number of times user has failed to log in 
			HttpSession session = request.getSession();
			Integer failedAttempts = (Integer) session.getAttribute("failedAttempts");
			if (failedAttempts == null) {
				failedAttempts = 1;
			} else {
				failedAttempts++;
			}
			session.setAttribute("failedAttempts", failedAttempts);
			session.setAttribute("userid",null);
			session.setAttribute("username",null);
			
			// Forward user to the try-again page
			// TODO Need address for try-again page/servlet
			String tryAgainPage = "tryagain.jsp";
			RequestDispatcher dispatch = request.getRequestDispatcher(tryAgainPage);
			dispatch.forward(request, response);
		} else {
			// Accept request and log the user in
			HttpSession session = request.getSession();
			session.setAttribute("userid",userid);
			session.setAttribute("username",request.getParameter("username"));
			
			//construct userConnection and User object
			ServletContext sc = request.getServletContext();
			DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
			User user = new User(userid, new UserConnection(dc));
			session.setAttribute("user", user);
			
			// TODO Need address for welcome page/servlet
			String welcomePage = "welcomepage.jsp";
			RequestDispatcher dispatch = request.getRequestDispatcher(welcomePage);
			dispatch.forward(request, response);
			
		}
	}
	
	/**
	 * If valid username and password are submitted, returns a userid. If not, returns null.
	 * @param request
	 * @return
	 */
	private Integer getUserID(HttpServletRequest request) {
		// Get username and password
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username == null || password == null) return null;
		
		// Hash the password with a salt
		// Skips hashing if MYDB_LOGINSKIPHASH == true
		String hashpw = password;
		if (!MyDBInfo.MYDB_LOGINSKIPHASH) {
			String saltpw = password + MyDBInfo.MYDB_LOGINSALT;
			hashpw = hexToString(hashString(saltpw));
		}
		
		// Construct query for users database
		String query = "select id from " + MyDBInfo.USERTABLE + " where username = \"" + username + "\" and password = \"" + hashpw + "\" LIMIT 1;";
		//System.out.println("LoginServlet.getUserID() query: " + query);
		
		// Get DatabaseConnection and execute query
		ServletContext sc = getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		if(dc == null) return null;
		
		ResultSet rs = dc.executeQuery(query);
		if (rs == null) return null;
		
		// Extract the userid from rs if present
		boolean hasUserID = false;
		try {
			// Moves cursor to first row of rs. Returns false if rs is empty.
			hasUserID = rs.first();
		} catch(SQLException ignored) {};
		
		if(!hasUserID) return null;
		
		// Extract User ID and return it
		String idstr = null;
		try {
			idstr = rs.getString("id");
		} catch (SQLException ignored) {};
		
		if (idstr == null) return null;
		return Integer.parseInt(idstr);
	}
	
	/**
	 * Hash a string password with the SHA algorithm
	 * @param pwd
	 * @return
	 */
	private byte[] hashString(String pwd) {
		byte[] hashBytes = null;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");

			// Load the string into the MessageDigest and hash it
			md.update(pwd.getBytes());
			hashBytes = md.digest();
		
		} catch (NoSuchAlgorithmException ignored) {};
		
		return hashBytes;
	}
	
	/**
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/**
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}

}
