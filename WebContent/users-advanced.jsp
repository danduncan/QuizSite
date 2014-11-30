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
			<div class="searchinstructions" >Search for Users</div>
			<form class="advancedSearchForm" method="get" action="users-advanced.jsp" >
				<span class="criteriaSelect" >Match <select><option value="any">any</option><option value="all">all</option></select> of the following criteria:</span>
				
				<div class="searchInputs">
					<div class="searchBlock leftSearchBlock">
						<span class="searchlabel">First Name</span>
						<input type="text" name="firstName" class="searchField" />
					</div>
					<div class="searchBlock">
						<span class="searchlabel">Last Name</span>
						<input type="text" name="lastName" class="searchField" />
					</div>
					<div class="searchBlock leftSearchBlock">
						<span class="searchlabel">Username</span>
						<input type="text" name="username" class="searchField" />
					</div>
					<div class="searchBlock">
						<span class="searchlabel">Email</span>
						<input type="text" name="email" class="searchField longSearchField" />
					</div>
					
				</div>	
				<input type="submit" value="Advanced Search" class="searchbutton" />
			</form>

			<div class="advancedsearchlink" >
				<a href="users.jsp" >Basic Search</a>
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
			if (query != null && !query.isEmpty()) {
				//out.println("User query = \"" + query + "\";");
				quizsite.DatabaseConnection dc = (quizsite.DatabaseConnection) application.getAttribute("DatabaseConnection");
				if (dc != null) {
					ResultSet rs = users.SearchForUsers.basicSearch(dc,query);
					//out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(rs));
					out.println(sharedHtmlGenerators.HtmlUserThumbnailGenerator.getHtml(rs,dc,session));
				}
			}
		%>

	</div>
	
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>