package com.dot3digital.framework.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.DataSnapshot;

/**
 * @description     EntriesForZone Model (represent for entriesForZone
 *
 * @author          Stelian
 */
public class EntryForZone extends BaseModel implements Parcelable {
    protected String mHeadline;
    protected String mText, mTeaserText;
    protected String mImage;
    protected String mDisplayType;

    public EntryForZone(String nodeKey) {
        super(nodeKey);
    }

    /** Getter **/
    public String getHeadline() { return mHeadline; }
    public String getText() { return mText; }
    public String getTeaserText() { return mTeaserText; }
    public String getImage() { return mImage; }
    public String getDisplayType() { return mDisplayType; }

    @Override
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
        String isDraft = getStringField(dataSnapshot, "draft");
        if (isDraft.equalsIgnoreCase("true"))
            return false;

        mDisplayType = getStringField(dataSnapshot, "entryDisplayType");
        if (mDisplayType.equalsIgnoreCase("push-notice") ||
            mDisplayType.equalsIgnoreCase("video") ||
            mDisplayType.equalsIgnoreCase("web-view-image"))
            return false;

        mHeadline = getStringField(dataSnapshot, "entryHeadline");
        mText = getStringField(dataSnapshot, "entryText");
        mTeaserText = getStringField(dataSnapshot, "entryTeaserText");
        mImage = getStringField(dataSnapshot, "entryImage");
        mPriority = getStringField(dataSnapshot, "entryPriority");

        return true;
    }

    ////////////// Manage Parcelable //////////////
    public EntryForZone(Parcel in) {
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
        dest.writeString(mDisplayType);
    }

    private void readFromParcel(Parcel in) {
        mHeadline = in.readString();
        mText = in.readString();
        mTeaserText = in.readString();
        mImage = in.readString();
        mDisplayType = in.readString();
    }

    public static final Creator CREATOR =
            new Creator() {
                public EntryForZone createFromParcel(Parcel in) {
                    return new EntryForZone(in);
                }

                public EntryForZone[] newArray(int size) {
                    return new EntryForZone[size];
                }
            };
}
