//
//  MaskEffect.h
//  master.fire
//
//  Created by Jason Vieira on 2015-01-07.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface MaskEffect : NSObject

+ (void)maskImageView:(UIImageView *)imageView passAlpha:(NSNumber*)alpha;

+(void)darkenViewPop:(UIView *)view;
+(void)blackenViewPop:(UIView *)view;


@end
