package com.dot3digital.framework.model;

import com.firebase.client.DataSnapshot;

/**
 * @description     Geofence Model (represent for geofence
 *
 * @author          Stelian
 */
public class Geofence extends BaseModel {
    private String mName;
    private String mAssignedPlace;
    private String mLatitude, mLongitude, mRadius;

    public Geofence(String nodeKey) {
        super(nodeKey);
    }

    /** Getter **/
    public String getName() { return mName; }
    public String getAssignedPlace() { return mAssignedPlace; }
    public String getLatitude() { return mLatitude; }
    public String getLongitude() { return mLongitude; }
    public String getRadius() { return mRadius; }

    @Override
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
        mName = getStringField(dataSnapshot, "geofenceName");
        mAssignedPlace = getStringField(dataSnapshot, "assignedPlace");
        mLatitude = getStringField(dataSnapshot, "geoLat");
        mLongitude = getStringField(dataSnapshot, "geoLong");
        mRadius = getStringField(dataSnapshot, "geoRadius");
        return true;
    }
}
