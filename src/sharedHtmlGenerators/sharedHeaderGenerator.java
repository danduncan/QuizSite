package sharedHtmlGenerators;

import javax.servlet.http.HttpSession;

public class sharedHeaderGenerator {
	private static String localPath = "/sharedHTML/";
	private static String filePublic = "sharedheaderpublic.html";
	private static String fileSignedIn = "sharedHeadersignedin.html";
	
	public sharedHeaderGenerator() {
	}
	
	public static String getHTML(String rootPath, HttpSession session) {
		users.User usr = (users.User) session.getAttribute("user");
		
		String username = null;
		Integer userid = null;
		
		if (usr == null) {
			username = (String) session.getAttribute("username");
			userid = (Integer) session.getAttribute("userid");
		} else if (usr.username != null && !usr.username.isEmpty() && usr.id != null && usr.id > 0) {
			username = usr.username;
			userid = usr.id;
			session.setAttribute("username",username);
			session.setAttribute("userid",userid);
		}
		
		if (username != null && !username.equals("") && userid != null && userid > 0) {
			String firstName = username;
			if (usr != null && usr.firstname != null && !usr.firstname.isEmpty()) {
				firstName = usr.firstname;
			} else if (session.getAttribute("firstName") != null) {
				firstName = (String) session.getAttribute("firstName");
			} else if (session.getAttribute("firstname") != null) {
				firstName = (String) session.getAttribute("firstname");
			}
			
			return getSignedInHeader(rootPath,firstName,userid);
		} else {
			return getPublicHeader(rootPath);
		}
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
}
