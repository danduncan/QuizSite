package sharedHtmlGenerators;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import java.sql.ResultSetMetaData;

import quizsite.DatabaseConnection;

public class HtmlUserThumbnailGenerator {
	// Strings for generating the link to a user's profile
	public static final String profileURL = "/QuizSite/profile.jsp";
	public static final String profileQueryString = "id";
	public static final String sendMessageUrl = "sendmessage.jsp";
	
	
	// Strings of class names in the HTML
	public static final String classSearchResults = "usersearchresults";
	public static final String classUserProfileThumbnail = "userprofilethumbnail";
	public static final String classProfilePictureContainer = "profilepicturecontainer";
	public static final String classProfilePicture = "profilepicturethumbnail";
	public static final String classProfileInfo = "profilethumbnailinfo";
	public static final String classProfileName = "profilethumbnailname";
	public static final String classProfileStats = "profilethumbnailstats";
	public static final String classProfileButtons = "profilethumbnailbuttons";
	
	// Button classnames
	public static final String classAddFriendBtn = "addFriendBtn";
	public static final String classAlreadyFriendsBtn = "alreadyFriendsBtn";
	public static final String classSelfBtn = "selfBtn";
	public static final String classLoginBtn = "loginBtn";
	public static final String classPendingBtn = "pendingBtn";
	public static final String classConfirmBtn = "confirmBtn";

	// ResultSet column names
	public static final String colUserId = "id";
	public static final String colUserName = "username";
	public static final String colFirstName = "firstname";
	public static final String colLastName = "lastname";
	public static final String colUserType = "usertype";
	public static final String colTaken = "numtaken";
	public static final String colCreated = "numcreated";
	public static final String colPicture = "profilepicture";
	public static final String colDateJoined = "datejoined";
	
	// Text to include in labels and buttons
	public static final String labelAchievements = "Achievements: ";
	public static final String labelDate = "Member since: ";
	public static final String labelCreated = "Quizzes created: ";
	public static final String labelTaken = "Quizzes taken: ";
	public static final String labelAddFriend = "Add Friend";
	public static final String labelAlreadyFriends = "Friends";
	public static final String labelSelf = "This Is You";
	public static final String labelLogIn = "Log In First";
	public static final String labelPending = "Request Pending";
	public static final String labelConfirm = "Confirm Request";
	public static final String labelSendMessage = "Send a Message";
	
	// URL's for button actions
	public static final String formAction = "post";
	
	// Constants for determining nature of friendship
	private static final int KNOTFRIENDS = 0;
	private static final int KFRIENDS = 1;
	private static final int KSELF = 2; 
	private static final int KNOTLOGGEDIN = 3;
	private static final int KPENDING = 4; // User already has a pending request to this receiver
	private static final int KCONFIRM = 5; // User has already received a request from the receiver
	
	
	public HtmlUserThumbnailGenerator() {
	}
	
