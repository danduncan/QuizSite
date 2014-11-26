<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*, users.*,quizsite.*,Quiz.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<link href="/QuizSite/stylesheets/home.css" 
	type="text/css" 
	rel="stylesheet" 
	id="homeStylesheet" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session)  %>
<% 
	User user = (User) session.getAttribute("user");
	ServletContext sc = getServletContext();
	DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
	//welcome back info
	out.println("<title>Welcome "+user.firstname+"!</title>");
	
%>
<% 
	out.println("<h1>Welcome "+user.firstname+"!</h1>");
	
	out.println("<h2> Quiz Info </h2>");
	//quizzes taken info
	out.println("<p> Quizzes Taken: "+user.quizzestaken.size()+ "</p>");
	out.println("<ul type = \"circle\">");
	if (user.numtaken > 0){
		for(int i = 0; i < user.quizzestaken.size(); i++){
			Integer id = user.quizzestaken.get(i).quizid;
			String quizname = Quiz.getName(id,dc);
			out.println("<li> Quiz Name: <a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a>    " + user.quizzestaken.get(i).toString()+ "</li>");
		}
	}
	out.println("</ul>");
	
	//quizzes made info
	out.println("<p> Quizzes Made: " + user.quizzesmade.size() + "</p>");
	out.println("<form method=\"get\" action=\"CreateQuizServlet\">");
	out.println("<p><input type=\"submit\" value=\"Create New Quiz\" /></p>");
	out.println("</form>");
	out.println("<ul type = \"circle\">");
	out.println("<form method=\"get\" action=\"QuizHomepageServlet\">");
	if (user.quizzesmade.size() > 0){
		for(int i = 0; i < user.quizzesmade.size(); i++){
			Integer id = user.quizzesmade.get(i).quizid;
			String quizname = Quiz.getName(id,dc);
			//out.println("<li>"+user.quizzesmade.get(i).toString()+"<button name =\"quizid\" type=\"submit\" value=\""+id+"\"> "+id+" </button></li>");
			out.println("<li> Quiz Name: <a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a>    " + user.quizzesmade.get(i).toString()+ "</li>");
		}
	}
	out.println("</form>");
	out.println("</ul>");
	
	//achievements info
	out.println("<p> Achievements: " + user.achievements.size() + "</p>");
	out.println("<ul type = \"circle\">");
	for(int i = 0; i < user.achievements.size(); i++){
		out.println("<li>"+user.achievements.get(i).toString()+"</li>");
	}
	out.println("</ul>");
	
	//messages
	out.println("<h2> Messages </h2>");
	int count = 0;
	ArrayList<Integer> newmessageNums = new ArrayList<Integer>();
	ArrayList<String> newMessages = new ArrayList<String>();
	for (int i = 0; i < user.messages.size(); i++){
		//determine if new message
		if (user.messages.get(i).opened == false && user.messages.get(i).receiverid == user.id){
			newmessageNums.add(i);
			count++;
			newMessages.add(user.messages.get(i).toString());
		}
	}
	out.println("<p> You have " + count + " new Messages</p>");
	out.println("<ul type = \"circle\">");
	for(int i = 0; i < newMessages.size(); i++){
		out.println("<li><a href=\"checkmessage.jsp?messageNum="+newmessageNums.get(i)+"\"> From: "+user.userconnection.getAttribute("username",user.messages.get(newmessageNums.get(i)).senderid)+newMessages.get(i).toString()+"</a></li>");
	}
	out.println("</ul>");
	
	//view all messages button
	out.println("<p><a href=\"checkmessage.jsp?messageNum=-1\">View All Messages</a></li>");
%>

<h3>Create New Message</h3>
<form method="post" action="CreateMessageServlet">
<p>User name: <input type="text" name="receiverUsername"></p>
<p>Subject: <input type="text" name="subject"></p>
<p>Body: <textarea name="body" cols="50" rows="10"></textarea>
<p><input type="submit" value="Send Message" /></p>
<input name="type" type="hidden" value= "2"/>
</form>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>
