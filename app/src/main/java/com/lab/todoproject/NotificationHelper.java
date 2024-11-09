package com.lab.todoproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "task_channel";
    private static final String CHANNEL_NAME = "Task Reminders";
    private static final int NOTIFICATION_ID = 1;

    public static void createNotification(Context context, String taskName, String taskDescription) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create Notification Channel (needed for Android 8.0 and above)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.plus) // Make sure you have a valid icon
                .setContentTitle(taskName)
                .setContentText(taskDescription)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
