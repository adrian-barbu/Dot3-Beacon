package com.dot3digital.framework;


import android.content.Context;

import com.dot3digital.framework.geofencing.GeofencesRegionManager;
import com.dot3digital.framework.interfaces.IDot3Application;
import com.dot3digital.framework.model.BaseModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arkady on 03/08/15.
 */
public class D3Services
{
	protected static final String LOG_TAG = Class.class.getSimpleName();

	public static void setupAppWithKey(String spaceKeyStr, Context context) {
		D3Core.setupSpaceKey(spaceKeyStr, context);
		//TODO: [D3Set registerAnonymousUser];
	}

	public static void startBeaconMonitoring(IDot3Application iDot3Application) {
		// See: dot3Xcode app, D3Services.m

		//TODO: implement startBeaconMonitoring
		// Calling Dot3 SDK
		// It will callback the parameter's method handleFirebaseResponse()
		D3Get.getFleetArrayWithCompletion(new D3Services.BeaconsCallback(iDot3Application));
	}

	public static void startGeofencesMonitoring(IDot3Application iDot3Application) {
		// See: dot3Xcode app, D3Services.m

		// Calling Dot3 SDK
		// It will callback the parameter's method handleFirebaseResponse()
		D3Get.getGeofencesArrayWithCompletion(new D3Services.GeofencesCallback(iDot3Application));
	}


	public static ArrayList<BaseModel> fleetArrayList;
	public static String[] fleetKeys;

	static public class BeaconsCallback implements D3Get.IGet
	{
		private IDot3Application iDot3Application;

		public BeaconsCallback(IDot3Application iDot3Application) {
			this.iDot3Application = iDot3Application;
		}

		@Override
		public void handleFirebaseResponse(ArrayList<BaseModel> beacons) {
			D3Services.fleetArrayList = beacons; // not used so far
		}
		@Override
		public void handleFirebaseResponse(BaseModel beacon) {}
	}


	public static ArrayList<BaseModel> geofencesArrayList;
	public static String[] geofencesKeys;

	static public class GeofencesCallback implements D3Get.IGet
	{
		private IDot3Application iDot3Application;

		public GeofencesCallback(IDot3Application iDot3Application) {
			this.iDot3Application = iDot3Application;
		}

		@Override
		public void handleFirebaseResponse(ArrayList<BaseModel> geofencies) {
			D3Services.geofencesArrayList = geofencies;

			// Prepare geofence monitoring
			GeofencesRegionManager geofencesRegionManager = GeofencesRegionManager.getInstance();
			//TODO: rework (?) the temporary geofencesRegionManager's method names:
			// The order of method calls is important!
			geofencesRegionManager.onCreate(this.iDot3Application);
			geofencesRegionManager.onStart();
			geofencesRegionManager.onResume();
		}

		@Override
		public void handleFirebaseResponse(BaseModel geofence) {}
	}

}
