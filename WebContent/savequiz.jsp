<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Quiz.*, users.*, quizsite.*, java.util.List, java.text.DecimalFormat, java.sql.*, java.io.IOException" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz Saved!</title>
</head>
<body>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedheader.html") %>
	<h1> Quiz Saved! </h1>
	<%
		ServletContext con = request.getServletContext();
		User user = (User) request.getSession().getAttribute("user");
		List<Achievement> achieved = Achievement.updateQuizMadeAchievements(user);
		Achievement.updateSiteAchievements(user, achieved, (DatabaseConnection)con.getAttribute("DatabaseConnection"));
		if(achieved.size()>0){
			List<AchievementType> achievementTypes = (List<AchievementType>)con.getAttribute("achievementtypes");
			String achievementStr = Achievement.getAchievementNames(achieved, achievementTypes);
			out.println(achievementStr+"<br>");
		}
	%>
	
	<a href="welcomepage.jsp">Go to my home page!</a>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>