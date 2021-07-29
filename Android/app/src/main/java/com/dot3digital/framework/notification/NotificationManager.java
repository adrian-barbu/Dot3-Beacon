package com.dot3digital.framework.notification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.dot3digital.R;
import com.dot3digital.ui.real.MainActivity;

import java.util.List;

/**
 * @description     Notification Manager
 *
 * @author          Stelian
 */
public class NotificationManager {

    // Variables
    private static NotificationManager instance;

    /**
     * Static Instance
     *
     * @return
     */
    public static NotificationManager getInstance() {
        if (instance == null)
            instance = new NotificationManager();

        return instance;
    }

    /**
     * Send Notification With ZoneKeys
     *
     * @param context  : Context (assume it is represent for MainActivity)
     * @param zoneKeys : zone keys with comma
     */
    public void sendBeaconDetectNotification(Context context, String zoneKeys, String message) {
        Intent intent = new Intent(context, MainActivity.class);

        // Add parameter to send discover tab
        intent.putExtra(MainActivity.PARAM_BEACON_DISCOVERED, true);
        intent.putExtra(MainActivity.PARAM_ZONE_KEYS, zoneKeys);

        //[2015.10.25][Stelian] If the app is running on foreground, then call activity directly
        //                      Otherwise, send notification so that user can tap notification and then go discover tab

        boolean isOpenDiscoverTab = ((MainActivity) context).isOpenDiscoverTab();

        if(isAppIsInBackground(context) || !isOpenDiscoverTab) {
            // Send notification
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(MainActivity.PARAM_REQUEST_FROM_NOTIFICATION, true);

            int icon = R.mipmap.kew_app_icon_itunes;
            int mNotificationId = 10203;    // Unique ID
            String title, content;
            title = context.getString(R.string.notification_discover_new_region_title);
            //content = context.getString(R.string.notification_discover_new_region_content);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            intent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );

            // The vibration pattern to use for the notification.
            long[] vibrate = {0,100,200,200,200,200};

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(resultPendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon))
                    .setContentText(message)
                    .setVibrate(vibrate)
                    .build();

            android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(mNotificationId, notification);
        }
    }

    /**
     * Open Discover Tab With ZoneKeys
     *
     * @param context  : Context (assume it is represent for MainActivity)
     * @param zoneKeys : zone keys with comma
     */
    public void openDiscoverTab(Context context, String zoneKeys) {
        Intent intent = new Intent(context, MainActivity.class);

        // Add parameter to send discover tab
        intent.putExtra(MainActivity.PARAM_BEACON_DISCOVERED, true);
        intent.putExtra(MainActivity.PARAM_ZONE_KEYS, zoneKeys);

        //[2015.10.25][Stelian] If the app is running on foreground, then call activity directly
        //                      Otherwise, send notification so that user can tap notification and then go discover tab

        // If the app is running on foreground, then once set zone key
        // So they can see entries on discover tab without click notification bar
        if (!isAppIsInBackground(context))
        {
            intent.putExtra(MainActivity.PARAM_REQUEST_FROM_NOTIFICATION, true /* isOpenDiscoverTab */);

            // Start discover tab directly
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * Method checks if the app is in background or not
     *
     * @param context
     * @return
     */
    public boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
