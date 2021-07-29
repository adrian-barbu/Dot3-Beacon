package com.dot3digital.framework.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.dot3digital.R;

/**
 * Created by arkady on 20/08/15.
 */
public class Utilities
{

    static public void createAndSendNotification(Intent notificationIntent,
                                                 Class<?> sourceActivityClass, // The source Activity class, all parents of this activity will be added
                                                 int requestCode,
                                                 ContextWrapper contextWrapper,
                                                 int notifId, // An identifier for this notification, must be unique within the application.
                                                 String contentTitle,
                                                 String contentText)
    {
        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create( contextWrapper );

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack( sourceActivityClass );

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder( contextWrapper );

        // The vibration pattern to use for the notification.
        long[] vibrate = {0,100,200,200,200,200}; 

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.kew_app_icon_itunes)
                // In a real app, you may want to use a library like Volley to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(contextWrapper.getResources(),
                        R.mipmap.kew_app_icon_itunes))
                .setColor(Color.RED)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) 
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS) 
                .setVibrate(vibrate) 
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) contextWrapper.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(notifId, builder.build()); // SecurityException: Requires VIBRATE permission - I faced it on HUAWEY device with Android Jelly Bean 4.1.2
    }

    /*
    // http://stackoverflow.com/questions/1198558/how-to-send-parameters-from-a-notification-click-to-an-activity
    private void sendNotificationWithIntentExtras1(String notificationDetails) {
        Intent notificationIntent = new Intent(getApplicationContext(),
                PlaceEntriesListActivity.class);
        String notificationTitle = notificationDetails; //AK
        String notificationMessage = getString(R.string.geofence_transition_notification_text); //AK

        notificationIntent.putExtra("PlacesListActivity_key", key); //AK
        notificationIntent.putExtra("PlacesListActivity_placeImage", placeImage); //AK
        notificationIntent.putExtra("PlacesListActivity_placeText", placeText); //AK
        notificationIntent.putExtra("PlacesListActivity_placeName", placeName); //AK
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int requestCode = 0;                    //AK
        int notificationIndex = requestCode;    //AK
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(getApplicationContext(), notificationIndex, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //AK
        Notification notification = new Notification(R.mipmap.kew_app_icon_itunes, //AK
                "Message received", System.currentTimeMillis());
        //AK
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(getApplicationContext(), notificationTitle, notificationMessage, pendingNotificationIntent);
        //AK
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
        //AK
    }
    // http://stackoverflow.com/questions/9208547/android-status-bar-notifications-intent-getting-the-old-extras-on-the-second-t
    // >>> http://pilhuhn.blogspot.ca/2010/12/pitfall-in-pendingintent-with-solution.html
    private void sendNotificationWithIntentExtras2(String notificationDetails) {
        Context context = getApplicationContext(); //AK
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.mipmap.kew_app_icon_itunes, //AK
                "Message received", System.currentTimeMillis());
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        Intent intent = new Intent(context,
                PlaceEntriesListActivity.class); //AK
        //AK//intent.setAction("android.intent.action.MAIN");
        //AK//intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("msg_id", msg_id);
        intent.putExtra("title", title);

        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        notification.setLatestEventInfo(context, "New message", title + String.valueOf(msg_id), pendingIntent);
        notificationManager.notify(0, notification);
    }
    // http://stackoverflow.com/questions/7370324/notification-passes-old-intent-extras
    private void sendNotificationWithIntentExtras3(String notificationDetails) {
        Context context = getApplicationContext(); //AK
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
        int icon = R.mipmap.kew_app_icon_itunes; //R.drawable.ic_stat_notification;     //AK
        CharSequence tickerText = "New Notification";
        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, tickerText, when);
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        long[] vibrate = {0,100,200,200,200,200};
        notification.vibrate = vibrate;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        CharSequence contentTitle = "Title";
        CharSequence contentText = "Text";
        Intent notificationIntent = new Intent(context,
                PlaceEntriesListActivity.class); //AK
        notificationIntent.putExtra(Settings.Global.INTENT_EXTRA_FOO_ID, foo_id);

        //PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        PendingIntent contentIntent = PendingIntent.getActivity(context, UNIQUE_INT_PER_CALL, notificationIntent, 0);

        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

        int mynotification_id = 1;

        mNotificationManager.notify(mynotification_id, notification);
    }
    */
    
}
