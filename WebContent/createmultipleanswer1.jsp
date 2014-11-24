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
	<h1><%=CreateQuizServlet.MULTI_ANSWER%></h1>
	<form action="CreateQuizServlet" method="post">
		Does the order of the answers matter? <br>
		<select name="<%= CreateQuizServlet.ORDER_MATTERS%>">
			<option value="false">No</option>
			<option value="true">Yes</option>
		</select><br>
		How many answers does the question have?
		<select name="<%= CreateQuizServlet.NUM_ANSWERS %>">
			<option value="1">1</option>
			<option value="2">2</option>
			<option value="3">3</option>
			<option value="4">4</option>
			<option value="5">5</option>
			<option value="6">6</option>
			<option value="7">7</option>
			<option value="8">8</option>
		</select><br>
		<input type="hidden" name="<%= CreateQuizServlet.QUESTION_TYPE %>" value="<%= CreateQuizServlet.MULTI_ANSWER %>">
		<input type="submit" value="Update Settings">
	</form>
</body>
</html>