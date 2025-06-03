package com.example.androidfrontend;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_STUDENT_ID = "studentId";
    private static final String KEY_STUDENT_NAME = "studentName"; // ✅ New key

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // ✅ Save token, ID, and name
    public void saveLoginData(String token, int studentId, String studentName) {
        editor.putString(KEY_TOKEN, token);
        editor.putInt(KEY_STUDENT_ID, studentId);
        editor.putString(KEY_STUDENT_NAME, studentName);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public int getStudentId() {
        return sharedPreferences.getInt(KEY_STUDENT_ID, -1);
    }

    public String getStudentName() {
        return sharedPreferences.getString(KEY_STUDENT_NAME, null);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}
