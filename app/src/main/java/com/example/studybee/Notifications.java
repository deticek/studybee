/*
 * Author: [Aleks Detiček]
 * Project: StudyBee
 * Year: 2026
 *
 * This source code is proprietary and confidential.
 * Unauthorized copying, distribution or use is strictly prohibited.
 */
package com.example.studybee;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class Notifications {

    public static void pushNotification(Context context, String title, String text, Class<?> openActivity) {
        String channelId = "focus_channel";

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Focus Mode",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Focus mode notifications");
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, openActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0)
        );

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setColor(0xFFFFC107)
                .setContentIntent(pendingIntent)
                .build();

        manager.notify(1, notification);
    }
}