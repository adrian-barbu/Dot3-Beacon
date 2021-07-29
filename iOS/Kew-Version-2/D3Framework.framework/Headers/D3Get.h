//
//  D3Get.h
//  dot3Xcode
//
//  Created by Shanmin on 2015-08-07.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import <Foundation/Foundation.h>

// NOTIFICATIONS BASED ON TRIGGERS - Beacons, geofences, etc

#define D3BeaconDidEnterRegion @"D3BeaconDidEnterRegion"
#define D3BeaconDidExitRegion @"D3BeaconDidExitRegion"
#define D3BeaconDidRangeRegion @"D3BeaconDidRangeRegion"
#define D3DidEnterZone @"D3DidEnterZone"

#define D3GeofenceDidEnterRegion @"D3GeofenceDidEnterRegion"
#define D3GeofenceDidExitRegion @"D3GeofenceDidExitRegion"
#define D3GeofenceDidArriveRegion @"D3GeofenceDidArriveRegion"

// Notice for geofence unknown to allow update of UserState
#define D3GeofenceUnknown @"D3GeofenceUnknown"

// NOTIFICATIONS BASED ON REAL-TIME CHANGES TO THE DATABASE - Receive notices when there is a change to the data

#define D3ViewDataDidChange @"D3ViewDataDidChange"
#define D3ViewCatDataDidChange @"D3ViewCatDataDidChange"
#define D3EntriesForViewCatDataDidChange @"D3EntriesForViewCatDataDidChange"
#define D3ZoneDataDidChange @"D3ZoneDataDidChange"
#define D3EntriesForZoneDataDidChange @"D3EntriesForZoneDataDidChange"
#define D3PlaceDataDidChange @"D3PlaceDataDidChange"
#define D3EntriesForPlaceDataDidChange @"D3EntriesForPlaceDataDidChange"

// NOTIFICATIONS FOR RULE CHANGE
#define D3DidApplyRule @"D3DidApplyRule"

@interface D3Get : NSObject {
    
}


//Views
+ (void)getViewsArrayWithCompletion:(void(^)(NSArray *))completion;
+ (void)getViewWithViewKey:(NSString *)viewKey completion:(void(^)(NSDictionary *))completion;
+ (void)getViewCatsArrayWithViewKey:(NSString *)viewKey completion:(void(^)(NSArray *))completion;

//Fleet
+ (void)getFleetArrayWithCompletion:(void(^)(NSArray *))completion;

//Zones
+ (void)getZoneWithZoneKey:(NSString *)zoneKey completion:(void(^)(NSDictionary *))completion;

//Entries
+ (void)getEntriesArrayWithZoneKey:(NSString *)zoneKey completion:(void(^)(NSArray *))completion;
+ (void)getEntriesArrayWithCatKey:(NSString *)catKey completion:(void(^)(NSArray *))completion;
+ (void)prefetchAllEntriesImagesWithProgress:(void(^)(NSUInteger noOfFinishedUrls, NSUInteger noOfTotalUrls))progress completion:(void(^)(NSUInteger noOfFinishedUrls, NSUInteger noOfSkippedUrls))completion;

//Geofences
+ (void)getGeofencesArrayWithCompletion:(void(^)(NSArray *))completion;
+ (void)getPlacesArrayWithCompletion:(void(^)(NSArray *))completion;
+ (void)getPlaceListingWithCompletion:(void(^)(NSArray *))completion;
+ (void)getPlaceWithPlaceKey:(NSString *)placeKey completion:(void(^)(NSDictionary *))completion;
+ (void)getEntriesArrayWithPlaceKey:(NSString *)placeKey completion:(void(^)(NSArray *))completion;

// Rules
+ (void)getRulesArrayForPlaceWithCompletion:(void(^)(NSArray *))completion;

// User state to apply rules
+ (void)getUserStateForPlaceWithCompletion:(NSString *)userId completion:(void(^)(NSArray *))completion;

+ (NSMutableArray *)viewCatsArr;

@end
