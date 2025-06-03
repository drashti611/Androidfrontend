package com.example.androidfrontend.Model;

public class NotificationModel {
    private int id;
    private int studentId;
    private String message;
    private String sentDate;
    private boolean isRead;

    // Getters (Optional setters if needed)
    public int getId() { return id; }
    public int getStudentId() { return studentId; }
    public String getMessage() { return message; }
    public String getSentDate() { return sentDate; }
    public boolean isRead() { return isRead; }
}
