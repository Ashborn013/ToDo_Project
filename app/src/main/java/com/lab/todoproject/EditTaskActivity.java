package com.lab.todoproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {
    private EditText etTaskName, etTaskDesc;
    private TextView tvTaskDeadline;
    private Button btnEditDeadline, btnSaveTask;
    private String taskDeadline;
    private DatabaseHelper databaseHelper;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        etTaskName = findViewById(R.id.et_task_name);
        etTaskDesc = findViewById(R.id.et_task_desc);
        tvTaskDeadline = findViewById(R.id.tv_task_deadline);
        btnEditDeadline = findViewById(R.id.btn_edit_deadline);
        btnSaveTask = findViewById(R.id.btn_save_task);

        databaseHelper = new DatabaseHelper(this);

        // Get the task ID passed from the previous activity
        Intent intent = getIntent();
        taskId = intent.getIntExtra("TASK_ID", -1);

        // Load the task details from the database
        loadTaskDetails();

        btnEditDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDateTime();
            }
        });

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUpdatedTask();
                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                finish();
            }
        });
    }

    private void loadTaskDetails() {
        Task task = databaseHelper.getTaskById(taskId);
        if (task != null) {
            etTaskName.setText(task.getName());
            etTaskDesc.setText(task.getDescription());
            tvTaskDeadline.setText(task.getDeadline());
            taskDeadline = task.getDeadline();
        } else {
            Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show();
        }
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
            taskDeadline = calendar.getTime().toString();
            tvTaskDeadline.setText(taskDeadline);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void saveUpdatedTask() {
        String taskName = etTaskName.getText().toString().trim();
        String taskDesc = etTaskDesc.getText().toString().trim();

        if (taskName.isEmpty() || taskDeadline == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updated = databaseHelper.editTask(taskId,taskName, taskDesc, taskDeadline,false);
        if (updated) {
            Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error Updating Task", Toast.LENGTH_SHORT).show();
        }
    }
}
