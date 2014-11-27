package quizsite;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import users.Friend;
import users.Message;
import users.User;

/**
 * Servlet implementation class FriendRequestServlet
 */
@WebServlet("/FriendRequestServlet")
public class FriendRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FriendRequestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String act = request.getParameter("friendreq");
		Integer messageNum = Integer.parseInt(request.getParameter("messageNum"));
		Integer friendID = Integer.parseInt(request.getParameter("friendID"));
		ServletContext sc = request.getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		
		//update message to read
		user.messages.get(messageNum).opened = true;
		Message msg = user.messages.get(messageNum);
		if (act == null) {
		    //no button has been selected
		} else if (act.equals("accept")) {
		    //accept button was pressed
			user.messages.get(messageNum).replied = 1;
			msg.updateRead(dc);
			//ToDo (decide how to handle groups)
			Friend friend = new Friend(friendID,user.id);
			friend.insertToDB(dc);
			
			//forward to successful message send page
			RequestDispatcher dispatch = request.getRequestDispatcher("welcomepage.jsp");
			dispatch.forward(request, response);
			
		} else if (act.equals("decline")) {
		    //decline button was pressed
			user.messages.get(messageNum).replied = 2;
			msg.updateRead(dc);
			
			//forward to successful message send page
			RequestDispatcher dispatch = request.getRequestDispatcher("welcomepage.jsp");
			dispatch.forward(request, response);
			
		} else {
		    //someone has altered the HTML and sent a different value!
		}
		
	}

}
