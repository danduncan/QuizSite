package connection;

import java.sql.*;
import quizsite.MyDBInfo;
import java.util.*;

import Quiz.Question;

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
		if (field.equals(MyDBInfo.QUIZZESTAKENTABLE)){
			//construct query
			//query = "SELECT * FROM users u JOIN "+ MyDBInfo.QUIZZESTAKENTABLE +" q ON u.id = q.userid WHERE (u.id = "+ID+")";
			query = "SELECT * FROM "+ MyDBInfo.QUIZZESTAKENTABLE + " WHERE (userid = "+ID+")";
			//System.out.println(query);
			ResultSet rs = db.executeQuery(query);
			
			//construct object from query results
			ArrayList<QuizTaken> quizzestaken = new ArrayList<QuizTaken>();
			//create list of quizzes taken
			while(rs.next()) {
				quizzestaken.add(new QuizTaken(rs.getInt("id"),rs.getInt("quizid"),rs.getInt("id"),rs.getString("datetaken"),rs.getInt("score"),rs.getInt("time")));
			}
			//want array list to be sorted by most recent
			Collections.sort(quizzestaken, new Comparator<QuizTaken>() {
				@Override public int compare(QuizTaken q1, QuizTaken q2) {
					return Integer.parseInt((q2.datetaken.split(" ")[0])) - Integer.parseInt(q1.datetaken.split(" ")[0]); // Descending
				}

			});
			
			return quizzestaken;
			
		} else if (field.equals(MyDBInfo.MESSAGESTABLE)){
			//construct query
			//query = "SELECT * FROM users u JOIN messages m ON u.id = m.senderid WHERE (u.id = "+ID+")";
			query = "SELECT * FROM " + MyDBInfo.MESSAGESTABLE + " WHERE (senderid = "+ID+") OR (receiverid = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			//construct object from query results
			ArrayList<Message> messages = new ArrayList<Message>();
			while(rs.next()) {
				messages.add(new Message(rs.getInt("id"),rs.getInt("type"),rs.getString("datesent"),rs.getString("timesent"), rs.getInt("senderid"), rs.getInt("receiverid"), rs.getBoolean("opened"), rs.getInt("replied"),rs.getString("subject"), rs.getString("body")));
			}
			
			return messages;	
		} else if (field.equals(MyDBInfo.FRIENDSTABLE)){
			//construct query
			query = "SELECT * FROM " + MyDBInfo.FRIENDSTABLE + " WHERE (friend1 = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			//construct object from query results
			ArrayList<Friend> friends = new ArrayList<Friend>();

			while(rs.next()) {
				friends.add(new Friend(rs.getInt("friend1"),rs.getInt("friend2"),rs.getString("datefriended"),rs.getString("groupname")));
			}
			
			return friends;	
		} else if (field.equals(MyDBInfo.ACHIEVEMENTSTABLE)){
			//construct query
			query = "SELECT * FROM " + MyDBInfo.ACHIEVEMENTSTABLE +" WHERE (userid = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			//construct object from query results
			ArrayList<Achievement> achievements = new ArrayList<Achievement>();

			while(rs.next()) {
				achievements.add(new Achievement(rs.getInt("type"),rs.getInt("userid"),rs.getString("dateachieved")));
			}
			//sort by most recent
			Collections.sort(achievements, new Comparator<Achievement>() {
				@Override public int compare(Achievement q1, Achievement q2) {
					return Integer.parseInt((q2.dateachieved.split(" ")[0])) - Integer.parseInt(q1.dateachieved.split(" ")[0]); // Descending
				}

			});
			
			return achievements;
		} else if (field.equals("quizzesmade")){
			//construct query
			query = "SELECT * FROM " + MyDBInfo.QUIZZESTABLE + " WHERE (authorid = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			//construct object from query results
			ArrayList<QuizMade> quizzesmade = new ArrayList<QuizMade>();

			while(rs.next()) {
				quizzesmade.add(new QuizMade(rs.getInt("authorid"),rs.getInt("id"),rs.getString("datemade")));
			}
			
			Collections.sort(quizzesmade, new Comparator<QuizMade>() {
				@Override public int compare(QuizMade q1, QuizMade q2) {
					return Integer.parseInt((q2.date.split(" ")[0])) - Integer.parseInt(q1.date.split(" ")[0]); // Descending
				}

			});
			
			
			return quizzesmade;
			
		} else if (field.equals(MyDBInfo.HIGHSCORESTABLE)){
			//construct query
			query = "SELECT * FROM " + MyDBInfo.HIGHSCORESTABLE + " WHERE (quiztakenid1 = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			//determine # high scores from query results
			Integer highscores = 0;
			while(rs.next()) {
				highscores++;
			}
			
			return highscores.toString();	
			
		} else if (field.equals(MyDBInfo.IPADDRESSTABLE)){
			//construct query
			query = "SELECT * FROM " + MyDBInfo.IPADDRESSTABLE + " WHERE (userid = "+ID+")";
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
