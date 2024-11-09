package com.lab.todoproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TaskNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve the task details from the Intent
        String taskName = intent.getStringExtra("task_name");
        String taskDescription = intent.getStringExtra("task_description");

        // If necessary, provide a fallback message if task details are missing
        if (taskName == null || taskDescription == null) {
            taskName = "Task Reminder";
            taskDescription = "You have a task due soon!";
        }

        // Show the notification using NotificationHelper
        NotificationHelper.createNotification(context, taskName, taskDescription);
    }
}
