<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedpagehead.html") %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="/QuizSite/stylesheets/about.css" 
	type="text/css" 
	rel="stylesheet" 
	id="homeStylesheet" />
</head>
<body>
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session, (quizsite.DatabaseConnection) application.getAttribute("DatabaseConnection"))  %>
	<div class="mainBody">
		<div class="bigFrame">
			<h1>About Qwizard and the Qwizard Team</h1>
			
			<ul>
			<li>3 person team:
				<ul><li><a href="http://localhost:8080/QuizSite/user?userid=1">Gino Rooney</a></li> 
				<li><a href="http://localhost:8080/QuizSite/user?userid=2">Matt Wilson</a></li> 
				<li><a href="http://localhost:8080/QuizSite/user?userid=3">Dan Duncan</a></li></ul></li>
			<li>All three of us are EE grad students</li>
			<li>Qwizard consists of over 100 source code files and 11,000 lines of code</li>
			</ul>
		</div>
		<div class="bigFrame notDone">
			<h1><span>Known Bugs</span></h1>
			<ul>
			<li>Welcome page gets nullpointerexception
				<ul>
					<li>Bug is still present as of Tuesday</li>
					<li>Stack trace is as follows:
					<ul>
						<li>at com.mysql.jdbc.ResultSetImpl.next(ResultSetImpl.java:6334)</li>
						<li>at connection.UserConnection.getAttribute(UserConnection.java:159)</li>
						<li>at users.User.init(User.java:56)</li>
						<li>at users.Friend.getFriendActivity(Friend.java:72)</li>
					</ul></li>
				</ul></li>
			<li>DatabaseConnection crashes after being open too long. This may not be fixable</li>
			<li>Site crashes if you try to access a quiz while not logged in. There should either be a public quiz view, or the servlet should forward the user to the home/signin page
			<ul><li>To see what happens, log out of your account and then visit a <a href="http://localhost:8080/QuizSite/QuizHomepageServlet?quizid=23">quiz link</a></li></ul></li>
			<li>numTaken does not get incremented when a user takes a quiz</li>
			<li>When a user receives a message, it does not show up until the user logs out and logs back in
			<ul><li>The Display Messages code needs to check the database each time it executes, rather than checking sesssion.getAttribute("user")</li> 
			</ul></li>
			<li><strong>Debug all major functions that we will have to show in our demo</strong>
				<ul><li>Receive and reply to messages without logging out and logging back in</li>
					<li>Ensure all variables are in sync with the database at all times (don't read from a session attribute if the attribute references something affected by other users, such as messages received)</li>
					<li>Make sure Create Question and Create Quiz are referencing the SiteManager and getting accurate ID's</li>
					<li>"Challenge friend" drop-down menu needs to check the user's friends from the database, not the user object</li>
					<li>getFriendActivity() needs to check with the database to have the correct friends</li>
			</ul></li>
			<li>Empty out these tables in the database: quizzes, questions 
				<ul><li>Manually update the siteManager table to have correct values for nextquizid, nextquestionid, nextmessageid, nextquizt
					<li>Then, populate database with a bunch of real quizzes</li>
					
				</ul></li>
			
		</div>
		<div class="bigFrame">
			<h1>Required Features</h1>
			<ul class="checklist">
			<li>All required quiz properties
				<ul>
					<li>Question randomization</li>
					<li>One page and multi-page quizzes</li>
					<li>Immediate correction</li>
					<li>Scoring of user responses</li>
					<li>Tracking of time to completion</li>
				</ul></li>
			<li>All required question types
				<ul>
					<li>Question-response</li>
					<li>Fill in the blank</li>
					<li>Multiple choice</li>
					<li>Picture response</li>
				</ul></li>
			<li>All user requirements
				<ul>
					<li>Users have logins and passwords</li>
					<li>Passwords are encrypted</li>
					<li>Users can add friends</li>
					<li>Users can delete friends</li>
					<li>Users have viewable profiles</li>
					<li>Users can search for other users</li>
				</ul></li>
			<li>All message requirements
				<ul>
					<li>Friend request</li>
					<li>Challenge</li>
					<li>Note</li>
				</ul></li>
			</ul>
			<h2>Required pages:</h2>
			<ul class="checklist">
				<li>Home page
					<ul>
						<li>List of popular quizzes</li>
						<li>List of recently created quizzes</li>
						<li>List of the user's recent quizzes taken</li>
						<li>List of the user's recent quizzes created, if any</li>
						<li>Indication of recently received messages
							<ul>
								<li>Friend requests</li>
								<li>Challenges</li>
								<li>Notes</li>
							</ul></li>
						<li>Friends' recent activities
							<ul>
								<li>Quizzes taken</li>
								<li>Quizzes created</li>
								<li>Achievements earned</li>
								<li>Links to friends' profiles</li>
								<li>Links to friends' taken and created quizzes</li>
							</ul></li>
						<li>Limit number of entries in all lists to number that looks good on website</li>
					</ul></li>
				<li>Quiz summary page
					<ul>
						<li>Quiz name</li>
						<li>Quiz creator</li>
						<li>Quiz description</li>
						<li>Date created</li>
						<li>Link to take the quiz</li>
						<li>Link to take the quiz in practice mode, if available</li>
					</ul></li>
				<li>Quiz itself and all associated questions</li>
				<li>Quiz results page
					<ul>
						<li>Appears when user completes a quiz</li>
						<li>Tells user their score and time</li>
					</ul></li>
				<li>User-related pages
					<ul>
						<li>Users have viewable profiles</li>
						<li>Users have viewable profile thumbnails</li>
					</ul></li>
				<li>Error checking
					<ul>User can never receive a 404 error from clicking a site link
						<li>Quiz profile page gracefully handles malformed URLs (e.g. forward them to the homepage)</li>
						<li>User profile page gracefully handles malformed URLs (e.g. forward them to the homepage)</li>
					</ul></li>
				<li>Use of sessions allows multiple users to visit site simultaneously</li>
					
			</ul>
		</div>
		
		
		<div class="bigFrame">
			<h1>Recommended Features and Extensions</h1>
			<h2>(Based on requirements for a three-person team)</h2>
			<ul class="checklist">
				<li class="notDone">Quiz practice mode <span>(not yet implemented)</span></li>
				<li>Quizzes track performance of users</li> 
				<li>Quizzes track high scores</li>
				<li>Quiz pages display top scores</li>
				<li>Optional question types
					<ul>
						<li>Multi-answer questions</li>
						<li>Multiple choice with multiple answers</li>
					</ul></li>
				<li>Dedicated user sub-pages <span>(not yet implemented)</span>
						<ul><li>Messages page to receive messages/challenges/friend requests, and to send replies or compose new messages</li>
						<li>Friends page where a user can view their friends</li>
						<li>Edit account page to change picture, name, email, or password</li>
						</ul></li>	
				<li>Optional user features
					<ul>
						<li>Passwords are salted prior to encryption</li>
						<li>Basic and advanced user search are both implemented</li>
						<li>Friends requests are sent via Ajax</li>
						<li>Users can confirm pending friend requests via Ajax</li>
						<li>User profiles track and display past quiz performance</li>
					</ul></li>
				<li>Achievements
					<ul>
						<li>Badges corresponding to all major achievement types</li>
						<li>Achievements page lists all possible achievements</li>
						<li>Achievements display on user profile</li>
						<li>Achievements display on profile thumbnails
							<ul><li>Floating captions give the achievement name and timestamp</li>
							</ul></li>
						<li>System tracks six major kind of user achievement:
							<ul>
								<li>Amateur author - the user created a quiz</li>
								<li>Prolific author - the user created five quizzes</li>
								<li>Prodigious author - the user created ten quizzes</li>
								<li>Quiz Machine - the user took ten quizzes</li>
								<li>I am the Greatest - user got a top score on a quiz</li>
								<li class="notDone">Practice Makes Perfect - the user took a quiz in practice mode <span>(not yet implemented)</span></li>
							</ul></li>
					</ul>
				
				<li>Quiz pages display the user's past performance on a quiz
					<ul>
						<li>User can sort this list by date, percent correct, or time taken</li>
					</ul></li>
				<li>Quiz pages display other users' scores
					<ul>
						<li>Today's top scores</li>
						<li>All-time top scores</li>
						<li>Recent scores</li>
					</ul></li>
				<li>Quiz pages display summary statistics of popularity and user performance</li>
				<li>Quiz results page lists user's individual answers along with the correct answers</li>
				<li>Quiz results page has tables that
					<ul>
						<li>Compare the user's latest score with their past score history</li>
						<li>Compare the user's score with those of other users</li>
						<li>Compare the user's score with those of their friends</li>
					</ul></li>
				<li>Website is attractive and presentable
					<ul>
						<li class="notDone"><span>User profiles need to styled</span></li>
						<li class="notDone"><span>Quiz homepages need to styled</span></li>
						<li class="notDone"><span>Quizzes need to styled</span></li>
						<li class="notDone"><span>Quiz results need to styled</span></li>
						<li class="notDone"><span>Challenge request needs to be implemented and styled</span></li>
						<li>CSS, JSP's, and Javascript used to create a uniform website experience</li>
						<li>GET, POST, and AJAX requests are all used depending on which is the most intuitive for a particular action</li>
						<li>Standard header and footer present on every page</li>
						<li>Header and footer feature useful links, including links customized to the user and working About/Contact links</li>
						<li>Qwizard logo included in header and as icon in window title</li>
						<li>Scripts used to uniformly generate well-formatted website components
							<ul>
								<li>All website tables</li>
								<li>User search results</li>
								<li>Quiz search results</li>
								<li>Thumbnail view of user profiles</li>
								<li>Thumbnail view of quiz pages</li>
							</ul></li>
						<li>Users who do not select a profile picture are automatically given a default image</li>
						<li>Achievements all have corresponding badges</li>
						<li>Achievement thumbnails display helpful floating captions when the user hovers over them </li>
						<li>Thumbnail view of quizzes very gracefully handles long quiz descriptions</li>
						<li>Javascript/AJAX used to smoothly handle many common website functions in the background:
							<ul>
								<li>Sending friend requests</li>
								<li>Confirming friend requests</li>
								<li>Validating user input on sign-in page</li>
								<li>Validating user input on Create Account page</li>
								<li>Sending messages to other users</li>
							</ul></li>
					</ul></li>
				<li>Messaging system is robust and user-friendly
					<ul>
						<li>Messages sent via pop-up window using AJAX, making for smoother user experience</li>
						<li>Group messages can also be sent via the AJAX interface
							<ul><li>To send a group message, just type in the usernames of the recipient, each name separated by a space</li></ul>
						</li>
					</ul></li>
				<li>Public view of website
					<ul><li>User profiles</li>
						<li>User thumbnails</li>
						<li>User search</li>
						<li class="notDone">Quiz-related pages <span>(not yet implemented)</span></li>
						</ul></li>
				<li>Website pages feature robust error handling
					<ul>
						<li>Sign in page uses Javascript for error checking</li>
						<li>Create account page uses Javascript for error checking</li>
						<li>User inputs throughout website are sanitized to prevent SQL injection</li>
						<li>Malformed inputs and URL's are handled gracefully
							<ul><li>e.g. when a signed-in user visits the sign in page or the create account page, they are forwarded on to the homepage</li></ul>
						</li>
						<li>Messaging interface uses Javascript for instantaneous error checking</li></ul>
				<li>Catchy name for website</li>
				<li>Website includes an awesome easter egg! A couple hints to help you find it:
					<ul>
						<li>It's on the user search page</li>
						<li>We got the idea from Google</li>
					</ul>
				</li>
			</ul>
		</div>
		<div class="bigFrame notDone">
			<h1>Extensions Not Implemented</h1>
			<ul>
				<li>Administrator view <span>(not implemented)</span>
					<ul>
						<li>Create announcements for homepage</li>
						<li>Remove user accounts</li>
						<li>Remove quizzes</li>
						<li>Clear a quiz's history</li>
						<li>Promote users to admin status</li>
						<li>See global site statistics
							<ul>
								<li>Total users</li>
								<li>Number of quizzes taken</li>
								<li> etc.</li>
							</ul></li>
					</ul></li>
				<li>Homepage displays announcements <span>(not implemented)</span></li>
				<li>Link to edit the quiz if the user is the quiz owner</li>
				<li>Quiz categories</li>
				<li>Question categories</li>
				<li>User ratings and user reviews for quizzes</li>
				<li>User comment system for quizzes</li>
				<li>Allow users to flag quizzes as inappropriate</li>
				<li>Public view of website
					<ul>
						<li>Public view of homepage</li>
						<li>Public view of quiz pages</li>
					</ul></li>
				<li>Cookies to provide long-term tracking of users</li>
				<li>User privacy settings</li>
				<li>XML formatting and loading of quizzes</li>
			</ul>
		</div>
	</div>
	
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>