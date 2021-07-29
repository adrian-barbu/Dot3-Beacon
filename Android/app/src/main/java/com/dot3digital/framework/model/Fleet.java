package com.dot3digital.framework.model;

import com.firebase.client.DataSnapshot;

/**
 * @description     Fleet Data Model
 *                  This model will be used to filter beacon region.
 *
 * @author          Stelian
 */
public class Fleet extends BaseModel {
    private String mUuid, mName;

    public Fleet(String nodeKey) {
        super(nodeKey);
    }

    public Fleet(String uuid, String name) {
        mUuid = uuid;
        mName = name;
    }

    public String getUUID() {
        return mUuid;
    }
    public String getName() {
        return mName;
    }

    public void setUUID(String value) {
        mUuid = value;
    }
    public void setName(String value) {
        mName = value;
    }

    @Override
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
        mUuid = getStringField(dataSnapshot, "beaconUUID");
        mName = getStringField(dataSnapshot, "fleetName");
        return true;
    }
}
