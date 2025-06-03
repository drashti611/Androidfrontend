package com.example.androidfrontend.Model;

import java.util.List;

public class facultylistresponse {
    private boolean success;
    private String message;
    private List<Faculty> faculty;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<Faculty> getFaculty() { return faculty; }
}
