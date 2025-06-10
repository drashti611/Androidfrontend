package com.example.androidfrontend;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfrontend.Api.ApiClient;
import com.example.androidfrontend.Api.AuthService;
import com.example.androidfrontend.Api.JwtUtils;
import com.example.androidfrontend.Model.LoginRequest;
import com.example.androidfrontend.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ImageView ivTogglePassword;
    private boolean isPasswordVisible = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        if (sharedPrefManager.getToken() != null) {
            startActivity(new Intent(this, Dashboard.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);

        ivTogglePassword.setOnClickListener(view -> {
            if (isPasswordVisible) {
                // Hide password
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                ivTogglePassword.setImageResource(R.drawable.icon_visibility_off);
                isPasswordVisible = false;
            } else {
                // Show password
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ivTogglePassword.setImageResource(R.drawable.icon_visibility);
                isPasswordVisible = true;
            }
            // Move cursor to the end of text
            etPassword.setSelection(etPassword.getText().length());
        });

        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                loginUser(email, password);
            } else {
                showAlert("Login Failed", "Please enter both email and password.", false);
            }
        });
    }

    private void loginUser(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AuthService apiService = ApiClient.getClient().create(AuthService.class);
        Call<LoginResponse> call = apiService.loginStudent(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null && response.body().getSuccess()) {
                    String token = response.body().getToken();
                    String studentName = response.body().getStudentName();
                    int studentId = JwtUtils.extractStudentIdFromToken(token);

                    if (studentId == -1) {
                        showAlert("Error", "Failed to parse Student ID.", false);
                        return;
                    }

                    SharedPrefManager sharedPrefManager = new SharedPrefManager(MainActivity.this);
                    sharedPrefManager.saveLoginData(token, studentId, studentName);

                    // Show success alert with navigation
//                    showAlert("Success", "Login Successful", true);
                    startActivity(new Intent(MainActivity.this, Dashboard.class));
                    finish();


                } else {
                    showAlert("Login Failed", "Invalid Email or Password.", false);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                showAlert("Network Error", "Error: " + t.getMessage(), false);
            }
        });
    }

    private void showAlert(String title, String message, boolean navigateToDashboard) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialogg, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        TextView dialogMessage = dialogView.findViewById(R.id.dialogMessage);
        Button okButton = dialogView.findViewById(R.id.okyButton);

        dialogTitle.setText(title);
        dialogMessage.setText(message);

        okButton.setOnClickListener(v -> {
            dialog.dismiss();
            if (navigateToDashboard) {
                startActivity(new Intent(MainActivity.this, Dashboard.class));
                finish();
            }
        });

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }
}
