package com.dot3digital.framework.model;

import com.firebase.client.DataSnapshot;

/**
 * @description     Push Time Model
 *
 * @author          Stelian
 */
public class PushTime extends BaseModel {
    private long mTimePushed;
    private String mZoneKey;

    public PushTime(String nodeKey) {
        super(nodeKey);
    }

    /** Getter **/
    public long getTimePushed() { return mTimePushed; }
    public String getZoneKey() { return mZoneKey; }

    @Override
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
        try {
            mTimePushed = Long.parseLong(getStringField(dataSnapshot, "timePushed"));
        } catch (Exception e) {}
        mZoneKey = getStringField(dataSnapshot, "zoneKey");
        return true;
    }

}
