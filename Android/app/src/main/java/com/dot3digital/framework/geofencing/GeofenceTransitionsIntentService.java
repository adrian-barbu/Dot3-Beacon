package com.dot3digital.framework.geofencing;

import com.dot3digital.R;

import android.app.IntentService;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;

import com.dot3digital.framework.D3Get;
import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.util.Utilities;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;


//TODO: rework it!
/**
 * Listener for geofence transition changes.
 *
 * Receives geofence transition events from Location Services in the form of an Intent containing
 * the transition type and geofence id(s) that triggered the transition. Creates a notification
 * as the output.
 */
public class GeofenceTransitionsIntentService extends IntentService {

    protected static final String LOG_TAG = Class.class.getSimpleName(); // = "geofence-transitions-service";

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public GeofenceTransitionsIntentService() {
        // Use the LOG_TAG to name the worker thread.
        super(LOG_TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Handles incoming intents.
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(LOG_TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            //////////////////
            // Get the Ids of each geofence that was triggered.

            //[2015.11.12][Stelian] Temporailry Ignored
            /*
            ArrayList triggeringGeofencesIdsList = new ArrayList();
            for (Geofence geofence : triggeringGeofences) {
                /////
                for (HashMap<String, String> listElement : D3Services.geofencesArrayList) {
                    // Set the request ID of the geofence. This is a string to identify this geofence.
                    String geofenceKey = listElement.get(D3Constants.FIREBASE_NODE_KEY);
                    // Note: In GeofencesRegionManager.m, didEnterRegion uses "geofenceName" as geofence requestId.
                    // But what if there are more than one geofence with same "geofenceName"? I think searching by geofence key is safer.
                    if(geofenceKey.equals(geofence.getRequestId())) {
                        String assignedPlaceKey = listElement.get("assignedPlace");
                        String geofenceName = listElement.get("geofenceName");

                        //TODO: issue: DataSnapshot { key = -Jul2SV8jdlyXyY4or2D, value = null }
                        D3Get.getPlaceWithPlaceKey(new PlaceCallback(geofenceTransition, geofenceName),
                                assignedPlaceKey); // It will callback the parameter's method handleFirebaseResponse()

                        triggeringGeofencesIdsList.add(geofenceName); //(geofence.getRequestId());

                        // Note: In GeofencesRegionManager.m, didEnterRegion, the break stops searching geofences on the 1st matching geofence.
                        //break;
                    }
                }
            }

            //////////////////

            // Get the geofence transition details as a String.
            String geofenceTransitionDetails = getTransitionString(geofenceTransition) + ": "
                    + TextUtils.join(", ",  triggeringGeofencesIdsList);

            //// Send notification with the transition details.
            ////TODO: rework, since as coded, this notification only might appear for a moment and then covered by the place(s) notification(s).
            //sendNotification(geofenceTransitionDetails);

            // Log the transition details.
            Log.d(LOG_TAG, geofenceTransitionDetails);

            */
        } else {
            // Log the error.
            Log.e(LOG_TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }

    //static
    public class PlaceCallback implements D3Get.IGet
    {
        int geofenceTransition;
        String geofenceName;

        public PlaceCallback(int geofenceTransition, String geofenceName) {
            this.geofenceTransition = geofenceTransition;
            this.geofenceName = geofenceName;
        }

        @Override
        public void handleFirebaseResponse(ArrayList<BaseModel> places) {}

        @Override
        public void handleFirebaseResponse(BaseModel place) {
            //[2015.11.13][Stelian] Temporarily Commented
            /*
            String placeKey = place.get(D3Constants.FIREBASE_NODE_KEY);
            String placeImage = place.get("placeImage");
            String placeName = place.get("placeName");
            String placeText = place.get("placeText");

            // Send notification with the place details.
            sendNotification(getTransitionString(
                    this.geofenceTransition) +". GEOFENCE: " + geofenceName + "; PLACE: " + placeName + ", " + placeText,
                    placeKey, placeImage, placeName, placeText);
            */
        }
    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity1.
     */
    private void sendNotification(String notificationDetails, String placeKey, String placeImage, String placeName, String placeText) {
        //[2015.11.13][Stelian] Temporarily Commented
        /*
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(getApplicationContext(),
                PlaceEntriesListActivity.class);

        notificationIntent.putExtra("PlacesListActivity_key", placeKey);
        notificationIntent.putExtra("PlacesListActivity_placeImage", placeImage);
        notificationIntent.putExtra("PlacesListActivity_placeText", placeText);
        notificationIntent.putExtra("PlacesListActivity_placeName", placeName);
        notificationIntent.putExtra("PlacesListActivity_fromNotificationBar", true);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Class<?> sourceActivityClass = PlacesListActivity.class; //?//= PlaceEntriesListActivity.class);
        int requestCode = 0; //TODO
        ContextWrapper contextWrapper = this;
        int notifId = 123; //TODO An identifier for this notification, must be unique within the application.

        Utilities.createAndSendNotification( notificationIntent,
                sourceActivityClass, requestCode, contextWrapper, notifId,
                notificationDetails,
                this.getString(R.string.geofence_transition_notification_text));
        */
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType    A transition type constant defined in Geofence
     * @return                  A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }
}
