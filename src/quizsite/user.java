package quizsite;

import java.io.IOException;

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
		int requestedUser = Integer.parseInt(request.getParameter("userid"));
		int currentUser = (Integer) request.getSession().getAttribute("userid");
		
		if(requestedUser == currentUser){
			RequestDispatcher dispatch = request.getRequestDispatcher("welcomepage.jsp");
			dispatch.forward(request, response);
		}else{
			RequestDispatcher dispatch = request.getRequestDispatcher("userprofilepage.jsp");
			dispatch.forward(request, response);
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

}
