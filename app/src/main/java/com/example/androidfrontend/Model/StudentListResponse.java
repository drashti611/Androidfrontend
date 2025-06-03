package com.example.androidfrontend.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentListResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("student")
    private List<StudentResponse> student;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<StudentResponse> getStudent() {
        return student;
    }
}
