package com.dot3digital.framework.locationupdates;


import com.dot3digital.R;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.dot3digital.framework.interfaces.IDot3Application;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;

/**
 * Getting Location Updates.
 */
public class LocationUpdatesManager
        implements ConnectionCallbacks, OnConnectionFailedListener,
            LocationListener
{
    protected static final String LOG_TAG = Class.class.getSimpleName();

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent than this value.
     * Note: "The priority of PRIORITY_HIGH_ACCURACY, combined with the ACCESS_FINE_LOCATION permission setting that you've defined in the app manifest, and a fast update interval of 5000 milliseconds (5 seconds), causes the fused location provider to return location updates that are accurate to within a few feet. This approach is appropriate for mapping apps that display the location in real time."
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

    // An application reference
    private IDot3Application iDot3Application;


    ///// singleton /////
    static private LocationUpdatesManager singleton = null;
    static public LocationUpdatesManager getInstance() {
        return singleton == null ? new LocationUpdatesManager() : singleton;
    }
    private LocationUpdatesManager() {
        singleton = this;
    }
    /////


public void create(Bundle savedInstanceState, GoogleApiClient googleApiClient, IDot3Application iDot3App) {
        this.iDot3Application = iDot3App;

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        /*
        //TODO: Do it in an application (it its activity)
        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);
        */

        // Having a GoogleApiClient built..,
        this.mGoogleApiClient = googleApiClient;
        // ... request the LocationServices API.
        createLocationRequest(); // It supposes that the a GoogleApiClient is built already.
    }


    /**
     * Sets up the location request.
     * Note: Android has two location request settings:ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION.
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * These settings are appropriate for mapping applications that show real-time location updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. The app may not receive updates at all if no location sources are available, or
        // the app may receive them slower than requested. The app may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and the
        // app will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Requests start of location updates. Does nothing if updates have already been requested.
     */
    public void startUpdates() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            iDot3Application.onStartStopLocationUpdates(mRequestingLocationUpdates);
            startLocationUpdates();
        }
    }
    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to requestLocationUpdates() is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Requests removal of location updates. Does nothing if updates were not previously requested.
     */
    public void stopUpdates() {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            // Callback the app to let it, e.g. update its UI accordingly.
            iDot3Application.onStartStopLocationUpdates(mRequestingLocationUpdates);
            stopLocationUpdates();
        }
    }
    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // Note: It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to requestLocationUpdates() is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    public void resume() {
        // Within pause(), we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    public void pause() {
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }


    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(LOG_TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use FusedLocationApi.getLastLocation() to get it.
        // If it was previously requested, we store its value in the Bundle and check for it in create().
        // We do not request it again unless an app specifically requests location updates by calling startUpdates().
        //
        // TODO: Take into account in app: // Because we cache the value of the initial location in the Bundle, it means that if an app launches an activity, // and device is moved to a new location, and then the device orientation is changed, the original location // is displayed as the activity is re-created.

        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

            iDot3Application.onLocationUpdate(mCurrentLocation, mLastUpdateTime);
        }

        // If an app calls startUpdates() before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdates()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in onConnectionFailed.
        Log.d(LOG_TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.d(LOG_TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        iDot3Application.onLocationUpdate(mCurrentLocation, mLastUpdateTime);

        //Toast.makeText(Dot3Application.getInstance(), Dot3Application.getInstance().getString(R.string.location_updated_message),Toast.LENGTH_SHORT).show();
    }




    /*
    //TODO: Do it in an application (it its activity)

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";


    // Updates fields based on data stored in the bundle.
    // @param savedInstanceState The activity state saved in the Bundle.
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);

                // Callback the app to let it, e.g. update its UI accordingly.
                iDot3Application.onStartStopLocationUpdates(mRequestingLocationUpdates);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }

            iDot3Application.onLocationUpdate(mCurrentLocation, mLastUpdateTime);
        }
    }

    // Stores activity data in the Bundle.
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        //TODO
        //super.onSaveInstanceState(savedInstanceState);
    }
    */

}
