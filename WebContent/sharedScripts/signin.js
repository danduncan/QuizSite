/**
 * signin.js
 */

// Get the ID's of all the text boxes
usernameBox = document.getElementById('usernameBox');
pwdBox = document.getElementById('pwdBox');

// Get ID's of error messages
noUsername = document.getElementById('noUsername');
usernameInvalidChars = document.getElementById('usernameInvalidChars');
noPwd = document.getElementById('noPwd');
pwdInvalidChars = document.getElementById('pwdInvalidChars');
wrongPwd = document.getElementById('wrongPwd');
qwizardError = document.getElementById("qwizardError");

//Function to hide all error messages
function hideAllErrors() {
	msgs = document.getElementsByClassName('errorMsg');
	for (i in msgs) {
		msgs[i].style.display = 'none';
	}
}
hideAllErrors();

//Show or hide a particular error message
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
	pwdInvalid = "[^0-9a-zA-Z !@#$%^&*()_=+:;,.?-]";
	
	// Boolean returns true if there is an error
	err = false;
	
	err1 = setError(noUsername,usernameBox.value == "");
	err = err || err1;
	err1 = setError(usernameInvalidChars,usernameBox.value.match(usernameInvalid));
	err = err || err1;
		
	err1 = setError(noPwd,pwdBox.value == "");
	err = err || err1;
	err1 = setError(pwdInvalidChars,pwdBox.value.match(pwdInvalid));
	err = err || err1;
	
	return err;
}

function getResponseAndSubmit() {
	statusStr = requestObj.responseText;
	KSUCCESS = "0";
	KFAILURE = "1";
	KERROR = "2";
	
	switch(statusStr) {
		case KSUCCESS:
			hideError(wrongPwd);
			hideError(qwizardError);
			document.getElementById("signinform").submit();
			break;
		case KFAILURE:
			showError(wrongPwd);
			hideError(qwizardError);
			break;
		case KERROR:
			hideError(wrongPwd);
			showError(qwizardError);
			break;		
	}
}

function checkCredentialsAndSubmit() {
	query = 'username=' + usernameBox.value + '&password=' + pwdBox.value;
	checkLoginCredentialsServletURL = "/QuizSite/CheckLoginCredentialsServlet";
	requestObj = new XMLHttpRequest();
	requestObj.addEventListener("load",function anonfcn() { getResponseAndSubmit(); },null);
	requestObj.open("POST",checkLoginCredentialsServletURL,true); // True makes the request asynchronous
	requestObj.setRequestHeader("Content-type","application/x-www-form-urlencoded");	
	requestObj.send(query);
}

function validate() {
	err = validateInputFormatting();
	if (!err) {
		//document.getElementById("signinform").submit();
		checkCredentialsAndSubmit();
	}
}