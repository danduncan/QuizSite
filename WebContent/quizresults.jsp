<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Quiz.*, users.*, quizsite.*, java.util.List, java.text.DecimalFormat, java.sql.*, java.io.IOException, connection.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=ShowQuizServlet.PAGE_TITLE%></title>
</head>
<body>
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session, (quizsite.DatabaseConnection) application.getAttribute("DatabaseConnection"))  %>
	<div class="typicalQwizardMainBody">
	<%
		HttpSession ses = request.getSession();
		ServletContext con = request.getServletContext();
		int numAttempted = (Integer) ses.getAttribute(ShowQuizServlet.NUM_ATTEMPTED);
		int numCorrect = (Integer) ses.getAttribute(ShowQuizServlet.NUM_CORRECT);
		long startTime = (Long) ses.getAttribute(ShowQuizServlet.START_TIME);
		double secondsElapsed = (double)(System.currentTimeMillis()-startTime)/1000;
		Quiz quiz = (Quiz) ses.getAttribute(ShowQuizServlet.QUIZ);
		List<String[]> quizAnswers = (List<String[]>) ses.getAttribute(ShowQuizServlet.QUIZ_ANSWERS);
		User user = (User) ses.getAttribute("user");
		ScoreManager scoreManager = (ScoreManager) con.getAttribute("ScoreManager");
		SiteManager sm = (SiteManager) con.getAttribute("SiteManager");	
		DatabaseConnection dc = (DatabaseConnection) con.getAttribute("DatabaseConnection");
		
		Score quizScore = new Score(user.id, quiz.id, numCorrect, (int)secondsElapsed, FormatDateTime.getCurrentSystemDate());
		quiz.numtaken++;
		QuizConnection qc = new QuizConnection(dc, null);
		qc.storeQuiz(quiz, false);
		user.quizzestaken.add(new QuizTaken(sm.popNextQuizTakenID(), quiz.id, user.id, quizScore.score,secondsElapsed));	
		user.updateUserDatabase();
		ses.setAttribute("user", user);
		
		int rank = scoreManager.addScore(quizScore);
		DecimalFormat df = new DecimalFormat("#.00");
		String percentScore = df.format(100*((double)numCorrect/(double)numAttempted));
		
		List<Achievement> achieved = Achievement.updateQuizTakenAchievements(user, rank);
		Achievement.updateSiteAchievements(user, achieved, dc);

		ResultSet rs = SearchForQuizzes.getQuizByID(dc,quiz.id);
		rs.first();
		out.println(sharedHtmlGenerators.HtmlQuizThumbnailGenerator.getThumbnail(rs));
	%>
	
	<h1>Quiz Results</h1>
	Points earned: <%=numCorrect%><br>
	Points possible: <%=numAttempted%><br>
	
	
	Percent score:  <%=percentScore%>%<br>
	Time: <%= secondsElapsed %> seconds<br>
	<%
		if(achieved.size()>0){
			List<AchievementType> achievementTypes = (List<AchievementType>)con.getAttribute("achievementtypes");
			String achievementStr = Achievement.getAchievementNames(achieved, achievementTypes);
			out.println(achievementStr+"<br>");
		}
	
		if(rank != 0){
			out.println("<h1>You got a high score!!</h1>");
			rs = scoreManager.getHighScores(quiz.id);
		 	ScoreManager.printScoresToJSP(out, rs, quiz.numPointsPossible(), dc);
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
	<a href="home.jsp">Go to welcome page!</a>
	</div>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>