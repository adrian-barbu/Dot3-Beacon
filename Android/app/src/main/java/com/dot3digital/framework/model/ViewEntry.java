package com.dot3digital.framework.model;

import com.firebase.client.DataSnapshot;

/**
 * @description     ViewEntry Model (represent for view)
 *
 * @author          Stelian
 */
public class ViewEntry extends BaseModel {
    private String mName;
    private String mTitle, mTagline;
    private String mDescription;
    private String mImage;

    public ViewEntry(String nodeKey) {
        super(nodeKey);
    }

    /** Getter **/
    public String getName() { return mName; }
    public String getTitle() { return mTitle; }
    public String getTagline() { return mTagline; }
    public String getDescription() { return mDescription; }
    public String getImage() { return mImage; }

    @Override
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
        mName = getStringField(dataSnapshot, "viewName");
        mTitle = getStringField(dataSnapshot, "viewTitle");
        mTagline = getStringField(dataSnapshot, "viewTagline");
        mDescription = getStringField(dataSnapshot, "viewDescription");
        mImage = getStringField(dataSnapshot, "viewImage");
        return true;
    }

}
