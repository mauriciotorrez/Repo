function dashboard(){
	/*
	var self = Ti.UI.createWindow({
		backgroundColor: '#9f9f9f',
		navBarHidden: true
	});*/
	
	
	var dayApp = Ti.App.dayApp;
	var currentSerie;
	
		var storeInfo = function (){
			
			dayApp.db.addSerie(currentSerie);	

		};
	
	
	var getData = function(serie){
		
		
		
		var data = [];
		
		
		var urlGetSerie = Ti.App.Properties.getString('getSerie') + serie;
		//var urlGetArtist = Ti.App.Properties.getString('getArtist') + '&entity=musicVideo';
		//http://www.omdbapi.com/?t=simpson
		//https://itunes.apple.com/search?term=jack+johnson&limit=25
		//http://www.omdbapi.com/?t=game&y=&plot=short&r=json
		//'http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=2de143494c0b295cca9337e1e96b00e0'
		
		//var urlGetSerie = 'http://www.omdbapi.com/?t=game&y=&plot=short&r=json';
		
	
		Ti.API.info('=> url series: ' + urlGetSerie);
		
	
		
		dayApp.net.getJSON(urlGetSerie, function (data) {
			if (data.Response == 'True'){
				var serie1 = data;
				Ti.API.info('=>' + JSON.stringify(serie1));
				
				setSerie(serie1);
				currentSerie = serie1;
				btnFavorite.setVisible(true);
				//dayApp.db.resetTables();
				//storeInfo(listSongs);
				
			}else{
				setSerie(data);
				btnFavorite.setVisible(false);
				alert(data.Error);	
			}
			
		}, 'POST', null);
		
	};
	
	var imgPhoto;
	var title;
	var genre;
	var btnFavorite;
	var actors;
	var anho;
	
	var drawSerie = function () {
    	
    	imgPhoto = Ti.UI.createImageView({
			//image: data.Poster,
			top: 80,
			width: 140,
			heigth: 140
		});
				
		title = Ti.UI.createLabel({
			//text: '1',
			top: 40,
			//left: 60,
			//width: Ti.UI.FILL,
			textAlign:'center',
			width:'auto',
			color: '#c3bfbf',
			font: { fontSize: 15, fontFamily: 'Helvetica-Neue', fontWeight: 'Bold'}
		});
		
		actors = Ti.UI.createLabel({
			//text: '1',
			top: 330,
			//left: 60,
			//width: Ti.UI.FILL,
			textAlign:'center',
	        width:'auto',
			color: '#c3bfbf',
			font: { fontSize: 13, fontFamily: 'Helvetica-Neue', fontWeight: 'Normal'}
		});
		
		genre = Ti.UI.createLabel({
			//text: '1',
			top: 360,
			textAlign:'center',
	        width:'auto',
			color: '#c3bfbf',
			font: { fontSize: 13, fontFamily: 'Helvetica-Neue', fontWeight: 'Normal'}
		});
		
		anho = Ti.UI.createLabel({
			//text: '1',
			top: 390,
			textAlign:'center',
	        width:'auto',
			color: '#c3bfbf',
			font: { fontSize: 13, fontFamily: 'Helvetica-Neue', fontWeight: 'Normal'}
		});
		
		btnFavorite = Ti.UI.createButton({
			top:420,
			//left:105,
			width: 250,
			height: 40,
			visible:false,
			title: L('addFavorites'),
			backgroundColor: '#fc8506',
			color: '#FFFFFF',
			font: {fontFamily: 'Helvetica', fontSize: 16, fontWeigth: 'normal'}
		});
		
		btnFavorite.addEventListener('click', function(){
			
			storeInfo();
			
		});	
		win1.add(btnFavorite);
		
		
		win1.add(imgPhoto);
		win1.add(title);
		win1.add(actors);
		win1.add(genre);
		win1.add(anho);
			
    	
    };
	
	var setSerie = function (data) {
		imgPhoto.setImage(data.Poster);
		title.setText(data.Title);
		genre.setText(data.Genre);
		actors.setText(data.Actors);
		anho.setText(data.Year);
		
		
	};
	
	
	
	// this sets the background color of the master UIView (when there are no windows/tab groups on it)
//Titanium.UI.setBackgroundColor('#333333');

// create tab group
var tabGroup = Titanium.UI.createTabGroup({
	barColor:'#333333'
});




var search = Titanium.UI.createSearchBar({
    barColor:'#fc8506', 
    showCancel:true,
    height:43,
    top:0,
});



search.addEventListener('return', function(e) {
	getData(search.value);
 });

search.addEventListener('bookmark', function(e) {
		getData(search.value);
 });



//
// create base UI tab and root window
//
var win1 = Titanium.UI.createWindow({  
    title:L('searcherOnlineProg'),
    color:'#c3bfbf',
    backgroundColor:'#434341'
});
var tab1 = Titanium.UI.createTab({  
    icon:'KS_nav_views.png',
    title:L('programsFinder'),
    window:win1
});

win1.add(search);

//
// create controls tab and root window
//
var win2 = Titanium.UI.createWindow({  
    title:L('favorites'),
    color:'#c3bfbf',
    backgroundColor:'#434341'
});
var tab2 = Titanium.UI.createTab({  
    icon:'KS_nav_ui.png',
    title:L('favorites'),
    window:win2
});


var btnGetData = Ti.UI.createButton({
	top:0,
	width: 250,
	height: 40,
	title: L('updateData'),
	backgroundColor: '#fc8506',
	color: '#FFFFFF',
	font: {fontFamily: 'Helvetica', fontSize: 16, fontWeigth: 'normal'}
	
});

btnGetData.addEventListener('click', function(){
	
	var savedSeries = dayApp.db.getSeries();

	//Ti.API.info(' series from database=>' + (savedSeries[0])['poster']);

	tableViewSeries.removeAllChildren();
	
	fillTableViewSeries(savedSeries);
	
});



	var fillTableViewSeries = function (args){
		var rows = [];
		
		for (var x = 0 ; x< args.length; x++){
			var element = args[x];
			//Ti.API.info('=> element: ' + element );
			
			var row = Ti.UI.createTableViewRow({
				backgroundColor: 'transparent',
				selectedBackgroundColor: 'transparent',
				height: 100,
				width: Ti.UI.FILL,
				FieldRow: element
			});
			
			var dbPhoto = Ti.UI.createImageView({
				image: element['poster'],
				top: 0,
				left: 0,
				width: 100,
				heigth: 99
			});
			
			var dbTitle = Ti.UI.createLabel({
				text: element['title'],
				top: 5,
				left: 105,
				width: Ti.UI.FILL,
				color: '#9e9e9e',
				font: { fontSize: 15, fontFamily: 'Helvetica-Neue', fontWeight: 'Bold'}
			});
			
			var dbGenre = Ti.UI.createLabel({
				text: element['genre'],
				top: 45,
				left: 105,
				width: Ti.UI.FILL,
				color: '#9e9e9e',
				font: { fontSize: 13, fontFamily: 'Helvetica-Neue', fontWeight: 'Normal'}
			});
			
			var dbActors = Ti.UI.createLabel({
				text: element['actors'],
				top: 65,
				left: 105,
				width: Ti.UI.FILL,
				color: '#BDBDBD',
				font: { fontSize: 13, fontFamily: 'Helvetica-Neue', fontWeight: 'Normal'}
			});
			
			
			row.add(dbPhoto);
			row.add(dbTitle);
			row.add(dbGenre);
			row.add(dbActors);

			rows.push(row);
			
		}
		
		tableViewSeries.setData(rows);

	};




	var tableViewSeries = Ti.UI.createTableView({
		top: 60,
		scrollable: true,
		backgroundColor:'#434341'
		
	});
	win2.add(tableViewSeries);










win2.add(btnGetData);

drawSerie();
//
//  add tabs
//
tabGroup.addTab(tab1);  
tabGroup.addTab(tab2);  


// open tab group
//tabGroup.open();
	
	//self.add(tabGroup);
	
	return tabGroup;
	
}

module.exports = dashboard;


