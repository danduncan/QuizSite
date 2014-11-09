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
	public ArrayList<IPAddress> ipaddresses;
	public Integer numcreated;
	public Integer numtaken;
	public Integer numtakenpractice;
	public Integer highscores;
	public String usertype;
	public ArrayList<QuizTaken> quizzestaken;
	public ArrayList<QuizMade> quizzesmade;
	public ArrayList<Friends> friends;
	public ArrayList<Achievements> achievements;
	public ArrayList<Messages> messages;
	
	//two constructors - one for when we create a new user, the second is for when a user logs in
	//create new, empty user
	public User(String ID){
	}
	
	//user login
	public User(Integer ID, UserConnection userconnection){
		//get information about user
		quizzestaken = (ArrayList<QuizTaken>) userconnection.getAttribute("quizzestaken", id);
		id = ID;
		username = (String) userconnection.getAttribute("username", id);
		password = (String) userconnection.getAttribute("password", id);
		firstname = (String) userconnection.getAttribute("firstname", id);
		lastname = (String) userconnection.getAttribute("lastname", id);
		email = (String) userconnection.getAttribute("email", id);
		datejoined = (String) userconnection.getAttribute("datejoined", id);
		profilepicture = (String) userconnection.getAttribute("profilepicture", id);
		ipaddresses = (ArrayList<IPAddress>) userconnection.getAttribute("ipaddresses", id);
		numcreated = (Integer) userconnection.getAttribute("numcreated", id);
		numtaken = (Integer) userconnection.getAttribute("numtaken", id);
		numtakenpractice = (Integer) userconnection.getAttribute("numtakenpractice", id);
		highscores = (Integer) userconnection.getAttribute("highscores", id);
		usertype = (String) userconnection.getAttribute("usertype", id);
		quizzestaken = (ArrayList<QuizTaken>) userconnection.getAttribute("quizzestaken", id);
		quizzesmade = (ArrayList<QuizMade>) userconnection.getAttribute("quizzesmade", id);
		friends = (ArrayList<Friends>) userconnection.getAttribute("friends", id);
		achievements = (ArrayList<Achievements>) userconnection.getAttribute("achievements", id);
		messages = (ArrayList<Messages>) userconnection.getAttribute("messages", id);
		
	}
	//method to set users attributes
	public void set(String field, Object value, UserConnection userconnection){
		userconnection.setAttribute(field, value, id);
	}

}
