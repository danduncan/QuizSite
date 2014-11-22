<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% 
	ServletContext sc = getServletContext();
	//DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
	int receiverid = (Integer) request.getSession().getAttribute("receiverid");
	String username = request.getParameter("receiverUsername");
	
   	if (receiverid == -1){
   		out.println("<title> Message Failed </title>");
   		out.println("</head>");
   		out.println("<body>");
   		out.println("<h1> The username "+ username + " was not found.  Please try a different username </h1>");
   		out.println("<p> <a href=\"welcomepage.jsp\"> Return to Previous Page </a> </p>");
   		out.println("</body>");
   		out.println("</html>");
   	} else {
   		out.println("<title> Message Sent </title>");
   		out.println("</head>");
   		out.println("<body>");
   		out.println("<h1>"+username + " has been sent the following message:  </h1>");
   		out.println("<p>Subject: "+ request.getParameter("subject")+"</p>");
   		out.println("<p>Body: "+ request.getParameter("body")+"</p>");
   		out.println("<p> <a href=\"welcomepage.jsp\"> Return to Previous Page </a> </p>");
   		out.println("</body>");
   		out.println("</html>");
   	}

%>