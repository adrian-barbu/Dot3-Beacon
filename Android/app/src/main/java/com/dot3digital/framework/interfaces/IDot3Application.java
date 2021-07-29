package com.dot3digital.framework.interfaces;

import android.app.Application;
import android.location.Location;
import android.os.Bundle;

/**
 * Created by arkady on 04/08/15.
 */
public interface IDot3Application
{
	Application getAppInstance();

	// An app callback, to notify the app that now it can start geofence monitoring (by calling addGeofences()).
	void onGoogleApiClientConnected(Bundle connectionHint);

	String getSharedPreferencesFileName();
	String getGeofencesAddedPrefname();
	long getGeofenceExpirationMilliseconds();
	void onAddRemoveGeofencesResult(boolean areGeofencesAdded);
	void onStartStopLocationUpdates(boolean requestingLocationUpdates);
	void onLocationUpdate(Location currentLocation, String lastUpdateTime);

}
