<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ page import="java.util.*, users.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> Create Profile </title>
</head>
<body>
<h1> Create your profile </h1>
<form action="CreateServlet" method="post">
<p>Please fill out some information to help get you started. </p>
<p>User Name: <input type="text" name="username" /> </p>
<p>Password: <input type="text" name="password" /> </p>
<p>First Name:<input type="text" name="firstname" /> </p>
<p>Last Name: <input type="text" name="lastname" /> </p>
<p> Email Address: <input type="text" name="email" /> </p>
<p> Profile Picture (url): <input type="text" name="profilepicture" /> </p>
<BUTTON name="CreateProfile" value="Login" type="submit"> Create Profile </BUTTON>
</form>
</body>
</html>