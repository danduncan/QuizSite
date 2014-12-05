<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*, users.*,quizsite.*,Quiz.*,java.sql.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<link href="/QuizSite/stylesheets/home.css" 
	type="text/css" 
	rel="stylesheet" 
	id="homeStylesheet" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session, (DatabaseConnection) application.getAttribute("DatabaseConnection"))  %>
	<div class="typicalQwizardMainBody">
<% 
	User user = (User) session.getAttribute("user");
	user.updateUserDatabase();
	ServletContext sc = getServletContext();
	DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
	//welcome back info
	out.println("<title>Welcome "+user.firstname+"!</title>");
	
%>
<% 
	out.println("<h1>Welcome "+user.firstname+"!</h1>");
		
	//display user thumbnail
	String query = (String) user.userconnection.getAttribute("username", user.id);
	if (query != null && !query.isEmpty()) {
		//out.println("User query = \"" + query + "\";");

		if (dc != null) {
			ResultSet rs = users.SearchForUsers.basicSearch(dc,query);
			//out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(rs));
			out.println(sharedHtmlGenerators.HtmlUserThumbnailGenerator.getHtml(rs,dc,session));
		}
	}
%>
<table border = "0">
<tr>

<td><form method ="post" action = "quizSearch.jsp">
<input type = "submit" name = "Find Quizzes" value = "Find Quizzes" class="welcomepageBtn" />
</form></td>

<td><form method="get" action="CreateQuizServlet">
<input type="submit" value="Create New Quiz" class="welcomepageBtn" />
</form></td>

<td><form method ="post" action = "users.jsp">
<input type = "submit" name = "Find Friends" value = "Find Friends" class="welcomepageBtn" />
</form></td>

<td>
<input type = "submit" name = "Find Friends" value = "Send a Message" class="welcomepageBtn" onclick="displayMessage()" />
</td>

</tr>
</table>

