<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
</head>
<body>
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session)  %>

	<h1>Create New Account</h1>
	<p>Please enter proposed name and password.</p>
	<form method="post" action="CreateServlet">
		<p>
			User Name: <input type="text" name="username">
		</p>
		<p>
			Password: <input type="password" name="password">
		</p>
		<input name="create" type=hidden value="newuser"> <input
			name="create" type=hidden value="newprofile">
		<p>
			<input type="submit" value="Login">
		</p>
	</form>

	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>