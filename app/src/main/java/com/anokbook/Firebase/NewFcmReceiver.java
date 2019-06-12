package com.anokbook.Firebase;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.anokbook.Activites.HomeScreen;
import com.anokbook.Models.FCMData;
import com.anokbook.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

public class NewFcmReceiver extends FirebaseMessagingService {

    public static final String FCM_PARAM = "fcm_param";
    private static final String CHANNEL_NAME = "AnokBook";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
    private static final String TAG = "tag";

    Intent intentMain;
    private int numMessages = 0;
    private FCMData fcmData;

    public static boolean isAppIsInBackground(Context context) {
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

    @Override
    public void onCreate() {
    }

    @SuppressLint("NewApi")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Map<String, String> data = remoteMessage.getData();
            Log.d("FROM", remoteMessage.getFrom());

            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            wl.acquire(15000);
            sendNotification(notification, data);
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {

        fcmData = new FCMData();
        fcmData.setBody(data.get("body"));
        fcmData.setTitle(data.get("title"));
        fcmData.setType(data.get("type"));

        try {
            Log.e("NOTIFICATION BODY", data.get("body"));
            Log.e("NOTIFICATION TITLE", data.get("title"));
            Log.e("NOTIFICATION TYPE", data.get("type"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(fcmData.getBody());
        style.setBigContentTitle(fcmData.getTitle());

        Log.d("DataStore", fcmData.toString());

        if (isAppIsInBackground(this) == true) {

            Bundle bundle = new Bundle();
            bundle.putString(FCM_PARAM, data.get(FCM_PARAM));

            intentMain = new Intent(this, HomeScreen.class);
            intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);

            intentMain.putExtras(bundle);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentMain, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default")
                    .setContentTitle(fcmData.getTitle())
                    .setContentText(fcmData.getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setStyle(style)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setLights(Color.RED, 1000, 300)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setNumber(++numMessages)
                    .setSmallIcon(R.drawable.logo);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        "default", CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                );
                channel.setDescription(CHANNEL_DESC);
                channel.setShowBadge(true);
                channel.canShowBadge();
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


                if (notificationSoundUri != null) {
                    // Changing Default mode of notification_green
                    notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

                    // Creating an Audio Attribute
                    AudioAttributes audioAttributes = new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .build();

                    channel.setSound(notificationSoundUri, audioAttributes);
                }

                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);
            }
            assert notificationManager != null;
            notificationManager.notify(0, notificationBuilder.build());
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(FCM_PARAM, data.get(FCM_PARAM));


            intentMain = new Intent(this, HomeScreen.class);
            intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);

            intentMain.putExtras(bundle);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentMain, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default")
                    .setContentTitle(fcmData.getTitle())
                    .setContentText(fcmData.getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setStyle(style)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setLights(Color.RED, 1000, 300)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setNumber(++numMessages)
                    .setSmallIcon(R.drawable.logo);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        "default", CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                );
                channel.setDescription(CHANNEL_DESC);
                channel.setShowBadge(true);
                channel.canShowBadge();
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


                if (notificationSoundUri != null) {
                    // Changing Default mode of notification_green
                    notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

                    // Creating an Audio Attribute
                    AudioAttributes audioAttributes = new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .build();

                    channel.setSound(notificationSoundUri, audioAttributes);
                }

                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);
            }
            assert notificationManager != null;
            notificationManager.notify(0, notificationBuilder.build());

        }
    }

}
