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







function deleteFriend(button)
{
	buttonid = button.id;
	friendidArr = buttonid.split('-');
	friendid = friendidArr[1];
	query = "buttonid=" + buttonid + "&friendid=" + friendid + "&userid=" + useridjs;

	requestObj = new XMLHttpRequest();
	requestObj.addEventListener("load",function anonfcn() { processDeleteFriendResponse(button); },null);
	requestObj.open("POST","http://localhost:8080/QuizSite/DeleteFriendServlet",true);
	requestObj.setRequestHeader("Content-type","application/x-www-form-urlencoded");	
	requestObj.send(query);
}

function processDeleteFriendResponse(friendButton) {
	statusStr = requestObj.responseText;
	
	KSUCCESS = "0";
	KFAILURE = "1";
	KNOTFRIENDS = "2";
	KNOTLOGGEDIN = "3";
	KSELF = "4";
	
	// Response status is 0 for success, nonzero for failure
	switch(statusStr) {
		case KSUCCESS:
			friendButton.className = "successfulDeleteBtn";
			friendButton.value = "Friend Deleted";
			friendButton.disabled = true;
			break;
		case KNOTFRIENDS:
			friendButton.className = "alreadyFriendsBtn";
			friendButton.value = "Not Friends";
			friendButton.disabled = true;
			break;
		case KNOTLOGGEDIN:
			friendButton.className = "loginBtn";
			friendButton.value = "Log In First";
			friendButton.onclick = "window.location='/QuizSite/signin.jsp';";
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















// If you are calling confirmFriendRequest directly, don't bother supplying the second argument
// The second argument is only needed for denyFriendRequest()
function confirmFriendRequest(button,deny) {
	if(deny == null || deny == false) {
		action = "confirm";
		confirm = true;
	} else {
		action = "deny";
		confirm = false;
	}
	buttonid = button.id;
	senderidArr = buttonid.split('-');
	senderid = senderidArr[1];
	//senderid = "1";
	query = "buttonid=" + buttonid + "&senderid=" + senderid + "&userid=" + useridjs + "&action=" + action;

	requestObj = new XMLHttpRequest();
	requestObj.addEventListener("load",function anonfcn() { processConfirmResponse(button,confirm); },null);
	requestObj.open("POST","http://localhost:8080/QuizSite/ConfirmFriendServlet",true);
	requestObj.setRequestHeader("Content-type","application/x-www-form-urlencoded");	
	requestObj.send(query);
}

function denyFriendRequest(button) {
	confirmFriendRequest(button,true);
}

function processConfirmResponse(button,confirm) {
	// Find both buttons
	btnid = button.id;
	senderidArr = btnid.split('-');
	senderid = senderidArr[1];
	
	confirmBtnId = 'confirmFriendButtonId-' + senderid;
	denyBtnId = 'denyFriendButtonId-' + senderid;
	
	confirmButton = document.getElementById(confirmBtnId);
	denyButton = document.getElementById(denyBtnId);
	
	if(confirmButton == null && denyButton == null) {
		// Neither button exists. Check that you gave the buttons the correct ID's
		alert("Neither a confirm button nor a deny button could be found. Check that you gave the buttons the correct ID's \nconfirmFriendButtonId-X \ndenyFriendButtonId-X");
		return;
	}
	
	// Check if one of the buttons does not exist
	// If both buttons exist, hide the deny button
	if (confirmButton == null) {
		confirmButton = denyButton;
	} else if (denyButton != null) {
		// Hide the deny button
		denyButton.style.display = 'none';		
	}
	
	statusStr = requestObj.responseText;	
	KSUCCESS = "0";
	KFAILURE = "1";
	KFRIENDS = "2";
	KNOTLOGGEDIN = "3";
	KNOTPENDING = "4";
	KSELF = "5";
	
	//statusStr = KSUCCESS;
	
	// Response status is 0 for success, nonzero for failure
	switch(statusStr) {
		case KSUCCESS:
			confirmButton.className = "alreadyFriendsBtn";
			confirmButton.disabled = true;
			if (confirm) {
				confirmButton.value = "Friends Confirmed";
			} else {
				confirmButton.value = "Request Denied";
			}
			break;
		case KFRIENDS:
			confirmButton.className = "alreadyFriendsBtn";
			confirmButton.value = "Friends";
			confirmButton.disabled = true;
			break;
		case KNOTLOGGEDIN:
			confirmButton.className = "loginBtn";
			confirmButton.value = "Log In First";
			confirmButton.onclick = "window.location='/QuizSite/signin.jsp';";
			break;
		case KNOTPENDING:
			confirmButton.className = "addFriendBtn";
			confirmButton.value = "Add Friend";
			confirmButton.onclick = "sendFriendRequest(this)";
			break;
		case KSELF:
			confirmButton.className = "selfBtn";
			confirmButton.value = "This Is You";
			confirmButton.disabled = true;
			break;
		case KFAILURE:
			confirmButton.className = "errorBtn";
			confirmButton.value = "Database Error";
			confirmButton.disabled = true;
			break;
		default:
			confirmButton.className = "errorBtn";
			confirmButton.value = "Server Error";
			confirmButton.disabled = true;
	}
	
}
