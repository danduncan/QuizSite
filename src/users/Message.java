package users;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.jsp.JspWriter;

import quizsite.DatabaseConnection;
import quizsite.MyDBInfo;

public class Message {
	public Integer id;
	public Integer type;
	public String datesent;
	public String timesent;
	public Integer senderid;
	public Integer receiverid;
	public Boolean opened;
	public Integer replied;
	public String subject;
	public String body;
	
	public Message(Integer ID, Integer Type, String DateSent, String TimeSent, Integer SenderID, Integer ReceiverID, Boolean Opened, Integer Replied, String Subject, String Body){
		id = ID;
		type = Type;
		datesent = DateSent;
		timesent = TimeSent;
		senderid = SenderID;
		receiverid = ReceiverID;
		opened = Opened;
		replied = Replied;
		subject = Subject;
		body = Body;
	}
	public String toString(){
		return "  Sent: " + datesent + "  Type: " + type + " Subject: " + subject;
	}
	public void printtoJSP(PrintWriter out) throws IOException{
		out.println("<p>Date: "+datesent+"</p>");
		out.println("<p>Subject: "+subject+"</p>");
		out.println("<p>Body: "+body+"</p>");
		out.println("<p> ------------------------ <p>");
	}
	public void updateRead(DatabaseConnection dc){
		opened = true;
		// Add message to database
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE "+ MyDBInfo.MESSAGESTABLE+" SET ");
		sb.append("opened = "+opened);
		sb.append(", replied = "+replied);
		sb.append(" WHERE (id = "+id+")");
		
		try {
			dc.executeUpdate(sb.toString());
		}	
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
