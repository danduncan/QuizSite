<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Account</title>
</head>
<body>
<form action="CreateServlet" method="post">
<% 
String name = request.getParameter("name");

out.println("<h1>The User Name " + name + " is Already in Use</h1>");
%>

<p>Please enter another name and password.
<p>User Name:<input type="text" name="name" /> </p>
<p>Password: <input type="password" name="pswd" /> 
<BUTTON name="Login" value="Login" type="submit"  >Login</BUTTON>
</p>
</form>
</body>
</html>