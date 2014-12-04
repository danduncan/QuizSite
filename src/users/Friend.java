package users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Quiz.Quiz;

import connection.UserConnection;

import quizsite.DatabaseConnection;
import quizsite.FormatDateTime;
import quizsite.MyDBInfo;

public class Friend {
	public Integer friend1;
	public Integer friend2;
	public String datefriended;
	public String groupname;
	//pull friends from database
	public Friend(Integer Friend1, Integer Friend2, String DateFriended, String GroupName){
		friend1 = Friend1;
		friend2 = Friend2;
		datefriended = DateFriended;
		groupname = GroupName;		
	}
	//create new friend
	public Friend(Integer Friend1, Integer Friend2, String GroupName){
		friend1 = Friend1;
		friend2 = Friend2;
		datefriended = FormatDateTime.getCurrentSystemDate();
		groupname = GroupName;		
	}
	
	//create new friend in default group
	public Friend(Integer Friend1, Integer Friend2){
		friend1 = Friend1;
		friend2 = Friend2;
		datefriended = FormatDateTime.getCurrentSystemDate();
		groupname = "Default Group";		
	}
	
	public void insertToDB(DatabaseConnection dc){
		String insert = "INSERT INTO friends VALUES("+friend1+","+friend2+",\""+datefriended+"\",\""+groupname+"\")";
		dc.executeUpdate(insert);
	}
	//get friend activity for user
	public static ArrayList<String> getFriendActivity(User user, DatabaseConnection dc, ArrayList<AchievementType> at) throws SQLException{
		ArrayList<String> activity = new ArrayList<String>();
		
		//get users friendsIDs
		String query = "SELECT friend2 FROM "+MyDBInfo.FRIENDSTABLE+" where friend1 = "+user.id;
		ResultSet rs = dc.executeQuery(query);
		ArrayList<Integer> friendIDs = new ArrayList<Integer>();

		while(rs.next()){
			//Integer ID1 = rs.getInt("friend1");
			Integer ID2 = rs.getInt("friend2");
			
			//if(!friendIDs.contains(ID1)){
				//friendIDs.add(ID1);
			//}
			if(friendIDs != null && !friendIDs.contains(ID2)){
				friendIDs.add(ID2);
			}
		
		}
		
		rs.close();
		
		for (int i = 0; i < friendIDs.size(); i++ ){
			User friend = new User(friendIDs.get(i),new UserConnection(dc));
			//search for recent activity
			//quiz made
			for(int j = 0; j < friend.quizzesmade.size(); j++){
				if (FormatDateTime.isRecent(friend.quizzesmade.get(j).date)){
					Integer id = friend.quizzesmade.get(j).quizid;
					String quizname = Quiz.getName(id,dc);
					activity.add("<a href=\"user?userid="+friend.id+"\">"+ friend.username+"</a> created a new quiz called <a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a>");
				}
			}
			//quiz taken
			for(int j = 0; j < friend.quizzestaken.size(); j++){
				if (FormatDateTime.isRecent(friend.quizzestaken.get(j).datetaken)){
					Integer id = friend.quizzestaken.get(j).quizid;
					String quizname = Quiz.getName(id,dc);
					activity.add("<a href=\"user?userid="+friend.id+"\">"+ friend.username+"</a> took a quiz called <a href=\"QuizHomepageServlet?quizid="+id+"\">"+ quizname+"</a> and got a score of "+friend.quizzestaken.get(j).score+"!");
				}
			}
			
			//Achievement
			for(int j = 0; j < friend.achievements.size(); j++){
				if (FormatDateTime.isRecent(friend.achievements.get(j).dateachieved)){
					activity.add("<a href=\"user?userid="+friend.id+"\">"+ friend.username+"</a> earned a new badge: "+at.get(friend.achievements.get(j).type).name+"  " + "<img src = \""+at.get(friend.achievements.get(j).type).icon+"\" height = \"20\" width = \"20\"");
				}
			}
			
			
		}
		
		return activity;
	}
	
