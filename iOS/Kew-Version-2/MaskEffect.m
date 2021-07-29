//
//  MaskEffect.m
//  master.fire
//
//  Created by Jason Vieira on 2015-01-07.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "MaskEffect.h"
#import "Pop.h"


@implementation MaskEffect

// -----------------------------------UBER METHOD FOR IMAGE MASK---------------------------
// -----------------------------------Call with:---------------------------
// ------------ [MaskEffect maskImageView:self.entryThumb passAlpha:[NSNumber numberWithDouble:0.55]];

+ (void)maskImageView:(UIImageView *)imageView passAlpha:(NSNumber*)alpha{
    
    float alphaValue = [alpha floatValue];
    CALayer *newLayer = [CALayer layer];
    
    newLayer.frame = imageView.layer.frame;
    
    newLayer.backgroundColor = [[UIColor blackColor] CGColor];
    newLayer.opacity = 0;
    CABasicAnimation* fadeAnim = [CABasicAnimation animationWithKeyPath:@"opacity"];
    fadeAnim.fromValue = [NSNumber numberWithFloat:0.0];
    fadeAnim.toValue = alpha;
    fadeAnim.duration = 1.6;
    [newLayer addAnimation:fadeAnim forKey:@"opacity"];
    newLayer.opacity = alphaValue;
    [imageView.layer addSublayer: newLayer];
}

+ (void)darkenViewPop:(UIView *)view{
    POPBasicAnimation *anim = [POPBasicAnimation animationWithPropertyNamed:kPOPViewAlpha];
    view.backgroundColor = [UIColor blackColor];
    anim.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
    anim.fromValue = @(0.0);
    anim.toValue = @(0.70);
    anim.duration = 1.4;
    [view pop_addAnimation:anim forKey:@"anim"];
    
}

+ (void)blackenViewPop:(UIView *)view{
    POPBasicAnimation *anim = [POPBasicAnimation animationWithPropertyNamed:kPOPViewAlpha];
    view.backgroundColor = [UIColor blackColor];
    anim.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
    anim.fromValue = @(0.0);
    anim.toValue = @(1.0);
    anim.duration = 1.6;
    [view pop_addAnimation:anim forKey:@"anim"];
    
}


@end
