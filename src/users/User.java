package users;


import java.util.*;

import connection.UserConnection;

public class User {
	public Integer id;
	public String username;
	public String password;
	public String firstname;
	public String lastname;
	public String email;
	public String datejoined;
	public String profilepicture;
	public int numfriends;
	public ArrayList<IPAddress> ipaddresses;
	public Integer numcreated;
	public Integer numtaken;
	public Integer numtakenpractice;
	public Integer highscores;
	public String usertype;
	public ArrayList<QuizTaken> quizzestaken = new ArrayList<QuizTaken>();
	public ArrayList<QuizMade> quizzesmade = new ArrayList<QuizMade>(); 
	public ArrayList<Friend> friends = new ArrayList<Friend>();
	public ArrayList<Achievement> achievements = new ArrayList<Achievement>();
	public ArrayList<Message> messages = new ArrayList<Message>(); 
	public UserConnection userconnection;
	public boolean create = false;
	
	//two constructors - one for when we create a new user, the second is for when a user logs in
	//create new, empty user
	public User(UserConnection uc){
		userconnection = uc;
	}
	
	//user login
	public User(Integer ID, UserConnection uc){
		userconnection = uc;
		
		//get information about user		
		id = ID;
		quizzestaken = (ArrayList<QuizTaken>) userconnection.getAttribute("quizzestaken", id);
		username = (String) userconnection.getAttribute("username", id);
		password = (String) userconnection.getAttribute("password", id);
		firstname = (String) userconnection.getAttribute("firstname", id);
		lastname = (String) userconnection.getAttribute("lastname", id);
		email = (String) userconnection.getAttribute("email", id);
		datejoined = (String) userconnection.getAttribute("datejoined", id);
		profilepicture = (String) userconnection.getAttribute("profilepicture", id);
		ipaddresses = (ArrayList<IPAddress>) userconnection.getAttribute("ipaddresses", id);
		numcreated = Integer.parseInt((String) userconnection.getAttribute("numcreated", id));
		numtaken = Integer.parseInt((String)userconnection.getAttribute("numtaken", id));
		numtakenpractice = Integer.parseInt( (String) userconnection.getAttribute("numtakenpractice", id));
		highscores = Integer.parseInt((String) userconnection.getAttribute("highscores", id));
		//usertype = (String) userconnection.getAttribute("usertype", id);
		quizzestaken = (ArrayList<QuizTaken>) userconnection.getAttribute("quizzestaken", id);
		quizzesmade = (ArrayList<QuizMade>) userconnection.getAttribute("quizzesmade", id);
		friends = (ArrayList<Friend>) userconnection.getAttribute("friends", id);
		achievements = (ArrayList<Achievement>) userconnection.getAttribute("achievements", id);
		messages = (ArrayList<Message>) userconnection.getAttribute("messages", id);
		System.out.println("user successfully retrieved");
		
	}
	//method to set users attributes
	public void set(String field, Object value, UserConnection userconnection){
		userconnection.setAttribute(field, value, id);
	}
	
	public void updateUserDatabase(){
		//add new user to database
		if (create){
			String insert = "INSERT INTO users VALUES("+id+",\""+username+"\", \"" + password + "\", \"" + firstname + "\",\"" + lastname + "\", \"" + email
			                  + "\", \"" + datejoined + "\"," + numfriends + ","+numtaken+ ","+ numtakenpractice +","+numcreated+","+highscores+",\""+profilepicture+ "\")";   
			userconnection.updateUserDatabase(insert);
			create = false;
		//update current user in database
		} else {
			String update = "UPDATE users SET (id, username, password, firstname, lastname, email, datejoined, numfriends, numtaken, numtakenpractice, numcreated, highscores, profilepicture) = " +
					"("+id+",\""+username+"\", \"" + password + "\", \"" + firstname + "\",\"" + lastname + "\", \"" + email
            + "\", \"" + datejoined + "\"," + numfriends + ","+numtaken+ ","+ numtakenpractice +","+numcreated+","+highscores+",\""+profilepicture+ "\") WHERE ( id = " + id +")";   
			System.out.println(update);
			userconnection.updateUserDatabase(update);
		}
	}

}
