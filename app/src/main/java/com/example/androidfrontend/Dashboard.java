package com.example.androidfrontend;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private GridLayout mainGrid;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle drawer item clicks here
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, profile.class));
        } else if (id == R.id.nav_subjects) {
            startActivity(new Intent(this, subjects.class));
        } else if (id == R.id.nav_fees) {
            startActivity(new Intent(this, FeePaymentActivity.class));
        } else if (id == R.id.nav_teachers) {
            startActivity(new Intent(this, Teachers.class));
        } else if (id == R.id.nav_notifications) {
            startActivity(new Intent(this, Notification.class));
        } else if (id == R.id.nav_material) {
            startActivity(new Intent(this, content.class));
        } else if (id == R.id.nav_logout) {
            showLogoutConfirmation();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
