//
//  DiscoverCollectionViewCell.h
//  Kew-Version-2
//
//  Created by remedy on 2015-08-05.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DiscoverCollectionViewCell : UICollectionViewCell

@property (strong, nonatomic) IBOutlet UIImageView *entryImage;
@property (strong, nonatomic) IBOutlet UILabel *entryTitle;
@property (strong, nonatomic) IBOutlet UILabel *entryTeaserText;

@end
