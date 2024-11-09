package com.lab.todoproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddTask extends AppCompatActivity {
    private EditText etTaskName, etTaskDesc;
    private TextView tvDeadline;
    private Button btnPickDeadline, btnSubmit;
    private String deadline;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTaskName = findViewById(R.id.et_task_name);
        etTaskDesc = findViewById(R.id.et_task_desc);
        tvDeadline = findViewById(R.id.tv_deadline);
        btnPickDeadline = findViewById(R.id.btn_pick_deadline);
        btnSubmit = findViewById(R.id.btn_submit);

        databaseHelper = new DatabaseHelper(this);

        btnPickDeadline.setOnClickListener(v -> pickDateTime());

        btnSubmit.setOnClickListener(v -> saveTask());
    }

    private void pickDateTime() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            calendar.set(year1, month1, dayOfMonth);
            pickTime(calendar);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void pickTime(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute1);
            deadline = calendar.getTime().toString();
            tvDeadline.setText(deadline);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void saveTask() {
        String taskName = etTaskName.getText().toString().trim();
        String taskDesc = etTaskDesc.getText().toString().trim();

        if (taskName.isEmpty() || deadline == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean inserted = databaseHelper.insertTask(taskName, taskDesc, deadline);
        if (inserted) {
            Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();

            // Schedule the notification
            scheduleTaskNotification(taskName, taskDesc, deadline);

            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
            finish();
        } else {
            Toast.makeText(this, "Error Adding Task", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleTaskNotification(String taskName, String taskDesc, String deadline) {
        // Convert deadline to a Calendar object (Assuming it's in a valid format)
        Calendar calendar = Calendar.getInstance();
        // (Parse the deadline into Calendar if needed)

        // Set the time to 1 minute before the deadline
        calendar.add(Calendar.MINUTE, -1); // 1 minute before deadline

        // Schedule the notification using AlarmManager
        long triggerAtMillis = calendar.getTimeInMillis();

        Intent intent = new Intent(this, TaskNotificationReceiver.class);
        intent.putExtra("task_name", taskName);
        intent.putExtra("task_description", taskDesc);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }
}
