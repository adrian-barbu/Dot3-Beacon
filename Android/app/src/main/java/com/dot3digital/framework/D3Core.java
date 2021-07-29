package com.dot3digital.framework;

import android.content.Context;

import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.Beacon;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by arkady on 04/08/15.
 *
 * Modified by Stelian
 */
public class D3Core
{
	static private Firebase FirebaseRef;

	static public Firebase getFirebaseRef() {
		return FirebaseRef;
	}

	static public void setupSpaceKey(String spaceKeyStr, Context context) {

		Firebase.setAndroidContext(context);
		FirebaseRef = new Firebase(D3Constants.BASE_URL);

		D3Get.SPACE_KEY = spaceKeyStr;
	}

	// Dot3 SDK

	//Beacons
	//+ (void)getBeaconArrayWithMajor:(NSInteger)majorValue minor:(NSInteger)minorValue completion:(void(^)(NSArray *))completion;
	public static void getBeaconArrayWithMajorOrMinor(final D3Get.IGet iGetBeacons, final int nearbyBeaconMajor, final int nearbyBeaconMinor) {
		D3Core.getFirebaseRef()
				.child("/" + D3Get.SPACE_KEY + "/" + D3Constants.BEACON_URL)
				//.addListenerForSingleValueEvent(    // "Reading Data Once ... It is triggered one time and then will not be triggered again."
				.addValueEventListener(             // "It is triggered once with the initial data and again every time the data changes."
						new ValueEventListener() {
							@Override
							public void onDataChange(DataSnapshot dataSnapshot) {
								ArrayList<BaseModel> beacons = new ArrayList<BaseModel>();
								for (DataSnapshot dss : dataSnapshot.getChildren()) {
									String beaconMajor = dss.child("beaconMajor").getValue().toString();
									String beaconMinor = dss.child("beaconMinor").getValue().toString();
									int intNearbyBeaconMajor = nearbyBeaconMajor; // avoiding a crash
									int intNearbyBeaconMinor = nearbyBeaconMinor; // avoiding a crash
									boolean predicateBeaconMajor = Integer.toString(intNearbyBeaconMajor).equals(beaconMajor);
									boolean predicateBeaconMinor = Integer.toString(intNearbyBeaconMinor).equals(beaconMinor);

									if ((intNearbyBeaconMinor > 1 && predicateBeaconMinor) ||
											(intNearbyBeaconMajor > 1 && predicateBeaconMajor)
										) {
										Beacon beacon = new Beacon(dss.getKey());
										if (beacon.parseSnapshot(dss))
											beacons.add(beacon);

										break;
									}
								}

								// Now, call callback
								iGetBeacons.handleFirebaseResponse(beacons);
							}

							@Override
							public void onCancelled(FirebaseError firebaseError) {
								System.out.println("The read failed: " + firebaseError.getMessage());
							}
						});
	}
}