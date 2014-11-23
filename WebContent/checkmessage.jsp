<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ page import="java.util.*, users.*,quizsite.*"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Check Message</title>
</head>
<body>
<% 
	ServletContext sc = getServletContext();
	DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
	User user = (User) session.getAttribute("user");
	Integer messageNum = Integer.parseInt(request.getParameter("messageNum"));
	Message msg = user.messages.get(messageNum);
	out.println("<p>From: "+msg.receiverid+"</p>");
	out.println("<p>Date: "+msg.datesent+"</p>");
	out.println("<p>Subject: "+msg.subject+"</p>");
	out.println("<p>Body: "+msg.body+"</p>");
	
	out.println("<p> <a href=\"welcomepage.jsp\"> Return to Previous Page </a> </p>");
	
	//update message to read
	user.messages.get(messageNum).opened = true;
	msg.opened = true;
	// Add message to database
	StringBuilder sb = new StringBuilder();
	sb.append("UPDATE "+ MyDBInfo.MESSAGESTABLE+" SET ");
	sb.append("opened = "+msg.opened);
	sb.append(" WHERE (id = "+msg.id+")");
	
	try {
		dc.executeUpdate(sb.toString());
	}	
	catch (Exception e){
		e.printStackTrace();
	}
		
		
%>
</body>
</html>