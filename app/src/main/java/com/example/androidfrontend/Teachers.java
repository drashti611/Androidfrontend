package com.example.androidfrontend;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfrontend.Adapter.FacultyAdapter;
import com.example.androidfrontend.Api.ApiClient;
import com.example.androidfrontend.Api.ApiService;
import com.example.androidfrontend.Model.Faculty;
import com.example.androidfrontend.Model.facultylistresponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Teachers extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FacultyAdapter adapter;
    private int studentId;
    private List<Faculty> facultyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back button in toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Get in touch with your guides");
        }

        // Handle back button press on toolbar
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        recyclerView = findViewById(R.id.recyclerViewFaculty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("studentId", -1);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<facultylistresponse> call = apiService.GetFacultyInDepartment(studentId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<facultylistresponse> call, Response<facultylistresponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    facultyList = response.body().getFaculty();
                    adapter = new FacultyAdapter(facultyList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<facultylistresponse> call, Throwable t) {
                Toast.makeText(Teachers.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}