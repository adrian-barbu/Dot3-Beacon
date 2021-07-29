package com.dot3digital.framework.model;

import com.firebase.client.DataSnapshot;

/**
 * @description     EntriesForPlace Model (represent for entriesForPlace
 *
 * @author          Stelian
 */
public class EntryForPlace extends BaseModel {
    private String mHeadline;
    private String mText, mTeaserText;
    private String mImage;

    public EntryForPlace(String nodeKey) {
        super(nodeKey);
    }

    /** Getter **/
    public String getHeadline() { return mHeadline; }
    public String getText() { return mText; }
    public String getTeaserText() { return mTeaserText; }
    public String getPriority() { return mPriority; }
    public String getImage() { return mImage; }

    @Override
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
        String isDraft = getStringField(dataSnapshot, "draft");
        if (isDraft.equalsIgnoreCase("true"))
            return false;

        mHeadline = getStringField(dataSnapshot, "entryHeadline");
        mText = getStringField(dataSnapshot, "entryText");
        mTeaserText = getStringField(dataSnapshot, "entryTeaserText");
        mImage = getStringField(dataSnapshot, "entryImage");
        mPriority = getStringField(dataSnapshot, "entryPriority");

        return true;
    }
}
