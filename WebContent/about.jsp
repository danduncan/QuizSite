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
				</ul><ul>
			<li>All three of us are EE grad students</li>
			</ul><ul>
			<li>Qwizard consists of over 100 source code files and 11,000 lines of code
				<ul><li>7,000 lines of Java code and 4,400 lines of JSPs, HTML, Javascript, and CSS</li></ul></li>
			<li>Version control was handled via Github and the Eclipse EGit interface</li>
				<ul><li>Over 300 commits were made to our Github repository in the making of this project</li></ul>
			<li>All features listed on this page are fully implemented in our website</li>
			<li>An attractively-formatted version of this documentation is available at the "About" link on our project website</li>
			</ul>
		</div>
		<div class="bigFrame notDone">
			<h1><span>Known Bugs</span></h1>
			<ul>
			<li><strong>Debug all major functions that we will have to show in our demo with two users accessing simultaneously (no logging out!)</strong>
				<ul><li>Receive and reply to messages</li>
					<li>Make sure Create Question and Create Quiz are referencing the SiteManager and getting accurate ID's</li>
					<li>"Challenge friend" drop-down menu needs to check the user's friends from the database, not the user object</li>
					<li>getFriendActivity() needs to check with the database and guard against null pointers</li>
			</ul></li>
			<li>I added some CSS to much of the website to make it look a little more attractive. I've tested every page and they look fine, but look carefully during final testing in case I missed anything</li>
			<li>Populate database with a bunch of real quizzes, quizzes taken, and achievements
				<ul><li>We ideally want to have plenty of data for the tables of recent quizzes, quizzes taken, and friend activity</li>
					<li>Lots of achievements scattered among users to show off achievement badges and floating captions</li>
				</ul></li>
			</ul>
		</div>
		<div class="bigFrame">
			<h1>Required Features</h1>
			<ul class="checklist">
			<li>All required quiz properties
				<ul>
					<li>Question randomization</li>
					<li>One page and multi-page quizzes</li>
					<li>Correction of wrong answers</li>
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
			<li>Error checking
					<ul>User can never receive a 404 error from clicking a site link
						<li>Quiz profile page gracefully handles malformed URLs</li>
						<li>User profile page gracefully handles malformed URLs</li>
					</ul></li>
				<li>Use of sessions allows multiple users to visit site simultaneously</li>
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
				
					
			</ul>
		</div>
		
		
		<div class="bigFrame">
			<h1>Recommended Features and Extensions</h1>
			<h2>(Based on requirements for a three-person team)</h2>
			<ul class="checklist">
				<li>Quiz pages track and display performance history for each quiz:
					<ul>
						<li>The user's personal performance</li>
						<li>Other users' recent scores</li>
						<li>Today's high scores</li>
						<li>All-time high and low scores</li>
						<li>Summary statistics on popularity and average performance</li>
						<li>Comparison of a user's latest score with their past scores</li>
						<li>Corrections show a user which questions they missed and what the correct answers are</li>
					</ul></li>
				<li>Optional question types
					<ul>
						<li>Multi-answer questions</li>
						<li>Multiple choice with multiple answers</li>
					</ul></li>
				<li>Search functionality
					<ul><li>Basic and advanced user search interfaces are both provided</li>
						<li>Basic and advanced quiz search interfaces are both provided</li>
					</ul></li>
				<li>Achievements
					<ul>
						<li>Badges corresponding to all major achievement types</li>
						<li>Achievements page lists all possible achievements</li>
						<li>Achievements display on user profile</li>
						<li>Achievements display on profile thumbnails
							<ul><li>Floating captions give the achievement name and timestamp</li>
							</ul></li>
						<li>System tracks five major kind of user achievement:
							<ul>
								<li>Amateur author - the user created a quiz</li>
								<li>Prolific author - the user created five quizzes</li>
								<li>Prodigious author - the user created ten quizzes</li>
								<li>Quiz Machine - the user took ten quizzes</li>
								<li>I am the Greatest - user got a top score on a quiz</li>
							</ul></li>
					</ul>
				<li>Optional user features
					<ul>
						<li>Passwords are salted prior to encryption</li>
						<li>User profiles track and display past quiz performance</li>
						<li>User profiles track and display friends' recent activity</li>
						<li>Friend requests are sent and confirmed via Ajax</li>
						<li>AJAX-based messaging system is robust and user-friendly
						<li>Group messages can also be sent via the AJAX interface
							<ul><li>To send a group message, just type in the usernames of the recipients, each name separated by a space</li>
							</ul></li>
						</ul></li>
				
				<li>Public view of website
					<ul><li>User profiles</li>
						<li>User thumbnails</li>
						<li>User search</li>
						<li>Quiz-related pages</li>
						</ul></li>

				<li>Website pages feature robust error handling
					<ul>
						<li>Sign in page uses Javascript for error checking</li>
						<li>Create account page uses Javascript for error checking</li>
						<li>User inputs through most of the website are sanitized to prevent SQL injection</li>
						<li>Malformed inputs and URL's are handled gracefully by all pages
							<ul><li>e.g. when a signed-in user visits the sign in page or the create account page, they are forwarded on to the homepage</li></ul>
						</li>
						<li>Messaging interface uses Javascript for instantaneous error checking</li>
						<li>System listens for timeouts in the MySQL database connection and automatically reopens the connection</li></ul>

				<li>Website is attractive and presentable
					<ul>
						<li>CSS and Javascript used to create a pleasant and uniform user experience</li>
						<li>GET, POST, and AJAX requests are all used depending on which provides the best functionality</li>
						<li>A standard header and footer are present on every page</li>
						<li>Header and footer feature useful links, including links customized to the user and working About/Contact links</li>
						<li>Qwizard logo included in header and as icon in window title</li>
						<li>Scripts are used to uniformly generate well-formatted website components
							<ul>
								<li>All website tables</li>
								<li>User and quiz search results</li>
								<li>User profiles and quiz pages</li>
								<li>Thumbnail views of user profiles and quiz pages</li>
							</ul></li>
						<li>Users who do not select a profile picture are automatically given a default image</li>
						<li>Achievements all have corresponding badges</li>
						<li>Achievement thumbnails display helpful floating captions when the user hovers over them </li>
						<li>Thumbnail view of quizzes gracefully handles long quiz descriptions</li>
						<li>Javascript/AJAX used to smoothly handle many common website functions in the background:
							<ul>
								<li>Sending friend requests</li>
								<li>Confirming friend requests</li>
								<li>Validating user input on sign-in page</li>
								<li>Validating user input on Create Account page</li>
								<li>Sending messages to other users</li>
							</ul></li>
					</ul></li>
				<li>Catchy name for website</li>
				<li>Website includes an awesome easter egg! A couple hints to help you find it:
					<ul>
						<li>It's accessible within the user search page</li>
						<li>Google (and <a href="http://youtu.be/wZ8z9FQEQiQ" target="_blank" style="target-new: tab;">Star Fox</a>) did it first</li>
					</ul>
				</li>
			</ul>
		</div>
		<div class="bigFrame notDone">
			<h1>Extensions Not Implemented</h1>
			<ul>
				<li class="notDone">Quiz practice mode</li>
				<li class="notDone">Practice Makes Perfect achievement <span>(badge only)</span></li>
				<li>Dedicated user sub-pages 
				<ul><li>Messages page to receive messages/challenges/friend requests, and to send replies or compose new messages</li>
					<li>Friends page where a user can view their friends</li>
					<li>Edit account page to change picture, name, email, or password</li>
				</ul></li>	
				<li class="notDone"><span>User profiles need to styled</span></li>
				<li class="notDone"><span>Quiz homepages need to styled</span></li>
				<li class="notDone"><span>Quizzes need to styled</span></li>
				<li class="notDone"><span>Quiz results need to styled</span></li>
				<li class="notDone"><span>Challenge request needs to be implemented and styled</span></li>
				<li>Administrator view 
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
				<li>Homepage displays announcements</li>
				<li>Link to edit the quiz if the user is the quiz owner</li>
				<li>Quiz categories</li>
				<li>Question categories</li>
				<li>User ratings and user reviews for quizzes</li>
				<li>User comment system for quizzes</li>
				<li>Allow users to flag quizzes as inappropriate</li>
				<li>Cookies to provide long-term tracking of users</li>
				<li>User privacy settings</li>
				<li>XML formatting and loading of quizzes</li>
			</ul>
		</div>
	</div>
	
	<%= sharedHtmlGenerators.sharedHtmlGenerator.getHTML(application.getRealPath("/") + "/sharedHTML/sharedfooter.html") %>
</body>
</html>