//
//  DTColor.m
//  master.fire
//
//  Created by remedy on 2014-12-26.
//  Copyright (c) 2014 Dot3. All rights reserved.
//

#import "UIColor+DTColor.h"


@implementation UIColor (DTColor)

// ---------- Default tint is tab bar ------------------- //

+ (UIColor*)defaultTintColor {
    return [UIColor whiteColor];
}

+ (UIColor *)kewGreenTintColor{
    return [UIColor colorWithRed:102.0/255.0 green:153.0/255.0 blue:0.0/255.0 alpha:1.0];
}


+ (UIColor *)kewBrightGreenTintColor{
    return [UIColor colorWithRed:0.0/255.0 green:170.0/255.0 blue:73.0/255.0 alpha:1.0];
}

+ (UIColor *)kewMaroonTintColor{
    return [UIColor colorWithRed:187.0/255.0 green:19.0/255.0 blue:26.0/255.0 alpha:1.0];
}


+ (UIColor *)kewAquaTintColor{
    return [UIColor colorWithRed:0.0/255.0 green:136.0/255.0 blue:138.0/255.0 alpha:1.0];
}

+ (UIColor *)kewDarkBlueTintColor{
    return [UIColor colorWithRed:0.0/255.0 green:139.0/255.0 blue:191.0/255.0 alpha:1.0];
}

// ---------- Nav bar tint ------------------- //
/*
+ (UIColor *)defaultNavBarTintColor{
    AppSettings *appSetting = [AppSettings sharedInstance];
    NSString *test = appSetting.colorWithRed;
    NSLog(@"Test: %@", test);
    
    double red = [appSetting.colorWithRed doubleValue];
     NSLog(@"Red: %f", red);
     NSLog(@"Set: %@", appSetting.colorWithRed);
    double green = [appSetting.colorWithRed doubleValue];
    double blue = [appSetting.colorWithRed doubleValue];
    
    
    return [UIColor colorWithRed:red/255.0 green:green/255.0 blue:blue/255.0 alpha:1.0];
}
 */

//------ Blackhawk Color

/*
+ (UIColor *)defaultNavBarTintColor{
    return [UIColor colorWithRed:7.0/255.0 green:119.0/255.0 blue:183.0/255.0 alpha:1.0];
}

+ (UIColor *)channelTagOneColor{
    return [UIColor colorWithRed:0.0/255.0 green:89.0/255.0 blue:179.0/255.0 alpha:1.0];
}

+ (UIColor *)channelTagTwoColor{
    return [UIColor colorWithRed:82.0/255.0 green:147.0/255.0 blue:207.0/255.0 alpha:1.0];
}

+ (UIColor *)channelTagThreeColor{
    return [UIColor colorWithRed:131.0/255.0 green:162.0/255.0 blue:212.0/255.0 alpha:1.0];
}
*/


//------ Heineken Color
/*
+ (UIColor *)defaultNavBarTintColor{
    return [UIColor colorWithRed:45.0/255.0 green:57.0/255.0 blue:65.0/255.0 alpha:1.0];
}

+ (UIColor *)channelTagOneColor{
    return [UIColor colorWithRed:0.0/255.0 green:89.0/255.0 blue:179.0/255.0 alpha:1.0];
}

+ (UIColor *)channelTagTwoColor{
    return [UIColor colorWithRed:82.0/255.0 green:147.0/255.0 blue:207.0/255.0 alpha:1.0];
}

+ (UIColor *)channelTagThreeColor{
    return [UIColor colorWithRed:131.0/255.0 green:162.0/255.0 blue:212.0/255.0 alpha:1.0];
}
*/

@end
