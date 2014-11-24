<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>

<link href="/QuizSite/stylesheets/signin.css" 
	type="text/css" 
	rel="stylesheet" 
	id="def" />

</head>
<body>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedheader.html") %>

	<div class="loginFrame">
		<div class="logininstructions" >Sign In</div>
		<form method="post" action="LoginServlet" >
			<span class="loginlabel" >Enter your username: </span>
				<input type="text" name="username" class="logintextbox" >
			<span class="loginlabel" >Enter your password: </span>
				<input type="password" name="password" class="logintextbox" > 
				<input type="submit" value="Sign In" class="loginbutton" />
		</form>

		<div class="createaccountlink" ><a href="createAccount.html" >
			Create New Account</a></div>
	</div>
</body>
</html>