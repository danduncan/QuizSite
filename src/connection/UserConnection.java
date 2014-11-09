package connection;

import java.sql.*;
import java.util.*;

import users.*;
import quizsite.DatabaseConnection;

public class UserConnection {
	public DatabaseConnection db;
	
	//take/store in connection
	public UserConnection(DatabaseConnection DB) throws SQLException{
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
			ResultSet rs = db.executeQuery(query);
			
			//construct object from query results
			ArrayList<QuizTaken> quizzestaken = new ArrayList<QuizTaken>();
			Integer id = 0;
			Integer quizid = 0;
			Integer userid = 0;
			String datetaken = "";
			Integer score = 0;
			Integer time = 0;
			
			//create list of quizzes taken
			while(rs.next()) {
				id = rs.getInt("q.id");
				quizid = rs.getInt("q.quizid");
				userid = rs.getInt("u.id");
				datetaken = rs.getString("q.datetaken");
				score = rs.getInt("q.score");
				time = rs.getInt("q.time");
				quizzestaken.add(new QuizTaken(id,quizid,userid,datetaken,score,time));
			}
			
		} else if (field.equals("ipaddresses")){
			//construct query
			query = "SELECT * FROM users u JOIN ipaddresses i ON u.id = i.userid WHERE (u.id = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			//construct object from query results
			ArrayList<IPAddress> ipaddresses = new ArrayList<IPAddress>();
			Integer id = 0;
			Object ip = null;
			//create list of IPAddresses
			while(rs.next()) {
				id = rs.getInt("id");
				ip = rs.getObject("ip");
				ipaddresses.add(new IPAddress(id,ip));
			}
			
			return ipaddresses;
		//return entry from field
		} else {
			query = "SELECT" + field + "  FROM users WHERE (id = "+ID+")";
			ResultSet rs = db.executeQuery("SELECT" + field + "  FROM users WHERE (id = "+ID+")");
			
			Object fieldvalue = null;
			while(rs.next()) {
				fieldvalue = rs.getObject(field);
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
}
