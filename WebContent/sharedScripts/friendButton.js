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
	query = "buttonid=" + buttonid + "&receiverid=" + receiverid;

	requestObj = new XMLHttpRequest();
	requestObj.open("POST","http://localhost:8080/QuizSite/AddFriendServlet",true);
	requestObj.setRequestHeader("Content-type","application/x-www-form-urlencoded");	
	requestObj.send(query);
	
	friendButton.style.background='#71AB57';
	friendButton.value='Friend Requested';
	friendButton.style.boxShadow='none';
	friendButton.disabled = true;
	friendButton.style.border='1px solid gray';
}

