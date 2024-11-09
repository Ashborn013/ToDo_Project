package com.lab.todoproject;
public class Task {
    private int id;
    private String name;
    private String description;
    private String deadline;
    private boolean completed;

    public Task(int id, String name, String description, String deadline, boolean completed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

