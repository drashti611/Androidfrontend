package com.example.androidfrontend.Model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("roleId")
    private int roleId;

    @SerializedName("studentId")
    private int studentId;

    @SerializedName("success")
    private boolean success;

    @SerializedName("studentName")
    private String studentName;

    // Getters
    public String getToken() {
        return token;
    }

    public boolean getSuccess() {
        return success;
    }

    public int getRoleId() {
        return roleId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }
}
