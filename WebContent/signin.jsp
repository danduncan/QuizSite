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
		<div class="logininstructions" >Sign In</div>
		<form method="post" action="LoginServlet" id="signinform">
			<span class="loginlabel" >Enter your username: </span>
				<input type="text" name="username" class="logintextbox" id="usernameBox" onkeypress="if (event.keyCode==13) { event.preventDefault(); validate(); }" >
				<span class="errorMsg" id="noUsername">You must provide a username</span>
				<span class="errorMsg" id="usernameInvalidChars">Username may only contain letters, numbers, and periods</span>
			<span class="loginlabel" >Enter your password: </span>
				<input type="password" name="password" class="logintextbox" id="pwdBox" onkeypress="if (event.keyCode==13) { event.preventDefault(); validate(); }" >
				<span class="errorMsg" id="noPwd">You must provide a password</span>
				<span class="errorMsg" id="pwdInvalidChars">Password may only contain [0-9a-zA-Z !@#$%^&*()_=+:;,.?-]</span>
		</form>
			<span class="errorMsg errorMsgTall" id="wrongPwd">Username or password incorrect. Please try again</span>
			<span class="errorMsg errorMsgTall" id="qwizardError">Error: Qwizard is unavailable right now. Please try again in a few minutes</span>
			<input type="submit" onclick="validate()" value="Sign In" class="loginbutton" />
			
		<div class="createaccountlink" >
			<a href="createprofile.jsp" >Create Account</a>
		</div>

	</div>
	
	<%= "<script src=\"/QuizSite/sharedScripts/signin.js\"></script>" %>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>
