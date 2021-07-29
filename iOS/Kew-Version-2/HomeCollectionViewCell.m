//
//  HomeCollectionViewCell.m
//  Kew-Version-2
//
//  Created by remedy on 2015-07-22.
//
//

#import "HomeCollectionViewCell.h"
#import <POP/POP.h>
#import "MaskEffect.h"

@implementation HomeCollectionViewCell


- (void)awakeFromNib {
    POPSpringAnimation *sprintAnimation = [POPSpringAnimation animationWithPropertyNamed:kPOPViewScaleXY];
    sprintAnimation.velocity = [NSValue valueWithCGPoint:CGPointMake(8, 8)];
    sprintAnimation.springBounciness = 20.f;
    [self.viewcatname pop_addAnimation:sprintAnimation forKey:@"sendAnimation"];
    
    [MaskEffect maskImageView:self.viewcatimage passAlpha:[NSNumber numberWithDouble:0.28]];
    
}



@end
