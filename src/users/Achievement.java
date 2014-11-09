package users;

public class Achievement {
	public Integer type;
	public Integer userid;
	public String dateachieved;
	
	public Achievement(Integer Type, Integer UserID, String DateAchieved){
		type = Type;
		userid = UserID;
		dateachieved = DateAchieved;	
	}
}
