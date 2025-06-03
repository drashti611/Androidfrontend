package com.example.androidfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfrontend.MainActivity;

public class activity_splashscreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(activity_splashscreen.this, MainActivity.class));
            finish(); // Prevent returning to splash screen
        }, 2000); // Show for 2 seconds
    }
}
