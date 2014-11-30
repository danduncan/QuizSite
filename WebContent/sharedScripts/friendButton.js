/**
 * This is the code you need to import into any file that includes "Add Friend" buttons
 */



function sendFriendRequest(friendButton)
{
	receiveridArray = friendButton.id.split('-');
	receiverid = receiveridArray[1];
	buttonid = friendButton.id;
	receiveridArr = buttonid.split('-');
	receiverid = receiveridArr[1];
	query = "buttonid=" + buttonid + "&receiverid=" + receiverid + "&userid=" + useridjs;

	requestObj = new XMLHttpRequest();
	requestObj.addEventListener("load",function anonfcn() { processFriendRequestResponse(friendButton); },null);
	requestObj.open("POST","http://localhost:8080/QuizSite/AddFriendServlet",true);
	requestObj.setRequestHeader("Content-type","application/x-www-form-urlencoded");	
	requestObj.send(query);
}

function processFriendRequestResponse(friendButton) {
	statusStr = requestObj.responseText;
	
	KSUCCESS = "0";
	KFAILURE = "1";
	KFRIENDS = "2";
	KNOTLOGGEDIN = "3";
	KPENDING = "4";
	KCONFIRM = "5";
	KSELF = "6";
	
	// Response status is 0 for success, nonzero for failure
	switch(statusStr) {
		case KSUCCESS:
			friendButton.className = "successfulRequestBtn";
			friendButton.value = "Friend Requested";
			friendButton.disabled = true;
			break;
		case KFRIENDS:
			friendButton.className = "alreadyFriendsBtn";
			friendButton.value = "Friends";
			friendButton.disabled = true;
			break;
		case KNOTLOGGEDIN:
			friendButton.className = "loginBtn";
			friendButton.value = "Log In First";
			friendButton.onclick = "window.location='/QuizSite/signin.jsp';";
			break;
		case KPENDING:
			friendButton.className = "pendingBtn";
			friendButton.value = "Request Pending";
			friendButton.disabled = true;
			break;
		case KCONFIRM:
			friendButton.className = "confirmBtn";
			friendButton.value = "Confirm Request";
			friendButton.disabled = true;
			break;
		case KSELF:
			friendButton.className = "selfBtn";
			friendButton.value = "This Is You";
			friendButton.disabled = true;
			break;
		case KFAILURE:
			friendButton.className = "errorBtn";
			friendButton.value = "Database Error";
			friendButton.disabled = true;
			break;
		default:
			friendButton.className = "errorBtn";
			friendButton.value = "Server Error";
			friendButton.disabled = true;
	}
	
	
}

