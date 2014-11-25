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
	<h1><%=CreateQuizServlet.MULTI_ANSWER%></h1>
	<% String orderMatters = request.getParameter(CreateQuizServlet.ORDER_MATTERS); %>
	Please fill in the question and answer fields below.
 	If multiple answers are acceptable for an answer field then please separate each one by a ";".
	<form action="CreateQuizServlet" method="post">
		Question: <input type="text" name="<%=CreateQuizServlet.QUESTION%>"><br>
		<%
		int numAnswerFields = (Integer)session.getAttribute(CreateQuizServlet.NUM_ANSWERS);
		for(int i=0; i<numAnswerFields; i++){
			out.println("Answers: <input type=\"text\" name=\""+CreateQuizServlet.ANSWER+i+"\"><br>");	
		}
		%>
		<input type="hidden" name="<%=CreateQuizServlet.ORDER_MATTERS%>" value="<%=orderMatters%>"><br>
		<input type="submit" value="Complete Question">
	</form>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>