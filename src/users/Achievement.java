package users;

import java.util.ArrayList;
import java.util.List;

import quizsite.DatabaseConnection;
import quizsite.FormatDateTime;
import quizsite.MyDBInfo;

public class Achievement {
	public static final int AMATEUR_AUTHOR = 0;
	public static final int PROLIFIC_AUTHOR = 1;
	public static final int PRODIGIOUS_AUTHOR = 2;
	public static final int QUIZ_MACHINE = 3;
	public static final int HIGH_SCORE = 4;
	
	private static final String table = MyDBInfo.ACHIEVEMENTSTABLE;
	
	public Integer type;
	public Integer userid;
	public String dateachieved;
	
	public Achievement(Integer Type, Integer UserID, String DateAchieved){
		type = Type;
		userid = UserID;
		dateachieved = DateAchieved;	
	}
	
	public String toString(){
		return "Achievement: " + type + "  Date Achieved: " + dateachieved;
	}
	
	public static List<Achievement> updateQuizMadeAchievements(User user){
		List<Achievement> achieved = new ArrayList<Achievement>();
		String date = FormatDateTime.getCurrentSystemDateTime();
		int numCreated = user.quizzesmade.size();
		if(numCreated== 1){
			achieved.add(new Achievement(AMATEUR_AUTHOR, user.id, date));
		}else if(numCreated == 5){
			achieved.add(new Achievement(PROLIFIC_AUTHOR, user.id, date));
		}else if(numCreated == 10){
			achieved.add(new Achievement(PRODIGIOUS_AUTHOR, user.id, date));
		}
		return achieved;
	}
	
	public static List<Achievement> updateQuizTakenAchievements(User user, int rank){
		List<Achievement> achieved = new ArrayList<Achievement>();
		String date = FormatDateTime.getCurrentSystemDateTime();
		int numTaken = user.quizzestaken.size();
		if(rank != 0){
			achieved.add(new Achievement(HIGH_SCORE, user.id, date));
		}
		if(numTaken == 10){
			achieved.add(new Achievement(QUIZ_MACHINE, user.id, date));
		}
		return achieved;
	}
	
	public static void updateSiteAchievements(User user, List<Achievement> achieved, DatabaseConnection dc){
		for(int i = 0; i<achieved.size(); i++){
			Achievement curr = achieved.get(i);
			user.achievements.add(curr);
			saveAchievementToDB(curr, dc);
		}
	}
	
	private static void saveAchievementToDB(Achievement achievement, DatabaseConnection dc){
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO " + table + " ");
		sb.append("(type, userid, dateachieved) ");
		sb.append("VALUES ");
		sb.append("(" + achievement.type + ", " + achievement.userid + ", \"" + achievement.dateachieved + "\");");
		// Insert row into table
		dc.executeUpdate(sb.toString());
	}
	
	public static String getAchievementNames(List<Achievement> achieved, List<AchievementType> achievementTypes){
		String str = "Achieved: ";
		for(int i = 0; i<achieved.size(); i++){
			int aType = achieved.get(i).type;
			String typeName = achievementTypes.get(aType).name;
			str += "\""+typeName+"\"";
			if(i != achieved.size()-1) str += ", ";
		}
		return str;
	}
}
