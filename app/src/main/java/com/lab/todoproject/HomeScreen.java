package com.lab.todoproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeScreen extends AppCompatActivity {
    private ListView lvTasks;
    private TaskAdapter taskAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        lvTasks = findViewById(R.id.lv_tasks);
        databaseHelper = new DatabaseHelper(this);
        loadTasks();

        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = taskList.get(position);
                markTaskAsComplete(task);
            }
        });



        FloatingActionButton fltb = findViewById(R.id.floatingActionButton2);
        fltb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddTask.class));
            }
        });

    }
    private void loadTasks() {
        try {
            // Fetch remaining tasks from the database
            taskList = databaseHelper.getRemainingTasks();

            if (taskList == null) {
                taskList = new ArrayList<>(); // Ensure taskList is not null
            }

            // Initialize adapter and set it to ListView
            taskAdapter = new TaskAdapter(this, taskList, databaseHelper);
            lvTasks.setAdapter(taskAdapter);

            // Check if tasks loaded correctly
            if (taskList.isEmpty()) {
                Toast.makeText(this, "No remaining tasks to display", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Catch any exceptions and log for debugging
            e.printStackTrace();
            Toast.makeText(this, "Failed to load tasks: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void markTaskAsComplete(Task task) {
        task.setCompleted(true);
        boolean result = databaseHelper.updateTask(task);
        if (result) {
            Toast.makeText(this, "Task marked as complete", Toast.LENGTH_SHORT).show();
            loadTasks();
        } else {
            Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show();
        }
    }
}