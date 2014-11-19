package quizsite;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CreateMessageServlet
 */
@WebServlet("/CreateMessageServlet")
public class CreateMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Table where messages are held
	private static final String table = MyDBInfo.MESSAGESTABLE;
	
	// Required parameter names to be included in the request:
	private static final String messageType = "type";
	private static final String messageReceiverUsername = "receiverUsername";
	private static final String messageSubject = "subject";
	private static final String messageBody = "body";
	
	// Message parameters that are not required:
	//private static final String messageDate = "datesent";
	//private static final String messageTime = "timesent";
	//private static final String messageSenderID = "senderid";
	//private static final String messageReceiverID = "receiverid";
	//private static final String messageOpened = "opened";
	//private static final String messageReplied = "replied";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateMessageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get requests are invalid. Forward client to homepage.
		// TODO Add address of homepage
		String homePage = "";
		RequestDispatcher dispatch = request.getRequestDispatcher(homePage);
		dispatch.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get the site manager and database connection
		ServletContext sc = getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		SiteManager sm = (SiteManager) sc.getAttribute("SiteManager");
		HttpSession session = request.getSession();
		if (dc == null || sm == null || session == null) return;
		
		// Get the message info sent with post request
		Integer type = Integer.parseInt(request.getParameter(messageType));
		String subject = request.getParameter(messageSubject);
		String body = request.getParameter(messageBody);

		// Get auto-generated message components
		Integer id = sm.popNextMessageID();
		String date = FormatDateTime.getCurrentSystemDate();
		String time = FormatDateTime.getCurrentSystemTime();
		Boolean opened = false;
		Integer replied = 0;
		
		// Get ID of sender
		users.User user = (users.User) session.getAttribute("user");
		Integer senderid = user.id;
		
		// Get ID of receiver
		String receiverUsername = request.getParameter(messageReceiverUsername);
		int receiverid = getUserIDFromDatabase(receiverUsername, dc);
		
		// Construct the new Message
		users.Message msg = new users.Message(id, type, date, time, senderid, receiverid, opened, replied, subject, body);
		
		// Update the database
		updateDatabase(msg,dc);
		
		// Add message to user
		user.messages.add(msg);
	}
	
	private int getUserIDFromDatabase(String username, DatabaseConnection dc) {
		if (username == null) return -1;
		String query = "SELECT id from " + MyDBInfo.USERTABLE + " WHERE username = \"" + username + "\";";
		ResultSet rs = dc.executeQuery(query);
		int receiverid = 0;
		try {
			if (!rs.first()) return -1;
			receiverid = rs.getInt("id");
			//if (receiverid == -1) return -1; // Be careful. 0 is a valid userid
		} catch (SQLException ignored) {
			return -1;
		}
		return -1;
	}
	
	private void updateDatabase(users.Message msg, DatabaseConnection dc) {
		// Add message to database
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT into " + this.table + " ");
		sb.append("(id, type, datesent, timesent, senderid, receiverid, opened, replied, subject, body) ");
		sb.append("VALUES "); //(1, 2, 3, "20141225", 100, 5);");
		sb.append("(" + msg.id + ", " + msg.type + ", ");
		sb.append(msg.datesent + ", " + msg.timesent + ", ");
		sb.append(msg.senderid + ", " + msg.receiverid + ", ");
		sb.append(msg.opened + ", " + msg.replied + ", ");
		sb.append(msg.subject + ", " + msg.body + ");");
		dc.executeUpdate(sb.toString());
	}

}
