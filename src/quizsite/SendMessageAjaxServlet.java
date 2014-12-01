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
 * Servlet implementation class SendMessageAjaxServlet
 */
@WebServlet("/SendMessageAjaxServlet")
public class SendMessageAjaxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public static final Integer KSENT = 0; // Message sent successfully
	public static final Integer KDOESNOTEXIST = 1; // One or more users does not exist
	public static final Integer KERROR = 3; // Database or other system error
    public static final String userTable = MyDBInfo.USERTABLE;   
	public static final String msgTable = MyDBInfo.MESSAGESTABLE;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMessageAjaxServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("SendMessageAjaxServlet GET was called. Forwarding to POST...");
		doPost(request,response);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("SendMessageAjaxServlet called");
		// Get all message parameters
		
		// Get the sender
		users.User sender = (users.User) request.getSession().getAttribute("user");
		Integer senderid = null;
		if (sender == null) {
			String sidStr = request.getParameter("senderid");
			if (sidStr != null && !sidStr.isEmpty()) {
				senderid = Integer.parseInt(sidStr);
			}
		} else {
			senderid = sender.id;
		}
		if (senderid == null) {
			// No sender info was provided
			System.out.println("SendMessageAjaxServlet: Send failed because senderid is unknown");
			setResponse(response,KERROR);
			return;
		}

		// Username may contain multiple '+' delimited usernames. Will need to parse
		String username = request.getParameter("username");
		String subject = request.getParameter("subject");
		String body = request.getParameter("body");
		String type = request.getParameter("type");
		
		ServletContext sc = getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		SiteManager sm = (SiteManager) sc.getAttribute("SiteManager");

		if (username == null || username.isEmpty() || type == null || type.isEmpty() || dc == null || sm == null) {
			System.out.println("SendMessageAjaxServlet.doPost(): Send failed due to null inputs");
			setResponse(response,KERROR);
			return;
		}
		System.out.println("SendMessageAjax: Username string received: " + username);
		
		// Autopopulate null subject and body
		Integer msgType = Integer.parseInt(type);
		if (msgType < 0 || msgType > 2) {
			System.out.println("SendMessageAjaxServlet.doPost(): Send failed due to invalid msgType=" + msgType);
			setResponse(response,KERROR);
			return;
		}
		if (subject == null) subject = "";
		if (body == null) body = "";
		
		// Parse usernames
		//String delimiter = "\\+"; // For + sign delimiter
		String delimiter = "\\s+";
		String[] usernames = username.split(delimiter);
		
		// Get array of userid's corresponding to each username
		// An id of -1 means the username was invalid
		// An id of -2 means there was a SQL exception
		Integer[] ids = getUserIDs(usernames,dc);
		
		StringBuilder sbt = new StringBuilder();
		sbt.append("All userids[] = { ");
		for (int i = 0; i < ids.length; i++) {
			sbt.append(ids[i] + " ");
		}
		sbt.append("}");
		System.out.println(sbt.toString());
		
		// Check for errors 
		if (ids[0] <= -2) {
			setResponse(response,KERROR);
			return;
		} else if (ids[0] < 1) {
			setResponse(response,KDOESNOTEXIST);
			return;
		}
		
		// User(s) exist. Create message for each user
		String date = FormatDateTime.getCurrentSystemDate();
		String time = FormatDateTime.getCurrentSystemTime();
		Boolean opened = false;
		Integer replied = 0;
		
		
		for (int i = 0; i < ids.length; i++) {
			// Construct the new Message
			Integer msgId = sm.popNextMessageID();
			users.Message msg = new users.Message(msgId, msgType, date, time, senderid, ids[i], opened, replied, subject, body);
			
			// Update the database
			updateDatabase(msg,dc);
			
			// Add message to user
			if(sender != null) sender.messages.add(msg);
		}
		
		setResponse(response,KSENT);
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
	
	// Get userid's from array of usernames
	private static Integer[] getUserIDs(String[] usernames,DatabaseConnection dc) {
		Integer[] userIDs = new Integer[usernames.length];
		for (int i = 0; i < userIDs.length; i++) {
			userIDs[i] = getUserID(usernames[i],dc);
			if (userIDs[i] < 1) {
				userIDs[0] = userIDs[i]; // Sets flag that calling fcn can easily check
				break;
			}
		}
		return userIDs;		
	}
	
	// Check an individual username against the database
	private static Integer getUserID(String username, DatabaseConnection dc) {
		if (username == null || username.isEmpty()) return -1;
		String query = "SELECT id FROM " + userTable + " WHERE username=\"" + username + "\" LIMIT 1";
		System.out.println("SendMsgAjax query= \"" + query + "\"");
		ResultSet rs = dc.executeQuery(query);
		try {
			if(rs.first()) {
				return rs.getInt("id");
			} else {
				return -1;
			}
		} catch (SQLException e) {
			return -2;
		}
	}
	
	private void updateDatabase(users.Message msg, DatabaseConnection dc) {
		// Add message to database
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT into " + this.msgTable + " ");
		sb.append("(id, type, datesent, timesent, senderid, receiverid, opened, replied, subject, body) ");
		sb.append("VALUES "); //(1, 2, 3, "20141225", 100, 5);");
		sb.append("(" + msg.id + ", " + msg.type + ", ");
		sb.append("\""+msg.datesent + "\", \"" + msg.timesent + "\", ");
		sb.append(msg.senderid + ", \"" + msg.receiverid + "\", ");
		sb.append(msg.opened + ", " + msg.replied + ", ");
		sb.append("\""+msg.subject + "\", \"" + msg.body + "\");");
		dc.executeUpdate(sb.toString());
		System.out.println("SendMessageAjax - Updating table with new message:");
		System.out.println("\t" + sb.toString());
	}
}
