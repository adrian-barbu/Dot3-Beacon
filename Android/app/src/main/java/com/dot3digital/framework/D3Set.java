package com.dot3digital.framework;

import android.content.Context;

import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.User;
import com.dot3digital.framework.util.D3SharedPreference;
import com.dot3digital.framework.util.D3TimeUtil;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * @description     D3Set Engine
 *
 * @modified        Stelian
 */
public class D3Set
{
    protected static final String LOG_TAG = Class.class.getSimpleName();

    /**
     * Define IGet Interface
     */
    public interface IGet
    {
        void handleFirebaseResponse(BaseModel data);
    }

    /**
     * registerAnonymousUser
     *
     */
    public static void registerAnonymousUser(final Context context, final IGet iGetUser) {
        String userId = D3SharedPreference.getUserId(context);
        if (userId == null) {
            // IF THE USER KEY DOES NOT EXIST THEN SET IT
            // FIRST, create an anonymous user on Firebase. Note that this also serves as an authentication token.

            D3Core.getFirebaseRef().authAnonymously(new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    // Get the authData from the payload and set to shared preference
                    String authID = authData.getUid();
                    D3SharedPreference.setUserId(context, authID);

                    // Get today's date
                    long currentTimeStamp = D3TimeUtil.getTimeStamp();
                    Map<String, String> currentTimeStampDict = new HashMap<String, String>();
                    currentTimeStampDict.put("timeCreated", String.valueOf(currentTimeStamp));

                    // Next, we need to set a node inside the Firebase space so we can keep track of data
                    D3Core.getFirebaseRef()
                            .child("/" + D3Get.SPACE_KEY + "/" + D3Constants.USER_URL)
                            .child(authID)
                            .setValue(currentTimeStampDict);

                    // Call callback
                    iGetUser.handleFirebaseResponse(new User(authID));
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {

                }
            });
        } else {
            // Call callback
            iGetUser.handleFirebaseResponse(new User(userId));
        }
    }

    /**
     * Get User Id
     *
     * @return
     */
    public static void getUserIdWithCompletion(Context context, final IGet iGetUser) {
        String userId = D3SharedPreference.getUserId(context);

        // Check to see if UserID is stored locally.
        if (userId != null) {
            iGetUser.handleFirebaseResponse(new User(userId));
        }
        else {
            registerAnonymousUser(context, iGetUser);
        }
    }
}
