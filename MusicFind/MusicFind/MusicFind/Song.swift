//
//  Song.swift
//  MusicFind
//
//  Created by onix on 1/10/16.
//  Copyright © 2016 maurisoft. All rights reserved.
//

import Foundation
import SwiftyJSON
import RealmSwift

class Song : Object {

  dynamic var trackId = ""
  dynamic var artistName = ""
  dynamic var songName = ""
  dynamic var albumName = ""
  dynamic var genre = ""
  dynamic var albumPrice = ""
  dynamic var currency = ""
  dynamic var imageUrl = ""
  dynamic var previewUrl = ""

  override static func primaryKey() -> String? {
    return "trackId"
  }

  func songInformation() -> String {
    return "\(artistName) - \(albumName)"
  }

  func priceInformation() -> String {
    return "Precio: \(albumPrice) \(currency)"
  }

  func genreInformation() -> String {
    return "Género: \(genre)"
  }

  class func songByJSON(json : JSON) -> Song {
    let song = Song()
    song.trackId = json["trackId"].stringValue
    song.artistName = json["artistName"].stringValue
    song.albumName = json["collectionName"].stringValue
    song.songName = json["trackName"].stringValue
    song.genre = json["primaryGenreName"].stringValue
    song.albumPrice = json["collectionPrice"].stringValue
    song.currency = json["currency"].stringValue
    song.imageUrl = json["artworkUrl100"].stringValue
    song.previewUrl = json["previewUrl"].stringValue

    return song
  }

  class func songsByJSON(json : JSON) -> [Song] {
    var songs = [Song]()
    for songDictionary in json.arrayValue {
      let song = Song.songByJSON(songDictionary)
      songs.append(song)
    }
    return songs
  }
  
}


