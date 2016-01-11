//
//  FavoritesViewController.swift
//  MusicFind
//
//  Created by onix on 1/10/16.
//  Copyright © 2016 maurisoft. All rights reserved.
//

import UIKit
import RealmSwift

class FavoritesViewController: UITableViewController {

  var songs = [Song]()
  var songSelected: Song?

  let realm = try! Realm()

  override func viewDidLoad() {
    super.viewDidLoad()

    // Configuraciones de UI
    tableView.registerNib(UINib(nibName: "SongCell", bundle: NSBundle.mainBundle()), forCellReuseIdentifier: "SongCell")

    loadFavoritesSongs()
  }

  override func didReceiveMemoryWarning() {
    super.didReceiveMemoryWarning()
  }

  func loadFavoritesSongs() {
    let data = realm.objects(Song)
    songs = data.map {$0}

    tableView.reloadData()
  }

  override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
    return 1
  }

  override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
    return self.songs.count
  }

  override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
    let cell = tableView.dequeueReusableCellWithIdentifier("SongCell", forIndexPath: indexPath) as! SongCell

    let song = self.songs[indexPath.row]

    // Configure the cell...
    cell.songLabel.text = song.songName
    cell.albumArtistLabel.text = song.songInformation()
    let imageUrl = NSURL(string: song.imageUrl)
    cell.coverImageView.sd_setImageWithURL(imageUrl, placeholderImage: nil)
    cell.genreLabel.text = song.genreInformation()
    cell.priceLabel.text = song.priceInformation()

    return cell
  }

  override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
    self.songSelected = self.songs[indexPath.row]
    self.performSegueWithIdentifier("showSongSegue", sender: nil)
  }

  override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
    if segue.identifier == "showSongSegue" {
      let detailSongVC = segue.destinationViewController as! DetailSongViewController
      detailSongVC.song = self.songSelected
    }
  }
  
}
