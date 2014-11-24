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
</head>
<body>
	<%
		// Set the parameters for determining user identity
		String username = (String) session.getAttribute("username");
		Integer userid = (Integer) session.getAttribute("userid");
		if (username == null || username == null) { username = null; userid = null;}
		out.println("<input type=\"hidden\" id=\"docUsername\" value=\"" + username + "\">");
		//out.println("<input type=\"hidden\" id=\"docUserID\" value=\"" + userid + "\">");

		out.println(sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedheader.html")); 
	%>
	<h1>You need to sign in. <a href="/QuizSite/signin.jsp">Sign in here</a>.</a></h1>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>