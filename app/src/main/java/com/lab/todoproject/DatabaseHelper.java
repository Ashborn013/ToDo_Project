package com.lab.todoproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_DEADLINE = "deadline";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TASKS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_DESC + " TEXT, "
                + COLUMN_DEADLINE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public boolean insertTask(String name, String description, String deadline) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESC, description);
        values.put(COLUMN_DEADLINE, deadline);

        long result = db.insert(TABLE_TASKS, null, values);
        db.close();
        return result != -1;
    }
    public ArrayList<Task> getRemainingTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE completed = 0", null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getInt(0), // id
                        cursor.getString(1), // name
                        cursor.getString(2), // description
                        cursor.getString(3), // deadline
                        cursor.getInt(4) == 1 // completed
                );
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }
    public boolean updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("completed", task.isCompleted() ? 1 : 0);

        return db.update("tasks", values, "id = ?", new String[]{String.valueOf(task.getId())}) > 0;
    }
    public boolean deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("tasks", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

}
