package users;

import quizsite.DatabaseConnection;
import quizsite.FormatDateTime;

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
	
}
