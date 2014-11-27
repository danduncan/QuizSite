<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*, users.*,quizsite.*,Quiz.*"%>

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
	
	out.println("<h2> Quiz Info </h2>");
	//quizzes taken table
	out.println("<p> Quizzes Taken: "+user.quizzestaken.size()+ "</p>");
	String[] colNames = new String[]{"Quiz Name","Date Taken","Score","Time"};
	String[][] takenData = new String[user.quizzestaken.size()][colNames.length];

	if (user.quizzestaken.size() > 0){
		for(int i = 0; i < user.quizzestaken.size(); i++){
			Integer id = user.quizzestaken.get(i).quizid;
			String quizname = Quiz.getName(id,dc);
			takenData[i][0] = "<a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a>";
			takenData[i][1] = FormatDateTime.getUserDateTime(user.quizzestaken.get(i).datetaken)[0];
			takenData[i][2] = user.quizzestaken.get(i).score.toString();
			takenData[i][3] = String.valueOf(user.quizzestaken.get(i).time);

		}
	}
	out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(takenData,colNames));
	
	//quizzes made table
	out.println("<p> Quizzes Made: " + user.quizzesmade.size() + "</p>");
	//create new quiz
	out.println("<form method=\"get\" action=\"CreateQuizServlet\">");
	out.println("<p><input type=\"submit\" value=\"Create New Quiz\" /></p>");
	out.println("</form>");
	//out.println("<ul type = \"circle\">");
	out.println("<form method=\"get\" action=\"QuizHomepageServlet\">");
	
	String[] columnNames = new String[]{"Quiz Name","Date Made"};
	String[][] madeData = new String[user.quizzesmade.size()][columnNames.length];
	
	
	if (user.quizzesmade.size() > 0){
		for(int i = 0; i < user.quizzesmade.size(); i++){
			Integer id = user.quizzesmade.get(i).quizid;
			String quizname = Quiz.getName(id,dc);
			//out.println("<li>"+user.quizzesmade.get(i).toString()+"<button name =\"quizid\" type=\"submit\" value=\""+id+"\"> "+id+" </button></li>");
			//out.println("<li> Quiz Name: <a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a>    " + user.quizzesmade.get(i).toString()+ "</li>");
			madeData[i][0] = "<a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a>";
			madeData[i][1] = FormatDateTime.getUserDateTime(user.quizzesmade.get(i).date)[0];
		}
	}
	out.println("</form>");
	//out.println("</ul>");
	out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(madeData,columnNames));

	
	
	//achievements table
	out.println("<p> Achievements: " + user.achievements.size() + "</p>");
	
	String[] cNames = new String[]{"Name","Description","Date Achieved"};
	String[][] achieveData = new String[user.achievements.size()][cNames.length];
	
	ArrayList<AchievementType> achievementtypes = (ArrayList<AchievementType>) sc.getAttribute("achievementtypes");
	
	for(int i = 0; i < user.achievements.size(); i++){
		achieveData[i][0] = achievementtypes.get(user.achievements.get(i).type).name;
		achieveData[i][1] = achievementtypes.get(user.achievements.get(i).type).description;
		achieveData[i][2] = FormatDateTime.getUserDateTime(user.achievements.get(i).dateachieved)[0];            
	}
	out.println(sharedHtmlGenerators.HtmlTableGenerator.getHtml(achieveData,cNames));

	
	
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
<p>Body: <textarea name="body" cols="50" rows="10"></textarea>
<p><input type="submit" value="Send Message" /></p>
<input name="type" type="hidden" value= "2"/>
</form>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>

<h2> Friends </h2>
<%	int countFriendReq = 0;
	ArrayList<Integer> messageNum = new ArrayList<Integer>();
	ArrayList<Integer> newFriendID = new ArrayList<Integer>();
	ArrayList<Message> newFriendReq = new ArrayList<Message>();
	for (int i = 0; i < user.messages.size(); i++){
	//determine if new message
	if (user.messages.get(i).opened == false && user.messages.get(i).receiverid == user.id  && user.messages.get(i).type == 0){
		newFriendID.add(user.messages.get(i).senderid);
		messageNum.add(i);
		countFriendReq++;
		newFriendReq.add(user.messages.get(i));
		}
	}
	out.println("<p> You have " + countFriendReq + " new Friend Requests</p>");
	out.println("<ul type = \"circle\">");
	for(int i = 0; i < newFriendReq.size(); i++){
		out.println("<li> From: <a href=\"profile.jsp?userID="+newFriendID.get(i)+"\">"+ user.userconnection.getAttribute("username",newFriendID.get(i))+"</a> On: "+newFriendReq.get(i).datesent);
		out.println("<form method=\"post\" action=\"FriendRequestServlet\">");
		out.println("<input type=\"submit\" name = \"friendreq\" value = \"accept\"/>   <input type=\"submit\" name = \"friendreq\" value = \"decline\">");
		out.println("<input type=\"hidden\" name = \"friendID\" value = \""+newFriendID.get(i)+"\"");
		out.println("<input type=\"hidden\" name = \"messageNum\" value = \""+messageNum.get(i)+"\"");
		out.println("</form>");
		out.println("</li>");	
	
	}
	out.println("</ul>"); %>


</html>
