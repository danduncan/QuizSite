package users;

public class Messages {
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
	
	public Messages(Integer ID, Integer Type, String DateSent, String TimeSent, Integer SenderID, Integer ReceiverID, Boolean Opened, Integer Replied, String Body, String Subject){
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
}
