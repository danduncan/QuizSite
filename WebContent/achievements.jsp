<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*, users.*,quizsite.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<link href="/QuizSite/stylesheets/home.css" 
	type="text/css" 
	rel="stylesheet" 
	id="homeStylesheet" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Achievements</title>
</head>
<body>
<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session, (quizsite.DatabaseConnection) application.getAttribute("DatabaseConnection"))  %>
<div class="achievementsMainBody">
<h1>Qwizard Achievements:</h1>
<% 
 ServletContext sc = getServletContext();
 ArrayList<AchievementType> achievementtypes = (ArrayList<AchievementType>) sc.getAttribute("achievementtypes");
 String[] colNames = {"Achievement Name","Description","Badge"};
 
 String[][] achievements = new String[achievementtypes.size()+1][colNames.length];
 achievements[0] = colNames;
 for (int i = 0; i < achievementtypes.size(); i++){
	 achievements[i+1][0] =  achievementtypes.get(i).name;
	 achievements[i+1][1] =  achievementtypes.get(i).description;
	 achievements[i+1][2] =  "<img src = \""+achievementtypes.get(i).icon+"\" height = \"50\" width = \"50\"";
 }
 out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(achievements));
%>
</div>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>
