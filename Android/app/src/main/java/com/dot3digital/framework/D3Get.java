package com.dot3digital.framework;

import android.content.Context;

import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.EntryForPlace;
import com.dot3digital.framework.model.EntryForViewCat;
import com.dot3digital.framework.model.EntryForZone;
import com.dot3digital.framework.model.Fleet;
import com.dot3digital.framework.model.Geofence;
import com.dot3digital.framework.model.Place;
import com.dot3digital.framework.model.PushNoticeForZone;
import com.dot3digital.framework.model.ViewEntry;
import com.dot3digital.framework.model.ViewCats;
import com.dot3digital.framework.model.Zone;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * @description     D3Get Engine
 *
 * @modified        Stelian
 */
public class D3Get
{
    protected static final String LOG_TAG = Class.class.getSimpleName();

    public static String SPACE_KEY; // See: D3Core.setupSpaceKey()

    /**
     * Define IGet Interface
     */
    public interface IGet
    {
        //[2015.11.10][Stelian] Engine Optimization
        void handleFirebaseResponse(ArrayList<BaseModel> datas);
        void handleFirebaseResponse(BaseModel data);
    }

    /**
     * Get Views
     *
     * @param iGetViews
     */
    public static void getViewsArrayWithCompletion(final IGet iGetViews) {
        //String[] firebaseChildKeys = {"viewName", "viewDescription", "viewImage"};
        //D3Get.getXXXArrayWithCompletion(iGetViews, D3Constants.VIEWS_URL, false, false, firebaseChildKeys);
        D3Core.getFirebaseRef()
                .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.VIEWS_URL)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<BaseModel> views = new ArrayList<BaseModel>();
                                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                    ViewEntry viewEntry = new ViewEntry(dss.getKey());
                                    if (viewEntry.parseSnapshot(dss))
                                        views.add(viewEntry);
                                }

                                // Now, call callback
                                iGetViews.handleFirebaseResponse(views);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });
    }

    /**
     * Get ViewEntry with ViewKey
     *
     * @param iGetView
     * @param viewKey
     */
    public static void getViewWithViewKey(final D3Get.IGet iGetView, String viewKey) {
        //String[] firebaseChildKeys = {"viewImage", "viewDescription", "viewName", "viewTitle", "viewTagline"};
        //D3Get.getXXXWithXXXKey(iGetView, viewKey, D3Constants.VIEWS_URL, false, firebaseChildKeys);
        D3Core.getFirebaseRef()
                .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.VIEWS_URL).child(viewKey)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ViewEntry viewEntry = new ViewEntry(dataSnapshot.getKey());
                                viewEntry.parseSnapshot(dataSnapshot);
                                iGetView.handleFirebaseResponse(viewEntry);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });
    }

    /**
     * Get ViewEntry Cats
     *
     * @param iGetViewcats
     * @param viewKey
     */
    public static void getViewCatsArrayWithViewKey(final D3Get.IGet iGetViewcats, String viewKey) {
        D3Core.getFirebaseRef()
                .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.VIEW_CATS_URL).child(viewKey)
                .orderByChild("active").equalTo(true) // Sorting + filtering.
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<BaseModel> viewCats = new ArrayList<BaseModel>();
                                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                    ViewCats vc = new ViewCats(dss.getKey());
                                    if (vc.parseSnapshot(dss))
                                        viewCats.add(vc);
                                }

                                // Sort by priority
                                BaseModel.sort(viewCats);

                                // Now, call callback
                                iGetViewcats.handleFirebaseResponse(viewCats);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });
    }

    /**
     * Get Fleet Array
     *
     * @param iGetFleet
     */
    public static void getFleetArrayWithCompletion(final IGet iGetFleet) {
        D3Core.getFirebaseRef()
                .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.FLEET_URL)
                 //.addListenerForSingleValueEvent(    // "Reading Data Once ... It is triggered one time and then will not be triggered again."
                .addValueEventListener(             // "It is triggered once with the initial data and again every time the data changes."
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<BaseModel> fleets = new ArrayList<BaseModel>();

                                if (dataSnapshot != null) {
                                    int i = 0;
                                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                        if (i <= 20) { // See dot3Xcode D3Get.m
                                            Fleet fleet = new Fleet(dss.getKey());
                                            if (fleet.parseSnapshot(dss))
                                                fleets.add(fleet);
                                        } else {
                                            break;
                                        }
                                        i++;
                                    }
                                }

                                // Callback, e.g. to render Dot3 UI
                                iGetFleet.handleFirebaseResponse(fleets);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });
    }

    /**
     * Get Zone with Zone Key
     *
     * @param iGetZone
     * @param zoneKey
     */
    public static void getZoneWithZoneKey(final D3Get.IGet iGetZone, String zoneKey) {
        D3Core.getFirebaseRef()
                .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.ZONES_URL).child(zoneKey)
                //.addListenerForSingleValueEvent(    // "Reading Data Once ... It is triggered one time and then will not be triggered again."
                .addValueEventListener(             // "It is triggered once with the initial data and again every time the data changes."
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Zone zone = new Zone(dataSnapshot.getKey());
                                zone.parseSnapshot(dataSnapshot);
                                iGetZone.handleFirebaseResponse(zone);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });
    }

    /**
     * Get Entries With CatKey
     *
     * @param iGetEntries
     * @param viewcatKey
     */
    public static void getEntriesArrayWithCatKey(final D3Get.IGet iGetEntries, String viewcatKey) {
        D3Core.getFirebaseRef()
                .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.ENTRIES_FOR_VIEWCAT).child(viewcatKey)
                .orderByChild("deleted").startAt().endAt(false) // Sorting + filtering.
                //.addListenerForSingleValueEvent(    // "Reading Data Once ... It is triggered one time and then will not be triggered again."
                .addValueEventListener(             // "It is triggered once with the initial data and again every time the data changes."
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                ArrayList<BaseModel> entries = new ArrayList<BaseModel>();
                                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                    EntryForViewCat entry = new EntryForViewCat(dss.getKey());
                                    if (entry.parseSnapshot(dss))
                                        entries.add(entry);
                                }

                                // Sort by priority
                                BaseModel.sort(entries);

                                // Now, call callback
                                iGetEntries.handleFirebaseResponse(entries);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });
    }

    /**
     * Get Entries With ZoneKey
     *
     * @param iGetEntries
     * @param zoneKey
     */
    public static void getEntriesArrayWithZoneKey(final D3Get.IGet iGetEntries, String zoneKey) {
        D3Core.getFirebaseRef()
                .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.ENTRIES_FOR_ZONE).child(zoneKey)
                // See dot3Xcode app: [[[[firebase queryOrderedByChild:@"deleted"] queryStartingAtValue:nil] queryEndingAtValue:@NO] observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot)
                .orderByChild("deleted").startAt().endAt(false) // Sorting + filtering.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ArrayList<BaseModel> entries = new ArrayList<BaseModel>();
                        for (DataSnapshot dss : dataSnapshot.getChildren()) {
                            EntryForZone entry = new EntryForZone(dss.getKey());
                            if (entry.parseSnapshot(dss))
                                entries.add(entry);
                        }

                        // Sort by priority
                        BaseModel.sort(entries);

                        // Now, call callback
                        iGetEntries.handleFirebaseResponse(entries);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });
    }

    /**
     * Get Push Notice Array With ZoneKey
     *
     * @param iGetEntries
     * @param zoneKey
     */
    public static void getPushNoticeArrayWithZoneKey(final D3Get.IGet iGetEntries, String zoneKey) {
        D3Core.getFirebaseRef()
                .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.ENTRIES_FOR_ZONE).child(zoneKey)
                // See dot3Xcode app: [[[[firebase queryOrderedByChild:@"deleted"] queryStartingAtValue:nil] queryEndingAtValue:@NO] observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot)
                .orderByChild("deleted").startAt().endAt(false) // Sorting + filtering.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ArrayList<BaseModel> entries = new ArrayList<BaseModel>();
                        for (DataSnapshot dss : dataSnapshot.getChildren()) {

                            PushNoticeForZone entry = new PushNoticeForZone(dss.getKey());
                            if (entry.parseSnapshot(dss))
                                entries.add(entry);
                        }

                        // Now, call callback
                        iGetEntries.handleFirebaseResponse(entries);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });
    }

    /**
     * Get geofence array
     *
     * @param iGetGeofences
     */
    public static void getGeofencesArrayWithCompletion(final IGet iGetGeofences) {
        //String[] firebaseChildKeys = {"assignedPlace", "geoLat", "geoLong", "geoRadius", "geofenceName"};
        //D3Get.getXXXArrayWithCompletion(iGetGeofences, D3Constants.GEOFENCES_URL, false, false, firebaseChildKeys);
        D3Core.getFirebaseRef()
                .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.GEOFENCES_URL)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<BaseModel> geofences = new ArrayList<BaseModel>();
                                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                    Geofence gf = new Geofence(dss.getKey());
                                    if (gf.parseSnapshot(dss))
                                        geofences.add(gf);
                                }

                                // Now, call callback
                                iGetGeofences.handleFirebaseResponse(geofences);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });
    }


    /**
     * Get Place Array
     *
     * @param iGetPlaces
     */
    public static void getPlacesArrayWithCompletion(final IGet iGetPlaces) {
        //String[] firebaseChildKeys = {"placeAddress", "placeImage", "placeName", "placeOnMap", "placeTeaserText", "placeText", "placeWebSite"};
        //D3Get.getXXXArrayWithCompletion(iGetPlaces, D3Constants.PLACES_URL, true, false, firebaseChildKeys);
        D3Core.getFirebaseRef()
                .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.PLACES_URL)
                .orderByChild("active").equalTo(true)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<BaseModel> places = new ArrayList<BaseModel>();
                                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                    Place place = new Place(dss.getKey());
                                    if (place.parseSnapshot(dss))
                                        places.add(place);
                                }

                                // Now, call callback
                                iGetPlaces.handleFirebaseResponse(places);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });
    }

    public static void getPlaceListingWithCompletion(final IGet iGetPlaces) {
        //String[] firebaseChildKeys = {"placeAddress", "placeImage", "placeName", "placeOnMap", "placeTeaserText", "placeText", "placeWebSite"};
        //D3Get.getXXXArrayWithCompletion(iGetPlaces, D3Constants.PLACES_URL, false, true, firebaseChildKeys);
        D3Core.getFirebaseRef()
                .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.PLACES_URL)
                .orderByChild("placeOnMap").equalTo(true)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<BaseModel> places = new ArrayList<BaseModel>();
                                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                    Place place = new Place(dss.getKey());
                                    if (place.parseSnapshot(dss))
                                        places.add(place);
                                }

                                // Now, call callback
                                iGetPlaces.handleFirebaseResponse(places);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });
    }

    /**
     * Get Place with PlaceKey
     *
     * @param iGetPlace
     * @param placeKey
     */
    public static void getPlaceWithPlaceKey(final D3Get.IGet iGetPlace, String placeKey) {
        //String[] firebaseChildKeys = {"active", "entryPriority", "placeAddress", "placeImage", "placeName", "placeOnMap", "placeTeaserText", "placeText", "placeWebSite"};
        //D3Get.getXXXWithXXXKey(iGetPlaces, placeKey, D3Constants.PLACES_URL, true, firebaseChildKeys);
        D3Core.getFirebaseRef()
                .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.PLACES_URL).child(placeKey)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Place place = new Place(dataSnapshot.getKey());
                                place.parseSnapshot(dataSnapshot);
                                iGetPlace.handleFirebaseResponse(place);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });
    }

    /**
     * Get Entries With Place Key
     *
     * @param iGetEntries
     * @param placeKey
     */
    public static void getEntriesArrayWithPlaceKey(final D3Get.IGet iGetEntries, String placeKey) {
        D3Core.getFirebaseRef()
                .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.ENTRIES_FOR_PLACE).child(placeKey)
                // See dot3Xcode app: [[[[firebase queryOrderedByChild:@"deleted"] queryStartingAtValue:nil] queryEndingAtValue:@NO] observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot)
                .orderByChild("deleted").startAt().endAt(false) // Sorting + filtering.
                //.addListenerForSingleValueEvent(    // "Reading Data Once ... It is triggered one time and then will not be triggered again."
                .addValueEventListener(             // "It is triggered once with the initial data and again every time the data changes."
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                ArrayList<BaseModel> entries = new ArrayList<BaseModel>();
                                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                    EntryForPlace entry = new EntryForPlace(dss.getKey());
                                    if (entry.parseSnapshot(dss))
                                        entries.add(entry);
                                }

                                // Sort by priority
                                BaseModel.sort(entries);

                                // Now, call callback
                                iGetEntries.handleFirebaseResponse(entries);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });
    }

    //TODO (see dot3Xcode app, D3Get.h and D3Get.m)

    //// NOTIFICATIONS BASED ON TRIGGERS - Beacons, geofences, etc
    //
    //#define D3BeaconDidEnterRegion @"D3BeaconDidEnterRegion"
    //#define D3BeaconDidExitRegion @"D3BeaconDidExitRegion"
    //#define D3BeaconDidRangeRegion @"D3BeaconDidRangeRegion"
    //#define D3DidEnterZone @"D3DidEnterZone"
    //
    //#define D3GeofenceDidEnterRegion @"D3GeofenceDidEnterRegion"
    //#define D3GeofenceDidExitRegion @"D3GeofenceDidExitRegion"
    //#define D3GeofenceDidArriveRegion @"D3GeofenceDidArriveRegion"
    //
    //// NOTIFICATIONS BASED ON REAL-TIME CHANGES TO THE DATABASE - Receive notices when there is a change to the data
    //
    //#define D3ViewDataDidChange @"D3ViewDataDidChange"
    //#define D3ViewCatDataDidChange @"D3ViewCatDataDidChange"
    //#define D3EntriesForViewCatDataDidChange @"D3EntriesForViewCatDataDidChange"
    //#define D3ZoneDataDidChange @"D3ZoneDataDidChange"
    //#define D3EntriesForZoneDataDidChange @"D3EntriesForZoneDataDidChange"
    //#define D3EntriesForPlaceDataDidChange @"D3EntriesForPlaceDataDidChange"
    //#define D3EntriesForPlaceDataDidChange @"D3EntriesForPlaceDataDidChange"

    ////Views
    // [AK:done] + (void)getViewsArrayWithCompletion:(void(^)(NSArray *))completion;
    // [AK:done] + (void)getViewWithViewKey:(NSString *)viewKey completion:(void(^)(NSDictionary *))completion;
    // [AK:done] + (void)getViewCatsArrayWithViewKey:(NSString *)viewKey completion:(void(^)(NSArray *))completion;
    //
    ////Fleet
    // [AK:done] + (void)getFleetArrayWithCompletion:(void(^)(NSArray *))completion;
    //
    ////Zones
    // [AK:done] + (void)getZoneWithZoneKey:(NSString *)zoneKey completion:(void(^)(NSDictionary *))completion;
    //
    ////Entries
    // [AK: done] + (void)getEntriesArrayWithZoneKey:(NSString *)zoneKey completion:(void(^)(NSArray *))completion;
    // [AK: done] + (void)getEntriesArrayWithCatKey:(NSString *)catKey completion:(void(^)(NSArray *))completion;
    //
    ////Geofences
    // [AK: done] + (void)getGeofencesArrayWithCompletion:(void(^)(NSArray *))completion;
    // [AK: done] + (void)getPlacesArrayWithCompletion:(void(^)(NSArray *))completion;
    // [AK: done, but still TO-DO] + (void)getPlaceListingWithCompletion:(void(^)(NSArray *))completion; //TODO: to workaround the multiple orderBy, else the IllegalArgumentException: You can't combine multiple orderBy calls!
    // [AK: done] + (void)getPlaceWithPlaceKey:(NSString *)placeKey completion:(void(^)(NSDictionary *))completion;
    // [AK: done] + (void)getEntriesArrayWithPlaceKey:(NSString *)placeKey completion:(void(^)(NSArray *))completion;

    // [AK: found in Get.m only?] + (void)getGeofencesWithPlaceKey:(NSString *)placeKey completion:(void(^)(NSDictionary *))completion {

}
