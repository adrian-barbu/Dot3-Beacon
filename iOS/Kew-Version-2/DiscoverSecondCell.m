//
//  DiscoverSecondCell.m
//  
//
//  Created by remedy on 2015-09-02.
//
//

#import "DiscoverSecondCell.h"
#import <POP/POP.h>
#import "MaskEffect.h"
#import "UIColor+DTColor.h"


@implementation DiscoverSecondCell

- (void)awakeFromNib {
    POPSpringAnimation *sprintAnimation = [POPSpringAnimation animationWithPropertyNamed:kPOPViewScaleXY];
    sprintAnimation.velocity = [NSValue valueWithCGPoint:CGPointMake(8, 8)];
    sprintAnimation.springBounciness = 20.f;
    //[self.viewcatname pop_addAnimation:sprintAnimation forKey:@"sendAnimation"];
    
   // [MaskEffect maskImageView:self.entryImage passAlpha:[NSNumber numberWithDouble:0.33]];
    
}

@end
