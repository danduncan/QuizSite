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

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/CreateServlet")
public class CreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
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
		//get database connection
		ServletContext sc = request.getServletContext();
		DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
		
		//get entered username and password
		String name = request.getParameter("name");
		String pswd = request.getParameter("pswd");		
		
		if (openUsername(name, dc)){
			RequestDispatcher dispatch = 
				request.getRequestDispatcher("createprofile.jsp");
				dispatch.forward(request, response);
			
		} else {
			RequestDispatcher dispatch = 
				request.getRequestDispatcher("nameinuse.jsp");
				dispatch.forward(request, response);
		}
	}
	//determine if username is open
	private boolean openUsername(String name, DatabaseConnection dc){
		ResultSet rs = dc.executeQuery("SELECT username FROM users WHERE ( username = \""+ name + "\")");		
		try {
			while(rs.next()){
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

}
