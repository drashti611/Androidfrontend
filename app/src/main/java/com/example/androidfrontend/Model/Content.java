package com.example.androidfrontend.Model;

import com.google.gson.annotations.SerializedName;

public class Content {
    private int contentId;

    @SerializedName("subjectId")
    private int subjectId;

    @SerializedName("title")
    private String title;

    @SerializedName("subjectName")
    private String subjectName;

    @SerializedName("filePath")
    private String filePath;

    public int getContentId() { return contentId; }
    public int getSubjectId() { return subjectId; }
    public String getTitle() { return title; }
    public String getsubjectName() { return subjectName; }
    public String getFilePath() { return filePath; }
}
