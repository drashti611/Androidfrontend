package com.example.androidfrontend.Model;

import com.google.gson.annotations.SerializedName;

public class PaymentRequest {

    @SerializedName("studentId")
    private int studentId;

    @SerializedName("feeStructureId")
    private int feeStructureId;

    @SerializedName("paidAmount")
    private double paidAmount;

    @SerializedName("totalAmount")
    private double totalAmount;

    @SerializedName("transactionId")
    private String transactionId;

    // No-arg constructor (useful for serialization/deserialization)
    public PaymentRequest() {
    }

    // All-args constructor
    public PaymentRequest(int studentId, int feeStructureId, double paidAmount, double totalAmount, String transactionId) {
        this.studentId = studentId;
        this.feeStructureId = feeStructureId;
        this.paidAmount = paidAmount;
        this.totalAmount = totalAmount;
        this.transactionId = transactionId;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getFeeStructureId() {
        return feeStructureId;
    }

    public void setFeeStructureId(int feeStructureId) {
        this.feeStructureId = feeStructureId;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
