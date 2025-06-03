package com.example.androidfrontend;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.androidfrontend.Api.ApiClient;
import com.example.androidfrontend.Api.ApiService;
import com.example.androidfrontend.Model.StudentListResponse;
import com.example.androidfrontend.Model.StudentResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profile extends AppCompatActivity {

    // UI components
    private TextView studentName, studentDOB, studentGender, studentEmail, studentPhone,
            studentAddress, studentCityState, studentDepartment, studentCurrentSemester,
            studentGroupId, studentTenthSchool, studentTenthYear, studentTenthPercentage,
            studentTwelfthSchool, studentTwelfthYear, studentTwelfthPercentage;

    private ImageView studentImage;
    private ProgressBar progressBar;
    private ApiService apiService;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Bind UI views
        studentName = findViewById(R.id.studentName);
        studentDOB = findViewById(R.id.studentDOB);
        studentGender = findViewById(R.id.studentGender);
        studentEmail = findViewById(R.id.studentEmail);
        studentPhone = findViewById(R.id.studentPhone);
        studentAddress = findViewById(R.id.studentAddress);
        studentCityState = findViewById(R.id.studentCityState);
        studentDepartment = findViewById(R.id.studentDepartment);
        studentCurrentSemester = findViewById(R.id.studentCurrentSemester);
        studentTenthSchool = findViewById(R.id.studentTenthSchool);
        studentTenthYear = findViewById(R.id.studentTenthYear);
        studentTenthPercentage = findViewById(R.id.studentTenthPercentage);
        studentTwelfthSchool = findViewById(R.id.studentTwelfthSchool);
        studentTwelfthYear = findViewById(R.id.studentTwelfthYear);
        studentTwelfthPercentage = findViewById(R.id.studentTwelfthPercentage);

        studentImage = findViewById(R.id.studentImage);
        progressBar = findViewById(R.id.progressBar);

        // Get student ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("studentId", -1);

        Log.d("ProfileActivity", "StudentId: " + studentId);

        if (studentId != -1) {
            apiService = ApiClient.getApiService();
            fetchStudentData(studentId);
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchStudentData(int studentId) {
        progressBar.setVisibility(View.VISIBLE);

        Call<StudentListResponse> call = apiService.getStudentById(studentId);

        call.enqueue(new Callback<StudentListResponse>() {
            @Override
            public void onResponse(Call<StudentListResponse> call, Response<StudentListResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<StudentResponse> students = response.body().getStudent();

                    if (!students.isEmpty()) {
                        StudentResponse student = students.get(0);

                        studentName.setText("Name: " + safeString(student.getStudentName()));

                        getSupportActionBar().setTitle(safeString(student.getStudentName()) + " - Profile");

                        studentDOB.setText("DOB: " + safeString(student.getDob()));
                        studentGender.setText("Gender: " + safeString(student.getGender()));
                        studentEmail.setText("Email: " + safeString(student.getEmail()));
                        studentPhone.setText("Phone: " + safeString(student.getPhone()));
                        studentAddress.setText("Address: " + safeString(student.getAddress()));
                        studentCityState.setText("City/State: " + safeString(student.getCity()) + ", " + safeString(student.getState()));
                        studentDepartment.setText("Department: " + safeString(student.getdepname()));
                        studentCurrentSemester.setText("Current Semester: " + student.getCurrentSemester() + " Semester");
                        studentTenthSchool.setText("10th School: " + safeString(student.getTenthSchool()));
                        studentTenthYear.setText("10th Passing Year: " + student.getTenthPassingYear());
                        studentTenthPercentage.setText("10th Percentage: " + student.getTenthPercentage() + "%");
                        studentTwelfthSchool.setText("12th School: " + safeString(student.getTwelfthSchool()));
                        studentTwelfthYear.setText("12th Passing Year: " + student.getTewelfthPassingYear());
                        studentTwelfthPercentage.setText("12th Percentage: " + student.getTewelfthPercentage() + "%");

                        // Load student image
                        String img = student.getStudentImg();
                        if (img != null && !img.isEmpty()) {
                            String imageUrl = "http://10.0.2.2:5291/uploads/students/studentProfile/" + img;
                            Glide.with(profile.this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.student)
                                    .error(R.drawable.content)
                                    .into(studentImage);
                            Log.d("Image URL", imageUrl);
                        } else {
                            studentImage.setImageResource(R.drawable.student); // default image
                        }

                    } else {
                        Log.e("API", "Student list is empty");
                        Toast.makeText(profile.this, "Student not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.e("API", "Response failed: " + response.code() + " | " + response.message());
                    Toast.makeText(profile.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StudentListResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("API Error", t.getMessage());
                t.printStackTrace();
                Toast.makeText(profile.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String safeString(String input) {
        return input == null ? "" : input;
    }
}
