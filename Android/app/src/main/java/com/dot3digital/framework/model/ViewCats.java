package com.dot3digital.framework.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.DataSnapshot;

/**
 * @description     ViewCats Model (represent for viewcats
 *
 * @author          Stelian
 */
public class ViewCats extends BaseModel implements Parcelable {
    private String mName;
    private String mText, mTagline, mTeaserText;
    private String mImage;
    private String mScreenCategoryType;

    public ViewCats(String nodeKey) {
        super(nodeKey);
    }

    /** Getter **/
    public String getName() { return mName; }
    public String getText() { return mText; }
    public String getTagline() { return mTagline; }
    public String getTeaserText() { return mTeaserText; }
    public String getImage() { return mImage; }
    public String getScreenCategoryType() { return mScreenCategoryType; }

    @Override
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
        mName = getStringField(dataSnapshot, "viewCatName");
        mText = getStringField(dataSnapshot, "viewCatText");
        mTagline = getStringField(dataSnapshot, "viewCatTagline");
        mTeaserText = getStringField(dataSnapshot, "viewCatTeaserText");
        mImage = getStringField(dataSnapshot, "viewCatImage");
        mPriority = getStringField(dataSnapshot, "viewCatPriority");
        mScreenCategoryType = getStringField(dataSnapshot, "screenCategoryType");
        return true;
    }

    ////////////// Manage Parcelable //////////////
    public ViewCats(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mText);
        dest.writeString(mTagline);
        dest.writeString(mTeaserText);
        dest.writeString(mPriority);
        dest.writeString(mScreenCategoryType);
    }

    private void readFromParcel(Parcel in) {
        mName = in.readString();
        mText = in.readString();
        mTagline = in.readString();
        mTeaserText = in.readString();
        mPriority = in.readString();
        mScreenCategoryType = in.readString();
    }

    public static final Creator CREATOR =
            new Creator() {
                public ViewCats createFromParcel(Parcel in) {
                    return new ViewCats(in);
                }

                public ViewCats[] newArray(int size) {
                    return new ViewCats[size];
                }
            };

}
