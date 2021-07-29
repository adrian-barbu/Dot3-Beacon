package com.dot3digital.framework.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.DataSnapshot;

/**
 * @description     EntriesForViewCat Model (represent for entriesForViewCat
 *
 * @author          Stelian
 */
public class EntryForViewCat extends BaseModel implements Parcelable {
    private String mHeadline;
    private String mText, mTeaserText;
    private String mImage, mInitialImage;
    private String mDisplayType;

    private String mAddress;
    private String mMapLat, mMapLong;

    public EntryForViewCat(String nodeKey) {
        super(nodeKey);
    }

    /** Getter **/
    public String getHeadline() { return mHeadline; }
    public String getText() { return mText; }
    public String getTeaserText() { return mTeaserText; }
    public String getDisplayType() { return mDisplayType; }
    public String getImage() { return mImage; }
    public String getInitialImage() { return mInitialImage; }
    public String getAddress() { return mAddress; }
    public String getMapLatitude() { return mMapLat; }
    public String getMapLongitude() { return mMapLong; }

    @Override
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
        String draft = getStringField(dataSnapshot, "draft");
        if (draft.equalsIgnoreCase("true"))
            return false;

        mHeadline = getStringField(dataSnapshot, "entryHeadline");
        mText = getStringField(dataSnapshot, "entryText");
        mTeaserText = getStringField(dataSnapshot, "entryTeaserText");
        mImage = getStringField(dataSnapshot, "entryImage");
        mInitialImage = getStringField(dataSnapshot, "entryInitialImage");
        mPriority = getStringField(dataSnapshot, "entryPriority");
        mDisplayType = getStringField(dataSnapshot, "entryDisplayType");

        // Get Map Info
        DataSnapshot dsMap = dataSnapshot.child("mapPin");
        if (dsMap.exists()) {
            mAddress = getStringField(dsMap, "address");
            mMapLat = getStringField(dsMap, "geoLat");
            mMapLong = getStringField(dsMap, "geoLong");
        }
        return true;
    }

    ////////////// Manage Parcelable //////////////
    public EntryForViewCat(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mHeadline);
        dest.writeString(mText);
        dest.writeString(mTeaserText);
        dest.writeString(mImage);
        dest.writeString(mInitialImage);
        dest.writeString(mDisplayType);
        dest.writeString(mAddress);
        dest.writeString(mMapLat);
        dest.writeString(mMapLong);
    }

    private void readFromParcel(Parcel in) {
        mHeadline = in.readString();
        mText = in.readString();
        mTeaserText = in.readString();
        mImage = in.readString();
        mInitialImage = in.readString();
        mDisplayType = in.readString();
        mAddress = in.readString();
        mMapLat = in.readString();
        mMapLong = in.readString();
    }

    public static final Creator CREATOR =
            new Creator() {
                public EntryForViewCat createFromParcel(Parcel in) {
                    return new EntryForViewCat(in);
                }

                public EntryForViewCat[] newArray(int size) {
                    return new EntryForViewCat[size];
                }
            };
}