	public static int getNumRequests(DatabaseConnection dc, Integer userid) throws SQLException{
		String query =  "select replied from messages where type = 0 AND receiverid = " + userid; 
		ResultSet rs = dc.executeQuery(query);
		int numRequests = 0;
		while (rs.next()){
			numRequests++;
		}
		return numRequests;
	}
	
	public static ArrayList<Integer> getFriendRequestIDs(DatabaseConnection dc, Integer userid) throws SQLException{
		String query =  "select senderid from messages where type = 0 AND receiverid = " + userid; 
		ResultSet rs = dc.executeQuery(query);
		ArrayList<Integer> friendIDs = new ArrayList<Integer>();
		
		while (rs.next()){
			friendIDs.add(rs.getInt("senderid"));
		}
		return friendIDs;
	}
	
	
	// This resultset contains recent activity, ordered by date (most recent first)
	// The database only returns "limit" entries
	// limit = null or limit <= 0 defaults to infinite limit
	// userid is the id number of the user whose friends' activity you want
	// Format of ResultSet:
	// 	Columns:	| userid | username | date | achType | achName | achIcon | quizid | quizname | quizscore |
	// Three kinds of events are included, and for each event, non-relevant columns are null
	// 		User earned an achievement: quizid == quizname == quizscore == null
	//		User created a quiz:		achType == achName == achIcon == quizscore == null
	//		User took a quiz: 			achType == achName == achIcon == null
	// Query the null values to determine what kind of event each entry was
	private static ResultSet getTheBeast(Integer userid, Integer limit, DatabaseConnection dc) {
		if (userid == null || dc == null) return null;
		
		String theBeast = getBeastlyQuery(userid,limit);
		if (theBeast == null || theBeast.isEmpty()) return null;
		
		ResultSet rs = null;
		rs = dc.executeQuery(theBeast);
		if (rs == null) {
			System.out.println("Friend.getTheBeast(): Beastly query yielded a null ResultSet. Verify the query!");
		}
		return rs;
	}
	
	// Get beastly SQL query that lists recent friends' activity
	// Only gets n most recent items, where n = limit
	// Sorted in descending order by date, regardless of what kind of activity
	private static String getBeastlyQuery(Integer userid, Integer limit) {
		String limitStr = "";
		if (userid == null || userid < 1) return "";
		if (limit != null && limit > 0) limitStr = " LIMIT " + limit;
		
		StringBuilder theBeast = new StringBuilder();
		
		theBeast.append("SELECT userid,username,date,achType,achName,achIcon,quizid,quizname,quizscore FROM (select friend2 from friends where friend1=" + userid + ") foo JOIN (");
		theBeast.append("select achievements.userid,users.username,achievements.dateachieved as date,achievements.type as achType, achievementTypes.name as achName, achievementTypes.icon as achIcon, null as quizid, null as quizname, null as quizscore from achievements ");
		theBeast.append("JOIN achievementTypes ON (achievements.type=achievementTypes.id) JOIN users ON (users.id = achievements.userid) UNION ALL ");
		theBeast.append("select quizzes.authorid as userid, users.username, quizzes.datemade as date, null, null, null, quizzes.id as quizid, quizzes.name as quizname, null from ");
		theBeast.append("quizzes JOIN users ON (quizzes.authorid = users.id) UNION ALL ");
		theBeast.append("select users.id as userid, users.username, quizzesTaken.datetaken as date, null, null, null, quizzesTaken.quizid as quizid, quizzes.name as quizname, quizzesTaken.score as quizscore FROM quizzesTaken ");
		theBeast.append("JOIN users ON (quizzesTaken.userid = users.id) JOIN quizzes ON (quizzesTaken.quizid = quizzes.id) ");
		theBeast.append(") bar where foo.friend2 = bar.userid ");
		theBeast.append("ORDER BY date DESC" + limitStr + ";");
		
		return theBeast.toString();
	}
	
	public static void main(String[] args) {
		Integer userid = 4;
		Integer limit = 15;
		
		System.out.println(":::THE BEAST:::");
		System.out.println(getBeastlyQuery(userid,limit));
	}
	
}
