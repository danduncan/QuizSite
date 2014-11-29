/**
 * This is the code you need to import into any file that includes "Add Friend" buttons
 */



function sendFriendRequest(friendButton)
	{
		
		
	
		friendButton.style.background='#71AB57';
		friendButton.value='Friend Requested';
		friendButton.style.boxShadow='none';
		friendButton.disabled = true;
		friendButton.style.border='1px solid gray';
	}

