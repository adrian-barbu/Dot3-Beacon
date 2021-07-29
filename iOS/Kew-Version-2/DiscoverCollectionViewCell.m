//
//  DiscoverCollectionViewCell.m
//  Kew-Version-2
//
//  Created by remedy on 2015-08-05.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "DiscoverCollectionViewCell.h"
#import "MaskEffect.h"

@implementation DiscoverCollectionViewCell

- (void)awakeFromNib {
    
    
    [MaskEffect maskImageView:self.entryImage passAlpha:[NSNumber numberWithDouble:0.25]];
    
}

@end
