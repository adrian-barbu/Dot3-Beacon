//
//  D3Services.h
//  dot3Xcode
//
//  Created by Shanmin on 2015-06-22.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface D3Services : NSObject

+ (void)setupAppWithKey:(NSString *)key;
+ (void)startBeaconMonitoring;
+ (void)startGeofencesMonitoring;
+ (void)checkBeaconState;

@end
