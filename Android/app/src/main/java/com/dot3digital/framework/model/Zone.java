package com.dot3digital.framework.model;

import com.firebase.client.DataSnapshot;

/**
 * @description     Zone Model (represent for zones
 *
 * @author          Stelian
 */
public class Zone extends BaseModel {
    private String mActive;
    private String mName, mText, mTeaserText;
    private String mImage;

    public Zone(String nodeKey) {
        super(nodeKey);
    }

    /** Getter **/
    public String getActive() { return mActive; }
    public boolean isActive() { return !mActive.isEmpty() && mActive.equalsIgnoreCase("true"); }
    public String getName() { return mName; }
    public String getText() { return mText; }
    public String getTeaserText() { return mTeaserText; }
    public String getImage() { return mImage; }

    @Override
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
        mActive = getStringField(dataSnapshot, "active");
        if (mActive.isEmpty() || !mActive.equalsIgnoreCase("true"))
            return false;

        mName = getStringField(dataSnapshot, "zoneName");
        mText = getStringField(dataSnapshot, "zoneText");
        mTeaserText = getStringField(dataSnapshot, "zoneTeaserText");
        mImage = getStringField(dataSnapshot, "zoneImage");
        return true;
    }
}
