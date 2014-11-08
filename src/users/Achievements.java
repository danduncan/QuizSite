package users;

public class Achievements {
	public Integer type;
	public Integer userid;
	public String dateachieved;
	
	public void QuizTaken(Integer Type, Integer UserID, String DateAchieved){
		type = Type;
		userid = UserID;
		dateachieved = DateAchieved;	
	}
}
