//
//  HomeSecondCollectionViewCell.m
//  Kew-Version-2
//
//  Created by remedy on 2015-07-23.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "HomeSecondCollectionViewCell.h"
#import <POP/POP.h>
#import "MaskEffect.h"

@implementation HomeSecondCollectionViewCell

- (void)awakeFromNib {
    POPSpringAnimation *sprintAnimation = [POPSpringAnimation animationWithPropertyNamed:kPOPViewScaleXY];
    sprintAnimation.velocity = [NSValue valueWithCGPoint:CGPointMake(8, 8)];
    sprintAnimation.springBounciness = 20.f;
    [self.viewcatname pop_addAnimation:sprintAnimation forKey:@"sendAnimation"];
    
    [MaskEffect maskImageView:self.viewcatimage passAlpha:[NSNumber numberWithDouble:0.28]];
    
}
/*
- (void)layoutSubviews {
    [super layoutSubviews];
    
    for (CALayer *layer in [self.viewcatimage.layer.sublayers copy]) {
        if ( [layer.name isEqualToString:@"gradient"] )
            [layer removeFromSuperlayer];
    }
    CAGradientLayer *gradientLayer = [CAGradientLayer layer];
    gradientLayer.name = @"gradient";
    CGRect frame = self.viewcatimage.layer.bounds;
    gradientLayer.frame = frame;
    
    gradientLayer.colors = [NSArray arrayWithObjects:
                            (id)[UIColor colorWithWhite:0.0f alpha:0.0f].CGColor,
                            (id)[UIColor colorWithWhite:0.0f alpha:0.0f].CGColor,
                            (id)[UIColor colorWithWhite:0.0f alpha:0.85f].CGColor,
                            nil];
    
    gradientLayer.locations = [NSArray arrayWithObjects: [NSNumber numberWithFloat:0.0f],
                               [NSNumber numberWithFloat:0.4f],
                               [NSNumber numberWithFloat:1.0f],
                               nil];
    
    [self.viewcatimage.layer addSublayer:gradientLayer];
}
 */

@end
