exports.getJSON = function (getJSONurl, getJSONonSuccess, getMethod, getParameters) {
	Ti.API.info('=> Final URL: ' + getJSONurl);
	
	var getJSONData = null;
	try {
		
		var xhr = Ti.Network.createHTTPClient ({
			onload : function () {
				try {
					getJSONData = JSON.parse(this.responseText);
				} catch (err) {
					showgetJSONError (err);	
				}
				
				if (getJSONData != null) {
					getJSONonSuccess(getJSONData);	
				}
				
			},
			onerror : function (err){
				showgetJSONError (err);
				this.abort();
			},
			timeout: 30000
			
		}) ;
		
		xhr.open(getMethod, getJSONurl);
		//xhr.validatesSecureCertificate = Ti.App.Properties.getBool('hasSSL');
		xhr.send(getParameters);
		
	} catch (err) {
		showgetJSONError (err);
	}
	
	var showgetJSONError = function (err){
		
		
		var hasNetwork = Ti.Network.online;
				
		var errorMsg = err.error ? err.error.toLowerCase() : '';
		var isTimeout = !hasNetwork | (errorMsg.indexOf('timed out') >=0 | errorMsg.indexOf('sockettimeoutexception') >=0 || errorMsg.indexOf('unable to resolve host') >=0);
		var isAppError = hasNetwork && !isTimeout;
		message: isTimeout ? 'Revise su conexion a internet e intente de nuevo': 'El servidor tiene problemas al gestionar la informacion, intente mas tarde.';
		alert(message);
		
        /*
		Ti.UI.createAlertDialog ({
			title: L('appName'),
			message: isTimeout ? 'Revise su conexion a internet e intente de nuevo': 'El servidor tiene problemas al gestionar la informacion, intente mas tarde.',
			buttonNames: ['Ok']
		}).show();*/
	};
};
