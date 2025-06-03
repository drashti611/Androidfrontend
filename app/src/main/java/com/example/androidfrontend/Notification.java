package com.example.androidfrontend;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidfrontend.Api.ApiClient;
import com.example.androidfrontend.Api.ApiService;
import com.example.androidfrontend.Model.NotificationModel;
import com.example.androidfrontend.Api.JwtUtils;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification extends AppCompatActivity {

    private TextView notificationTextView;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Enable back button in toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Notification");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        notificationTextView = findViewById(R.id.notificationTextView);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
         studentId = prefs.getInt("studentId", -1);

        Log.d("STUDENT_ID", "Fetched studentId: " + studentId);

        if (studentId == -1) {
            Toast.makeText(this, "Student ID not found. Please login.", Toast.LENGTH_LONG).show();
            return;
        }



        fetchNotifications(studentId);
    }

    private void fetchNotifications(int studentId) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<List<NotificationModel>> call = api.getNotifications(studentId);

        call.enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NotificationModel> notifications = response.body();
                    Log.d("NOTIFICATION_API", "Response: " + new Gson().toJson(response.body()));

                    if (!notifications.isEmpty()) {
                        showPopup(notifications);
                    } else {
                        notificationTextView.setText("No new notifications.");
                    }
                } else {
                    notificationTextView.setText("Failed to fetch notifications");
                }
            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                notificationTextView.setText("Error: " + t.getMessage());
            }
        });
    }

    private void showPopup(List<NotificationModel> notifications) {
        StringBuilder builder = new StringBuilder();
        for (NotificationModel n : notifications) {
            if (!n.isRead()) {
                builder.append("• ").append(n.getMessage()).append("\n\n");
            }
        }

        if (builder.length() == 0) {
            notificationTextView.setText("All notifications already read.");
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("📢 Important Notification")
                .setMessage(builder.toString().trim())
                .setCancelable(false)
                .setPositiveButton("Close", (dialog, which) -> markAllAsRead())
                .show();
    }

    private void markAllAsRead() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = api.markAllAsRead(studentId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                notificationTextView.setText("All notifications marked as read.");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                notificationTextView.setText("Failed to mark as read: " + t.getMessage());
            }
        });
    }
}
