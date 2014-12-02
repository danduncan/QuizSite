/**
 * 
 */

captionID = 'achievementBadgeCaptionID';
captionClass = 'badgeCaption';
var badgeCaption = document.createElement('DIV');
badgeCaption.className = captionClass;
badgeCaption.id = captionID;
document.body.appendChild(badgeCaption);
console.log(badgeCaption);

function displayBadgeCaption(image,show) {
	caption = document.getElementById(captionID);
	if (caption == null) {
		caption = document.createElement('DIV');
		caption.className = captionClass;
		caption.id = captionID;
		document.body.appendChild(caption);
		console.log("creating a second time");
	}
	
	
	//console.log('Testing');
	var rect = image.getBoundingClientRect()
	centerx = (rect.left + rect.right) / 2;
	centery = (rect.top + rect.bottom) / 2;
	
	
	
	if(!show) {
		caption.style.visibility = "hidden";
		return;
	}
	
	caption.innerHTML = image.alt;
	caption.style.top = rect.bottom + 4 + "px";
	caption.style.left = rect.left - 2 + "px";
	caption.style.visibility = 'visible';
}