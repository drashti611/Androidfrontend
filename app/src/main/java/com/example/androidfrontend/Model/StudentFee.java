package com.example.androidfrontend.Model;

import com.google.gson.annotations.SerializedName;
public class StudentFee {
    @SerializedName("feeId")
    private int feeId;

    @SerializedName("studentId")
    private int studentId;

    @SerializedName("feeStructureId")
    private int feeStructureId;

    @SerializedName("totalAmount")
    private double totalAmount;

    @SerializedName("paidAmount")
    private double paidAmount;

    @SerializedName("status")
    private String status;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("paymentDate")
    private String paymentDate;

    @SerializedName("departmentName")
    private String departmentName;

    @SerializedName("semesterName")
    private String semesterName;

    @SerializedName("student_Name")
    private String studentName;

    @SerializedName("feeType")
    private FeeType feeType;

    // Getters and Setters

    public int getFeeId() {
        return feeId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getFeeStructureId() {
        return feeStructureId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public String getStudentName() {
        return studentName;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public double getAmount() {
        return totalAmount; // for convenience
    }
}
