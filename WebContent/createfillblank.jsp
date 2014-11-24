<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Quiz.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create a Question</title>
</head>
<body>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedheader.html") %>
	<h1><%=CreateQuizServlet.FILL_BLANK%></h1>
	Please fill in the question and answer fields below.
	If the question has multiple answers then please separate each one by a ";".
	<form action="CreateQuizServlet" method="post">
		Question before blank: <input type="text" name="<%= CreateQuizServlet.QUESTION %>"><br>
		Answers for blank: <input type="text" name="<%= CreateQuizServlet.ANSWER %>"><br>
		Question after blank: <input type="text" name="<%= CreateQuizServlet.QUESTION %>2"><br>
		<input type="submit" value="Complete Question">
	</form>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>