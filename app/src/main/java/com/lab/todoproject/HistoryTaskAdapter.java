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
public class HistoryTaskAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Task> completedTasks;
    private DatabaseHelper databaseHelper;

    public HistoryTaskAdapter(Context context, ArrayList<Task> completedTasks, DatabaseHelper databaseHelper) {
        this.context = context;
        this.completedTasks = completedTasks;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public int getCount() {
        return completedTasks.size();
    }

    @Override
    public Object getItem(int position) {
        return completedTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return completedTasks.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_item_history, parent, false);
        }

        TextView tvTaskName = convertView.findViewById(R.id.tv_task_name);
        TextView tvTaskDescription = convertView.findViewById(R.id.tv_task_description);
        TextView tvTaskDeadline = convertView.findViewById(R.id.tv_task_deadline);

        Task task = completedTasks.get(position);
        tvTaskName.setText(task.getName());
        tvTaskDescription.setText(task.getDescription());
        tvTaskDeadline.setText(task.getDeadline());

        return convertView;
    }
}
