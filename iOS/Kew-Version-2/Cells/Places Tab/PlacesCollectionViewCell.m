//
//  PlacesCollectionViewCell.m
//  Kew-Version-2
//
//  Created by Shanmin on 2015-07-29.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "PlacesCollectionViewCell.h"
#import <POP/POP.h>
#import "MaskEffect.h"

@interface PlacesCollectionViewCell()

@property (strong, nonatomic) IBOutlet UIImageView *imageView;
@property (strong, nonatomic) IBOutlet UILabel *labelTitle, *labelSubTitle;


@end

@implementation PlacesCollectionViewCell

- (void)awakeFromNib {
    POPSpringAnimation *sprintAnimation = [POPSpringAnimation animationWithPropertyNamed:kPOPViewScaleXY];
    sprintAnimation.velocity = [NSValue valueWithCGPoint:CGPointMake(8, 8)];
    sprintAnimation.springBounciness = 20.f;
    [self.labelTitle pop_addAnimation:sprintAnimation forKey:@"sendAnimation"];
    [MaskEffect maskImageView:self.imageView passAlpha:[NSNumber numberWithDouble:0.33]];
    
}

- (void)showContent:(NSDictionary *)dict {
    
    NSString *initialImageString = dict[@"entryInitialImage"];
    NSString *imageURL = dict[@"entryImage"];
    if (initialImageString != nil){
        [self.imageView sd_setImageWithURL:[NSURL URLWithString:initialImageString] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
    }else{
        [self.imageView sd_setImageWithURL:[NSURL URLWithString:imageURL] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
    }
    self.labelTitle.text = dict[@"entryHeadline"];
    self.labelSubTitle.text = dict[@"entryTeaserText"];

    
}

@end
