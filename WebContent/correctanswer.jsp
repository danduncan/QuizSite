<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Quiz.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=ShowQuizServlet.PAGE_TITLE%></title>
</head>
<body>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedheader.html") %>
	<h1>That is correct!</h1>
	<img src="http://media0.giphy.com/media/PS7d4tm1Hq6Sk/giphy.gif" width="200" height="200"><br>
	<form action="ShowQuizServlet" method="post">
		<input type="submit" value="Continue">
	</form>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>