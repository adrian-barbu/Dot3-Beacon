//
//  DiscoverFirstCell.m
//  Kew-Version-2
//
//  Created by remedy on 2015-09-02.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "DiscoverFirstCell.h"
#import <POP/POP.h>
#import "MaskEffect.h"
#import "UIColor+DTColor.h"

@implementation DiscoverFirstCell

- (void)awakeFromNib {
    POPSpringAnimation *sprintAnimation = [POPSpringAnimation animationWithPropertyNamed:kPOPViewScaleXY];
    sprintAnimation.velocity = [NSValue valueWithCGPoint:CGPointMake(8, 8)];
    sprintAnimation.springBounciness = 20.f;
    [self.entryHeadline pop_addAnimation:sprintAnimation forKey:@"sendAnimation"];
    //self.entryTagline.textColor = [UIColor kewAquaTintColor];
    
    [self.dividerView setBackgroundColor:[UIColor kewAquaTintColor]];
    
    [MaskEffect maskImageView:self.entryImage passAlpha:[NSNumber numberWithDouble:0.28]];
    
}

@end
