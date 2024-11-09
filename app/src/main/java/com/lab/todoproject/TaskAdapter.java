package com.lab.todoproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class TaskAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Task> tasks;
    private DatabaseHelper databaseHelper;

    public TaskAdapter(Context context, ArrayList<Task> tasks, DatabaseHelper databaseHelper) {
        this.context = context;
        this.tasks = tasks;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        }

        TextView tvTaskName = convertView.findViewById(R.id.tv_task_name);
        TextView tvTaskDeadline = convertView.findViewById(R.id.tv_task_deadline);
        Button btnComplete = convertView.findViewById(R.id.btn_mark_complete);
        Button btnEdit = convertView.findViewById(R.id.btn_edit);
        Button btnDelete = convertView.findViewById(R.id.btn_delete);

        final Task task = tasks.get(position);
        tvTaskName.setText(task.getName());
        tvTaskDeadline.setText(task.getDeadline());

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setCompleted(true);
                databaseHelper.updateTask(task);
                tasks.remove(position);
                notifyDataSetChanged();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, EditTaskActivity.class);
//                intent.putExtra("TASK_ID", task.getId());
//                context.startActivity(intent);
//
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteTask(task.getId());
                tasks.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
