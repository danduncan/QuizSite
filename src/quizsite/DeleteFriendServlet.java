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
 * Servlet implementation class DeleteFriendServlet
 */
@WebServlet({ "/DeleteFriendServlet", "/DeleteFriend" })
public class DeleteFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Constants for success or failure of friend request
	public static final Integer KSUCCESS = 0;
	public static final Integer KFAILURE = 1; // General failure
	public static final Integer KNOTFRIENDS = 2; // Sender and receiver are not friends
	public static final Integer KNOTLOGGEDIN = 3; // User is not logged in
	public static final Integer KSELF = 4; // Somehow, the user tried to add himself

	public static final String friendResponseField = "deleteFriendStatusCode";

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
    public DeleteFriendServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("DeleteFriendServlet GET was called. Forwarding to POST...");
		doPost(request,response);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("DeleteFriendServlet called");
		//printInputs(request);

		// Validate inputs
		String friendidStr = request.getParameter("friendid");
		String useridStr = request.getParameter("userid");
		ServletContext sc = getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		if (friendidStr == null || useridStr == null || friendidStr.isEmpty() || useridStr.isEmpty() || dc == null) {
			System.out.println("DeleteFriendServlet.doPost(): Friend request failed due to null inputs");
			setResponse(response,KFAILURE);
			return;
		}
		Integer friendId = Integer.parseInt(friendidStr);
		Integer userId = Integer.parseInt(useridStr);
		if (friendId == null || userId == null || friendId < 1 || userId < 1) {
			System.out.println("DeleteFriendServlet.doPost(): Friend request failed due to malformed inputs");
			setResponse(response,KFAILURE);
			return;
		}
		
		// Check permissions
		Integer canDelete = canDeleteFriend(userId,friendId,dc);
		if (canDelete != KSUCCESS) {
			System.out.println("DeleteFriendServlet.doPost(): Friend delete failed due to friend permissions, error code " + canDelete + ".");
			setResponse(response,canDelete);
			return;
		}

		HttpSession session = request.getSession();
		// Send the request
		if (deleteFriend(userId,friendId,dc)) {
			System.out.println("\t\tFriend deletion successful!");
			//response.addIntHeader(friendResponseField,KSUCCESS);
			setResponse(response,KSUCCESS);
			return;
		} else {
			System.out.println("\t\tFriend deletion failed during database update");
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
		String friendid = request.getParameter("friendid");
		String userid = request.getParameter("userid");
		if (buttonId!= null) {
			System.out.println("\tbuttonid = " + buttonId);
		} else {
			System.out.println("\tbuttonid is null");
		}
		if (friendid!= null) {
			System.out.println("\tfriendid = " + friendid);
		} else {
			System.out.println("\tfriendid is null");
		}
		if (userid!= null) {
			System.out.println("\tuserid = " + userid);
		} else {
			System.out.println("\tuserid is null");
		}
	}

	// Verify the user is allowed to delete the friend
	private static Integer canDeleteFriend(Integer userId, Integer friendId, DatabaseConnection dc) {
		// Validate inputs
		if(userId == null || friendId == null || userId < 1 || friendId < 1 || dc == null) {
			System.out.println("\tDeleteFriendServlet.canDeleteFriend(): Failed due to bad inputs");
			return KFAILURE;
		}

		// Verify user isn't deleting himself
		if(userId == friendId) {
			System.out.println("\tDeleteFriendServlet.canDeleteFriend(): Failed because userid = receiverid");			
			return KSELF;
		}

		// Verify the two are already friends
		String query1 = "SELECT datefriended FROM " + friendsTable + " WHERE " + colFriend1 + "=" + userId + " AND " + colFriend2 + "=" + friendId + " LIMIT 1;";
		String query2 = "SELECT datefriended FROM " + friendsTable + " WHERE " + colFriend2 + "=" + userId + " AND " + colFriend1 + "=" + friendId + " LIMIT 1;";
		try {
			boolean friends = false;
			ResultSet rs = dc.executeQuery(query1);
			if (rs.first()) {
				friends = true;
			}
			rs = dc.executeQuery(query2);
			if (rs.first()) {
				friends = true;
			}
			if (!friends) return KNOTFRIENDS;
		} catch (SQLException e) {
			System.out.println("\tDeleteFriendServlet.canDeleteFriend(): Failed. SQLException");
			return KFAILURE;
		}

		// User is not deleting himself. User is friends with receiver.
		return KSUCCESS;		
	}

	// Send the friend request. Returns true on success
	private boolean deleteFriend(Integer userid, Integer friendid, DatabaseConnection dc) {
		// Send the deletion
		updateDatabase(userid,friendid,dc);

		// Do we need to update the user like we do for sendFriendRequest?
		
		return true;
	}
	
	private void updateDatabase(Integer userid, Integer friendid, DatabaseConnection dc) {
		// Add message to database
		String base = "DELETE FROM " + this.friendsTable + " WHERE ";
		String query1 = base + "FRIEND1=" + userid + " AND FRIEND2=" + friendid + ";";
		String query2 = base + "FRIEND2=" + userid + " AND FRIEND1=" + friendid + ";";
		dc.executeUpdate(query1);
		dc.executeUpdate(query2);
	}
	
}