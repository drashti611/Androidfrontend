package com.example.androidfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class LoginSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        // Delay 2.5 seconds then go to Dashboard
        new Handler().postDelayed(() -> {
            startActivity(new Intent(LoginSuccessActivity.this, Dashboard.class));
            finish();
        }, 2500);
    }
}
