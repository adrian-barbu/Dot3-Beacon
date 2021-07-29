package com.dot3digital.framework;


import android.net.Uri;

public class D3Constants {

    public static String getViewCatURL(){

        // Not sure how you do this but...

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("getSpaceUrl") // Put getSpaceURL?
                .appendPath("getSpaceKey") // put Space Key
                .appendPath("viewcats"); // put viewcats
        String myUrl = builder.build().toString();

        return myUrl;

    }

    // Space URL (The Dot3 Firebase URL)
    //public static final String BASE_URL = "https://dot-test-bed.firebaseio.com/"; // test-bed
    //public static final String BASE_URL = "https://dot3-main.firebaseio.com/";
    public static final String BASE_URL = "https://dot3-release.firebaseio.com/"; // URL for kew, release

    ///// URLs ///////////////////////////////////////////////////////////////////////////////////////////////
    public static final String CHANNEL_URL = "channels";
    public static final String BEACON_URL = "beacons";
    public static final String ZONES_URL = "zones";
    public static final String ENTRIES_FOR_ZONE = "entriesForZone";
    public static final String FLEET_URL = "fleet";
    public static final String VIEWS_URL = "views";
    public static final String VIEW_CATS_URL = "viewcats";
    public static final String ENTRIES_FOR_VIEWCAT = "entriesForViewCat";

    public static final String USER_URL = "users";
    public static final String GLOBAL_COUNTER = "dataCountOfCalls";
    public static final String PLACES_URL = "places";
    public static final String ENTRIES_FOR_PLACE = "entriesForPlace";
    public static final String GEOFENCES_URL = "geofences";

    public static final String ZONE_PUSH_MESSAGE_TIME = "zonePushTime";      // [2015.11.25][Stelian]

    public static final String USER_COUNTS = "userCounts";
    public static final String COUNTS_VIEWS = "views";
    public static final String COUNTS_VIEWCATS = "viewCats";
    public static final String COUNTS_PLACES = "places";
    public static final String COUNTS_ENTRY = "entries";
    public static final String GENERAL_COUNTS = "counts";

    // DEPRECATED
    public static final String CHANNEL_ENTRIES_URL = "channelEntries";
    public static final String COUNTER_VIEWS_OF_CHANNEL_ENTRY_URL = "counterViewsOfChannelEntries";


    ///// Dot3 SDK constants /////////////////////////////////////////////////////////////////////////////////
    public static final String FIREBASE_NODE_KEY = "__firebase_node_key";

}
