<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Quiz.*, quizsite.*, users.*, connection.*, java.sql.*, java.util.*, java.io.IOException, java.text.DecimalFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Profile</title>
</head>
<body>
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session, (DatabaseConnection) application.getAttribute("DatabaseConnection"))  %>
	
	<%
	int userID = Integer.parseInt(request.getParameter("userid"));
	ServletContext sc = request.getServletContext();
	DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
	ScoreManager scoreManager = (ScoreManager) sc.getAttribute("ScoreManager");
	User user = new User(userID, new UserConnection(dc));
	 %>

	<%
	//display user thumbnail
	String query = (String) user.userconnection.getAttribute("username", user.id);
	if (query != null && !query.isEmpty()) {
		if (dc != null) {
			ResultSet rs = users.SearchForUsers.basicSearch(dc,query);
			out.println(sharedHtmlGenerators.HtmlUserThumbnailGenerator.getHtml(rs,dc,session));
		}
	}
	
	//quizzes taken table
	out.println("<h1> Recently Taken Quizzes </h1>");
	String[] colNames = new String[]{"Quiz Name","Date Taken","Score","Time"};
	String[][] takenData = new String[user.quizzestaken.size()][colNames.length];
	int numRecentTaken = 0;
	if (user.quizzestaken.size() > 0){
		for(int i = 0; i < user.quizzestaken.size(); i++){
			if(FormatDateTime.isRecent(user.quizzestaken.get(i).datetaken)){
				numRecentTaken++;
				Integer id = user.quizzestaken.get(i).quizid;
				String quizname = Quiz.getName(id,dc);
				takenData[i][0] = "<a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a>";
				takenData[i][1] = FormatDateTime.getUserDateTime(user.quizzestaken.get(i).datetaken)[0];
				takenData[i][2] = user.quizzestaken.get(i).score.toString();
				takenData[i][3] = String.valueOf(user.quizzestaken.get(i).time);
			}
		}
	}
	if (numRecentTaken > 0){
		
		out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(Arrays.copyOfRange(takenData,0,numRecentTaken),colNames));
	} else {
		out.println("No Recently Taken Quizzes");
	}
	
	//quizzes made table
	out.println("<h1> Recently Made Quizzes </h1>");
	out.println("<form method=\"get\" action=\"QuizHomepageServlet\">");
	String[] columnNames = new String[]{"Quiz Name","Date Made"};
	String[][] madeData = new String[user.quizzesmade.size()][columnNames.length];
	
	int numRecentMade = 0;
	if (user.quizzesmade.size() > 0){
		for(int i = 0; i < user.quizzesmade.size(); i++){
			if(FormatDateTime.isRecent(user.quizzesmade.get(i).date)){
				numRecentMade++;
				Integer id = user.quizzesmade.get(i).quizid;
				String quizname = Quiz.getName(id,dc);
				madeData[i][0] = "<a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a>";
				madeData[i][1] = FormatDateTime.getUserDateTime(user.quizzesmade.get(i).date)[0];
			}
		}
	}
	out.println("</form>");
	
	if (numRecentMade > 0){
		out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(Arrays.copyOfRange(madeData,0,numRecentMade),columnNames));
	} else {
		out.println("No Recently Created Quizzes");
	}
	
	
	//achievements table
	out.println("<h1> Achievements </h1>");
	String[] cNames = new String[]{"Name","Description","Date Achieved","Badge Earned"};
	String[][] achieveData = new String[user.achievements.size()][cNames.length];
	
	ArrayList<AchievementType> achievementtypes = (ArrayList<AchievementType>) sc.getAttribute("achievementtypes");
	
	for(int i = 0; i < user.achievements.size(); i++){
		achieveData[i][0] = achievementtypes.get(user.achievements.get(i).type).name;
		achieveData[i][1] = achievementtypes.get(user.achievements.get(i).type).description;
		achieveData[i][2] = FormatDateTime.getUserDateTime(user.achievements.get(i).dateachieved)[0];
		achieveData[i][3] = "<img src = \""+achievementtypes.get(user.achievements.get(i).type).icon+"\" height = \"50\" width = \"50\"";
	}
	if (user.achievements.size() > 0){
		out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(achieveData,cNames));
	} else {
		out.println("No Achievements have been earned.");
	}
	%>
	
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
	
</body>
</html>
