<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Quiz.*, quizsite.*, users.*, connection.*, java.sql.*, java.util.*, java.io.IOException, java.text.DecimalFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz Home Page</title>
</head>
<body>
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedheader.html") %>

	<%!	
	public void printResultSet(JspWriter out, ResultSet rs, int possiblePoints) throws IOException, SQLException{
		if(rs == null){
			out.println("Oops! Something went wrong! <br>");
			return;
		}
		
		rs.last();
		int numScores = rs.getRow();
		rs.beforeFirst();
		if(numScores>0){
			DecimalFormat df = new DecimalFormat("#.00");
			String[] columnNames = new String[]{"User ID", "Score", "Time", "Date Taken"};
			String[][] table = new String[numScores][columnNames.length];
			int rowInd = 0;
			while(rs.next()){
				try{
					table[rowInd][0] = "" + rs.getInt("userid");
					int score = rs.getInt("score");
					double percent = 100*((double)score/(double)possiblePoints);
					table[rowInd][1] = df.format(percent) + "%";
					table[rowInd][2] = rs.getInt("time") + "s";
					table[rowInd][3] = FormatDateTime.getUserDateTime(rs.getString("datetaken"))[0];
					rowInd++;
				} catch (SQLException ignored){}
			}
			out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(table, columnNames));
		}else{
			out.println("These statistics have not yet been populated! <br>");
		}
	}
	%>
	
	<%!
	public String printStats(double num, int possiblePoints){
		String str = "N/A";
		if(num != -1){
			DecimalFormat df = new DecimalFormat("#.00");
			str = df.format(100*((double)num/(double)possiblePoints)) + "%";
		}
		return str;
	}
	%>


	<%
	ServletContext sc = request.getServletContext();
	DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
	ScoreManager scoreManager = (ScoreManager) sc.getAttribute("ScoreManager");
	Quiz quiz = (Quiz) request.getSession().getAttribute(ShowQuizServlet.QUIZ);
	User quizAuthor = new User(quiz.authorid, new UserConnection(dc));
	double[] stats = scoreManager.getSummaryStats(quiz.id);
	 %>

	<h1>Quiz Summary</h1>
	Name: <%=quiz.name%><br>
	Description: <%=quiz.description%><br>
	Creator: <%=quizAuthor.firstname%> <%=quizAuthor.lastname%><br>
	Created on: <%= FormatDateTime.getUserDateTime(quiz.datemade)[0] %><br>
	Average Score: <%=printStats(stats[0], quiz.numPointsPossible())%><br>
	High Score: <%=printStats(stats[1], quiz.numPointsPossible())%><br>
	Low Score: <%=printStats(stats[2], quiz.numPointsPossible())%><br>
	<a href="ShowQuizServlet">Take this quiz!</a><br>
		
	<h1>Overall High Scores</h1>
	<%
		ResultSet rs = scoreManager.getHighScores(quiz.id);
	 	printResultSet(out, rs, quiz.numPointsPossible());
	%>
	
	<h1>Today's High Scores</h1>
	<%
		rs = scoreManager.getHighScoresToday(quiz.id);
	 	printResultSet(out, rs, quiz.numPointsPossible());
	%>
	
	<h1>You High Scores</h1>
	<%
		User currUser = (User) request.getSession().getAttribute("user");
		rs = scoreManager.getUserScores(quiz.id, currUser.id);
	 	printResultSet(out, rs, quiz.numPointsPossible());
	%>
	
	<h1>Most Recent Quiz Scores</h1>
	<%
		rs = scoreManager.getRecentScores(quiz.id);
	 	printResultSet(out, rs, quiz.numPointsPossible());
	%>
	
	<h1>Overall Low Scores</h1>
	<%
		rs = scoreManager.getLowScores(quiz.id);
	 	printResultSet(out, rs, quiz.numPointsPossible());
	%>
	
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
	
</body>
</html>