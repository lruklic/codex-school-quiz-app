window.fbAsyncInit = function() {
    	FB.init({
      		appId      : '909346752488844',
      		xfbml      : true,
      		version    : 'v2.4'
    	});
    		
};

(function(d, s, id){
    	var js, fjs = d.getElementsByTagName(s)[0];
    	if (d.getElementById(id)) {return;}
     	js = d.createElement(s); js.id = id;
     	js.src = "//connect.facebook.net/en_US/sdk.js";
     	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

function checkLoginState() {
    FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
    });
}

function statusChangeCallback(response) {
	if (response.status === "connected") {
		FB.logout(function(response) {
			window.location = "/logout";
	    });
	} else {
		window.location = "/logout";
	}

	
}

$(document).ready(function(){
	
	// Enable JS plugin tooltipster for every tag with .tooltip class
	$('.tooltip-custom').tooltipster({		   
		animation: 'grow',
		delay: 800,
		theme: 'tooltipster-default',
		touchDevices: false,
		trigger: 'hover'
	});
	
	$('.tooltip-info').tooltipster({		   
		animation: 'grow',
		position: 'right',
		maxWidth: 250,
		contentAsHTML: true,
		theme: 'tooltipster-default',
		touchDevices: false,
		trigger: 'click'
	});
	
});
