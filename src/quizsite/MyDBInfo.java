package quizsite;

public class MyDBInfo {
	// Database configuration
	public static final String MYSQL_USERNAME = "ccs108duncand";
	public static final String MYSQL_PASSWORD = "danpw"; // Original password was aepheiyu
	public static final String MYSQL_DATABASE_SERVER = "mysql-user-master.stanford.edu";
	public static final String MYSQL_DATABASE_NAME = "c_cs108_duncand";

	// Names of primary tables
	public static final String GLOBALTABLE = "globals";
	public static final String USERTABLE = "users";
	public static final String QUIZZESTABLE = "quizzes";
	public static final String QUESTIONSTABLE = "questions";
	public static final String ACHIEVEMENTSTABLE = "achievements";
	public static final String FRIENDSTABLE = "friends";
	public static final String MESSAGESTABLE = "messages";
	public static final String HIGHSCORESTABLE = "highscores";
	public static final String QUIZZESTAKENTABLE = "quizzestaken";
	
	// Names of helper tables
	public static final String QUESTIONTYPESTABLE = "questiontypes";
	public static final String MESSAGETYPESTABLE = "messagetypes";
	public static final String ACHIEVEMENTTYPESTABLE = "achievementtypes";
	public static final String QUIZCATEGORIESTABLE = "quizcategories";
	public static final String QUESTIONCATEGORIESTABLE = "questioncategories";
	public static final String IPADDRESSTABLE = "ipaddresses";
	
	
}