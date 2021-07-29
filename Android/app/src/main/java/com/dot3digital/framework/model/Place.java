package com.dot3digital.framework.model;

import com.firebase.client.DataSnapshot;

/**
 * @description     Place Model (represent for places)
 *
 * @author          Stelian
 */
public class Place extends BaseModel {
    private String mName;
    private String mText, mTeaserText;
    private String mAddress;
    private String mOnMap;
    private String mImage;
    private String mWebSite;

    public Place(String nodeKey) {
        super(nodeKey);
    }

    /** Getter **/
    public String getName() { return mName; }
    public String getText() { return mText; }
    public String getTeaserText() { return mTeaserText; }
    public String getAddress() { return mAddress; }
    public String getOnMap() { return mOnMap; }
    public String getWebSite() { return mWebSite; }
    public String getImage() { return mImage; }

    @Override
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
        mName = getStringField(dataSnapshot, "placeName");
        mText = getStringField(dataSnapshot, "placeText");
        mTeaserText = getStringField(dataSnapshot, "placeTeaserText");
        mAddress = getStringField(dataSnapshot, "placeAddress");
        mOnMap = getStringField(dataSnapshot, "placeOnMap");
        mWebSite = getStringField(dataSnapshot, "placeWebSite");
        mImage = getStringField(dataSnapshot, "placeImage");
        return true;
    }

}
