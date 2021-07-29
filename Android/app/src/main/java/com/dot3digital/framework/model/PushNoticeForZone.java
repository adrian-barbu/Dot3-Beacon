package com.dot3digital.framework.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.DataSnapshot;

/**
 * @description     Push Notice For Zone Model (this is specialized EntryForZone)
 *
 * @author          Stelian
 */
public class PushNoticeForZone extends EntryForZone {
    protected String mPushNoticeText;
    protected int mPushNoticeCooldown;

    public PushNoticeForZone(String nodeKey) {
        super(nodeKey);
    }

    public String getPushNoticeText() { return mPushNoticeText; }
    public int getPushNoticeCooldown() { return mPushNoticeCooldown; }

    @Override
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
        String isDraft = getStringField(dataSnapshot, "draft");
        if (isDraft.equalsIgnoreCase("true"))
            return false;

        mDisplayType = getStringField(dataSnapshot, "entryDisplayType");
        if (!mDisplayType.equalsIgnoreCase("push-notice"))
            return false;

        mHeadline = getStringField(dataSnapshot, "entryHeadline");
        mText = getStringField(dataSnapshot, "entryText");
        mTeaserText = getStringField(dataSnapshot, "entryTeaserText");
        mImage = getStringField(dataSnapshot, "entryImage");
        mPriority = getStringField(dataSnapshot, "entryPriority");

        mPushNoticeText = getStringField(dataSnapshot, "pushNoticeText");
        mPushNoticeCooldown = Integer.parseInt(getStringField(dataSnapshot, "pushNoticeCooldown"));
        return true;
    }
}
