//
//  DetailSongViewController.swift
//  MusicFind
//
//  Created by onix on 1/10/16.
//  Copyright © 2016 maurisoft. All rights reserved.
//

import UIKit
import SDWebImage
import AVFoundation
import JGProgressHUD
import RealmSwift

class DetailSongViewController: UIViewController {

  // outlets
  @IBOutlet weak var artistLabel: UILabel!
  @IBOutlet weak var songLabel: UILabel!
  @IBOutlet weak var coverImageView: UIImageView!

  var song: Song?
  var audioPlayer : AVAudioPlayer?

  let hud = JGProgressHUD(style: .Dark)
  let realm = try! Realm()

  override func viewDidLoad() {
    super.viewDidLoad()

    if let song = song {
      artistLabel.text = song.artistName
      songLabel.text = song.songName
      let imageUrl = NSURL(string: song.imageUrl)
      coverImageView.layer.cornerRadius = 10
      coverImageView.sd_setImageWithURL(imageUrl, placeholderImage: nil)
    }
  }

  override func didReceiveMemoryWarning() {
    super.didReceiveMemoryWarning()
    // Dispose of any resources that can be recreated.
  }

  // MARK: - Acciones

  @IBAction func playAction(sender: AnyObject) {

    if let song = song {
      self.hud.showInView(self.view)

      let previewUrl = NSURL(string: song.previewUrl)
      let songData = NSData(contentsOfURL: previewUrl!)

      self.audioPlayer = try! AVAudioPlayer(data: songData!)


      if let audioPlayer = self.audioPlayer {
        audioPlayer.numberOfLoops = 0
        audioPlayer.volume = 1
        audioPlayer.prepareToPlay()
        
        audioPlayer.play()
      }
      self.hud.dismiss()
    }
  }


  @IBAction func favoriteAction(sender: AnyObject) {
    debugPrint("boton favorito presionado")

    if let song = song {
      try! realm.write {
        realm.add(song, update: true)
      }

      let message = UIAlertController(title: "MusicFind", message: "Adicionaste esta cancion a tus favoritos...", preferredStyle: .Alert)
      message.addAction(UIAlertAction(title: "Oki Dokie!", style: UIAlertActionStyle.Cancel, handler: nil))
      presentViewController(message, animated: true, completion: nil)
    }

  }


}
