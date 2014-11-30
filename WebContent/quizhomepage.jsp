<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Quiz.*, quizsite.*, users.*, connection.*, java.sql.*, java.util.*, java.io.IOException, java.text.DecimalFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz Home Page</title>
</head>
<body>
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"),session) %>
	
	<%!
	public String printStats(double num, int possiblePoints){
		String str = "N/A";
		if(num != -1){
			DecimalFormat df = new DecimalFormat("#.00");
			str = df.format(100*((double)num/(double)possiblePoints)) + "%";
		}
		return str;
	}
	%>


	<%
	ServletContext sc = request.getServletContext();
	DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
	ScoreManager scoreManager = (ScoreManager) sc.getAttribute("ScoreManager");
	Quiz quiz = (Quiz) request.getSession().getAttribute(ShowQuizServlet.QUIZ);
	User quizAuthor = new User(quiz.authorid, new UserConnection(dc));
	double[] stats = scoreManager.getSummaryStats(quiz.id);
	 %>

	<h1>Quiz Summary</h1>
	Name: <%=quiz.name%><br>
	Description: <%=quiz.description%><br>
	Creator: <%=quizAuthor.firstname%> <%=quizAuthor.lastname%><br>
	Created on: <%= FormatDateTime.getUserDateTime(quiz.datemade)[0] %><br>
	Average Score: <%=printStats(stats[0], quiz.numPointsPossible())%><br>
	High Score: <%=printStats(stats[1], quiz.numPointsPossible())%><br>
	Low Score: <%=printStats(stats[2], quiz.numPointsPossible())%><br>
	<a href="ShowQuizServlet">Take this quiz!</a><br>
		
	<h1>Overall High Scores</h1>
	<%
		ResultSet rs = scoreManager.getHighScores(quiz.id);
		ScoreManager.printScoresToJSP(out, rs, quiz.numPointsPossible());
	%>
	
	<h1>Today's High Scores</h1>
	<%
		rs = scoreManager.getHighScoresToday(quiz.id);
		ScoreManager.printScoresToJSP(out, rs, quiz.numPointsPossible());
	%>
	
	<h1>You High Scores</h1>
	<%
		User currUser = (User) request.getSession().getAttribute("user");
		rs = scoreManager.getUserScores(quiz.id, currUser.id);
		ScoreManager.printScoresToJSP(out, rs, quiz.numPointsPossible());
	%>
	
	<h1>Most Recent Quiz Scores</h1>
	<%
		rs = scoreManager.getRecentScores(quiz.id);
		ScoreManager.printScoresToJSP(out, rs, quiz.numPointsPossible());
	%>
	
	<h1>Overall Low Scores</h1>
	<%
		rs = scoreManager.getLowScores(quiz.id);
		ScoreManager.printScoresToJSP(out, rs, quiz.numPointsPossible());
	%>
	
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
	
</body>
</html>