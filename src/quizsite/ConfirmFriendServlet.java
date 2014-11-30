
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
 * Servlet implementation class ConfirmFriendServlet
 */
@WebServlet("/ConfirmFriendServlet")
public class ConfirmFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final Integer KSUCCESS = 0;
	public static final Integer KFAILURE = 1; // General failure
	public static final Integer KFRIENDS = 2; // Sender and receiver are already friends
	public static final Integer KNOTLOGGEDIN = 3; // User is not logged in
	public static final Integer KNOTPENDING = 4; // There was no pending rquest to confirm
	public static final Integer KSELF = 5; // Somehow, the user tried to add himself

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
    public ConfirmFriendServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("AddFriendServlet GET was called. Forwarding to POST...");
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("ConfirmFriendServlet called");
		//printInputs(request);

		// Validate inputs
		String senderidStr = request.getParameter("senderid");
		String useridStr = request.getParameter("userid");
		ServletContext sc = getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		if (senderidStr == null || useridStr == null || senderidStr.isEmpty() || useridStr.isEmpty() || dc == null) {
			System.out.println("ConfirmFriendServlet.doPost(): Confirm failed due to null inputs");
			setResponse(response,KFAILURE);
			return;
		}
		Integer senderId = Integer.parseInt(senderidStr);
		Integer userId = Integer.parseInt(useridStr);
		if (senderId == null || userId == null || senderId < 1 || userId < 1) {
			System.out.println("ConfirmFriendServlet.doPost(): Confirm failed due to malformed inputs");
			setResponse(response,KFAILURE);
			return;
		}

		// Check permissions
		Integer canConfirm = canConfirmFriend(userId,senderId,dc);
		if (canConfirm != KSUCCESS) {
			System.out.println("ConfirmFriendServlet.doPost(): Confirm failed due to friend permissions, error code " + canConfirm + ".");
			setResponse(response,canConfirm);
			return;
		}

		// Send the request
		if (confirmFriendRequest(userId,senderId,dc)) {
			//System.out.println("\t\tConfirmFriendServlet: Confirmation successful!");
			setResponse(response,KSUCCESS);
			return;
		} else {
			System.out.println("\t\tConfirmFriendServlet: Confirmation failed during database update");
			setResponse(response,KFAILURE);
			return;
		}
	}
	
	// Set the response for either success or failure
	private static void setResponse(HttpServletResponse response, Integer status) {
		response.setContentType("text/XML");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(status.toString());
		} catch (Exception ignored) {};
	}

	private static void printInputs(HttpServletRequest request) {
		String senderid = request.getParameter("senderid");
		String userid = request.getParameter("userid");
		if (senderid!= null) {
			System.out.println("\treceiverid = " + senderid);
		} else {
			System.out.println("\tsenderid is null");
		}
		if (userid!= null) {
			System.out.println("\tuserid = " + userid);
		} else {
			System.out.println("\tuserid is null");
		}
	}

	// Verify the user is allowed to add the receiver 
	private static Integer canConfirmFriend(Integer userId, Integer senderId, DatabaseConnection dc) {
		// Validate inputs
		if(userId == null || senderId == null || userId < 1 || senderId < 1 || dc == null) {
			System.out.println("\tConfirmFriendServlet.canConfirmFriend(): Failed due to bad inputs");
			return KFAILURE;
		}

		// Verify user isn't adding himself
		if(userId == senderId) {
			System.out.println("\tConfirmFriendServlet.canConfirmFriend(): Failed because userid = receiverid");			
			return KSELF;
		}

		// Verify the two aren't already friends
		String query1 = "SELECT datefriended FROM " + friendsTable + " WHERE " + colFriend1 + "=" + userId + " AND " + colFriend2 + "=" + senderId + " LIMIT 1;";
		String query2 = "SELECT datefriended FROM " + friendsTable + " WHERE " + colFriend2 + "=" + userId + " AND " + colFriend1 + "=" + senderId + " LIMIT 1;";
		try {
			ResultSet rs = dc.executeSimultaneousQuery(query1);
			if (rs.first()) {
				System.out.println("\tConfirmFriendServlet.canConfirmFriend(): Failed. You are already friends");
				return KFRIENDS;
			}
			rs = dc.executeSimultaneousQuery(query2);
			if (rs.first()) {
				System.out.println("\tConfirmFriendServlet.canConfirmFriend(): Failed. You are already friends");
				return KFRIENDS;
			} 
		} catch (SQLException e) {
			System.out.println("\tConfirmFriendServlet.canConfirmFriend(): Failed. SQLException");
			return KFAILURE;
		}

		// Check that a friend request exists
		String query3 = "SELECT id FROM " + messagesTable + " WHERE " + colReceiverId + "=" + userId + " AND " + colSenderId + "=" + senderId + " AND " + colMsgType + "=" + friendRequestType + " LIMIT 1;";
		try {
			ResultSet rs = dc.executeQuery(query3);
			if (!rs.first()) {
				System.out.println("\tConfirmFriendServlet.canConfirmFriend(): Failed. No request pending");
				return KNOTPENDING;
			}
		} catch (SQLException e) {
			System.out.println("\tConfirmFriendServlet.canConfirmFriend(): Failed. SQLException");
			return KFAILURE;
		}

		// User is not adding himself. User is not already friends with receiver. The user has gotten a friend request from the receiver
		return KSUCCESS;		
	}

	// Send the friend request. Returns true on success
	private boolean confirmFriendRequest(Integer userid, Integer senderid, DatabaseConnection dc) {
		// Populate all fields
		String timestamp = "\"" + FormatDateTime.getCurrentSystemDateTime() + "\"";
		String group = "null";
		
		updateDatabase(userid,senderid,timestamp,group,dc);
		
		return true;
	}
	
	private void updateDatabase(Integer userid, Integer senderid, String timestamp, String group, DatabaseConnection dc) {
		// Build queries to create friendship
		String update1 = "INSERT INTO " + friendsTable + " VALUES (" + userid + "," + senderid + "," + timestamp + "," + group + ");";
		//System.out.println("ConfirmFriendServlet: Adding friendship with query1 = \"" + update1 + "\"");
		String update2 = "INSERT INTO " + friendsTable + " VALUES (" + senderid + "," + userid + "," + timestamp + "," + group + ");";
		//System.out.println("ConfirmFriendServlet: Adding friendship with query2 = \"" + update1 + "\"");
		
		// Build query to delete friend request
		String update3 = "DELETE FROM " + messagesTable + " WHERE senderid = " + senderid + " AND receiverid = " + userid + " AND type = " + friendRequestType + ";";
		
		// Send the request
		dc.executeUpdate(update1);
		dc.executeUpdate(update2);
		dc.executeUpdate(update3);
	}
}


