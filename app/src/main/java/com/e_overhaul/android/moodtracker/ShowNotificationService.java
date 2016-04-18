package com.e_overhaul.android.moodtracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class ShowNotificationService extends Service {
    private final String LOG_TAG = ShowNotificationService.class.getName();

    public ShowNotificationService () {
    }

    @Override
    public IBinder onBind (Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent mainIntent = new Intent(this, MainActivity.class);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this)
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT))
            .setContentTitle("How are you feeling?")
            .setContentText("Record how you are feeling now")
            .setSmallIcon(R.drawable.fart)
            .setTicker("Ticker Message")
            .setWhen(System.currentTimeMillis())
            .build();
        notificationManager.notify(0, notification);
    }
}
