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
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session, (quizsite.DatabaseConnection) application.getAttribute("DatabaseConnection"))  %>
	<div class="typicalQwizardMainBody">
	<h1>Create a Question</h1>
	<form action="CreateQuizServlet" method="post">
		<br>What type of question would you like to add to your quiz?<br>
		<select name="<%= CreateQuizServlet.QUESTION_TYPE%>" class="typicalQwizardSelect">
			<option value="<%= CreateQuizServlet.QUESTION_RESPONSE%>"><%= CreateQuizServlet.QUESTION_RESPONSE%></option>
			<option value="<%= CreateQuizServlet.FILL_BLANK%>"><%= CreateQuizServlet.FILL_BLANK%></option>
			<option value="<%= CreateQuizServlet.PIC_RESPONSE%>"><%= CreateQuizServlet.PIC_RESPONSE%></option>
			<option value="<%= CreateQuizServlet.MULTIPLE_CHOICE%>"><%= CreateQuizServlet.MULTIPLE_CHOICE%></option>
			<option value="<%= CreateQuizServlet.MULTI_ANSWER%>"><%= CreateQuizServlet.MULTI_ANSWER%></option>
			<option value="<%= CreateQuizServlet.MULTI_ANSWER_MULTI_CHOICE%>"><%= CreateQuizServlet.MULTI_ANSWER_MULTI_CHOICE%></option>
		</select>
	<br><input type="submit" value="Add Question" class="typicalQwizardBtn"></form>

	<form action="SaveQuizServlet" method="get">
		If you are done adding questions please press the "Complete Quiz" button below.<br>
	<input type="submit" value="Complete Quiz" class="typicalQwizardBtn" ></form>
	</div>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>