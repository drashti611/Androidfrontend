package com.example.androidfrontend.Api;

import android.util.Base64;

import org.json.JSONObject;

public class JwtUtils {
    public static int extractStudentIdFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));
            JSONObject json = new JSONObject(payload);
            return json.getInt("StudentUserId");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
