package com.example.androidfrontend.Model;

import com.google.gson.annotations.SerializedName;

public class FeeType {

    @SerializedName("tuitionFees")
    private double tuitionFees;

    @SerializedName("labFees")
    private double labFees;

    @SerializedName("collegeGroundFee")
    private double collegeGroundFee;

    @SerializedName("internalExam")
    private double internalExam;

    // Getters and Setters

    public double getTuitionFees() {
        return tuitionFees;
    }

    public double getLabFees() {
        return labFees;
    }

    public double getCollegeGroundFee() {
        return collegeGroundFee;
    }

    public double getInternalExam() {
        return internalExam;
    }
}
