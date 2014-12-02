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
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session, (quizsite.DatabaseConnection) application.getAttribute("DatabaseConnection"))  %>
	<div class="mainBody">
		<div class="searchFrame">
			<div class="searchinstructions" >Search for Users</div>
			<form class="advancedSearchForm" method="get" action="users-advanced.jsp" >
				<span class="criteriaSelect" >Match <select name="criterionSearch" id="criterionDropdown" ><option value="any">any</option><option value="all">all</option></select> of the following criteria:</span>
				
				<div class="searchInputs">
					<div class="searchBlock leftSearchBlock">
						<span class="searchlabel">First Name</span>
						<input type="text" name="firstNameSearch" id="firstNameTextBox" class="searchField" value="" />
					</div>
					<div class="searchBlock">
						<span class="searchlabel">Last Name</span>
						<input type="text" name="lastNameSearch" id="lastNameTextBox" class="searchField" value="" />
					</div>
					<div class="searchBlock leftSearchBlock">
						<span class="searchlabel">Username</span>
						<input type="text" name="usernameSearch" id="usernameTextBox" class="searchField" value="" />
					</div>
					<div class="searchBlock">
						<span class="searchlabel">Email</span>
						<input type="text" name="emailSearch" id="emailTextBox" class="searchField longSearchField" value="" />
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
			String criterionQuery = request.getParameter("criterionSearch");
			String firstNameQuery = request.getParameter("firstNameSearch");
			String lastNameQuery = request.getParameter("lastNameSearch");
			String usernameQuery = request.getParameter("usernameSearch");
			String emailQuery = request.getParameter("emailSearch");
			if (criterionQuery == null) criterionQuery = "any";
			if (!criterionQuery.equals("any") && !criterionQuery.equals("all")) criterionQuery = "any";
			if (firstNameQuery == null) firstNameQuery= "";
			if (lastNameQuery == null) lastNameQuery= "";
			if (usernameQuery == null) usernameQuery= "";
			if (emailQuery == null) emailQuery= "";
			
			// Autopopulate the text fields
			out.println("<pre><script>");
			out.println("document.getElementById('criterionDropdown').value = \"" + criterionQuery + "\";");
			out.println("document.getElementById('firstNameTextBox').value = \"" + firstNameQuery + "\";");
			out.println("document.getElementById('lastNameTextBox').value = \"" + lastNameQuery + "\";");
			out.println("document.getElementById('usernameTextBox').value = \"" + usernameQuery + "\";");
			out.println("document.getElementById('emailTextBox').value = \"" + emailQuery + "\";");
			out.println("</script></pre>");
			
			// Get search results for this query
			if (!firstNameQuery.isEmpty() || !lastNameQuery.isEmpty() || !usernameQuery.isEmpty() || !emailQuery.isEmpty()) {
				// User has entered at least one search term
				boolean andTerms = false;
				if (criterionQuery.equals("all")) andTerms = true;
				
				// Search for the user's parameters
				quizsite.DatabaseConnection dc = (quizsite.DatabaseConnection) application.getAttribute("DatabaseConnection");
				if (dc != null) {
					ResultSet rs = users.SearchForUsers.advancedSearch(dc,usernameQuery,firstNameQuery,lastNameQuery,emailQuery,andTerms);
					if (rs != null) {
						out.println(sharedHtmlGenerators.HtmlUserThumbnailGenerator.getHtml(rs,dc,session));
					}
				}
			}
		%>

	</div>
	
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>