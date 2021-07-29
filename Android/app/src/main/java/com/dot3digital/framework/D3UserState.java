package com.dot3digital.framework;

import android.content.Context;

import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.PushTime;
import com.dot3digital.framework.model.User;
import com.dot3digital.framework.model.Zone;
import com.dot3digital.framework.util.D3SharedPreference;
import com.dot3digital.framework.util.D3TimeUtil;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @description     D3UserState Engine
 *
 * @modified        Stelian
 */
public class D3UserState
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
     * setTimeForZonePushMessage
     *
     * @param context
     * @param zoneKey
     */
    public static void setTimeForZonePushMessage(Context context, final String zoneKey) {
        D3Set.getUserIdWithCompletion(context, new D3Set.IGet() {

            @Override
            public void handleFirebaseResponse(BaseModel data) {
                if (data != null) {
                    User user = (User) data;
                    String userId = user.getUserID();
                    if (userId != null) {
                        String fbUrl = String.format("/%s/%s/%s",
                                D3Get.SPACE_KEY, D3Constants.ZONE_PUSH_MESSAGE_TIME, userId);

                        // Get Time Stamp
                        long currentTimeStamp = D3TimeUtil.getTimeStamp();
                        Map<String, Object> pushTimeDict = new HashMap<String, Object>();
                        pushTimeDict.put("zoneKey", zoneKey);
                        pushTimeDict.put("timePushed", String.valueOf(currentTimeStamp));

                        // Update value
                        D3Core.getFirebaseRef()
                                .child(fbUrl)
                                .child(zoneKey) // Append child
                                .updateChildren(pushTimeDict);
                    }
                }
            }
        });
    }


    /**
     * getTimeForZonePushMessage
     *
     * @param context
     * @param zoneKey
     */
    public static void getTimeForZonePushMessage(Context context, final String zoneKey, final IGet iGetPushTime) {
        D3Set.getUserIdWithCompletion(context, new D3Set.IGet() {

            @Override
            public void handleFirebaseResponse(BaseModel data) {
                if (data != null) {
                    User user = (User) data;
                    String userId = user.getUserID();
                    if (userId != null) {
                        // D3Core.increaseCounter();

                        String fbUrl = String.format("/%s/%s/%s/%s",
                                D3Get.SPACE_KEY, D3Constants.ZONE_PUSH_MESSAGE_TIME, userId, zoneKey);

                        // Get Push Time Value
                        D3Core.getFirebaseRef()
                                .child(fbUrl)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    PushTime pushTime = new PushTime(dataSnapshot.getKey());
                                    if (dataSnapshot.getValue() != null)
                                        pushTime.parseSnapshot(dataSnapshot);

                                    iGetPushTime.handleFirebaseResponse(pushTime);
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    System.out.println("The read failed: " + firebaseError.getMessage());
                                }
                            });
                    }
                }
            }
        });
    }

}
