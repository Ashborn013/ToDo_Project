package com.lab.todoproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryTask extends AppCompatActivity {

    private ListView lvCompletedTasks;
    private TaskAdapter taskAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<Task> completedTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_task);

        // Initialize views and database helper
        lvCompletedTasks = findViewById(R.id.lv_completed_tasks);
        databaseHelper = new DatabaseHelper(this);

        // Load completed tasks
        loadCompletedTasks();

        // Set item click listener to view task details or edit
        lvCompletedTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = completedTaskList.get(position);
                // Handle item click, you can show task details or edit
                Toast.makeText(getApplicationContext(), "Task clicked: " + task.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCompletedTasks() {
        // Query the database for completed tasks
        completedTaskList = databaseHelper.getCompletedTasks();

        // Create adapter and set it to ListView
        taskAdapter = new TaskAdapter(this, completedTaskList, databaseHelper);
        lvCompletedTasks.setAdapter(taskAdapter);
    }
}
