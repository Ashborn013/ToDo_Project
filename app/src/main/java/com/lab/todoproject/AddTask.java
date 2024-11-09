package com.lab.todoproject;

import android.content.Intent;
import android.os.Bundle;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);

        etTaskName = findViewById(R.id.et_task_name);
        etTaskDesc = findViewById(R.id.et_task_desc);
        tvDeadline = findViewById(R.id.tv_deadline);
        btnPickDeadline = findViewById(R.id.btn_pick_deadline);
        btnSubmit = findViewById(R.id.btn_submit);

        databaseHelper = new DatabaseHelper(this);



        btnPickDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDateTime();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                finish();
            }
        });

    }
    private void pickDateTime() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                pickTime(calendar);
            }
        }, year, month, day);

        datePickerDialog.show();
    }
    private void pickTime(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                deadline = calendar.getTime().toString();
                tvDeadline.setText(deadline);
            }
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

        } else {
            Log.d("AddTask", "Error Adding Task"+taskName+taskDesc+deadline);
            Toast.makeText(this, "Error Adding Task", Toast.LENGTH_SHORT).show();
        }
    }
}
