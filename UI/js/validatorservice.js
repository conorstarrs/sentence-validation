function callAWS() {
    var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function () {
	  if(xhttp.readyState === XMLHttpRequest.DONE) {
		var status = xhttp.status;
		if (status === 0 || (status >= 200 && status < 400)) {
		  // The request has been completed successfully
		  console.log(xhttp.responseText);
		  buildResponse(xhttp.responseText)
		} else {
		  // Oh no! There has been an error with the request!
		}
	  }
	};
	
    var sentence = document.getElementById("sentence").value;
	response.innerHTML = "";
    
	if(sentence !== "" && sentence.trim().length > 0) {
		xhttp.open("POST", "https://assignment-api.conorstarrs.dev/sentence-validation", true);
		xhttp.send(sentence);		
	} else {
		document.getElementById('response').innerHTML = "<div class=\"_warning\">Please enter a sentence to validate.</div><br />";
	}	

    return false;
}

function buildResponse(responseText) {
	const response = document.getElementById('response');
	var obj = JSON.parse(responseText);
	
	renderOverallResult(response, obj);
	renderResultBreakdown(response, obj);
}

function renderOverallResult(response, obj) {
	if(obj.isValid) {
		response.innerHTML += "<div class=\"_success\">VALID</div><br />";
	} else {
		response.innerHTML += "<div class=\"_danger\">INVALID</div><br />";
	}
}	

function renderResultBreakdown(response, obj) {
	// String starts with a capital letter
    response.innerHTML += obj.validationDetails.isFirstLetterCapital ? "<img src=\"images/green-tick.png\"> " : "<img src=\"images/red-x.png\"> ";
	response.innerHTML += "String starts with a capital letter<br />";
	
	// String has an even number of quotation marks
	response.innerHTML += obj.validationDetails.hasEvenNumberOfQuotes ? "<img src=\"images/green-tick.png\"> " : "<img src=\"images/red-x.png\"> ";
    response.innerHTML += "String has an even number of quotation marks<br />";
	
	// String ends with a period character ".";
	response.innerHTML += obj.validationDetails.endsWithAPeriod ? "<img src=\"images/green-tick.png\"> " : "<img src=\"images/red-x.png\"> ";
    response.innerHTML += "String ends with a period character \".\"<br />";
	
	// String has no period characters other than the last character
	response.innerHTML += obj.validationDetails.hasNoPeriodOtherThanLastCharacter ? "<img src=\"images/green-tick.png\"> " : "<img src=\"images/red-x.png\"> ";
    response.innerHTML += "String has no period characters other than the last character<br />";
	
	// Numbers below 13 are spelled out (”one”, “two”, &quot;three”, etc…)
	response.innerHTML += obj.validationDetails.hasNoIntegersBelowThirteen ? "<img src=\"images/green-tick.png\"> " : "<img src=\"images/red-x.png\"> ";
    response.innerHTML += "Numbers below 13 are spelled out (\"one\", \"two\", \"three\", etc...)<br />";
}	