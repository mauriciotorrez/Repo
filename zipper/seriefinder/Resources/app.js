var db_version = "DAY_DB_1";

Ti.App.Properties.setBool('hasSSL', true);
Ti.App.Properties.setString('urlAPI', 'https://www.omdbapi.com/');
Ti.App.Properties.setString('getSerie', Ti.App.Properties.getString('urlAPI') + '?t=');
Ti.App.Properties.setString('databaseVersion', db_version);
//http://www.omdbapi.com/?t=simpson

(
	function() {
		
		var osName = Ti.Platform.osname;
    		osName = osName == 'iphone' || osName == 'ipad' ? 'ios' :  osName == 'mobileweb' ? osName : 'android';
    
    	var isAndroid, isiPhone, isiPad, isiOS;
    	
    	isAndroid = (osName ==  'android') ? true : false;
    	isiPhone = (Ti.Platform.osname ==  'iphone') ? true : false;
    	isiPad = (Ti.Platform.osname ==  'ipad') ? true : false;
    	isiOS = (osName ==  'ios') ? true : false;
    	
    	var configDB = require('/db/database');
    	
    	Ti.App.dayApp = {
    		osName: osName,
    		isAndroid: isAndroid,
    		isiPhone: isiPhone,
    		isiPad: isiPad,
    		isiOS: isiOS,
    		//util: require('/lib/common/utilities'),
    		net: require('/lib/common/network'),
    		db: new configDB()
    	};
    		
    		
		switch (osName) {
			case 'ios':
				var uiView = '/ui/common/login/login';
				break;
				
			case 'android':
				var uiView = '/ui/common/login/login';
				break;
		}
		
		mainWindow = new (require(uiView))();
		mainWindow.open();
		
	}
)();
            
 