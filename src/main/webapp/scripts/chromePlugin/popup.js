window.fbAsyncInit = function() {
	FB.init({
  		appId      : '144281815769348',	// App ID from the app dashboard
  		// No channel file for this app. check https://developers.facebook.com/docs/javascript/gettingstarted/#channel
  		// channelUrl : '//localhost:8888/AnonymizedShare/channel.html',	// Channel file for x-domain comms
  		status     : true,	// Check Facebook Login status
  		xfbml      : false	// Look for social plugins on the page
	});

    // Additional initialization code such as adding Event Listeners goes here 
    FB.login();
    //print to console friends info
    FB.api('/me/friends', function(response){
    	console.log(response);
    });
};

(function(d, s, id){
	var js, fjs = d.getElementsByTagName(s)[0];
 	if (d.getElementById(id)) {return;}
 	js = d.createElement(s); js.id = id;
 	js.src = "https://connect.facebook.net/en_US/all.js";
 	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));