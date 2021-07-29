package com.dot3digital.framework.model;

import com.firebase.client.DataSnapshot;

/**
 * @description     User Model
 *
 * @author          Stelian
 */
public class User extends BaseModel {
    private String mUserID;

    public User(String nodeKey) {
        super(nodeKey);
    }

    /** Getter **/
    public String getUserID() { return getNodeKey(); }

    @Override
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
//        mUserID = getStringField(dataSnapshot, "userID");
        return true;
    }

}
