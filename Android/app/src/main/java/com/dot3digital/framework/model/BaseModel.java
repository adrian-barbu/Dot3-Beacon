package com.dot3digital.framework.model;

import com.firebase.client.DataSnapshot;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * @description     Base Data Model
 *                  This models will be represented data from FireBase
 *
 * @author          Stelian
 */
public class BaseModel {
    // Common Variable
    protected String mFireBaseNodeKey;
    protected String mPriority;

    public BaseModel() {}
    public BaseModel (String nodeKey) {
        mFireBaseNodeKey = nodeKey;
    }

    public String getNodeKey() { return mFireBaseNodeKey; }
    public String getPriority() { return mPriority; }

    /**
     * Capture element string from DataSnapshot
     *
     * @param dataSnapshot
     * @param key
     */
    protected String getStringField(DataSnapshot dataSnapshot, String key) {
        try {
            DataSnapshot ds = dataSnapshot.child(key);
            return ds.exists() ? ds.getValue().toString() : "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Parse snapshot and fill element (must be override)
     *
     * @param dataSnapshot
     * @return
     */
    public boolean parseSnapshot(DataSnapshot dataSnapshot) {
        return false;
    }

    /**
     * Sort list by key
     *
     * @param datas
     */
    public static void sort(List<BaseModel> datas) {
        Collections.sort(datas, new Comparator<BaseModel>() {
            public int compare(BaseModel o1, BaseModel o2) {
                // Compare by viewcat child "viewCatPriority"
                int p1 = Integer.parseInt(o1.getPriority());
                int p2 = Integer.parseInt(o2.getPriority());
                return (p1 < p2) ? -1 : 1;
            }
        });
    }
}
