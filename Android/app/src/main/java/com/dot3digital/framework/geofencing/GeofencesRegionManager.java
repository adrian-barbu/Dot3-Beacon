package com.dot3digital.framework.geofencing;


import com.dot3digital.R;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dot3digital.framework.D3Constants;
import com.dot3digital.framework.D3Services;
import com.dot3digital.framework.interfaces.IDot3Application;
import com.dot3digital.framework.locationupdates.LocationUpdatesManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.GeofencingApi;

import java.util.ArrayList;
import java.util.HashMap;


public class GeofencesRegionManager
        implements ConnectionCallbacks, OnConnectionFailedListener,
            ResultCallback<Status>
{
    protected static final String LOG_TAG = Class.class.getSimpleName();

    // The entry point to Google Play services.
    protected GoogleApiClient mGoogleApiClient;

    // The list of geofences
    protected ArrayList<Geofence> mGeofenceList;

    // A flag to keep track of whether geofences were added.
    private boolean geofencesAdded;

    // For requesting to add or remove geofences.
    private PendingIntent mGeofencePendingIntent;

    // Used to persist an application's state about whether geofences were added.
    private SharedPreferences mSharedPreferences;

    // An application reference
    private IDot3Application iDot3Application;

    // Location manager
    LocationUpdatesManager locationUpdatesManager;

    // Singleton
    //TODO: should it actually be a singleton? may different apps want to work with different instances?
    static private GeofencesRegionManager singleton = null;
    static public GeofencesRegionManager getInstance() {
        return singleton == null ? new GeofencesRegionManager() : singleton;
    }
    private GeofencesRegionManager() {
        singleton = this;
    }


    public void onCreate(IDot3Application iDot3App) {
        this.iDot3Application = iDot3App;

        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<Geofence>();

        // Initially set the PendingIntent used in addGeofences() and removeGeofences() to null.
        mGeofencePendingIntent = null;

        // Retrieve an instance of the SharedPreferences object.
        mSharedPreferences = iDot3Application.getAppInstance().getSharedPreferences(
                iDot3Application.getSharedPreferencesFileName(), iDot3Application.getAppInstance().MODE_PRIVATE);

        // Get the value of geofencesAdded from SharedPreferences. Set to false as a default.
        geofencesAdded = mSharedPreferences.getBoolean(iDot3Application.getGeofencesAddedPrefname(), false);

        // Callback the app to let it, e.g. update its UI accordingly.
        iDot3Application.onAddRemoveGeofencesResult(geofencesAdded);

        // Get the geofences used.
        populateGeofenceList();

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();

        // Start location updates
        locationUpdatesManager = LocationUpdatesManager.getInstance();
        locationUpdatesManager.create(null, mGoogleApiClient, this.iDot3Application);
    }

    /* Builds a GoogleApiClient, calling the addApi method to request the LocationServices API. */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(iDot3Application.getAppInstance())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void onStart() {
        mGoogleApiClient.connect();
    }
    public void onResume() {
        locationUpdatesManager.resume();
    }
    public void onPause() {
        locationUpdatesManager.pause();
    }
    public void onStop() {
        mGoogleApiClient.disconnect();
    }

    /* Runs when a GoogleApiClient object successfully connects. */
    // Interface ConnectionCallbacks
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(LOG_TAG, "Connected to GoogleApiClient");

        // Start location updates when a GoogleApiClient object successfully connects.
        locationUpdatesManager.onConnected(connectionHint);

        // The callback, to notify the app that now it can start geofence monitoring (by calling addGeofences()).
        iDot3Application.onGoogleApiClientConnected(connectionHint);
    }

    // Interface ConnectionCallbacks
    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        Log.d(LOG_TAG, "Connection suspended");

        // Note, the callback onConnected() will be called again automatically when the service reconnects

        locationUpdatesManager.onConnectionSuspended(cause);
    }

    // Interface OnConnectionFailedListener
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in onConnectionFailed.
        Log.d(LOG_TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());

        locationUpdatesManager.onConnectionFailed(result);
    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build(); // If D3Services.geofencesArrayList (and mGeofenceList) are empty then IllegalArgumentException: No geofence has been added to this request.
    }

    /**
     * Adds geofences, which sets alerts to be notified when the device enters or exits one of the
     * specified geofences. Handles the success or failure results returned by GeofencingApi.addGeofences().
     */
    public void addGeofences() {
        if (!mGoogleApiClient.isConnected()) {
            String errGoogleApiClientNotConnected = iDot3Application.getAppInstance().getString(R.string.not_connected);
            Toast.makeText(iDot3Application.getAppInstance(), errGoogleApiClientNotConnected, Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, errGoogleApiClientNotConnected);
            return;
        }
        // If D3Services.geofencesArrayList (and mGeofenceList) are empty then IllegalArgumentException: No geofence has been added to this request.
        if (mGeofenceList.size() == 0) {
            String errGeofenceListEmpty = iDot3Application.getAppInstance().getString(R.string.geofence_list_empty);
            Toast.makeText(iDot3Application.getAppInstance(), errGeofenceListEmpty, Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, errGeofenceListEmpty);
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }

        // Request start of location updates
        locationUpdatesManager.startUpdates();
    }

    /**
     * Removes geofences, which stops further notifications when the device enters or exits
     * previously registered geofences.
     */
    public void removeGeofences() {
        if (!mGoogleApiClient.isConnected()) {
            String errGoogleApiClientNotConnected = iDot3Application.getAppInstance().getString(R.string.not_connected);
            Toast.makeText(iDot3Application.getAppInstance(), errGoogleApiClientNotConnected, Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, errGoogleApiClientNotConnected);
            return;
        }
        // If D3Services.geofencesArrayList (and mGeofenceList) are empty then IllegalArgumentException: No geofence has been added to this request.
        if (mGeofenceList.size() == 0) {
            String errGeofenceListEmpty = iDot3Application.getAppInstance().getString(R.string.geofence_list_empty);
            Toast.makeText(iDot3Application.getAppInstance(), errGeofenceListEmpty, Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, errGeofenceListEmpty);
            return;
        }

        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    // This is the same pending intent that was used in GeofencingApi.addGeofences().
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }

        // Request removal of location updates.
        locationUpdatesManager.stopUpdates();
    }

    private void logSecurityException(SecurityException securityException) {
        Log.e(LOG_TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    /**
     * Runs when the result of calling addGeofences() and removeGeofences() becomes available.
     * Either method can complete successfully or with an error.
     *
     * Since this activity implements the {@link ResultCallback} interface, we are required to
     * define this method.
     *
     * @param status The Status returned through a PendingIntent when addGeofences() or
     *               removeGeofences() get called.
     */
    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            geofencesAdded = !geofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(iDot3Application.getGeofencesAddedPrefname(), geofencesAdded);
            editor.commit();

            // Callback the app to let it, e.g. update its UI accordingly.
            iDot3Application.onAddRemoveGeofencesResult(geofencesAdded);

            //TODO: comment-out the Toast then
            Toast.makeText(
                    iDot3Application.getAppInstance(),
                        //Dot3Application.getInstance().getString(geofencesAdded ? R.string.geofences_added : R.string.geofences_removed),
                        "Geofences added or removed.", // temporary string
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(iDot3Application.getAppInstance(),
                    status.getStatusCode());
            Log.e(LOG_TAG, errorMessage);
        }
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(iDot3Application.getAppInstance(), GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addGeofences() and removeGeofences().
        return PendingIntent.getService(iDot3Application.getAppInstance(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void populateGeofenceList() {
        //[2015.11.12][Stelian] Temporailry Ignored
        /*
        for (HashMap<String, String> listElement : D3Services.geofencesArrayList) {
            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this geofence.
                    .setRequestId(listElement.get(D3Constants.FIREBASE_NODE_KEY)) // ("geofenceName"))
                            // Set the circular region of this geofence.
                    .setCircularRegion(
                            Double.parseDouble(listElement.get("geoLat")),
                            Double.parseDouble(listElement.get("geoLong")),
                            Float.parseFloat(listElement.get("geoRadius"))
                    )
                            // Set the expiration duration of the geofence. This geofence gets automatically
                            // removed after this period of time.
                    .setExpirationDuration(iDot3Application.getGeofenceExpirationMilliseconds())
                            // Set the transition types of interest. Alerts are only generated for these
                            // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                            // Create the geofence.
                    .build());
        }
        */
    }

}
