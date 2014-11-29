<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<link href="/QuizSite/stylesheets/home.css" 
	type="text/css" 
	rel="stylesheet" 
	id="homeStylesheet" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> Send Message </title>
</head>
<body>
<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session)  %>
<% 
	ServletContext sc = getServletContext();
	//DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
	Integer receiverid = (Integer) request.getSession().getAttribute("receiverid");
	if (receiverid == null) receiverid = Integer.parseInt(request.getParameter("receiverid"));
	String username = request.getParameter("receiverUsername");
	
   	if (receiverid == -1){
   		out.println("<h1> The username "+ username + " was not found.  Please try a different username </h1>");
   		out.println("<p> <a href=\"welcomepage.jsp\"> Return to Previous Page </a> </p>");
   	} else {
   		out.println("<h1>"+username + " has been sent the following message:  </h1>");
   		out.println("<p>Subject: "+ request.getParameter("subject")+"</p>");
   		out.println("<p>Body: "+ request.getParameter("body")+"</p>");
   	}
%>
<p> <a href=\"welcomepage.jsp\"> Return to Previous Page </a> </p>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>

</body>
</html>