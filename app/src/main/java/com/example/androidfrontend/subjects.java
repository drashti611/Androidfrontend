package com.example.androidfrontend;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfrontend.Adapter.SubjectContentAdapter;
import com.example.androidfrontend.Api.ApiClient;
import com.example.androidfrontend.Api.ApiService;
import com.example.androidfrontend.Model.Subject;
import com.example.androidfrontend.Model.SubjectListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class subjects extends AppCompatActivity {

    RecyclerView recyclerView;
    SubjectContentAdapter adapter;
    int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subjects);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back button in toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Subjects");
        }

        // Handle back button press on toolbar
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Handle window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve studentId from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("studentId", -1);

        fetchSubjects(studentId);
    }

    private void fetchSubjects(int studentId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<SubjectListResponse> call = apiService.GetSubjectsInDepartment(studentId);

        call.enqueue(new Callback<SubjectListResponse>() {
            @Override
            public void onResponse(Call<SubjectListResponse> call, Response<SubjectListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Subject> subjects = response.body().getSubject();
                    adapter = new SubjectContentAdapter(subjects);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(subjects.this, "No subjects found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubjectListResponse> call, Throwable t) {
                Toast.makeText(subjects.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
