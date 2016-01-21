function database(){
	var db;
	
	this.openDatabase = function (){
		var dbFileName = String.format('file://data/data/%s/databases/'+Titanium.App.Properties.getString("databaseVersion"),Ti.App.getId());
		
		var dbFile = Ti.Filesystem.getFile(dbFileName);
		
		if (!db) {
			if (dbFile.size > 0 ) {
				db = Ti.Database.open(Ti.App.Properties.getString("databaseVersion"));
			} else {
				db = Ti.Database.install('/db/serie.sqlite', Ti.App.Properties.getString('databaseVersion'));
			}
		}
		
		return db;
		
	};
	
	this.closeDatabase = function (){
		if (db) {
			db.close();
			db = null;
		}	
	};
	
	
	
	this.addSerie = function (arg) {
		var dayApp = Ti.App.dayApp;
		
		//Ti.API.info('=> SQL addArtist: ' + arg[0].artistName);
		
		var title = arg.Title;
		var genre = arg.Genre;
		var actors = arg.Actors;
		var poster = arg.Poster;
		
		this.openDatabase();
		db.execute ("BEGIN IMMEDIATE TRANSACTION");
		//db.execute ('INSERT INTO serie (artistId, artistName, collectionName, trackName)' + 'VALUES (?,?,?,?)', artistId, artistName, collectionName, trackName);
		db.execute ('INSERT INTO serie (title, genre, poster, actors)' + 'VALUES (?,?,?,?)', title, genre, poster, actors);
		db.execute ('COMMIT TRANSACTION');
		this.closeDatabase();
		
	};
	
	this.resetTables = function (){
		var dayApp = Ti.App.dayApp;
		
		this.openDatabase();
		db.execute ("BEGIN IMMEDIATE TRANSACTION");
		db.execute ('DELETE from serie');
		db.execute ('COMMIT TRANSACTION');
		this.closeDatabase();	
		
	};
	
	
	this.getSeries = function (){
		
		var dayApp = Ti.App.dayApp;
		
		var listSeries = [];
		var resultSet;
		
		this.openDatabase();
		db.execute ("BEGIN IMMEDIATE TRANSACTION");
		resultSet = db.execute ('Select * from serie ');
		
		var x = 0;
		while (resultSet.isValidRow()){
			var list = {};
			list['serieid'] = resultSet.fieldByName('serieid');
			list['title'] = resultSet.fieldByName('title');
			list['genre'] = resultSet.fieldByName('genre');
			list['poster'] = resultSet.fieldByName('poster');
			list['actors'] = resultSet.fieldByName('actors');
			listSeries.push(list);
			resultSet.next();
		}
		resultSet.close();
		db.execute ('COMMIT TRANSACTION');
		this.closeDatabase();
		
		return listSeries;
	};
	
};

module.exports = database;
