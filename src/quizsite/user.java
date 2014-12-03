package quizsite;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user")
public class user extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public user() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requested = request.getParameter("userid");
		Integer requestedUser = null;
		if (requested != null && !requested.isEmpty()) requestedUser = Integer.parseInt(requested);
		Integer currentUser = (Integer) request.getSession().getAttribute("userid");
		
		// Forward to home if input is invalid
		if(requestedUser == null || requestedUser < 1 || requestedUser == currentUser){
			RequestDispatcher dispatch = request.getRequestDispatcher("index.jsp");
			dispatch.forward(request, response);
			return;
		}
		
		// Forward to home if use does not exist
		DatabaseConnection dc = (DatabaseConnection) getServletContext().getAttribute("DatabaseConnection");
		if(dc == null || !userExists(requestedUser,dc)) {
			RequestDispatcher dispatch = request.getRequestDispatcher("index.jsp");
			dispatch.forward(request, response);
			return;
		}
		
		
		RequestDispatcher dispatch = request.getRequestDispatcher("userprofilepage.jsp");
		dispatch.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	
	// Verify this user exists
	private boolean userExists(Integer userid, DatabaseConnection dc) {
		if (userid == null || userid < 0 || dc == null) return false;
		String query = "SELECT * FROM users WHERE id=" + userid + " LIMIT 1";
		ResultSet rs = dc.executeSimultaneousQuery(query);
		try {
			if(rs.first()) return true; // Quiz exists
		} catch (SQLException ignored) {}
		return false;
	}
}
