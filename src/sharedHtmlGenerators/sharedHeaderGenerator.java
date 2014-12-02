package sharedHtmlGenerators;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import connection.UserConnection;
import quizsite.DatabaseConnection;
import users.User;

public class sharedHeaderGenerator {
	private static String localPath = "/sharedHTML/";
	private static String filePublic = "sharedheaderpublic.html";
	private static String fileSignedIn = "sharedHeadersignedin.html";
	private static String fileNewMessage = "sharedNewMessage.html";
	private static final String ls = System.getProperty("line.separator");
	
	public sharedHeaderGenerator() {
	}
	
	public static String getHTML(String rootPath, HttpSession session) {
		return getHTML(rootPath,session,null);
	}
	
	public static String getHTML(String rootPath, HttpSession session, DatabaseConnection dc) {
		if(dc != null) System.out.println("\tSharedHeaderGenerator.getHTML(): Successfully received DatabaseConnection");
		users.User usr = (users.User) session.getAttribute("user");
		
		String username = null;
		Integer userid = null;
		
		if (usr == null) {
			username = (String) session.getAttribute("username");
			userid = (Integer) session.getAttribute("userid");
			
			if (dc != null) {
				// Try to populate usr
				usr = getUserFromID(userid,dc);
				if (usr == null) usr = getUserFromUsername(username,dc);
				if (usr != null) session.setAttribute("user",usr);
			}
			
			
		} else if (usr.username != null && !usr.username.isEmpty() && usr.id != null && usr.id > 0) {
			username = usr.username;
			userid = usr.id;
			session.setAttribute("username",username);
			session.setAttribute("userid",userid);
		}
		
		StringBuilder sb = new StringBuilder();
		if (username != null && !username.equals("") && userid != null && userid > 0) {
			String firstName = username;
			if (usr != null && usr.firstname != null && !usr.firstname.isEmpty()) {
				firstName = usr.firstname;
			} else if (session.getAttribute("firstName") != null) {
				firstName = (String) session.getAttribute("firstName");
			} else if (session.getAttribute("firstname") != null) {
				firstName = (String) session.getAttribute("firstname");
			}
			sb.append(getSignedInHeader(rootPath,firstName,userid));
			sb.append("<script> useridjs = " + userid.toString() + "; </script>");
		} else {
			sb.append(getPublicHeader(rootPath));
			sb.append("<script> useridjs = null; </script>");
		}
		sb.append(ls + sharedHtmlGenerator.getHTML(rootPath + localPath + fileNewMessage));
		return sb.toString();
	}
	
	private static String getSignedInHeader(String rootPath, String username, Integer userid) {
		String header = sharedHtmlGenerator.getHTML(rootPath + localPath + fileSignedIn);
		
		header = header.replace("??USERNAME??",username);
		header = header.replace("??USERID??",userid.toString());
		
		return header;
	}
	
	private static String getPublicHeader(String rootPath) {
		return sharedHtmlGenerator.getHTML(rootPath + localPath + filePublic);
	}
	
	// Get user from the userid
	private static User getUserFromID(Integer userid, DatabaseConnection dc) {
		if (userid == null || userid < 1 || dc == null) return null;
		User usr = null;
		usr = new User(userid,new UserConnection(dc));
		
		// Validate usr
		if (usr.id == null || usr.id < 1 || usr.username == null || usr.username.isEmpty()) return null;
		return usr;
	}
	
	private static User getUserFromID(String userid, DatabaseConnection dc) {
		if (userid == null || userid.isEmpty() || dc == null) return null;
		Integer id = Integer.parseInt(userid);
		
		return getUserFromID(id,dc);
	}
	
	// Get the user from the username
	private static User getUserFromUsername(String username, DatabaseConnection dc) {
		if (username == null || username.isEmpty() || dc == null) return null;
		
		// First, get the user's userid
		String query = "SELECT * FROM users WHERE username=\"" + username + "\" LIMIT 1";
		ResultSet rs = null;
		rs = dc.executeQuery(query);
		Integer userid = null;
		try {
			if(!rs.first()) return null;
			userid = rs.getInt("id");
		} catch (SQLException e) {
			return null;
		}
		
		return getUserFromID(userid.toString(),dc);
	}
}
