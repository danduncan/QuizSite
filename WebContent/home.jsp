<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="java.sql.*" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>

<link href="/QuizSite/stylesheets/home.css" 
	type="text/css" 
	rel="stylesheet" 
	id="homeStylesheet" />
</head>
<body>
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session, (quizsite.DatabaseConnection) application.getAttribute("DatabaseConnection"))  %>
	<%
		users.User usr = (users.User) session.getAttribute("user");
		String username = (String) session.getAttribute("username");	
		Integer userid = (Integer) session.getAttribute("userid");
		if (usr != null || (username != null && !username.equals("")) || (userid != null && userid >= 0)) {
			//forward to unsuccessful send
			RequestDispatcher dispatch = request.getRequestDispatcher("welcomepage.jsp");
			dispatch.forward(request, response);
			//out.println("<h1>You are signed in, " + username + "!");
		} else {
			out.println("<h1>Welcome to Qwizard!</h1>");
			out.println("<h2>You need to sign in. <a href=\"/QuizSite/signin.jsp\" >Sign in here</a></h2>");
		}
	%>
		
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>