function login(){
	var dayApp = Ti.App.dayApp;
	
	Ti.API.info('=> Device: ' + dayApp.isiPad); 
	
	var self = Ti.UI.createWindow({
		backgroundColor: '#434341',
		navBarHidden: true
	});
	
	//var screenHeight = Ti.Platform.DisplayCaps.platformHeight;
	//var screenWidth = Ti.Platform.DisplayCaps.platformWidth;
	
	//Ti.API.info('=> screen height: ' + screenHeight + ', sw: ' + screenWidth );
	
	if (dayApp.isiOS) {
		var navWin = Ti.UI.iOS.createNavigationWindow({
			window: self
		});
	}else {
		self.addEventListener('open', function (){
			var actionBar = self.activity.actionBar;
			actionBar.hide();
			Ti.UI.Android.hideSoftKeyboard();
		});	
	}
	
	var containerView = Ti.UI.createView({
		top: 100,
		width: Ti.UI.FILL,
		height: 350
		
	});
	
	self.add(containerView);
	
	var imgLogo = Titanium.UI.createImageView({
		image: 'http://static.appcelerator.com/images/header/appc_logo.png',
		width:218,
		height:46,
		top: 70
	});
	
	var txtUserName = Ti.UI.createTextField({
		color: '#000000',
		top : 170,
		width: 250,
		height: 40,
		hintText: 'Username',
		keyboardType:Titanium.UI.KEYBOARD_DEFAULT,
		returnKeyType:Titanium.UI.RETURNKEY_DEFAULT,
		clearButtonMode : Ti.UI.INPUT_BUTTONMODE_ALWAYS,
		editable: true,
		backgroundColor: '#FFFFFF'
		
		//borderColor: '#000000'
		
	});
	
	var txtPassword = Ti.UI.createTextField({
		color: '#000000',
		top : 220,
		width: 250,
		height: 40,
		hintText: 'Password',
		keyboardType:Titanium.UI.KEYBOARD_DEFAULT,
		returnKeyType:Titanium.UI.RETURNKEY_DEFAULT,
		clearButtonMode : Ti.UI.INPUT_BUTTONMODE_ALWAYS,
		backgroundColor: '#FFFFFF',
		passwordMask: true,
		editable: true
	});
	
	var btnLogin = Ti.UI.createButton({
		width: 250,
		height: 40,
		top: 270,
		title: L('btnLogin'),
		backgroundColor: '#fc8506',
		color: '#FFFFFF',
		font: {fontFamily: 'Helvetica', fontSize: 16, fontWeigth: 'normal'}
		
	});
	
	containerView.add(imgLogo);
	containerView.add(txtUserName);
	containerView.add(txtPassword);
	containerView.add(btnLogin);
	
	
	btnLogin.addEventListener('click', function(){
		
		var title, message, access;
		access = true;
		
		if (txtUserName.value === '' || txtUserName.value === null || txtUserName.value === undefined ) {
			dayApp.util.alertMsg(L('appName'), L('msgUNEmpty'));
			access = false;
		} else if (txtPassword.value === '' || txtPassword.value === null || txtPassword.value === undefined ) {
			dayApp.util.alertMsg(L('appName'), L('msgPEmpty'));
			access = false;
		}else {
			login (txtUserName.value, txtPassword.value);	
		}
		
	});
	
	var login = function (usr, pswd){
		
		Ti.API.info('=> user: ' + usr);
		Ti.API.info('=> Pswd: ' + pswd);
		
		openWindowDashboard();
	};
	
	var openWindowDashboard = function (){
		
		var win = require('/ui/common/home/dashboard');
        new win().open();
        
        if (dayApp.isiOS) {
        	navWin.close(self);
        }else{
        	self.close();
        }
		
	};
	
	return self;
	
}

module.exports = login;

