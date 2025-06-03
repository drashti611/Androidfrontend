package com.example.androidfrontend.Model;

public class FeeDetailsResponse {
    private String departmentName;
    private String semesterName;
    private double defaultAmount;
    private int feeStructureId;

    // Getters and setters
    public int getFeeStructureId() {
        return feeStructureId;
    }
    public void setFeeStructureId(int feeStructureId) {
        this.feeStructureId = feeStructureId;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    public String getSemesterName() {
        return semesterName;
    }
    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }
    public double getAmount() {
        return defaultAmount;
    }
    public void setAmount(double amount) {
        this.defaultAmount = amount;
    }
}
