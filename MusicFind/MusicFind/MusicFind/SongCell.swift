//
//  SongCell.swift
//  MusicFind
//
//  Created by onix on 1/10/16.
//  Copyright © 2016 maurisoft. All rights reserved.
//

import UIKit

class SongCell: UITableViewCell {

  @IBOutlet weak var coverImageView: UIImageView!
  @IBOutlet weak var songLabel: UILabel!
  @IBOutlet weak var albumArtistLabel: UILabel!
  @IBOutlet weak var genreLabel: UILabel!
  @IBOutlet weak var priceLabel: UILabel!

  override func awakeFromNib() {
    super.awakeFromNib()
    // Initialization code
  }

  override func setSelected(selected: Bool, animated: Bool) {
    super.setSelected(selected, animated: animated)

    // Configure the view for the selected state
  }

}
