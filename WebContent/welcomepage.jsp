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
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session)  %>
<% 
	User user = (User) session.getAttribute("user");
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


	//get popular quizzes
	int limit = 10;
	out.println("<h2>Most Popular Quizzes </h2>");
	String[][] popularQuizzes = Quiz.getPopularQuizzes(dc,limit);
	out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(popularQuizzes));
	
	//get recent quizzes
	out.println("<h2>Recently Created Quizzes </h2>");
	String[][] recentQuizzes = Quiz.getRecentQuizzes(dc,limit);
	out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(recentQuizzes));
	%>
	
	<form method ="post" action = "quizSearch.jsp">
	<input type = "submit" name = "Find Quizzes" value = "Find Quizzes">
	</form>
	<% 
	//User's Info
	out.println("<h2> My Quiz Info </h2>");
	//quizzes taken table
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
		out.println("<p>Recently Taken Quizzes</p>");
		out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(Arrays.copyOfRange(takenData,0,numRecentTaken),colNames));
	} else {
		out.println("<p>No Recently Taken Quizzes");
	}
	
	//quizzes made table
	//create new quiz
	out.println("<form method=\"get\" action=\"CreateQuizServlet\">");
	out.println("<p><input type=\"submit\" value=\"Create New Quiz\" /></p>");
	out.println("</form>");
	//out.println("<ul type = \"circle\">");
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
				//out.println("<li>"+user.quizzesmade.get(i).toString()+"<button name =\"quizid\" type=\"submit\" value=\""+id+"\"> "+id+" </button></li>");
				//out.println("<li> Quiz Name: <a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a>    " + user.quizzesmade.get(i).toString()+ "</li>");
				madeData[i][0] = "<a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a>";
				madeData[i][1] = FormatDateTime.getUserDateTime(user.quizzesmade.get(i).date)[0];
			}
		}
	}
	out.println("</form>");
	//out.println("</ul>");
	
	if (numRecentMade > 0){
		out.println("<p>Recently Created Quizzes</p>");
		out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(Arrays.copyOfRange(madeData,0,numRecentMade),columnNames));
	} else {
		out.println("<p>No Recently Created Quizzes</p>");
	}
	
	
	//achievements table
	out.println("<p> Achievements: " + user.achievements.size() + "</p>");	
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
	
	
	//messages
	out.println("<h2> Messages </h2>");
	int count = 0;
	ArrayList<Integer> newmessageNums = new ArrayList<Integer>();
	ArrayList<String> newMessages = new ArrayList<String>();
	for (int i = 0; i < user.messages.size(); i++){
		//determine if new message
		if (user.messages.get(i).opened == false && user.messages.get(i).receiverid == user.id  && user.messages.get(i).type == 2){
			newmessageNums.add(i);
			count++;
			newMessages.add(user.messages.get(i).toString());
		}
	}
	out.println("<p> You have " + count + " new Messages</p>");
	out.println("<ul type = \"circle\">");
	for(int i = 0; i < newMessages.size(); i++){
		out.println("<li><a href=\"checkmessage.jsp?messageNum="+newmessageNums.get(i)+"\"> From: "+user.userconnection.getAttribute("username",user.messages.get(newmessageNums.get(i)).senderid)+newMessages.get(i).toString()+"</a></li>");
	}
	out.println("</ul>");
	
	//view all messages button
	out.println("<p><a href=\"checkmessage.jsp?messageNum=-1\">View All Messages</a></li>");
%>

<h3>Create New Message</h3>
<form method="post" action="CreateMessageServlet">
<p>User name: <input type="text" name="receiverUsername"></p>
<p>Subject: <input type="text" name="subject"></p>
<p>Body: <textarea name="body" cols="50" rows="10"></textarea></p>
<p><input type="submit" value="Send Message" /></p>
<input name="type" type="hidden" value= "2" />
</form>

</body>

<h2> Friends </h2>
<form method ="post" action = "users.jsp">
<input type = "submit" name = "Find Friends" value = "Find Friends">
</form>
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
		
//	determine if user has responded
// 	if (user.messages.get(i).replied == 0 && user.messages.get(i).receiverid == user.id  && user.messages.get(i).type == 0){
// 		newFriendID.add(user.messages.get(i).senderid);
// 		messageNum.add(i);
// 		countFriendReq++;
// 		newFriendReq.add(user.messages.get(i));
// 		}
// 	}
// 	out.println("<p> You have " + countFriendReq + " new Friend Requests</p>");
// 	out.println("<ul type = \"circle\">");
// 	for(int i = 0; i < newFriendReq.size(); i++){
// 		out.println("<form method=\"post\" action=\"FriendRequestServlet\">");
// 		out.println("<li> From: <a href=\"profile.jsp?userID="+newFriendID.get(i)+"\">"+ user.userconnection.getAttribute("username",newFriendID.get(i))+"</a> On: "+FormatDateTime.getUserDate(newFriendReq.get(i).datesent));		
// 		out.println("<input type=\"submit\" name = \"friendreq\" value = \"accept\"/>   <input type=\"submit\" name = \"friendreq\" value = \"decline\">");
// 		out.println("<input type=\"hidden\" name = \"msg_friendIDs\" value = \""+messageNum.get(i)+","+newFriendID.get(i)+"\"");		out.println("</form>");
// 		out.println("</li>");	
// 		out.println("</form>");
	
// 	}
// 	out.println("</ul>"); 
	

	out.println("<h3> Friend Activity </h3>");
	ArrayList<String> activity = Friend.getFriendActivity(user,dc,achievementtypes);
	if ( activity.size() > 0){
		out.println("<ul type = \"circle\">");
		for (int i = 0; i <activity.size(); i++){
			out.println("<li>"+activity.get(i)+"</li>");
		}
		out.println("</ul>");
	} else {
		out.println("<p>No Recent Friend Activity <p>");
	}
	
	%>

<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>


</html>
