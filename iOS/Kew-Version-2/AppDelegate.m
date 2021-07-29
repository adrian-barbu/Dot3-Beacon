//
//  AppDelegate.m
//  Kew-Version-2
//
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "AppDelegate.h"
#import <Fabric/Fabric.h>
#import <Crashlytics/Crashlytics.h>
#import "D3Get.h"
#import "D3Services.h"
#import "D3Set.h"


@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    [Fabric with:@[CrashlyticsKit]];
    
    // KEW //
    
    [D3Services setupAppWithKey:@"-JuNC480_FTHsPcy6mzG"];
    
    // Primarily this is a test of pre-fetch....it only does entries for view cat, will need to add other images etc
    // Also need to make sure that the way images are displayed will also work
    /*
    [D3Get prefetchAllEntriesImagesWithProgress:^(NSUInteger noOfFinishedUrls, NSUInteger noOfTotalUrls) {
        NSLog(@"Finished: %lu, total: %lu", (unsigned long)noOfFinishedUrls, (unsigned long)noOfTotalUrls);
    } completion:^(NSUInteger noOfFinishedUrls, NSUInteger noOfSkippedUrls) {
        NSLog(@"Finished: %lu, skipped: %lu", (unsigned long)noOfFinishedUrls, (unsigned long)noOfSkippedUrls);
    }];
     */
    
    [D3Services startBeaconMonitoring];
    [D3Services startGeofencesMonitoring];
    
    // Gordon Bros -JvueOKnmr4JCgeDuV4L //
     //[D3Services setupAppWithKey:@"-JvueOKnmr4JCgeDuV4L"]; //input your space key here from dot-3-release
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application {
    
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    
}

- (void)applicationWillTerminate:(UIApplication *)application {
    
}

@end
