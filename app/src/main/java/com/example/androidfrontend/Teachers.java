package com.example.androidfrontend;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
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
    private LinearLayout lottieContainer, profileLayout;
    private LottieAnimationView lottieSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers);

        lottieContainer = findViewById(R.id.lottieContainer);
        profileLayout = findViewById(R.id.subjectLayoutt);
        lottieSplash = findViewById(R.id.lottieSplash);

        // Play animation explicitly
        lottieSplash.playAnimation();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Teacher");
        }

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        recyclerView = findViewById(R.id.recyclerViewFaculty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("studentId", -1);

        // Load data AFTER 2.5 seconds of Lottie animation
        new Handler().postDelayed(() -> {
            fetchFacultyData();
        }, 2500); // 2500ms = 2.5 seconds
    }

    private void fetchFacultyData() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<facultylistresponse> call = apiService.GetFacultyInDepartment(studentId);
        call.enqueue(new Callback<facultylistresponse>() {
            @Override
            public void onResponse(Call<facultylistresponse> call, Response<facultylistresponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    facultyList = response.body().getFaculty();
                    adapter = new FacultyAdapter(facultyList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(Teachers.this, "Failed to load faculty data.", Toast.LENGTH_SHORT).show();
                }

                // Hide Lottie, Show Content
                lottieSplash.cancelAnimation();
                lottieContainer.setVisibility(View.GONE);
                profileLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<facultylistresponse> call, Throwable t) {
                Toast.makeText(Teachers.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                // Hide Lottie even on failure
                lottieSplash.cancelAnimation();
                lottieContainer.setVisibility(View.GONE);
                profileLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
