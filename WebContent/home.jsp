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
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session)  %>
	<%
		String username = (String) session.getAttribute("username");	
		Integer userid = (Integer) session.getAttribute("userid");
		if (username != null && !username.equals("") && userid != null && userid >= 0) {
			out.println("<h1>You are signed in, " + username + "!");
		} else {
			out.println("<h1>You need to sign in. <a href=\"/QuizSite/signin.jsp\" >Sign in here</a></h1>");
		}
	%>
	
	<h2>Test of HtmlTableGenerator:</h2>
	<h3><em>Correct output is the complete content of the "friends" table</em></h3>
	<% 
		quizsite.DatabaseConnection dc = (quizsite.DatabaseConnection) application.getAttribute("DatabaseConnection");
		String query = "SELECT * from friends";
		ResultSet rs = dc.executeQuery(query);
		out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(rs));
	%>
	
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>