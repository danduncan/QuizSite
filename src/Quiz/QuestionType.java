package Quiz;

import quizsite.DatabaseConnection;

public class QuestionType {
	public Integer id;
	public String name;
	//name of question type in database
	public String tableName;
	
	public QuestionType(Integer ID, String Name, String TableName){
		id = ID;
		name = Name;
		tableName = TableName;
	}
	//function to add questiontype to database
	public void addNewType(){
		
	}

}
