package com.dot3digital.framework.model;

import com.firebase.client.DataSnapshot;

/**
 * @description     Beacon Model (represent for beacon
 *
 * @author          Stelian
 */
public class Beacon extends BaseModel {
    private String mName;
    private String mFleet, mFleetName;
    private String mMajor, mMinor;
    private String mZoneKeys;

    public Beacon(String nodeKey) {
        super(nodeKey);
    }

    /** Getter **/
    public String getName() { return mName; }
    public String getFleet() { return mFleet; }
    public String getFleetName() { return mFleetName; }
    public String getMajor() { return mMajor; }
    public String getMinor() { return mMinor; }
    public String getZoneKeys() { return mZoneKeys; }

    @Override
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
        mName = getStringField(dataSnapshot, "beaconName");
        mFleet = getStringField(dataSnapshot, "beaconFleet");
        mFleetName = getStringField(dataSnapshot, "beaconFleetName");
        mMajor = getStringField(dataSnapshot, "beaconMajor");
        mMinor = getStringField(dataSnapshot, "beaconMinor");

        // Get Zone Keys
        DataSnapshot zones = dataSnapshot.child("zones");
        if (zones.exists()) {
            String zoneKeys = "";
            for (DataSnapshot beaconZoneKey : zones.getChildren()) {
                if (beaconZoneKey.getValue().toString().equalsIgnoreCase("true")) {
                    // Once, catch first zone key
                    zoneKeys += beaconZoneKey.getKey();
                    break;
                }
            }
            mZoneKeys = zoneKeys;
        }
        return true;
    }

}
