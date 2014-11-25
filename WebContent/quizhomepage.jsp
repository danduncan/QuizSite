<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Quiz.*, quizsite.*, users.*, connection.*, java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz Home Page</title>
</head>
<body>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedheader.html") %>

	<%
	ServletContext sc = request.getServletContext();
	DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
	ScoreManager scoreManager = (ScoreManager) sc.getAttribute("ScoreManager");
	Quiz quiz = (Quiz) request.getSession().getAttribute(ShowQuizServlet.QUIZ);
	User user = new User(quiz.authorid, new UserConnection(dc));
	 %>

	<h1>Quiz Overview</h1>
	Name: <%=quiz.name%><br>
	Description: <%=quiz.description%><br>
	Creator: <%=user.firstname%> <%=user.lastname%><br>
	Date Created: <%=quiz.datemade%><br>
	<a href="ShowQuizServlet">Take this quiz!</a><br>
		
	<h1>High scorers</h1>
	<ol>
		<%
		ResultSet rs = scoreManager.getHighScores(quiz.id);
		try {
			while(rs.next()){
				int userID = rs.getInt("userid");
				int score = rs.getInt("score");
				int time = rs.getInt("time");
				user = new User(userID, new UserConnection(dc));
				out.println("<li>"+ user.firstname +" "+user.lastname + " scored " + score+ " in " + time+" seconds</li>");
			}
		} catch (SQLException ignored){}
		%>
	</ol>
	
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
	
</body>
</html>