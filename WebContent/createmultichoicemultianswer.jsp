<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Quiz.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create a Question</title>
</head>
<body>
	<h1><%=CreateQuizServlet.MULTI_ANSWER_MULTI_CHOICE%></h1>
	Please fill in the question, choices, and answer fields below. Please separate choices by a ";".
	If the question has multiple answers then please separate each one by a ";".
	<form action="CreateQuizServlet" method="post">
		Question: <input type="text" name="<%= CreateQuizServlet.QUESTION %>"><br>
		Choices: <input type="text" name="<%= CreateQuizServlet.MC_CHOICES %>"><br>
		Answers: <input type="text" name="<%= CreateQuizServlet.ANSWER %>"><br>
		<input type="submit" value="Complete Question">
	</form>	
</body>
</html>