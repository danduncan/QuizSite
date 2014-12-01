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
		<form method="post" action="CreateServlet" id="createform" >
			<span class="loginlabel" >Username </span>
				<input type="text" name="username" id="usernameBox" class="logintextbox" value="" />
				<span class="errorMsg" id="noUsername">You must provide a username</span>
				<span class="errorMsg" id="usernameTaken">This username is already taken</span>
				<span class="errorMsg" id="usernameInvalidChars">Username may only contain letters, numbers, and periods</span>
				
			
			<span class="loginlabel" >Name</span>
				<input type="text" name="firstname" id="firstNameBox" class="logintextbox shortloginbox" placeholder="First" value="" />
				<input type="text" name="lastname" id="lastNameBox" class="logintextbox shortloginbox" placeholder="Last" value="" />
				<span class="errorMsg" id="noName">You must provide a first name and a last name</span>
				<span class="errorMsg" id="nameInvalidChars">Name may only contain a-z A-Z ' - and space</span>
				
			<span class="loginlabel" >Email</span>
				<input type="text" name="email" id="emailBox" class="logintextbox" value="" />
				<span class="errorMsg" id="noEmail">You must provide an email address</span>
				<span class="errorMsg" id="emailInvalidChars">Email may only contain a-z A-Z 0-9 . @ _ -</span>
				
			<span class="loginlabel" >Profile Picture URL (optional)</span>
				<input type="text" name="profilepic" id="profilePicBox" class="logintextbox" value="" />
				<span class="errorMsg" id="picInvalidChars">URL may only contain a-z A-Z 0-9 . _ / : ? = + -</span>
				
			<div class="spacer"> </div>

			<span class="loginlabel" >Password </span>
				<input type="password" id="pwdBox" name="password" class="logintextbox" value="" > 

			<span class="loginlabel" >Confirm password</span>
				<input type="password" id="confirmPwdBox" name="confirmpassword" class="logintextbox" value="" > 
				<span class="errorMsg" id="noPwd">You must provide a password</span>
				<span class="errorMsg" id="pwdLength">Your password must be at least 5 characters</span>
				<span class="errorMsg" id="pwdMatch">Your passwords must match</span>
				<span class="errorMsg" id="pwdInvalidChars">Password may only contain [0-9a-zA-Z !@#$%^&*()_=+:;,.?-]</span>
				
			</form>
			<input onclick="validateInputs()" type="submit" value="Register" class="loginbutton" value="" />
			<span class="errorMsg" id="qwizardError">Error: Qwizard is unavailable right now. Please try again in a few minutes</span>
	</div>

	<%= "<script src=\"/QuizSite/sharedScripts/createaccount.js\"></script>" %>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>