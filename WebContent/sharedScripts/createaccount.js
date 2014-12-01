/**
 * 
 */

// Get the ID's of all the text boxes
usernameBox = document.getElementById('usernameBox');
firstNameBox = document.getElementById('firstNameBox');
lastNameBox = document.getElementById('lastNameBox');
emailBox = document.getElementById('emailBox');
profilePicBox = document.getElementById('profilePicBox');
pwdBox = document.getElementById('pwdBox');
confirmPwdBox = document.getElementById('confirmPwdBox');

// Get all of the warning messages
noUsername = document.getElementById('noUsername');
usernamTaken = document.getElementById('usernameTaken');
usernameInvalidChars = document.getElementById('usernameInvalidChars');
noName = document.getElementById('noName');
nameInvalidChars = document.getElementById('nameInvalidChars');
noEmail = document.getElementById('noEmail');
emailInvalidChars = document.getElementById('emailInvalidChars');
picInvalidChars = document.getElementById('picInvalidChars');
noPwd = document.getElementById('noPwd');
pwdLength = document.getElementById('pwdLength');
pwdMatch = document.getElementById('pwdMatch');
pwdInvalidChars = document.getElementById('pwdInvalidChars');
qwizardError = document.getElementById("qwizardError");

// Function to hide all error messages
function hideAllErrors() {
	msgs = document.getElementsByClassName('errorMsg');
	for (i in msgs) {
		msgs[i].style.display = 'none';
	}
}
hideAllErrors();

// Show or hide a particular error message
function showError(elem) {
	elem.style.display = 'block';
}
function hideError(elem) {
	elem.style.display = 'none';
}
function setError(elem,errorBool) {
	if (errorBool) {
		showError(elem);
		return true;
	} else {
		hideError(elem);
		return false;
	}
}

function validateInputFormatting() {
	// Strings for invalid characters
	usernameInvalid = "[^0-9a-zA-Z.]";
	nameInvalid = "[^a-zA-Z' -]";
	emailInvalid = "[^0-9a-zA-Z.@_-]";
	picInvalid = "[^0-9a-zA-Z._/:?=+-]";
	pwdInvalid = "[^0-9a-zA-Z !@#$%^&*()_=+:;,.?-]";
	
	// Boolean returns true if there is an error
	err = false;
	
	err1 = setError(noUsername,usernameBox.value == "");
	err = err || err1;
	err1 = setError(usernameInvalidChars,usernameBox.value.match(usernameInvalid));
	err = err || err1;
	// Check if username is taken

	
	err1 = setError(noName,firstNameBox.value == "" || lastNameBox.value == "");
	err = err || err1;
	
	nameInvalidError = (firstNameBox.value.match(nameInvalid) != null) || (lastNameBox.value.match(nameInvalid) != null);
	err1 =  setError(nameInvalidChars,nameInvalidError);
	err = err || err1;
	
	err1 = setError(noEmail,emailBox.value == "");
	err = err || err1;
	err1 = setError(emailInvalidChars,emailBox.value.match(emailInvalid));
	err = err || err1;
	
	err1 = setError(picInvalidChars,profilePicBox.value.match(picInvalid));
	err = err || err1;
	
	err1 = setError(noPwd,pwdBox.value == "");
	err = err || err1;
	err1 = setError(pwdLength,pwdBox.value != "" && pwdBox.value.length < 5);
	err = err || err1;
	err1 = setError(pwdMatch,pwdBox.value != "" && pwdBox.value != confirmPwdBox.value);
	err = err || err1;
	err1 = setError(pwdInvalidChars,pwdBox.value.match(pwdInvalid));
	err = err || err1;
	
	return err;
}

function getResponseAndSubmit() {
	statusStr = requestObj.responseText;
	KAVAILABLE = "0";
	KTAKEN = "1";
	KERROR = "2";
	
	switch(statusStr) {
		case KAVAILABLE:
			hideError(usernameTaken);
			hideError(qwizardError);
			document.getElementById("createform").submit();
			break;
		case KTAKEN:
			showError(usernameTaken);
			hideError(qwizardError);
			break;
		case KERROR:
			hideError(usernameTaken);
			showError(qwizardError);
			break;		
	}
}

function checkUsernameAndSubmit() {
	query = 'username=' + usernameBox.value;
	checkUsernameServlettURL = "/QuizSite/CheckUsernameServlet";
	requestObj = new XMLHttpRequest();
	requestObj.addEventListener("load",function anonfcn() { getResponseAndSubmit(); },null);
	requestObj.open("POST",checkUsernameServlettURL,true); // True makes the request asynchronous
	requestObj.setRequestHeader("Content-type","application/x-www-form-urlencoded");	
	requestObj.send(query);
}

function validateInputs() {
	err1 = validateInputFormatting();
	if (!err) {
		checkUsernameAndSubmit();
	}
}