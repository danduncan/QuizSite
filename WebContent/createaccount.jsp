<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>

<link href="/QuizSite/stylesheets/signin.css" 
	type="text/css" 
	rel="stylesheet" 
	id="signinStylesheet" />
</head>
<body>
	<%-- Forward to homepage if user is already logged in --%>
	<% 
		String username = (String) session.getAttribute("username");	
		Integer userid = (Integer) session.getAttribute("userid");
		if (username != null && !username.equals("") && userid != null && userid >= 0) {
			%>
			<jsp:forward page="/home.jsp" />
			<%
		}
	%>
	
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session)  %>
	
	<div class="loginFrame">
		<div class="logininstructions" >Create Account</div>
		<form method="post" action="CreateServlet" >
			<span class="loginlabel" >Enter your desired username: </span>
				<input type="text" name="username" class="logintextbox" >
			<span class="loginlabel" >Enter your password: </span>
				<input type="password" name="password" class="logintextbox" > 
				<input type="submit" value="Register" class="loginbutton" />
		</form>
	</div>

	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>