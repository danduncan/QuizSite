package connection;

import java.sql.*;
import java.util.*;

import users.*;
import quizsite.DatabaseConnection;

public class UserConnection {
	public DatabaseConnection db;
	
	//take/store in connection
	public UserConnection(DatabaseConnection DB){
		//store connection as variable
		db = DB;
	}
	//get attribute function will return object of field requested from database
	public Object getAttribute(String field, Integer ID){
		String query;
		try{		
		//if statements for all unique objects we need to construct from database
		if (field.equals("quizzestaken")){
			//construct query
			query = "SELECT * FROM users u JOIN quizzestaken q ON u.id = q.userid WHERE (u.id = "+ID+")";
			System.out.println(query);
			ResultSet rs = db.executeQuery(query);
			
			//construct object from query results
			ArrayList<QuizTaken> quizzestaken = new ArrayList<QuizTaken>();
			//create list of quizzes taken
			while(rs.next()) {
				quizzestaken.add(new QuizTaken(rs.getInt("q.id"),rs.getInt("q.quizid"),rs.getInt("u.id"),rs.getString("q.datetaken"),rs.getInt("q.score"),rs.getInt("q.time")));
			}
			
			return quizzestaken;
			
		} else if (field.equals("messages")){
			//construct query
			//query = "SELECT * FROM users u JOIN messages m ON u.id = m.senderid WHERE (u.id = "+ID+")";
			query = "SELECT * FROM messages WHERE (senderid = "+ID+") OR (receiverid = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			//construct object from query results
			ArrayList<Message> messages = new ArrayList<Message>();
			while(rs.next()) {
				messages.add(new Message(rs.getInt("id"),rs.getInt("type"),rs.getString("datesent"),rs.getString("timesent"), rs.getInt("senderid"), rs.getInt("receiverid"), rs.getBoolean("opened"), rs.getInt("replied"),rs.getString("subject"), rs.getString("body")));
			}
			
			return messages;	
		} else if (field.equals("friends")){
			//construct query
			query = "SELECT * FROM friends WHERE (friend1 = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			//construct object from query results
			ArrayList<Friend> friends = new ArrayList<Friend>();

			while(rs.next()) {
				friends.add(new Friend(rs.getInt("friend1"),rs.getInt("friend2"),rs.getString("datefriended"),rs.getString("groupname")));
			}
			
			return friends;	
		} else if (field.equals("achievements")){
			//construct query
			query = "SELECT * FROM achievements WHERE (userid = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			//construct object from query results
			ArrayList<Achievement> achievements = new ArrayList<Achievement>();

			while(rs.next()) {
				achievements.add(new Achievement(rs.getInt("type"),rs.getInt("userid"),rs.getString("dateachieved")));
			}
			
			return achievements;
		} else if (field.equals("quizzesmade")){
			//construct query
			query = "SELECT * FROM quizzes WHERE (authorid = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			//construct object from query results
			ArrayList<QuizMade> quizzesmade = new ArrayList<QuizMade>();

			while(rs.next()) {
				quizzesmade.add(new QuizMade(rs.getInt("authorid"),rs.getInt("id"),rs.getString("datemade")));
			}
			
			return quizzesmade;
			
		} else if (field.equals("highscores")){
			//construct query
			query = "SELECT * FROM highscores WHERE (quiztakenid1 = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			//determine # high scores from query results
			Integer highscores = 0;
			while(rs.next()) {
				highscores++;
			}
			
			return highscores.toString();	
			
		} else if (field.equals("ipaddresses")){
			//construct query
			query = "SELECT * FROM ipaddresses WHERE (userid = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			//construct object from query results
			ArrayList<IPAddress> ipaddresses = new ArrayList<IPAddress>();
			//create list of IPAddresses
			while(rs.next()) {
				ipaddresses.add(new IPAddress(rs.getInt("userid"),rs.getObject("ip")));
			}
			
			return ipaddresses;
		//return entry object from field (simplest query)
		} else {
			query = "SELECT " + field + "  FROM users WHERE (id = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			String fieldvalue = "";
			while(rs.next()) {
				fieldvalue = rs.getString(field);
			}

			return fieldvalue;
		}
		
	
		} catch (SQLException e){
			e.printStackTrace();
		}
		return 0;
	}
	public void setAttribute(String field, Object value, Integer userID){
		
	}
	public void updateUserDatabase(String query){
		db.executeUpdate(query);
	}
}