<% 	
	//get popular quizzes
	int limit = 10;
	out.println("<h2>Most Popular Quizzes </h2>");
	String[][] popularQuizzes = Quiz.getPopularQuizzes(dc,limit);
	out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(popularQuizzes));
	
	//get recent quizzes
	out.println("<h2>Recently Created Quizzes</h2>");
	String[][] recentQuizzes = Quiz.getRecentQuizzes(dc,limit);
	out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(recentQuizzes));
	%>
	
	<% 
	//User's Info
	out.println("<h2> My Quiz Activity </h2>");
	//recent quizzes taken table
	String[] colNames = new String[]{"Quiz Name","Date Taken","Score","Time"};
	
	ArrayList<Integer> recent = new ArrayList<Integer>();
	int numRecentTaken = 0;
	if (user.quizzestaken.size() > 0){
		//find indexes of recent quizzes
		for(int i = 0; i < user.quizzestaken.size(); i++){
			if(FormatDateTime.isRecent(user.quizzestaken.get(i).datetaken)){				 
				recent.add(i); 				
			}
		}
		//construct 2d string array
		String[][] takenData = new String[recent.size()][colNames.length];
		for (int i = 0; i < recent.size(); i++){
			Integer id = user.quizzestaken.get(recent.get(i)).quizid;
			String quizname = Quiz.getName(id,dc);
			takenData[i][0] = "<a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a>";
			takenData[i][1] = FormatDateTime.getUserDateTime(user.quizzestaken.get(recent.get(i)).datetaken)[0];
			takenData[i][2] = user.quizzestaken.get(recent.get(i)).score.toString();
			takenData[i][3] = String.valueOf((int) user.quizzestaken.get(recent.get(i)).time)+"s";	
		}
		if (recent.size() > 0){
			out.println("<h3>Recently Taken Quizzes</h3>");
			out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(takenData,colNames));
		} else {
			out.println("<h3>No Recently Taken Quizzes</h3>");
		}
	} else { out.println("<h3>No Taken Quizzes</h3>");}

	
	
	//quizzes made table
	//out.println("<ul type = \"circle\">");
	out.println("<form method=\"get\" action=\"QuizHomepageServlet\">");
	
	String[] columnNames = new String[]{"Quiz Name","Date Made"};
		
	ArrayList<Integer> recentMade = new ArrayList<Integer>();
	if (user.quizzesmade.size() > 0){
		for(int i = 0; i < user.quizzesmade.size(); i++){
			if(FormatDateTime.isRecent(user.quizzesmade.get(i).date)){
				recentMade.add(i);
			}
		}
		String[][] madeData = new String[recentMade.size()][columnNames.length];
		for (int i = 0; i < recentMade.size(); i++){
			Integer id = user.quizzesmade.get(recentMade.get(i)).quizid;
			String quizname = Quiz.getName(id,dc);
			//out.println("<li>"+user.quizzesmade.get(i).toString()+"<button name =\"quizid\" type=\"submit\" value=\""+id+"\"> "+id+" </button></li>");
			//out.println("<li> Quiz Name: <a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a>    " + user.quizzesmade.get(i).toString()+ "</li>");
			madeData[i][0] = "<a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a>";
			madeData[i][1] = FormatDateTime.getUserDateTime(user.quizzesmade.get(recentMade.get(i)).date)[0];
		}
		if (recentMade.size() > 0){
			out.println("<h3>Recently Created Quizzes</h3>");
			out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(madeData,columnNames));
		} else {
			out.println("<h3>No Recently Created Quizzes</h3>");
		}
	} else { out.println("<h3>No Created Quizzes");
	}
	out.println("</form>");
	//out.println("</ul>");
	

	
	
	//achievements table
	out.println("<h2>Achievements</h2>");	
	String[] cNames = new String[]{"Name","Description","Date Achieved","Badge Earned"};
	String[][] achieveData = new String[user.achievements.size()][cNames.length];
	
	ArrayList<AchievementType> achievementtypes = (ArrayList<AchievementType>) sc.getAttribute("achievementtypes");
	
	for(int i = 0; i < user.achievements.size(); i++){
		achieveData[i][0] = achievementtypes.get(user.achievements.get(i).type).name;
		achieveData[i][1] = achievementtypes.get(user.achievements.get(i).type).description;
		achieveData[i][2] = FormatDateTime.getUserDateTime(user.achievements.get(i).dateachieved)[0];
		achieveData[i][3] = "<img src = \""+achievementtypes.get(user.achievements.get(i).type).icon+"\" height = \"50\" width = \"50\"";
		//System.out.println(achievementtypes.get(user.achievements.get(i).type).icon);
	}
	if (user.achievements.size() > 0){
	out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(achieveData,cNames));
	}
	
%>

<!-- <p>Create New Message</p>
<form method="post" action="CreateMessageServlet">
<p>User name: <input type="text" name="receiverUsername"></p>
<p>Subject: <input type="text" name="subject"></p>
<p>Body: <textarea name="body" cols="50" rows="10"></textarea></p>
<p><input type="submit" value="Send Message" /></p>
<input name="type" type="hidden" value= "2" />
</form> -->

<div class="inboxHeader"><h2><img src="https://cdn4.iconfinder.com/data/icons/REALVISTA/mail_icons/png/400/inbox.png" alt="HTML5 Icon"> Inbox </h2> </div>
<h3>Friend Requests</h3>

