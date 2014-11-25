<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<link href="/QuizSite/stylesheets/home.css" 
	type="text/css" 
	rel="stylesheet" 
	id="homeStylesheet" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Account</title>
</head>
<body>
<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session)  %>

	<% 
	
String name = request.getParameter("username");

out.println("<h1>The User Name " + name + " is Already in Use</h1>");
%>

	<h1>Create your profile</h1>
	<form action="CreateServlet" method="post">
		<p>Please fill out some information to help get you started.</p>
		<p>
			User Name: <input type="text" name="username" />
		</p>
		<p>
			Password: <input type="text" name="password" />
		</p>
		<p>
			First Name:<input type="text" name="firstname" />
		</p>
		<p>
			Last Name: <input type="text" name="lastname" />
		</p>
		<p>
			Email Address: <input type="text" name="email" />
		</p>
		<p>
			Profile Picture (url): <input type="text" name="profilepicture" />
		</p>
		<BUTTON name="CreateProfile" value="Login" type="submit">
			Create Profile</BUTTON>
	</form>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
	
</body>
</html>