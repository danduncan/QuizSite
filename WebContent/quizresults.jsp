<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Quiz.*, users.*, quizsite.*, java.util.List, java.text.DecimalFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=ShowQuizServlet.PAGE_TITLE%></title>
</head>
<body>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedheader.html") %>
	<%
		int numAttempted = (Integer) request.getSession().getAttribute(ShowQuizServlet.NUM_ATTEMPTED);
		int numCorrect = (Integer) request.getSession().getAttribute(ShowQuizServlet.NUM_CORRECT);
		long startTime = (Long) request.getSession().getAttribute(ShowQuizServlet.START_TIME);
		double secondsElapsed = (double)(System.currentTimeMillis()-startTime)/1000;
		Quiz quiz = (Quiz) request.getSession().getAttribute(ShowQuizServlet.QUIZ);
		List<String[]> quizAnswers = (List<String[]>) request.getSession().getAttribute(ShowQuizServlet.QUIZ_ANSWERS);
		User user = (User) request.getSession().getAttribute("user");
		ScoreManager scoreManager = (ScoreManager) request.getServletContext().getAttribute("ScoreManager");
		SiteManager sm = (SiteManager) request.getServletContext().getAttribute("SiteManager");	
		Score quizScore = new Score(user.id, quiz.id, numCorrect, (int)secondsElapsed, FormatDateTime.getCurrentSystemDate());
		user.quizzestaken.add(new QuizTaken(sm.popNextQuizTakenID(), quiz.id, user.id, quizScore.score,secondsElapsed));	
		int rank = scoreManager.addScore(quizScore);
		DecimalFormat df = new DecimalFormat("#.00");
		String percentScore = df.format(100*((double)numCorrect/(double)numAttempted));
		
	%>
	<h1>Quiz Results</h1>
	Points earned: <%=numCorrect%><br>
	Points possible: <%=numAttempted%><br>
	
	
	Percent score:  <%=percentScore%>%<br>
	Time: <%= secondsElapsed %> seconds<br>
	<%
		if(rank != 0){
			out.println("High score!!!<br>");
			out.println("All-time score rank: " +rank +"<br>");
		}
	%>
		
	<h1>Answers</h1>
	<ul style="list-style-type:square">
	<%
		for(int i = 0; i<quiz.getQuizSize(); i++){
			out.println("<li>");
			out.println("Question " + (i+1)+":<br>");
			ShowQuizServlet.printResponses(out, quizAnswers.get(i), quiz.getQuestion(i));
			ShowQuizServlet.printAnswers(out, quiz.getQuestion(i));
			out.println("<br>");
			out.println("</li>");
		}
	%>
	</ul>
	<a href="welcomepage.jsp">Go to welcome page!</a>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>