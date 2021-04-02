var googleUser = {};
var startApp = function() {
  gapi.load('auth2', function(){
    // Retrieve the singleton for the GoogleAuth library and set up the client.
    auth2 = gapi.auth2.init({
      client_id: '512455545742-d24qi4bku033qgg0pt4kglnpf4sprb95.apps.googleusercontent.com',
      cookiepolicy: 'single_host_origin',
      // Request scopes in addition to 'profile' and 'email'
      //scope: 'additional_scope'
    });
    attachSignin(document.getElementById('customBtn'));
  });
};

function attachSignin(element) {
  auth2.attachClickHandler(element, {},
      function(googleUser) {
        	var profile = googleUser.getBasicProfile();
		document.getElementById('gname').value = profile.getName();
	  	document.getElementById('gemail').value = profile.getEmail();
	  	document.getElementById('gemail').readOnly= true;
	  	document.getElementById('glink').value = profile.getId(); 
      }, function(error) {
        alert(JSON.stringify(error, undefined, 2));
      });
}