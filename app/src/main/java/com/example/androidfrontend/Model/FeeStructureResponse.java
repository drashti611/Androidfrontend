package com.example.androidfrontend.Model;

import com.google.gson.annotations.SerializedName;

public class FeeStructureResponse {
    @SerializedName("feeStructureId")
    private int feeStructureId;

    @SerializedName("departmentName")
    private String departmentName;

    @SerializedName("semesterName")
    private String semesterName;

    @SerializedName("defaultAmount")
    private double defaultAmount;

    // Getters and setters
    public int getFeeStructureId() { return feeStructureId; }
    public String getDepartmentName() { return departmentName; }
    public String getSemesterName() { return semesterName; }
    public double getDefaultAmount() { return defaultAmount; }
}
