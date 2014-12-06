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
<title>Check Message</title>
</head>
<body>
<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session)  %>
	<div class="typicalQwizardMainBody">
	<% 
	ServletContext sc = getServletContext();
	DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
	User user = (User) session.getAttribute("user");
	Integer messageNum = Integer.parseInt(request.getParameter("messageNum"));
	//only checking one unread message
	if (messageNum != -1){
		Message msg = user.messages.get(messageNum);
		out.println("<p>From: "+user.userconnection.getAttribute("username",msg.senderid)+"</p>");
		out.println("<p>Date: "+FormatDateTime.getUserDate(msg.datesent)+"</p>");
		out.println("<p>Subject: "+msg.subject+"</p>");
		out.println("<p>Body: "+msg.body+"</p>");
		
		out.println("<input type=\"submit\" value=\"Reply\" class=\"typicalQwizardBtn\" onclick=\"displayMessage('" + user.userconnection.getAttribute("username",msg.senderid) + "')\" >");
		
		out.println("<p> <a href=\"welcomepage.jsp\"> Return to Previous Page </a> </p>");
		
		//update message to read
		user.messages.get(messageNum).opened = true;
		//update database
		msg.updateRead(dc);
		
	//show all messages and mark as read
	} else {
		int count = 0;
		ArrayList<Integer> newmessageNums = new ArrayList<Integer>();
		ArrayList<Message> newMessages = new ArrayList<Message>();
		ArrayList<Message> sentMessages = new ArrayList<Message>();
		ArrayList<Message> receivedMessages = new ArrayList<Message>();
		for (int i = 0; i < user.messages.size(); i++){
			Message msg = user.messages.get(i);
			//determine new messsages
			if (msg.opened == false && msg.receiverid == user.id && msg.type == 2){
				count++;
				newMessages.add(msg);
				//update new messages to read			
				user.messages.get(i).opened = true;
				msg.updateRead(dc);
				
			//received messages	
			} else if(msg.receiverid == user.id && msg.type == 2){
				receivedMessages.add(msg);
			//sent messages
			} else if (msg.senderid == user.id && msg.type == 2){
				sentMessages.add(msg);
			}
		}

		//print out new messages		
		out.println("<p><h1> You have " + count + " new Messages</h1></p>");
		for(int i = 0; i < newMessages.size(); i++){
			Message msg = newMessages.get(i);
			out.println("<p>From: "+user.userconnection.getAttribute("username",msg.senderid)+"</p>");
			out.println("<p>Date: "+ FormatDateTime.getUserDateTime(msg.datesent)[0]+"</p>");
			out.println("<p>Subject: "+msg.subject+"</p>");
			out.println("<p>Body: "+msg.body+"</p>");
			out.println("<input type=\"submit\" value=\"Reply to " + user.userconnection.getAttribute("username",msg.senderid)  + "\" class=\"typicalQwizardBtn\" onclick=\"displayMessage('" + user.userconnection.getAttribute("username",msg.senderid) + "')\" >");
			out.println("<p> ------------------------ <p>");
			
		}
		
		//print out received messages
		out.println("<p><h1> Received Messages</h1></p>");
		for(int i = 0; i <  receivedMessages.size(); i++){
			Message msg = receivedMessages.get(i);
			out.println("<p>From: "+user.userconnection.getAttribute("username",msg.senderid)+"</p>");
			out.println("<p>Date: "+FormatDateTime.getUserDateTime(msg.datesent)[0]+"</p>");
			out.println("<p>Subject: "+msg.subject+"</p>");
			out.println("<p>Body: "+msg.body+"</p>");
			out.println("<input type=\"submit\" value=\"Reply to " + user.userconnection.getAttribute("username",msg.senderid)  + "\" class=\"typicalQwizardBtn\" onclick=\"displayMessage('" + user.userconnection.getAttribute("username",msg.senderid) + "')\" >");
			out.println("<p> ------------------------ <p>");
			}
		
		//print out sent messages
		out.println("<p><h1> Sent Messages</h1></p>");
		for(int i = 0; i <  sentMessages.size(); i++){
			Message msg = sentMessages.get(i);
			out.println("<p>To: "+user.userconnection.getAttribute("username",msg.receiverid)+"</p>");
			out.println("<p>Date: "+FormatDateTime.getUserDateTime(msg.datesent)[0]+"</p>");
			out.println("<p>Subject: "+msg.subject+"</p>");
			out.println("<p>Body: "+msg.body+"</p>");
			out.println("<p> ------------------------ <p>");
			}
		
		out.println("<p> <a href=\"welcomepage.jsp\"> Return to Previous Page </a> </p>");
	}
		
%>
</div>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>