package com.example.androidfrontend.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class contentlistResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("content")
    private List<Content> content;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<Content> getContent() { return content; }
}
