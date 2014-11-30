<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<link href="/QuizSite/stylesheets/users.css" 
	type="text/css" 
	rel="stylesheet" 
	id="signinStylesheet" />
</head>
<body>
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session)  %>
	<% String searchParam = "search"; %>
	<div class="mainBody">
		<div class="searchFrame">
			<div class="searchinstructions" >Search for Quizzes</div>
			<form method="get" action="quizSearch.jsp" >
				<span class="searchlabel" >Search quiz names, descriptions, and author usernames: </span>
					<input type="text" name="<% out.print(searchParam); %>" id="basicSearchTextBox" class="searchtextbox" >
					<input type="submit" value="Search" class="searchbutton" />
			</form>

			<div class="advancedsearchlink" >
				<a href="quizSearch.jsp" >Advanced Search</a>
			</div>
		</div>
		
		<%
			Integer uid = (Integer) session.getAttribute("userid");
			if (uid != null) {
				out.println("<script>useridjs = " + uid + "</script>");
			} else {
				out.println("<script>useridjs = -1</script>");
			}
		%>
		
		<%
			// Get user's original search query in the query string
			String query = request.getParameter(searchParam);
			if (query == null) query = "";
			
			// Autopopulate the text box
			out.println("<script>");
			out.println("document.getElementById('basicSearchTextBox').value=\"" + query + "\";");
			out.println("</script>");
			
			if (query != null && !query.isEmpty()) {
				//out.println("User query = \"" + query + "\";");
				quizsite.DatabaseConnection dc = (quizsite.DatabaseConnection) application.getAttribute("DatabaseConnection");
				if (dc != null) {
					ResultSet rs = Quiz.SearchForQuizzes.basicSearch(dc,query);
					out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(rs));
					//out.println(sharedHtmlGenerators.HtmlUserThumbnailGenerator.getHtml(rs,dc,session));
				}
			}
		%>

	</div>
	
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>