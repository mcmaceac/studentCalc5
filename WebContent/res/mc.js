/**
 * 
 */
function validate() {
	var ok = true;
	var p = document.getElementById("principal").value;
	
	if (isNaN(p) || p <= 0) {
		document.getElementById("principalError").innerHTML = "*";
		alert("Principal invalid!");
		ok = false;
	}
	else {
		document.getElementById("principalError").innerHTML = "";
	}
	
	p = document.getElementById("interest").value;
	if (isNaN(p) || p <= 0 || p >= 100) {
		document.getElementById("interestError").innerHTML = "*";
		alert("Interest invalid!");
		ok = false;
	}
	else {
		document.getElementById("interestError").innerHTML = "";
	}
	
	p = document.getElementById("period").value
	if (isNaN(p) || p <= 0) {
		document.getElementById("periodError").innerHTML = "*";
		alert("Period invalid!");
		ok = false;
	}
	else {
		document.getElementById("periodError").innerHTML = "";
	}
	
	return ok
}

function doSimpleAjax(address) {
	var request = new XMLHttpRequest();
	var data='';
	
	var principal = document.getElementById("principal").value;
	var interest = document.getElementById("interest").value;
	var period = document.getElementById("period").value;
	var grace = document.getElementById("graceEnabled").checked;
	
	data += "principal=" + principal + "&interest=" + interest + "&period=" + period;
	if (grace == true) {
		data += "&graceEnabled=" + grace;
	}
	
	request.onreadystatechange = function() {
		handler(request);
	};
	
	request.open("POST", address, true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(data);
}

function handler(request) { 
	var target = document.getElementById("ajaxResult");
	if ((request.readyState == 4) && (request.status == 200)) {
		target.innerHTML = request.responseText;
	}
}





