package com.dot3digital.framework.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @description     D3 Shared Preference
 *
 * @author          Stelian
 */
public class D3SharedPreference {
    public static final String KEW_PREFERENCE = "KewPrefs";

    public static final String FIELD_USER_ID = "userId";

    /**
     * Set User Id to Preference
     *
     * @param context
     * @param userId
     */
    public static void setUserId(Context context, String userId) {
        SharedPreferences sp = context.getSharedPreferences(KEW_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sp.edit();
        prefEditor.putString(FIELD_USER_ID, userId);
        prefEditor.commit();
    }

    /**
     * Get User Id from Preference
     *
     * @param context
     */
    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(KEW_PREFERENCE, context.MODE_PRIVATE);
        return sp.getString(FIELD_USER_ID, null);
    }
}
