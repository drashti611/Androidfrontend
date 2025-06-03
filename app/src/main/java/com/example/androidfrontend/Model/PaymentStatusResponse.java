package com.example.androidfrontend.Model;

import com.google.gson.annotations.SerializedName;

public class PaymentStatusResponse {
    @SerializedName("isPaid")
    private boolean isPaid;

    public boolean isPaid() {
        return isPaid;
    }
}
