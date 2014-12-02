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
			if(!friendIDs.contains(ID2)){
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
					activity.add("<a href=\"user?userid="+friend.id+"\">"+ friend.username+"</a> earned a new badge: "+at.get(friend.achievements.get(j).type).name+"  " + "<img src = \""+at.get(user.achievements.get(i).type).icon+"\" height = \"20\" width = \"20\"");
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
}