<%	
	ArrayList<Integer> friendIDs = Friend.getFriendRequestIDs(dc,user.id);
	out.println("<p> You have " + friendIDs.size() + " Friend Requests</p>");
	
	//add dan's friend thumbnails as incoming requests
	//get users name as query
	for (int i = 0; i < friendIDs.size(); i++){
		String newquery = (String) user.userconnection.getAttribute("username", friendIDs.get(i));
		if (newquery != null && !newquery.isEmpty()) {
		//out.println("User query = \"" + query + "\";");

			if (dc != null) {
				ResultSet rs = users.SearchForUsers.basicSearch(dc,newquery);
				//out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(rs));
				out.println(sharedHtmlGenerators.HtmlUserThumbnailGenerator.getHtml(rs,dc,session));
			}
		}
	}
	
	out.println("<h3>Notes</h3>");
	int count = 0;
	ArrayList<Integer> newmessageNums = new ArrayList<Integer>();
	ArrayList<String> newMessages = new ArrayList<String>();
	List<Message> messages = user.getMessages();
	for (int i = 0; i < messages.size(); i++){
		//determine if new message
		if (messages.get(i).opened == false && messages.get(i).receiverid == user.id  && messages.get(i).type == 2){
			newmessageNums.add(i);
			count++;
			newMessages.add(messages.get(i).toString());
		}
	}
	out.println("<p> You have " + count + " new Messages</p>");
	out.println("<ul type = \"circle\">");
	for(int i = 0; i < newMessages.size(); i++){
		out.println("<li><a href=\"checkmessage.jsp?messageNum="+newmessageNums.get(i)+"\"> From: "+user.userconnection.getAttribute("username",user.messages.get(newmessageNums.get(i)).senderid)+newMessages.get(i).toString()+"</a>");
		out.println("<input type=\"submit\" class=\"msgBtn\" value=\"Reply\" onclick=\"displayMessage('" + user.userconnection.getAttribute("username",user.messages.get(newmessageNums.get(i)).senderid) + "')\" />");
		out.println("</li>");
	}
	out.println("</ul>");
	
	//view all messages button
	out.println("<p><a href=\"checkmessage.jsp?messageNum=-1\">View All Messages</a></li>");
%>
<h3>Challenges</h3>

		
<% 		
//challenges setup
//	determine if user has responded
	ArrayList<Message> newChallenge = new ArrayList<Message>();
	List<Message> messageList = user.getMessages();
	for (int i = 0; i < messageList.size(); i++){
 		if (messageList.get(i).replied == 0 && messageList.get(i).receiverid == user.id  && messageList.get(i).type == 1){
			newChallenge.add(messageList.get(i));
 		}
  	}
 	out.println("<p> You have " + newChallenge.size() + " new challenges</p>");
 	//out.println("<ul type = \"circle\">");
 	out.println("<table>");
 	for(int i = 0; i < newChallenge.size(); i++){
 		//out.println("<li>");
 		out.println("<tr>");
 		out.println("<form method=\"post\" action=\"QuizHomepageServlet?quizid="+newChallenge.get(i).body+"\">");
 		out.println("<td><input type=\"hidden\" name = \"messagenum\" value = \""+newChallenge.get(i).id+"\">");
 		out.println("- From: <a href= \"user?userID=" + newChallenge.get(i).senderid+"\">"+ user.userconnection.getAttribute("username",newChallenge.get(i).senderid)+"</a> Quiz: <a href=\"QuizHomepageServlet?quizid="+newChallenge.get(i).body+"\"> "+Quiz.getName(Integer.parseInt(newChallenge.get(i).body),dc)+"</a>  On: " +FormatDateTime.getUserDate(newChallenge.get(i).datesent));
 		out.println("</td><td><input type=\"submit\" name = \"challenge\" value = \"accept\" class=\"challengeBtn acceptChallengeBtn\" />   <input type=\"submit\" name = \"challenge\" class=\"challengeBtn declineChallengeBtn\" value = \"decline\">");
 		out.println("</td></tr></form>");
 		//out.println("</li>");	
	
 	}
 	//out.println("</ul>"); 
	out.println("</table>");

	out.println("<h2> Friend Activity </h2>");
	ArrayList<String> activity = Friend.getFriendActivity(user,dc,achievementtypes);
	if ( activity.size() > 0){
		out.println("<ul type = \"circle\">");
		int len = 10;
		if( activity.size() < len){
			len = activity.size();
		}
		for (int i = 0; i < len; i++){
			out.println("<li>"+activity.get(i)+"</li>");
		}
		out.println("</ul>");
	} else {
		out.println("<p>No Recent Friend Activity <p>");
	}
	
	%>
</div>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>

</body>
</html>
