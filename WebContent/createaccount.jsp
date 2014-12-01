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

			<span class="loginlabel" >Username </span>
				<input type="text" name="username" class="logintextbox" value="" />
				
			<span class="loginlabel" >Name</span>
				<input type="text" name="firstname" class="logintextbox shortloginbox" placeholder="First" value="" />
				<input type="text" name="lastname" class="logintextbox shortloginbox" placeholder="Last" value="" />
			
			<span class="loginlabel" >Email</span>
				<input type="text" name="email" class="logintextbox" value="" />
			
			<span class="loginlabel" >Profile Picture URL (optional)</span>
				<input type="text" name="profilepic" class="logintextbox" value="" />
			
			<div class="spacer"> </div>

			<span class="loginlabel" >Password </span>
				<input type="password" name="password" class="logintextbox" value="" > 

			<span class="loginlabel" >Confirm password</span>
				<input type="password" name="confirmpassword" class="logintextbox" value="" > 

				<input type="submit" value="Register" class="loginbutton" value="" />
		</form>
	</div>

	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>