package com.example.androidfrontend;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {
    private GridLayout mainGrid;
    private CardView logoutCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        String studentName = sharedPrefManager.getStudentName();

        if (getSupportActionBar() != null) {
            if (studentName != null && !studentName.isEmpty()) {
                getSupportActionBar().setTitle("Welcome, " + studentName);
            } else {
                getSupportActionBar().setTitle("Welcome");
            }
        }

        mainGrid = findViewById(R.id.mainGrid);
//        logoutCard = findViewById(R.id.card_logout); // Make sure this is defined in XML layout

        // Grid card click listeners
        CardView profile = (CardView) mainGrid.getChildAt(0);
        CardView subjects = (CardView) mainGrid.getChildAt(1);
        CardView feesCard = (CardView) mainGrid.getChildAt(2);
        CardView teachers = (CardView) mainGrid.getChildAt(3);
        CardView notifications = (CardView) mainGrid.getChildAt(4);
        CardView material = (CardView) mainGrid.getChildAt(5);

        if (profile != null) profile.setOnClickListener(v -> startActivity(new Intent(this, profile.class)));
        if (subjects != null) subjects.setOnClickListener(v -> startActivity(new Intent(this, subjects.class)));
        if (feesCard != null) feesCard.setOnClickListener(v -> startActivity(new Intent(this, FeePaymentActivity.class)));
        if (teachers != null) teachers.setOnClickListener(v -> startActivity(new Intent(this, Teachers.class)));
        if (notifications != null) notifications.setOnClickListener(v -> startActivity(new Intent(this, Notification.class)));
        if (material != null) material.setOnClickListener(v -> startActivity(new Intent(this, content.class)));

        if (logoutCard != null) {
            logoutCard.setOnClickListener(view -> showLogoutConfirmation());
        }
    }

    private void showLogoutConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        TextView title = dialogView.findViewById(R.id.dialogTitle);
        TextView message = dialogView.findViewById(R.id.dialogMessage);
        Button okButton = dialogView.findViewById(R.id.okButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        title.setText("Logout Confirmation");
        message.setText("Are you sure you want to logout?");

        okButton.setOnClickListener(v -> {
            dialog.dismiss();
            logoutUser();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void logoutUser() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            showLogoutConfirmation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
