package sharedHtmlGenerators;

import javax.servlet.http.HttpSession;

public class sharedHeaderGenerator {
	private static String localPath = "/sharedHTML/";
	private static String filePublic = "sharedheaderpublic.html";
	private static String fileSignedIn = "sharedHeadersignedin.html";
	
	public sharedHeaderGenerator() {
	}
	
	public static String getHTML(String rootPath, HttpSession session) {
		String username = (String) session.getAttribute("username");
		Integer userid = (Integer) session.getAttribute("userid");
		
		if (username != null && !username.equals("") && userid != null && userid >= 0) {
			return getSignedInHeader(rootPath,username,userid);
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
