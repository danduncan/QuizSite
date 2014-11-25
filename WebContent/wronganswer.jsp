<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Quiz.*, users.*, quizsite.*, java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=ShowQuizServlet.PAGE_TITLE%></title>
</head>
<body>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedheader.html") %>
	<h1>That is wrong.</h1>
	<img src="http://www.reactiongifs.com/r/wrong-gif.gif" width="200" height="200"><br>
	<% 
		int quizPage = (Integer) request.getSession().getAttribute(ShowQuizServlet.QUIZ_PAGE);
		Quiz quiz = (Quiz) request.getSession().getAttribute(ShowQuizServlet.QUIZ);
		Question q = quiz.getQuestion(quizPage);
		ShowQuizServlet.printAnswers(out, q);
	%>
	<form action="ShowQuizServlet" method="post">
		<input type="submit" value="Continue">
	</form>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>