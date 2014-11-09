<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*, users.*" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% 
	User user = (User) session.getAttribute("user");
	out.println("<title>Welcome Back "+user.firstname+"!</title>");
	out.println(" </head>");
	out.println("<body>");
	out.println("<h1>Welcome Back "+user.firstname+"!</h1>");
	
	out.println("</body>");
	out.println("</html>");
	%>
