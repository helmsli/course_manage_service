angular.module('App', []);
//login with pass
angular.module('App').controller('control_loginwithpass', function() {
	var loginWithPassSession = this;
	loginWithPassSession.countryCode="0086";
	loginWithPassSession.phone="18612131415";
	loginWithPassSession.password="password";	

	loginWithPassSession.nglogin = function()
	{
		loginWithPassSession.getPublicKey();
	};
	
	loginWithPassSession.getPublicKey=function()
	{
		ajaxGetRsaPublicKey(loginWithPassSession.countryCode,loginWithPassSession.phone,function success(result,authCode)
		{
			if(result==0)
			{
				loginWithPassSession.transid=authCode.transid;
				loginWithPassSession.publicKey=authCode.publicKey;
				loginWithPassSession.random = authCode.random;
				loginWithPassSession.crcType = authCode.crcType;
                
				var encrypt = new JSEncrypt();
				console.log("**************");
				console.log(loginWithPassSession.random);
				console.log(loginWithPassSession.password);
				
				 encrypt.setPublicKey(loginWithPassSession.publicKey);
				 var encrypted = encrypt.encrypt(loginWithPassSession.random+loginWithPassSession.password);
			
				console.log(JSON.stringify(loginWithPassSession));
				console.log(")))))))))))))))))))))");
				console.log(encrypted);
				console.log("((((((((((((((((((((((");
				ajaxLoginWithPass(loginWithPassSession.transid,loginWithPassSession.countryCode,loginWithPassSession.phone,encrypted);

			}
		},
		function error(xhr,testStatus){
			
		});		
	};

	
	
  });
//login with code
angular.module('App').controller('control_loginwithcode', function() {
	var loginWithCodeSession = this;
	loginWithCodeSession.countryCode="0086";
	loginWithCodeSession.phone="18612131415";
	loginWithCodeSession.password="password";	

	loginWithCodeSession.nglogin = function()
	{
		ajaxLoginWithAuthCode(loginWithCodeSession.transid,loginWithCodeSession.countryCode,loginWithCodeSession.phone,loginWithCodeSession.authcode);
	};
	
	loginWithCodeSession.getAuthCode=function()
	{
		ajaxGetAuthCode(loginWithCodeSession.countryCode,loginWithCodeSession.phone,function success(result,authCode)
		{
			if(result==0)
			{
				loginWithCodeSession.transid=authCode.transid;
				loginWithCodeSession.publicKey=authCode.publicKey;
				loginWithCodeSession.random = authCode.random;
				loginWithCodeSession.crcType = authCode.crcType;

			}
		},
		function error(xhr,testStatus){
			
		});		
	};
	

	
	
  });
//register with code
angular.module('App').controller('control_register', function() {
	var registerSession = this;
	registerSession.countryCode="0086";
	registerSession.phone="18612131415";
	registerSession.password="password";	

	registerSession.register = function()
	{
		 var encrypt = new JSEncrypt();
		 encrypt.setPublicKey(registerSession.publicKey);
		 var encrypted = encrypt.encrypt(registerSession.random+registerSession.password);
		 ajaxRegisterWithAuth(registerSession.countryCode,registerSession.phone,encrypted,registerSession.transid,registerSession.authcode);
	};
	registerSession.getAuthCode=function()
	{
		ajaxGetAuthCode(registerSession.countryCode,registerSession.phone,function success(result,authCode)
		{
			if(result==0)
			{
				registerSession.transid=authCode.transid;
				registerSession.publicKey=authCode.publicKey;
				registerSession.random = authCode.random;
				registerSession.crcType = authCode.crcType;

			}
		},
		function error(xhr,testStatus){
			
		});		
	};
	
	
  });

//control_resetPassSession
//reset password
//register with code
angular.module('App').controller('control_resetPassSession', function() {
	var resetPassSession = this;
	resetPassSession.countryCode="0086";
	resetPassSession.phone="18612131415";
	resetPassSession.password="password";	

	resetPassSession.resetPassword = function()
	{
		 var encrypt = new JSEncrypt();
		 encrypt.setPublicKey(resetPassSession.publicKey);
		 var encrypted = encrypt.encrypt(resetPassSession.random+resetPassSession.password);
		 ajaxResetPassword(resetPassSession.countryCode,resetPassSession.phone,encrypted,resetPassSession.transid,resetPassSession.authcode);
	};
	resetPassSession.getAuthCode=function()
	{
		ajaxGetAuthCode(resetPassSession.countryCode,resetPassSession.phone,function success(result,authCode)
		{
			if(result==0)
			{
				resetPassSession.transid=authCode.transid;
				resetPassSession.publicKey=authCode.publicKey;
				resetPassSession.random = authCode.random;
				resetPassSession.crcType = authCode.crcType;

			}
		},
		function error(xhr,testStatus){
			
		});		
	};
	
	
  });

//control_modifyPassSession
//modify password
//modify
angular.module('App').controller('control_modifyPassSession', function() {
	var modifyPassSession = this;
	modifyPassSession.countryCode="0086";
	modifyPassSession.phone="18612131415";
	modifyPassSession.oldPassword="password";	
	modifyPassSession.newPassword="password";	

	modifyPassSession.modifyPassword = function()
	{
		modifyPassSession.getPublicKey();
	};
	modifyPassSession.getPublicKey=function()
	{
		ajaxGetRsaPublicKey(modifyPassSession.countryCode,modifyPassSession.phone,function success(result,authCode)
		{
			if(result==0)
			{
				modifyPassSession.transid=authCode.transid;
				modifyPassSession.publicKey=authCode.publicKey;
				modifyPassSession.random = authCode.random;
				modifyPassSession.crcType = authCode.crcType;
                
				var encrypt = new JSEncrypt();
				
				
				 encrypt.setPublicKey(modifyPassSession.publicKey);
				 var encryptNewPassword = encrypt.encrypt(modifyPassSession.random+modifyPassSession.newPassword);
				 var encryptOldPassword = encrypt.encrypt(modifyPassSession.random+modifyPassSession.oldPassword);
					
				console.log(JSON.stringify(modifyPassSession));
				
				ajaxModifyPassword(modifyPassSession.countryCode,modifyPassSession.phone,encryptOldPassword,encryptNewPassword,modifyPassSession.transid);

			}
		},
		function error(xhr,testStatus){
			
		});		
	};

});
/*
angular.module('app').controller('SecondCtrl',['$scope',function($scope){
    
}]);
*/