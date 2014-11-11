package users;

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
		return "From: " + senderid + "  Sent: " + datesent + "  Type: " + type + " Subject: " + subject;
	}
	
}
