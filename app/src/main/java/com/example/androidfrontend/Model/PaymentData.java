package com.example.androidfrontend.Model;

public class PaymentData {
    private int studentId;
    private String paymentId;
    private double amount;

    public PaymentData(int studentId, String paymentId, double amount) {
        this.studentId = studentId;
        this.paymentId = paymentId;
        this.amount = amount;
    }
}
