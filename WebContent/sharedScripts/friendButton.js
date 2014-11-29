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
	status = requestObj.responseText;
	
	// Response status is 0 for success, nonzero for failure
	if (status == 0) {
		friendButton.style.background='#71AB57';
		friendButton.value='Friend Requested';
		friendButton.style.boxShadow='none';
		friendButton.disabled = true;
		friendButton.style.border='1px solid gray';
	} else {
		friendButton.style.background='#AB7157';
		friendButton.value='Request Failed';
		friendButton.style.boxShadow='none';
		friendButton.disabled = true;
		friendButton.style.border='1px solid gray';
	}
}

