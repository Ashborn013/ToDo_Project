package com.lab.todoproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "iateYou.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_DEADLINE = "deadline";
    private static final String COLUMN_COMPLETED = "completed";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TASKS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_DESC + " TEXT, "
                + COLUMN_DEADLINE + " TEXT, "
                + COLUMN_COMPLETED + " INTEGER DEFAULT 0)";
        db.execSQL(createTable);
        // Optional: Insert some sample data
//        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // Sample 1
        values.put(COLUMN_NAME, "Sample Task 1");
        values.put(COLUMN_DESC, "This is the description for Task 1");
        values.put(COLUMN_DEADLINE, "2024-11-10 08:00");
        values.put(COLUMN_COMPLETED, 0);  // Task is not completed
        db.insert(TABLE_TASKS, null, values);

        // Sample 2
        values.put(COLUMN_NAME, "Sample Task 2");
        values.put(COLUMN_DESC, "This is the description for Task 2");
        values.put(COLUMN_DEADLINE, "2024-11-12 10:30");
        values.put(COLUMN_COMPLETED, 0);  // Task is not completed
        db.insert(TABLE_TASKS, null, values);

        // Sample 3
        values.put(COLUMN_NAME, "Sample Task 3");
        values.put(COLUMN_DESC, "This is the description for Task 3");
        values.put(COLUMN_DEADLINE, "2024-11-15 15:00");
        values.put(COLUMN_COMPLETED, 0);  // Task is not completed
        db.insert(TABLE_TASKS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Insert Task
    public boolean insertTask(String name, String description, String deadline) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Log the existing data before inserting
        logDatabaseContent(db);

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESC, description);
        values.put(COLUMN_DEADLINE, deadline);
        values.put(COLUMN_COMPLETED, 0);  // Default as not completed

        boolean isInserted = false;
        try {
            long result = db.insert(TABLE_TASKS, null, values);
            isInserted = result != -1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return isInserted;
    }

    // Method to log current content of the database
    private void logDatabaseContent(SQLiteDatabase db) {
        Log.d("DatabaseContent", "Logging current database content");
        Cursor cursor = null;
        try {
            // Query all tasks to see what's in the database
            cursor = db.rawQuery("SELECT * FROM " + TABLE_TASKS, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC));
                    String deadline = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE));
                    int completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED));

                    // Log the task details
                    Log.d("DatabaseContent", "ID: " + id + ", Name: " + name + ", Desc: " + description + ", Deadline: " + deadline + ", Completed: " + completed);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Get Remaining Tasks
    public ArrayList<Task> getRemainingTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_COMPLETED + " = 0", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Task task = new Task(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1
                    );
                    tasks.add(task);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return tasks;
    }

    // Get Task by ID (for editing)
    public Task getTaskById(int taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Task task = null;

        try {
            cursor = db.query(TABLE_TASKS, null, COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                task = new Task(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return task;
    }
    public ArrayList<Task> getCompletedTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query for tasks where completed = 1 (i.e., completed tasks)
            cursor = db.rawQuery("SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_COMPLETED + " = 1", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Task task = new Task(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1
                    );
                    tasks.add(task);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

    public boolean editTask(int taskId, String name, String description, String deadline, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Set the new values for the task
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESC, description);
        values.put(COLUMN_DEADLINE, deadline);
        values.put(COLUMN_COMPLETED, isCompleted ? 1 : 0);  // If completed, set to 1, else set to 0

        boolean isUpdated = false;
        try {
            // Update the task where the task ID matches
            isUpdated = db.update(TABLE_TASKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)}) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return isUpdated;
    }

    // Update Task (mark as completed)
    public boolean updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);  // Set to 1 if completed, 0 if not

        boolean isUpdated = false;
        try {
            isUpdated = db.update(TABLE_TASKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(task.getId())}) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return isUpdated;
    }

    // Delete Task
    public boolean deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isDeleted = false;
        try {
            isDeleted = db.delete(TABLE_TASKS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return isDeleted;
    }

    // Check if Database Exists
    public boolean isDatabaseCreated(Context context) {
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            return db != null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return false;
    }
}
