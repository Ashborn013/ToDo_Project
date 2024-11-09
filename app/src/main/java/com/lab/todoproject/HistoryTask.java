package com.lab.todoproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
public class HistoryTask extends AppCompatActivity {

    private ListView lvCompletedTasks;
    private DatabaseHelper databaseHelper;
    private HistoryTaskAdapter historyTaskAdapter;
    private ArrayList<Task> completedTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_task);

        lvCompletedTasks = findViewById(R.id.lv_completed_tasks);
        databaseHelper = new DatabaseHelper(this);

        // Fetch completed tasks from the database
        completedTasks = databaseHelper.getCompletedTasks();

        // Set the adapter
        historyTaskAdapter = new HistoryTaskAdapter(this, completedTasks, databaseHelper);
        lvCompletedTasks.setAdapter(historyTaskAdapter);
        FloatingActionButton flt = findViewById(R.id.floatingActionButton);
        flt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddTask.class));
            }
        });

    }
}
