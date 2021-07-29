package com.dot3digital.ui;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class PassableObject implements Parcelable
{
    private String myStringValue;
    //private CustomArrayAdapter listAdapter;
    
    public PassableObject() {}
    
    public PassableObject(Parcel inParcel) {
        myStringValue = inParcel.readString();
        //listAdapter = inParcel.read???();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flafs) {
        outParcel.writeString(myStringValue);
        //outParcel.write???(listAdapter);
    }
    
    public void setMyStringValue(String myStringValue) {
        this.myStringValue = myStringValue;
    }
    public String getMyStringValue() {
        return myStringValue;
    }
    
    //public void setMyListAdapterValue(CustomArrayAdapter listAdapter) {
    //    this.listAdapter = listAdapter;
    //    
    //    Log.i("PassableObject.setMyListAdapterValue()", "listAdapter="+this.listAdapter);
    //}
    //public CustomArrayAdapter getMyListAdapterValue() {
    //    return listAdapter;
    //}

    public static final Creator<PassableObject> CREATOR
        = new Creator<PassableObject>()
        {
            @Override
            public PassableObject createFromParcel(Parcel in) {
                return new PassableObject(in);
            }

            @Override
            public PassableObject[] newArray(int size) {
                return new PassableObject[size];
            }
        };
    
}