	/**
	 * Determine whether the user in the HttpSession is already friends with the current user pointed to by the ResultSet
	 * @param rs
	 * @param dc
	 * @param session
	 * @return
	 */
	private static int checkIfFriends(ResultSet rs, DatabaseConnection dc, HttpSession session) {
		// Check for valid inputs
		if (rs == null || dc == null || session == null) return KNOTFRIENDS;
		
		// First, determine the current user
		Integer currentUserId = null;
		currentUserId = (Integer) session.getAttribute("userid");
		if (currentUserId == null || currentUserId < 1) return KNOTLOGGEDIN;
		
		// Next, determine that we are on a valid entry in the ResultSet
		int row = -1;
		try {
			row = rs.getRow();
		} catch (SQLException e) {
			return KNOTFRIENDS;
		}
		if (row < 1) return KNOTFRIENDS;
		
		// Next, get the user's id
		Integer userId = null;
		try { 
			// Check if the column is null
			String userIdStr = null;
			userIdStr = rs.getString(colUserId);
			if (userIdStr == null) return KNOTFRIENDS;
			
			// Get the value
			userId = rs.getInt(colUserId);
		} catch (SQLException e) {
			// If column label is invalid
			return KNOTFRIENDS;
		}
		
		// Check if this user is viewing himself
		if (currentUserId == userId) return KSELF;
		// Query the database to see if these two users are friends
		String query = "SELECT * FROM friends WHERE friend1 = " + currentUserId + " AND friend2 = " + userId + " LIMIT 1;";
		ResultSet rsFriends = null;
		rsFriends = dc.executeSimultaneousQuery(query);
		if (rsFriends == null) return KNOTFRIENDS;
		
		// If ResultSet contains at least one entry, they are friends
		try {
			if(rsFriends.first()) return KFRIENDS;
		} catch (SQLException e) { return KNOTFRIENDS;}
		
		// Now, check to see if the user has already sent a friend request to the receiver
		String query2 = "SELECT * FROM messages WHERE senderid = " + currentUserId + " AND receiverid = " + userId + " AND type = 0 LIMIT 1;";
		ResultSet rsRequests = null;
		rsRequests = dc.executeSimultaneousQuery(query2);
		if (rsRequests == null) return KNOTFRIENDS;
		
		// If ResultSet contains at least one entry, there is a request pending
		try {
			if(rsRequests.first()) return KPENDING;
		} catch (SQLException e) { return KNOTFRIENDS;}

		// Now, check to see if the user has already received a friend request from the receiver
		String query3 = "SELECT * FROM messages WHERE receiverid = " + currentUserId + " AND senderid = " + userId + " AND type = 0 LIMIT 1;";
		rsRequests = null;
		rsRequests = dc.executeSimultaneousQuery(query3);
		if (rsRequests == null) return KNOTFRIENDS;

		// If ResultSet contains at least one entry, there is a request pending
		try {
			if(rsRequests.first()) return KCONFIRM;
		} catch (SQLException e) { return KNOTFRIENDS;}
		
		// User is logged in, not friends with receiver, and no requests have been sent to or from the receiver
		return KNOTFRIENDS;
	}
	
	
	/**
	 * Given a ResultSet, convert the current row of the ResultSet to a string user thumbnail
	 * @param rs
	 * @param dc
	 * @param session
	 * @return
	 */
	public static String getThumbnail(ResultSet rs, DatabaseConnection dc, HttpSession session) {
		// Verify that inputs are valid
		if (rs == null || dc == null || session == null) return "";
		int row = -1;
		try {
			row = rs.getRow();
		} catch (SQLException e) {
			return "";
		}
		if (row < 1) return "";
		
		// Check if the two users are friends
		int friends = checkIfFriends(rs,dc,session);
		
		// Get the relevant data about the user from rs
		Integer userid = null;
		String username = null;
		String firstName = null;
		String lastName = null;
		Integer quizzesTaken = null;
		Integer quizzesCreated = null;
		String pictureUrl = null;
		String dateJoined = null;
		try {
			// Populate all fields
			userid = rs.getInt("id"); //colUserId);
			username = rs.getString(colUserName);
			firstName = rs.getString(colFirstName);
			lastName = rs.getString(colLastName);
			quizzesTaken = rs.getInt(colTaken);
			quizzesCreated = rs.getInt(colCreated);
			pictureUrl = rs.getString(colPicture);
			dateJoined = rs.getString(colDateJoined);
		} catch (SQLException e) {
			System.out.println("HtmlUserThumbnailGenerator: Invalid column access in ResultSet");
			return "";
		}
		
		// Return empty string if any required fields are null
		if (userid == null || firstName == null || lastName == null || pictureUrl == null || dateJoined == null) return "";
		
		// Return empty string if any required fields are empty
		if ((firstName.isEmpty() && lastName.isEmpty())) return "";
		if (userid < 1) {
			System.out.println("HtmlUserThumbnailGenerator.getHtml(): Error: Got a userid=" + userid + ". User IDs must be >= 1");
			return "";
		}
		
		// Auto-populate any empty fields here:
		String fullName = (firstName.trim() + " " + lastName.trim()).trim();
		
		// Format the user's join date here
		String[] dateJoinedFormatted = quizsite.FormatDateTime.getUserDateTime(dateJoined);
		dateJoined = dateJoinedFormatted[0];
		
		// Generate HTML!
		StringBuilder sb = new StringBuilder();
		String ls = System.getProperty("line.separator");
		
		// Start the profile thumbnail
		sb.append("<div class =\"" + classUserProfileThumbnail + "\">" + ls);
		
		// Add the user's photo
		sb.append("\t<a href=\"" + profileURL + "?" + profileQueryString + "=" + userid + "\">" + ls);
		sb.append("\t\t<div class = \"" + classProfilePictureContainer + "\">" + ls);
		if (!pictureUrl.isEmpty()) sb.append("\t\t\t<img class=\"" + classProfilePicture + "\" src=\"" + pictureUrl + "\" alt=\"" + fullName + "\" />" + ls);
		sb.append("\t\t</div>" + ls);
		sb.append("\t</a>" + ls);
		
		// Add the user's profile info
		sb.append("\t<div class=\"" + classProfileInfo + "\">" + ls);
		sb.append("\t\t<div class=\"" + classProfileName + "\">" + ls);
		sb.append("\t\t\t<a href=\"" + profileURL + "?" + profileQueryString + "=" + userid + "\">" + fullName + "</a>" + ls);
		sb.append("\t\t</div>" + ls);
		sb.append("\t\t<div class=\"" + classProfileStats + "\">" + ls);
		sb.append("\t\t\t<span>" + labelAchievements + "</span>" + ls);
		sb.append("\t\t\t<span>" + labelDate + dateJoined + "</span>" + ls);
		sb.append("\t\t\t<span>" + labelCreated + quizzesCreated + "</span>" + ls);
		sb.append("\t\t\t<span>" + labelTaken + quizzesTaken + "</span>" + ls);
		sb.append("\t\t</div>" + ls);
		sb.append("\t</div>" + ls);
		
		// Add buttons to the profile based on friend status of the users
		String buttonClass = "";
		String buttonText = "";
		sb.append("\t<div class=\"" + classProfileButtons + "\">" + ls);
		if (friends == KFRIENDS) {
			buttonClass = classAlreadyFriendsBtn;
			buttonText = labelAlreadyFriends;
			sb.append("\t\t<input class=\"" + buttonClass + "\" type=\"submit\" value=\"" + buttonText + "\" />" + ls);
		} else if (friends == KSELF) {
			buttonClass = classSelfBtn;
			buttonText = labelSelf;
			sb.append("\t\t<input class=\"" + buttonClass + "\" type=\"submit\" value=\"" + buttonText + "\" />" + ls);
		} else if (friends == KNOTLOGGEDIN) {
			buttonClass = classLoginBtn;
			buttonText = labelLogIn;
			sb.append("\t\t<form action=\"signin.jsp\">" + ls);
			sb.append("\t\t\t<input class=\"" + buttonClass + "\" type=\"submit\" value=\"" + buttonText + "\" />" + ls);
			sb.append("\t\t</form>" + ls);
		} else if (friends == KNOTFRIENDS) {
			buttonClass = classAddFriendBtn;
			buttonText = labelAddFriend;
			String buttonId = "addFriendButtonId-" + userid;
			sb.append("\t\t<input onclick=\"sendFriendRequest(this)\" class=\"" + buttonClass + "\" type=\"submit\" value=\"" + buttonText + "\" id=\"" + buttonId + "\" />" + ls);
		} else if (friends == KPENDING) {
			buttonClass = classPendingBtn;
			buttonText = labelPending;
			sb.append("\t\t<input class=\"" + buttonClass + "\" type=\"submit\" value=\"" + buttonText + "\" />" + ls);
		} else if (friends == KCONFIRM) {
			buttonClass = classConfirmBtn;
			buttonText = labelConfirm;
			sb.append("\t\t<input class=\"" + buttonClass + "\" type=\"submit\" value=\"" + buttonText + "\" />" + ls);
		}
		
		if (friends != KSELF && friends != KNOTLOGGEDIN) {
			// Add a "Send Message" button
			sb.append("\t\t<form method=\"POST\" action=\"" + sendMessageUrl + "\">" + ls);
			sb.append("\t\t\t<input type=\"hidden\" name=\"receiverid\" value=\"" + userid + "\" />" + ls);
			sb.append("\t\t\t<input type=\"hidden\" name=\"receiverUsername\" value=\"" + username + "\" />" + ls);
			sb.append("\t\t\t<input class=\"" + "msgBtn" + "\" type=\"submit\" value=\"" + labelSendMessage + "\" />" + ls);
			sb.append("\t\t</form>" + ls);
		}
		sb.append("\t</div>" + ls);
		
		// Close the profile thumbnail
		sb.append("</div>" + ls);
		
		return sb.toString();
	}
	
	/**
	 * Given a ResultSet, convert all its entries into one big set of user profile thumbnails
	 */
	public static String getHtml(ResultSet rs, DatabaseConnection dc, HttpSession session) {
		try {
			if (!rs.first()) return "";
		} catch (SQLException e) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder("");
		String ls = System.getProperty("line.separator");
		sb.append("<div class=\"" + classSearchResults + "\">" + ls);
		
		try {
			rs.beforeFirst();
			while (true) {
				rs.next();
				sb.append(getThumbnail(rs,dc,session));
				
				if (rs.isLast()) break;
			}
		} catch (SQLException e) {
			System.out.println("HtmlUserThumbnailGenerator.getHtml(): Error occurred partway through HTML generation. Returning partial results.");
			sb.append("</div>" + ls);
			return sb.toString();
		}
		
		sb.append("</div>" + ls);
		sb.append("<script src=\"/QuizSite/sharedScripts/friendButton.js\"></script>" + ls);
		//System.out.println(sb.toString());
		return sb.toString();
	}
}
