package com.louiseeo.model;

public class Task {
    private int userId;
    private int id;
    private String title;
    private boolean complete;

    public Task(){}
    public Task(int userId, int id, String title, boolean complete) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.complete = complete;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public boolean isComplete() {
        return complete;
    }
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override 
    public String toString(){
        return String.format("Task #%d: %s [Status: %s], submitted by use #%d", 
        id, title, complete? "Completed": "To do", userId);
    }
}
