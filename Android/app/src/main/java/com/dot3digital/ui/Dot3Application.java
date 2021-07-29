package com.dot3digital.ui;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;

import com.dot3digital.framework.D3Services;
import com.dot3digital.framework.geofencing.GeofencesRegionManager;
import com.dot3digital.framework.interfaces.IDot3Application;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Initialize Firebase with the application context. This must happen before the client is used.
 */
public class Dot3Application extends Application
    implements IDot3Application
{
    ///// Google Location Services API - monitoring geofences ////////////////////////////////////////////////
    public static final String APP_PACKAGE_NAME = "com.dot3digital";
    public static final String SHARED_PREFERENCES_FILE_NAME = APP_PACKAGE_NAME + ".SHARED_PREFERENCES_FILE_NAME";
    public static final String ARE_GEOFENCES_ADDED_PREFNAME = APP_PACKAGE_NAME + ".ARE_GEOFENCES_ADDED_PREFNAME";
    // Used to set an expiration time for a geofence. After this amount of time Location Services stops tracking the geofence.
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    // For this app, geofences expire after twelve hours.
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;


    // ViewEntry key is home on load
    // See dot3Xcode: self.viewKey = @"-Jt3YtlNrSPoRDIDPTlV"; // But why it's absent in: https://dot-test-bed.firebaseio.com/-JqA-uunvcBzjXuub04s/views/
    //private String viewKey = "-Jt0GnsEy89tMT3F4WTC"; // in test-bed
    private String viewKey = "-Jubkn8WvoI_s-HmG0R-"; // in release

    private static Dot3Application singleton;
    public Dot3Application() {
        singleton = this;
    }
    public static Dot3Application getInstance(){
        return singleton;
    }

    ///// Interface IDot3Application /////
    @Override
    public Application getAppInstance() {
        return this; //return singleton;
    }
    // An app callback, to notify the app that now it can start geofence monitoring (by calling addGeofences()).
    @Override
    public void onGoogleApiClientConnected(Bundle connectionHint) {
        // Start geofence monitoring
        GeofencesRegionManager.getInstance().addGeofences();

        // Note, to stop geofence monitoring, call: GeofencesRegionManager.getInstance().removeGeofences(); // (having GoogleApiClient connected)
    }
    @Override
    public String getSharedPreferencesFileName() {
        return SHARED_PREFERENCES_FILE_NAME;
    }
    @Override
    public String getGeofencesAddedPrefname() {
        return ARE_GEOFENCES_ADDED_PREFNAME;
    }
    @Override
    public long getGeofenceExpirationMilliseconds() {
        return GEOFENCE_EXPIRATION_IN_MILLISECONDS;
    }
    /* A callback to let the app e.g. update its UI accordingly. */
    @Override
    public void onAddRemoveGeofencesResult(boolean areGeofencesAdded) {
        //TODO: Might need to use this callback in the app, e.g. to ensure that only one button among add and remove geofences, if any used, is enabled at any time.
        if (areGeofencesAdded) {
            //mAddGeofencesButton.setEnabled(false);
            //mRemoveGeofencesButton.setEnabled(true);
        } else {
            //mAddGeofencesButton.setEnabled(true);
            //mRemoveGeofencesButton.setEnabled(false);
        }
    }
    /**
     * A callback to let an app know if API is requesting location updates, e.g. to update the app's UI accordingly.
     */
    @Override
    public void onStartStopLocationUpdates(boolean requestingLocationUpdates) {
        //TODO: Might need to use this callback in the app, e.g. to ensure that only one button is enabled at any time.
        if (requestingLocationUpdates) {
            //mStartUpdatesButton.setEnabled(false);
            //mStopUpdatesButton.setEnabled(true);
        } else {
            //mStartUpdatesButton.setEnabled(true);
            //mStopUpdatesButton.setEnabled(false);
        }
    }
    /**
     * Callback the app to let it know the update on the latitude, the longitude, and the last location time in the UI.
     */
    @Override
    public void onLocationUpdate(Location currentLocation, String lastUpdateTime) {
        //TODO: Might need to use this callback in the app, e.g. to show location updates in the app's UI.
        if (currentLocation != null) {
            //mLatitudeTextView.setText(String.valueOf(currentLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(currentLocation.getLongitude()));
            //mLastUpdateTimeTextView.setText(lastUpdateTime);
        }
    }
    /////

    @Override
    public void onCreate() {
        super.onCreate();

        // See dot3Xcode app, AppDelegate.m
        //D3Services.setupSpaceKey("-JhMFwVygvM36yi6e_kI", this); //Stratford in dot3-main
        //D3Services.setupAppWithKey("-JqA-uunvcBzjXuub04s", this); //in dot-test-bed
        D3Services.setupAppWithKey("-JuNC480_FTHsPcy6mzG", this); //release

        // Initialize Image Loader
        initImageLoader(getApplicationContext());
    }

    public String getHomeViewKey() {
        return this.viewKey;
    }

    ///// List support  /////////////////////////////
    static public final String HEADER_INDICATOR = "-1";
    static public final String PARENT_ITEM_INDICATOR = "-2";
    static public final String PARENT_ITEM_URL_HASHMAPKEY = "ParentItemURLHashMapKey";
    static public final String PARENT_ITEM_TEXT_HASHMAPKEY = "ParentItemTextHashMapKey";
    public boolean AreTitlesSingleLine = true; // But in list_item.xml I need to have: android:singleLine="false", else it won't be toggled OK.
    public boolean AreURLsSingleLine = true; // But in list_item.xml I need to have: android:singleLine="false", else it won't be toggled OK.


    public Bitmap clickedItemBitmap;

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.FIFO);
//        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
