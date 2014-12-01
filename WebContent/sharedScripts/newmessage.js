/**
 * This is the javascript necessary to send a message via AJAX
 */

// Get the master divs
newMsgMasterContainer = document.getElementById("newMessageMasterContainer");

// Get user inputs
newMsgUsername = document.getElementById("newMsgUsername");
newMsgSubject = document.getElementById("newMsgSubject");
newMsgBody = document.getElementById("newMsgBody");
newMsgType = document.getElementById("newMsgType");

// Get the buttons
sendMsgBtn = document.getElementById("sendMsgBtn");
cancelMsgBtn = document.getElementById("cancelMsgBtn");
closeMsgBtn = document.getElementById("closeMsgBtn");

// Get the error messages
noUsername = document.getElementById("noUsername");
invalidUsername = document.getElementById("invalidUsername");
userDoesNotExist = document.getElementById("userDoesNotExist");
noContent = document.getElementById("noContent");
qwizardError = document.getElementById("qwizardError");

// Get the success message
msgSuccess = document.getElementById("msgSuccess");



// Hide all messages
function hideAllMessages() {
	msgs = document.getElementsByClassName('errorMsg');
	for (i = 0; i < msgs.length; i++) {
		msgs[i].style.display = 'none';
	}
	return;
}
hideAllMessages();

// Show or hide specific messages
function showMessage(elem) { elem.style.display = 'block'; }
function hideMessage(elem) { elem.style.display = 'none'; }
function setMessage(elem,boolShow) {
	if (boolShow) {
		showMessage(elem);
		return true;
	} else {
		hideMessage(elem);
		return false;
	}
}

function clearMessageFields() {
	newMsgUsername.value = "";
	newMsgSubject.value = "";
	newMsgBody.value = "";
}

function enableMessageFields(boolEnable) {
	newMsgUsername.readOnly = !boolEnable;
	newMsgSubject.readOnly = !boolEnable;
	newMsgBody.readOnly = !boolEnable;
}
function enableMessageButtons(boolEnable) {
	sendMsgBtn.disabled = !boolEnable;
	cancelMsgBtn.disabled = !boolEnable;
	closeMsgBtn.disabled = !boolEnable;
}

function validateUsernameFormatting() {
	// This function validates multiple comma-delimited usernames
	// Returns true if an invalid username was found
	username = newMsgUsername.value.trim();

	// Check if no username was specified
	err = setMessage(noUsername,username == "");
	if (err) return true;

	// Username was specified. Check that it is correctly formatted
	delimiter = ' ';
	un = username.replace(/\s{2,}/g, ' '); // Replace multiple whitespaces with single
	un = un.split(delimiter);
	invalidCharStr = "[^0-9a-zA-Z.]";

	if (un.length == 0) {
		showMessage(invalidUsername);
		return true;
	}

	for (i = 0; i < un.length; i++) {
		if(un[i] == "") {
			showMessage(invalidUsername);
			return true;
		}

		if(un[i].match(invalidCharStr) != null) {
			showMessage(invalidUsername);
			return true;
		}
	}

	// No errors found
	return false;
}

function validateInputFormatting() {
	err1 = validateUsernameFormatting();
	err2 = setMessage(noContent,newMsgBody.value == "");
	err = (err1 || err2);
	
	// Get sender ID
	if (typeof useridjs == 'undefined' || useridjs == null || useridjs < 1) {
		err = true;
	}
	
	return err;
}

function getUsernameUrlFormat(username) {
	username = username.trim(); // Eliminate leading and trailing whitespaces
	username = username.replace(/\s{2,}/g, ' '); // Replace multiple whitespaces with a single one
	username = username.replace(/ /g, '+'); // Replace all whitespaces with a + sign
	return username;
}


// Toggle the two button configurations
// True -> compose configuration, False -> close configuration
function buttonConfig(compose) {
	if (compose) {
		sendMsgBtn.style.display = 'block';
		cancelMsgBtn.style.display = 'block';
		closeMsgBtn.style.display = 'none';
	} else {
		sendMsgBtn.style.display = 'none';
		cancelMsgBtn.style.display = 'none';
		closeMsgBtn.style.display = 'block';
	}
}

function getAjaxMsgResponse() {
	statusStr = requestObj.responseText;
	KSENT = "0";
	KUSERDOESNOTEXIST = "1";
	KERROR = "2";
	
	switch(statusStr) {
		case KSENT:
			hideMessage(userDoesNotExist);
			hideMessage(qwizardError);
			showMessage(msgSuccess);
			buttonConfig(false);
			enableMessageButtons(true);
			break;
		case KUSERDOESNOTEXIST:
			showMessage(userDoesNotExist);
			hideMessage(qwizardError);
			hideMessage(msgSuccess);
			buttonConfig(true);
			enableMessageFields(true);
			enableMessageButtons(true);
			break;
		case KERROR:
			hideMessage(userDoesNotExist);
			showMessage(qwizardError);
			hideMessage(msgSuccess);
			buttonConfig(true);
			enableMessageFields(true);
			enableMessageButtons(true);
			break;
	}
}

function submitMessage() {
	username = getUsernameUrlFormat(newMsgUsername.value); // Gets a '+' delimited set of usernames
	subject = newMsgSubject.value;
	body = newMsgBody.value;
	type = newMsgType.value;
	query = 'username=' + username + '&subject=' + subject + '&body=' + body + '&type=' + type + "&senderid=" + useridjs;
	checkUsernameServletURL = "/QuizSite/SendMessageAjaxServlet";
	requestObj = new XMLHttpRequest();
	requestObj.addEventListener("load",function anonfcn() { getAjaxMsgResponse(); },null);
	requestObj.open("POST",checkUsernameServletURL,true); // True makes the request asynchronous
	requestObj.setRequestHeader("Content-type","application/x-www-form-urlencoded");	
	requestObj.send(query);
}

function sendMessage() {
	
	// Set everything read-only
	enableMessageFields(false);
	enableMessageButtons(false);
	
	// Check if inputs are formatted correctly. If not, have user go back and fix them
	if(validateInputFormatting()) {
		enableMessageFields(true);
		enableMessageButtons(true);
		return;
	}
	
	submitMessage();
}

function resetMessageForm() {
	hideAllMessages();
	clearMessageFields();
	enableMessageFields(true);
	enableMessageButtons(true);
	buttonConfig(true);
}

function closeMessage() {
	newMsgMasterContainer.style.display = 'none';
	resetMessageForm();
}
closeMessage();

function displayMessage(username) {
	resetMessageForm();
	if (username != null) newMsgUsername.value = username;
	newMsgMasterContainer.style.display = 'block';
}