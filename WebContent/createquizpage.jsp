<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Quiz.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Quiz</title>
</head>
<body>
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session, (quizsite.DatabaseConnection) application.getAttribute("DatabaseConnection"))  %>
	<h1>Select Quiz Options</h1>
	<form action="CreateQuizServlet" method="post">
		Quiz name: <input type="text" name="<%= CreateQuizServlet.QUIZ_NAME%>"><br>
		Description: <input type="text" name="<%= CreateQuizServlet.DESCRIPTION%>"><br>
		Should the questions be displayed in random order?<br>
		<select name="<%= CreateQuizServlet.RANDOM_ORDER%>">
			<option value="false">No</option>
			<option value="true">Yes</option>
		</select>
		<br>Should the quiz be displayed on multiple pages?<br>
		<select name="<%= CreateQuizServlet.MULTI_PAGE%>">
			<option value="false">No</option>
			<option value="true">Yes</option>
		</select>
		<br>Should the quiz display correct answers?<br>
		<select name="<%= CreateQuizServlet.CORRECTION %>">
			<option value="false">No</option>
			<option value="true">Yes</option>
		</select>
		<br><input type="submit" value="Create Questions">
	</form>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>