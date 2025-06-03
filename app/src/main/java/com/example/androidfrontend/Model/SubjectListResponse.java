package com.example.androidfrontend.Model;

import java.util.List;

public class SubjectListResponse {
    private boolean success;
    private String message;
    private List<Subject> subject;

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Subject> getSubject() { return subject; }
    public void setSubject(List<Subject> subject) { this.subject = subject; }
}
