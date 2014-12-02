/**
 * 
 */

captionID = 'achievementBadgeCaptionID';
captionClass = 'badgeCaption';
var badgeCaption = document.createElement('DIV');
badgeCaption.className = captionClass;
badgeCaption.id = captionID;
document.body.appendChild(badgeCaption);
//console.log(badgeCaption);

function displayBadgeCaption(image,show) {
	caption = document.getElementById(captionID);
	if (caption == null) {
		caption = document.createElement('DIV');
		caption.className = captionClass;
		caption.id = captionID;
		document.body.appendChild(caption);
		console.log("creating a second time");
	}
	
	if(!show) {
		caption.style.visibility = "hidden";
		return;
	}
	
	//console.log('Testing');
	var rect = image.getBoundingClientRect();
	//console.log(rect);
		
	
	
	topCo = rect.bottom + 6 + "px";
	leftCo = rect.left - 2 + "px";
	//console.log("Top is: " + topCo);
	//console.log("Left is: " + leftCo);
	caption.style.top = topCo;
	caption.style.left = leftCo;
	
	caption.innerHTML = image.alt;
	caption.style.visibility = 'visible';
	//console.log(caption.getBoundingClientRect());
}