package users;

import quizsite.DatabaseConnection;

public class AchievementType {
	public Integer id;
	public String name;
	public String description;
	public String attribute;
	public Integer value;
	public String icon;
	
	public AchievementType(Integer ID, String Name, String Description, String Attribute, Integer Value, String Icon){
		id = ID;
		name = Name;
		description = Description;
		attribute = Attribute;
		value = Value;
		icon = Icon;
	}

	public void addNewType(Integer ID,boolean addnew, DatabaseConnection dc){
		//variable add new determines whether this is a new or updated type
		if (addnew){
			String update = "INSERT INTO achievementTypes VALUES("+id+",\""+name+"\",\""+description+"\",\"" + attribute + "\"," + value + ", \""+icon+"\")";
			dc.executeUpdate(update);
		} else {
			String update = "UPDATE achievementTypes SET(id,name,description,attribute,value,icon) = ("+id+",\""+name+"\",\""+description+"\",\"" + attribute + "\"," + value + ", \""+icon+"\") WHERE ( id = " + id +")";
			dc.executeQuery(update);
		}
		 
	}
}
