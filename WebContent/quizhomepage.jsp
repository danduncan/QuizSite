<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="sharedHtmlGenerators.*, Quiz.*, quizsite.*, users.*, connection.*, java.sql.*, java.util.*, java.io.IOException, java.text.DecimalFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Quiz Home Page</title>
</head>
<body>
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session, (quizsite.DatabaseConnection) application.getAttribute("DatabaseConnection"))  %>
	
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
	SiteManager sm = (SiteManager) sc.getAttribute("SiteManager");
	Quiz quiz = (Quiz) request.getSession().getAttribute(ShowQuizServlet.QUIZ);
	User quizAuthor = new User(quiz.authorid, new UserConnection(dc));
	double[] stats = scoreManager.getSummaryStats(quiz.id);
	
	ResultSet rs = SearchForQuizzes.getQuizByID(dc,quiz.id);
	rs.first();
	out.println(sharedHtmlGenerators.HtmlQuizThumbnailGenerator.getThumbnail(rs));
	
	User user = (User)request.getSession().getAttribute("user");
	List<Friend> friends = user.friends;
	String friend = request.getParameter("challengeFriend");
	if(friend != null){
		int id = sm.popNextMessageID();
		int type = 1;
		String date = FormatDateTime.getCurrentSystemDate();
		String time = FormatDateTime.getCurrentSystemTime();
		Boolean opened = false;
		int replied = 0;
		Message challenge = new Message(id, type, date, time, user.id, Integer.parseInt(friend), opened, replied,"",""+quiz.id);
		CreateMessageServlet.updateDatabase(challenge,dc);
		user.messages.add(challenge);
		user.updateUserDatabase();
		request.getSession().setAttribute("user", user);
	}
	
	
	%>
	<h1><a href="ShowQuizServlet">Take this quiz!</a><br></h1>
	
	<h1>Challenge Friends</h1>
	<form action="quizhomepage.jsp">
		<select name="challengeFriend">
			<%
			for(int i = 0 ; i<friends.size(); i++){
				Friend curr = friends.get(i);
				User currUser = new User(curr.friend2, new UserConnection(dc));
				out.println("<option value=\""+currUser.id +"\">"+currUser.firstname+" "+currUser.lastname +"</option>");
			}
			
			%>
		</select>
		<input type="submit" value="Challenge Friend!">	
	</form>
	
	<h1>Quiz Statistics</h1>
	Average Score: <%=printStats(stats[0], quiz.numPointsPossible())%><br>
	High Score: <%=printStats(stats[1], quiz.numPointsPossible())%><br>
	Low Score: <%=printStats(stats[2], quiz.numPointsPossible())%><br>
		
	<h1>Overall High Scores</h1>
	<%
		rs = scoreManager.getHighScores(quiz.id);
		ScoreManager.printScoresToJSP(out, rs, quiz.numPointsPossible(), dc);
	%>
	
	<h1>Today's High Scores</h1>
	<%
		rs = scoreManager.getHighScoresToday(quiz.id);
		ScoreManager.printScoresToJSP(out, rs, quiz.numPointsPossible(), dc);
	%>
	
	<h1>Your High Scores</h1>
	<%
		User currUser = (User) request.getSession().getAttribute("user");
		rs = scoreManager.getUserScores(quiz.id, currUser.id);
		ScoreManager.printScoresToJSP(out, rs, quiz.numPointsPossible(), dc);
	%>
	
	<h1>Most Recent Quiz Scores</h1>
	<%
		rs = scoreManager.getRecentScores(quiz.id);
		ScoreManager.printScoresToJSP(out, rs, quiz.numPointsPossible(), dc);
	%>
	
	<h1>Overall Low Scores</h1>
	<%
		rs = scoreManager.getLowScores(quiz.id);
		ScoreManager.printScoresToJSP(out, rs, quiz.numPointsPossible(), dc);
	%>
	
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
	
</body>
</html>