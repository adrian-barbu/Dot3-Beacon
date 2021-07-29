package com.dot3digital.ui.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.altbeacon.beacon.BeaconManager;

/**
 * @description     Util Functions
 *
 * @author          Stelian
 */
public class CommonUtil {
    /**
     * Check Network Availablity
     *
     * @param context
     * @return
     */
    public static  boolean isNetworkAvailable(Context context) {
        if(context == null) { return false; }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // if no network is available networkInfo will be null, otherwise check if we are connected
        try {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            //Log.e(TAG, "isNetworkAvailable()" + e.getMessage());
        }
        return false;
    }

    /**
     * Check Bluetooth Status
     *
     */
    public static boolean isBluetoothAvailable(Context context) {
        try {
            if (!BeaconManager.getInstanceForApplication(context).checkAvailability())
                return false;
        } catch (RuntimeException e) {
            return false;
        }

        return true;
    }
}
