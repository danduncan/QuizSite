<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*, users.*" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% 
	User user = (User) session.getAttribute("user");
	//welcome back info
	out.println("<title>Welcome "+user.firstname+"!</title>");
	out.println(" </head>");
	out.println("<body>");
	out.println("<h1>Welcome "+user.firstname+"!</h1>");
	
	out.println("<h2> Quiz Info </h2>");
	//quizzes taken info
	out.println("<p> Quizzes Taken: "+user.numtaken+ "</p>");
	out.println("<ul type = \"circle\">");
	if (user.numtaken > 0){
		for(int i = 0; i < user.quizzestaken.size(); i++){
			out.println("<li>"+user.quizzestaken.get(i).toString()+"</li>");
		}
	}
	out.println("</ul>");
	
	//quizzes made info
	out.println("<p> Quizzes Made: " + user.numcreated + "</p>");
	out.println("<ul type = \"circle\">");
	if (user.numcreated > 0){
		for(int i = 0; i < user.quizzesmade.size(); i++){
			out.println("<li>"+user.quizzesmade.get(i).toString()+"</li>");
		}
	}
	out.println("</ul>");
	
	//achievements info
	out.println("<p> Achievements: " + user.achievements.size() + "</p>");
	out.println("<ul type = \"circle\">");
	for(int i = 0; i < user.achievements.size(); i++){
		out.println("<li>"+user.achievements.get(i).toString()+"</li>");
	}
	out.println("</ul>");
	
	//messages (TODO I want to allow users to click on their unread message that will take them to an inbox page)
	out.println("<h2> Messages </h2>");
	int count = 0;
	ArrayList<String> newMessages = new ArrayList<String>();
	for (int i = 0; i < user.messages.size(); i++){
		if (user.messages.get(i).opened == false){
			count++;
			newMessages.add(user.messages.get(i).toString());
		}
	}
	out.println("<p> You have " + count + " new Messages</p>");
	out.println("<ul type = \"circle\">");
	for(int i = 0; i < newMessages.size(); i++){
		out.println("<li>"+newMessages.get(i).toString()+"</li>");
	}
	
	out.println("</ul>");
	out.println("</body>");
	out.println("</html>");
	%>
