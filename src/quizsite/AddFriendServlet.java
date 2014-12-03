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
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AddFriendServlet
 */
@WebServlet("/AddFriendServlet")
public class AddFriendServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
	
	// Constants for success or failure of friend request
	public static final Integer KSUCCESS = 0;
	public static final Integer KFAILURE = 1; // General failure
	public static final Integer KFRIENDS = 2; // Sender and receiver are already friends
	public static final Integer KNOTLOGGEDIN = 3; // User is not logged in
	public static final Integer KPENDING = 4; // User has already sent a request and it is still pending
	public static final Integer KCONFIRM = 5; // Receiver has sent a request. Confirm it
	public static final Integer KSELF = 6; // Somehow, the user tried to add himself
	
	public static final String friendResponseField = "friendRequestStatusCode";
	
	// Constants for DatabaseConnection
	private static final String messagesTable = MyDBInfo.MESSAGESTABLE;
	private static final String friendsTable = MyDBInfo.FRIENDSTABLE;
	private static final String colFriend1 = "friend1";
	private static final String colFriend2 = "friend2";
	private static final Integer friendRequestType = 0;
	private static final String colMsgType = "type";
	private static final String colSenderId = "senderid";
	private static final String colReceiverId = "receiverid";
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddFriendServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("AddFriendServlet GET was called. Forwarding to POST...");
		doPost(request,response);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("AddFriendServlet called");
		//printInputs(request);

		// Validate inputs
		String receiveridStr = request.getParameter("receiverid");
		String useridStr = request.getParameter("userid");
		ServletContext sc = getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		if (receiveridStr == null || useridStr == null || receiveridStr.isEmpty() || useridStr.isEmpty() || dc == null) {
			System.out.println("AddFriendServlet.doPost(): Friend request failed due to null inputs");
			setResponse(response,KFAILURE);
			return;
		}
		Integer receiverId = Integer.parseInt(receiveridStr);
		Integer userId = Integer.parseInt(useridStr);
		if (receiverId == null || userId == null || receiverId < 1 || userId < 1) {
			System.out.println("AddFriendServlet.doPost(): Friend request failed due to malformed inputs");
			setResponse(response,KFAILURE);
			return;
		}
		
		// Check permissions
		Integer canAdd = canAddFriend(userId,receiverId,dc);
		if (canAdd != KSUCCESS) {
			System.out.println("AddFriendServlet.doPost(): Friend request failed due to friend permissions, error code " + canAdd + ".");
			setResponse(response,canAdd);
			return;
		}

		HttpSession session = request.getSession();
		// Send the request
		if (sendFriendRequest(userId,receiverId,session,dc,sc)) {
			//System.out.println("\t\tFriend addition successful!");
			//response.addIntHeader(friendResponseField,KSUCCESS);
			setResponse(response,KSUCCESS);
			return;
		} else {
			System.out.println("\t\tFriend addition failed during database update");
			//response.addIntHeader(friendResponseField,KFAILURE);
			setResponse(response,KFAILURE);
			return;
		}
	}
	
	// Set the response for either success or failure
	private static void setResponse(HttpServletResponse response, Integer status) {
		response.setContentType("text/XML");
		response.setCharacterEncoding("UTF-8");
		try {
			//response.getWriter().write("<friendRequest:status id=\"friendRequestResponse\" value=\"" + status + "\" />");
			response.getWriter().write(status.toString());
		} catch (Exception ignored) {};
	}
	
	private static void printInputs(HttpServletRequest request) {
		String buttonId = request.getParameter("buttonid");
		String receiverid = request.getParameter("receiverid");
		String userid = request.getParameter("userid");
		if (buttonId!= null) {
			System.out.println("\tbuttonid = " + buttonId);
		} else {
			System.out.println("\tbuttonid is null");
		}
		if (receiverid!= null) {
			System.out.println("\treceiverid = " + receiverid);
		} else {
			System.out.println("\treceiverid is null");
		}
		if (userid!= null) {
			System.out.println("\tuserid = " + userid);
		} else {
			System.out.println("\tuserid is null");
		}
	}

	// Verify the user is allowed to add the receiver 
	private static Integer canAddFriend(Integer userId, Integer receiverId, DatabaseConnection dc) {
		// Validate inputs
		if(userId == null || receiverId == null || userId < 1 || receiverId < 1 || dc == null) {
			System.out.println("\tAddFriendServlet.canAddFriend(): Failed due to bad inputs");
			return KFAILURE;
		}

		// Verify user isn;t adding himself
		if(userId == receiverId) {
			System.out.println("\tAddFriendServlet.canAddFriend(): Failed because userid = receiverid");			
			return KSELF;
		}

		// Verify the two aren't already friends
		String query1 = "SELECT datefriended FROM " + friendsTable + " WHERE " + colFriend1 + "=" + userId + " AND " + colFriend2 + "=" + receiverId + " LIMIT 1;";
		String query2 = "SELECT datefriended FROM " + friendsTable + " WHERE " + colFriend2 + "=" + userId + " AND " + colFriend1 + "=" + receiverId + " LIMIT 1;";
		try {
			ResultSet rs = dc.executeQuery(query1);
			if (rs.first()) {
				System.out.println("\tAddFriendServlet.canAddFriend(): Failed. You are already friends");
				return KFRIENDS;
			}
			rs = dc.executeQuery(query2);
			if (rs.first()) {
				System.out.println("\tAddFriendServlet.canAddFriend(): Failed. You are already friends");
				return KFRIENDS;
			} 
		} catch (SQLException e) {
			System.out.println("\tAddFriendServlet.canAddFriend(): Failed. SQLException");
			return KFAILURE;
		}

		// Check that no friend requests have been sent before
		String query3 = "SELECT id FROM " + messagesTable + " WHERE " + colSenderId + "=" + userId + " AND " + colReceiverId + "=" + receiverId + " AND " + colMsgType + "=" + friendRequestType + " LIMIT 1;";
		String query4 = "SELECT id FROM " + messagesTable + " WHERE " + colReceiverId + "=" + userId + " AND " + colSenderId + "=" + receiverId + " AND " + colMsgType + "=" + friendRequestType + " LIMIT 1;";
		try {
			ResultSet rs = dc.executeQuery(query3);
			if (rs.first()) {
				System.out.println("\tAddFriendServlet.canAddFriend(): Failed. Request already pending");
				return KPENDING;
			}
			rs = dc.executeQuery(query4);
			if (rs.first()) {
				System.out.println("\tAddFriendServlet.canAddFriend(): Failed. Request already received from this person");
				return KCONFIRM;
			}
		} catch (SQLException e) {
			System.out.println("\tAddFriendServlet.canAddFriend(): Failed. SQLException");
			return KFAILURE;
		}

		// User is not adding himself. User is not already friends with receiver. The user and the receiver have not previously sent each other a friend request.
		return KSUCCESS;		
	}

	// Send the friend request. Returns true on success
	private boolean sendFriendRequest(Integer senderid, Integer receiverid, HttpSession session, DatabaseConnection dc, ServletContext sc) {
		// Populate all message fields
		SiteManager sm = (SiteManager) sc.getAttribute("SiteManager");
		Integer msgId = sm.popNextMessageID();
		String date = FormatDateTime.getCurrentSystemDate();
		String time = FormatDateTime.getCurrentSystemTime();
		Boolean opened = false;
		Integer replied = 0;
		String subject = null;
		String body = null;

		// Send the request
		users.Message msg = new users.Message(msgId, friendRequestType, date, time, senderid, receiverid, opened, replied, subject, body);
		updateDatabase(msg,dc);

		// Update the user and return success
		users.User user = (users.User) session.getAttribute("user");		
		if (user != null) user.messages.add(msg);
		return true;
	}
	
	private void updateDatabase(users.Message msg, DatabaseConnection dc) {
		// Add message to database
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT into " + this.messagesTable + " ");
		sb.append("(id, type, datesent, timesent, senderid, receiverid, opened, replied, subject, body) ");
		sb.append("VALUES "); //(1, 2, 3, "20141225", 100, 5);");
		sb.append("(" + msg.id + ", " + msg.type + ", ");
		sb.append("\""+msg.datesent + "\", \"" + msg.timesent + "\", ");
		sb.append(msg.senderid + ", \"" + msg.receiverid + "\", ");
		sb.append(msg.opened + ", " + msg.replied + ", ");
		sb.append("\""+msg.subject + "\", \"" + msg.body + "\");");
		dc.executeUpdate(sb.toString());
	}
	
}
