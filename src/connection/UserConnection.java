package connection;

import java.sql.*;

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
		//want user's id
		if (field.equals("ipaddresses")){
			query = "SELECT" + field + "  FROM users WHERE (id = "+ID+")";
			ResultSet rs = db.executeQuery(query);
			
			Integer id = 0;
			while(rs.next()) {
				id = rs.getInt(field);
			}
			
			return id;
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
