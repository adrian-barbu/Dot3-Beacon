//
//  DiscoverFirstCell.h
//  Kew-Version-2
//
//  Created by remedy on 2015-09-02.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DiscoverFirstCell : UICollectionViewCell
@property (strong, nonatomic) IBOutlet UIImageView *entryImage;
@property (strong, nonatomic) IBOutlet UILabel *entryHeadline;
@property (strong, nonatomic) IBOutlet UILabel *entryTagline;
@property (strong, nonatomic) IBOutlet UILabel *entryTeaserText;
@property (strong, nonatomic) IBOutlet UIView *dividerView;

@end
