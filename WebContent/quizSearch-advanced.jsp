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
			<div class="searchinstructions" >Search for Quizzes</div>
			<form class="advancedSearchForm" method="get" action="quizSearch-advanced.jsp" >
				<span class="criteriaSelect" >Match <select name="criterionSearch" id="criterionDropdown" ><option value="any">any</option><option value="all">all</option></select> of the following criteria:</span>
				
				<div class="searchInputs">
					<div class="searchBlock leftSearchBlock">
						<span class="searchlabel">Quiz Name</span>
						<input type="text" name="quizNameSearch" id="quizNameTextBox" class="searchField" value="" />
					</div>
					<div class="searchBlock">
						<span class="searchlabel">Author Username</span>
						<input type="text" name="usernameSearch" id="usernameTextBox" class="searchField" value="" />
					</div>
					<div class="searchBlock leftSearchBlock">
						<span class="searchlabel">Quiz Description</span>
						<input type="text" name="descriptionSearch" id="descriptionTextBox" class="searchField longSearchField" value="" />
					</div>					
				</div>	
				<input type="submit" value="Advanced Search" class="searchbutton" />
			</form>

			<div class="advancedsearchlink" >
				<a href="quizSearch.jsp" >Basic Search</a>
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
			String quizNameQuery = request.getParameter("quizNameSearch");
			String usernameQuery = request.getParameter("usernameSearch");
			String descriptionQuery = request.getParameter("descriptionSearch");
			if (criterionQuery == null) criterionQuery = "any";
			if (!criterionQuery.equals("any") && !criterionQuery.equals("all")) criterionQuery = "any";
			if (quizNameQuery == null) quizNameQuery= "";
			if (usernameQuery == null) usernameQuery= "";
			if (descriptionQuery == null) descriptionQuery= "";
			
			// Autopopulate the text fields
			out.println("<script>");
			out.println("document.getElementById('criterionDropdown').value = \"" + criterionQuery + "\";");
			out.println("document.getElementById('quizNameTextBox').value = \"" + quizNameQuery + "\";");
			out.println("document.getElementById('usernameTextBox').value = \"" + usernameQuery + "\";");
			out.println("document.getElementById('descriptionTextBox').value = \"" + descriptionQuery + "\";");
			out.println("</script>");
			
			// Get search results for this query
			if (!quizNameQuery.isEmpty() || !usernameQuery.isEmpty() || !descriptionQuery.isEmpty()) {
				// User has entered at least one search term
				boolean andTerms = false;
				if (criterionQuery.equals("all")) andTerms = true;
				
				// Search for the user's parameters
				quizsite.DatabaseConnection dc = (quizsite.DatabaseConnection) application.getAttribute("DatabaseConnection");
				if (dc != null) {
					ResultSet rs = null;
					rs = Quiz.SearchForQuizzes.advancedSearch(dc,quizNameQuery,usernameQuery,descriptionQuery,andTerms);
					if (rs != null) {
						out.println(sharedHtmlGenerators.HtmlQuizThumbnailGenerator.getHtml(rs));
					}
				}
			}
		%>

	</div>
	
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>