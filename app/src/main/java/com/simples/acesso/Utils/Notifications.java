package com.simples.acesso.Utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.simples.acesso.R;
import com.simples.acesso.Views.Login;

public class Notifications {

    public static void create (Activity activity, int id_notifi,  String title, String message, int icon){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(activity)
                        .setSmallIcon(icon)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(message);
        Intent resultIntent = new Intent(activity, Login.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);
        stackBuilder.addParentStack(Login.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id_notifi, mBuilder.build());
    }
}
