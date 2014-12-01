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
	<%= sharedHtmlGenerators.sharedHeaderGenerator.getHTML(application.getRealPath("/"), session)  %>
	<div class="mainBody">
		<div class="bigFrame">
			<h1>About Qwizard and the Qwizard Team</h1>
			
			<ul>
			<li>3 person team:
				<ul><li>Gino Rooney</li> 
				<li>Matt Wilson</li> 
				<li>Dan Duncan</li></ul></li>
			<li>All three of us are EE grad students</li>
			<li>Qwizard consists of over 100 source code files and nearly 9,500 lines of code</li>
			</ul>
		</div>
		<div class="bigFrame">
			<h1>Project Requirements</h1>
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
					<li>Users can delete friends<span> (Not yet implemented)</span></li>
					<li>Users have viewable profiles<span> (Not yet implemented)</span></li>
					<li>Users can search for other users</li>
				</ul></li>
			<li>All message requirements
				<ul>
					<li>Friend request</li>
					<li>Challenge <span>(Goes on quiz profile page))</span></li>
					<li>Note</li>
				</ul></li>
			</ul>
			<h2>Required pages:</h2>
			<ul class="checklist">
				<li>Home page <span>(not yet implemented)</span>
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
				<li>Quiz summary page <span>(almost done)</span>
					<ul>
						<li>Quiz name</li>
						<li>Quiz creator</li>
						<li>Quiz description</li>
						<li>Date created</li>
						<li>Link to take the quiz</li>
						<li>Link to take the quiz in practice mode, if available</li>
					</ul></li>
				<li>Quiz results page
					<ul>
						<li>Appears when user completes a quiz</li>
						<li>Tells user their score and time</li>
					</ul></li>
				<li>Error checking
					<ul>User can never receive a 404 error from clicking a site link
						<li>Quiz profile page needs to handle a user trying to access a quiz that does not exist (e.g. forward them to the homepage)</li>
						<li>User profile page needs to handle a user trying to access a user that does not exist (e.g. forward them to the homepage)</li>
					</ul></li>
				<li>User sessions allow multiple users to visit site simultaneously</li>
					
						
						
			</ul>
		</div>
		
		
		<div class="bigFrame">
			<h1>Recommended Features and Extensions for Three-Person Team</h1>
			<ul class="checklist">
				<li>Quiz practice mode <span>(not yet implemented)</span></li>
				<li>Quizzes track performance of users</li> 
				<li>Quizzes track high scores</li>
				<li>Quiz pages display top scores<span> (not yet implemented)</span></li>
				<li>Optional question types
					<ul>
						<li>Multi-answer questions</li>
						<li>Multiple choice with multiple answers</li>
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
						<li>System tracks five major kind of user achievement:
							<ul>
								<li>Amateur author - the user created a quiz</li>
								<li>Prolific author - the user created five quizzes</li>
								<li>Prodigious author - the user created ten quizzes</li>
								<li>Quiz Machine - the user took ten quizzes</li>
								<li>Practice Makes Perfect - the user took a quiz in practice mode <span>(not yet implemented)</span></li>
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
						<li>CSS, JSP's, and Javascript used to create a uniform website experience</li>
						<li>Standard header and footer present on every page</li>
						<li>Header and footer feature useful links, including links customized to the user</li>
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
				<li>Website pages feature robust error handling
					<ul>
						<li>Sign in page uses Javascript for error checking</li>
						<li>Create account page uses Javascript for error checking</li>
						<li>User inputs throughout website are sanitized to prevent SQL injection</li>
						<li>Malformed inputs and URL's are handled gracefully
							<ul><li>e.g. when a signed-in user visits the sign in page, they are forwarded on to the homepage</li></ul>
						</li>
						<li>Messaging interface uses Javascript for instantaneous error checking</li></ul>
				<li>Catchy name for website</li>
			</ul>
		</div>
		<div class="bigFrame">
			<h1>Recommended features and extensions not implemented</h1>
			<ul>
				<li>User achievements displayed via badges <span>(needs to be done)</span></li>
				<li>Achievement badges displayed on user profiles <span>(needs to be done)</span></li>
				<li>Achievement badges display on user thumbnails <span>(needs to be done)</span></li>
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
				<li>Homepage displays a user's achievements <span>(not implemented)</span></li>
				<li>Link to edit the quiz if the user is the quiz owner</li>
				<li>Quiz categories</li>
				<li>Question categories</li>
				<li>User ratings and user reviews for quizzes</li>
				<li>User comment system for quizzes</li>
				<li>Allow users to flag quizzes as inappropriate</li>
				<li>Public view of website
					<ul>
						<li>Public view of user search (implemented)</li>
						<li>Public view of homepage</li>
						<li>Public view of user profiles</li>
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