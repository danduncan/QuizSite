package quizsite;

public class MyDBInfo {
	// Database configuration
	public static final String MYSQL_USERNAME = "ccs108duncand";
	public static final String MYSQL_PASSWORD = "danpw"; // Original password was aepheiyu
	public static final String MYSQL_DATABASE_SERVER = "mysql-user-master.stanford.edu";
	public static final String MYSQL_DATABASE_NAME = "c_cs108_duncand";
	
	// Login configuration
	public static final String MYDB_LOGINSALT = "mattginodan";
	public static final boolean MYDB_LOGINSKIPHASH = false; // Skip hashing passwords if set to true (for testing)

	// Names of primary tables
	public static final String SITEMANAGERTABLE = "siteManager";
	public static final String USERTABLE = "users";
	public static final String QUIZZESTABLE = "quizzes";
	public static final String ACHIEVEMENTSTABLE = "achievements";
	public static final String FRIENDSTABLE = "friends";
	public static final String MESSAGESTABLE = "messages";
	public static final String HIGHSCORESTABLE = "highScores";
	public static final String QUIZZESTAKENTABLE = "quizzesTaken";
	
	// Names of question tables (also should be contained in questionTypes object)
	public static final String QUESTIONFILLBLANK = "qFillBlank";
	public static final String QUESTIONMULTIPLECHOICE = "qMultipleChoice";
	public static final String QUESTIONPICTURE = "qPicture";
	public static final String QUESTIONMULTIPLEANSWER = "qMultipleAnswer";
	public static final String QUESTIONMULTIPLECHOICEMULTIPLEANSWER = "qMultipleChoiceMultipleAnswer";
	public static final String QUESTIONRESPONSE = "qResponse";
	
	// Names of helper tables
	public static final String QUESTIONTYPESTABLE = "questionTypes";
	public static final String MESSAGETYPESTABLE = "messageTypes";
	public static final String ACHIEVEMENTTYPESTABLE = "achievementTypes";
	public static final String QUIZCATEGORIESTABLE = "quizCategories";
	public static final String QUESTIONCATEGORIESTABLE = "questionCategories";
	public static final String IPADDRESSTABLE = "ipAddresses";
	
	// URL of default profile picture
	public static final String DEFAULTPROFILEPIC = "/QuizSite/qwizard_images/profile_picture_default_full.png";
	
	
}