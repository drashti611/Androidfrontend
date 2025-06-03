package com.example.androidfrontend.Model;

import com.google.gson.annotations.SerializedName;

public class StudentResponse {
    @SerializedName("studentId")
    private int studentId;

    @SerializedName("studentName")
    private String studentName;

    @SerializedName("email")
    private String email;

    @SerializedName("dob")
    private String dob;

    @SerializedName("gender")
    private String gender;

    @SerializedName("address")
    private String address;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("phone")
    private String phone;



    @SerializedName("currentSemester")
    private int currentSemester;

    @SerializedName("groupId")
    private int groupId;

    @SerializedName("studentImg")
    private String studentImg;

    @SerializedName("tenthSchool")
    private String tenthSchool;

    @SerializedName("tenthPassingYear")
    private int tenthPassingYear;

    @SerializedName("tenthPercentage")
    private int tenthPercentage;

    @SerializedName("twelfthSchool")
    private String twelfthSchool;

    @SerializedName("twelfthPassingYear")
    private int twelfthPassingYear;

    @SerializedName("twelfthPercentage")
    private int twelfthPercentage;

    @SerializedName("depname")
    private String depname;

    // Getters
    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }
    public String getdepname() {
        return depname;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPhone() {
        return phone;
    }


    public int getCurrentSemester() {
        return currentSemester;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getStudentImg() {
        return studentImg;
    }

    public String getTenthSchool() {
        return tenthSchool;
    }

    public int getTenthPassingYear() {
        return tenthPassingYear;
    }

    public int getTenthPercentage() {
        return tenthPercentage;
    }


    public String getTwelfthSchool() {
        return twelfthSchool;
    }

    public int getTewelfthPassingYear() {
        return twelfthPassingYear;
    }

    public int getTewelfthPercentage() {
        return twelfthPercentage;
    }


}
