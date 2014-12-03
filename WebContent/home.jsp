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
		String username = (String) session.getAttribute("username");	
		Integer userid = (Integer) session.getAttribute("userid");
		if (username != null && !username.equals("") && userid != null && userid >= 0) {
			//forward to unsuccessful send
			RequestDispatcher dispatch = request.getRequestDispatcher("welcomepage.jsp");
			dispatch.forward(request, response);
			//out.println("<h1>You are signed in, " + username + "!");
		} else {
			out.println("<h1>Welcome to Qwizard!</h1>");
			out.println("<p>Show off your intellectual prowess by making and taking quizzes with your friends!</p>");
			out.println("<p>Please <a href=\"/QuizSite/signin.jsp\" >sign in</a> or <a href=\"/QuizSite/createaccount.jsp\" >create an account</a>!</p>");
		}
	%>
	
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>