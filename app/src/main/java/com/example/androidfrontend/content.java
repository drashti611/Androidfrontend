package com.example.androidfrontend;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.androidfrontend.Adapter.SubjectsAdapter;
import com.example.androidfrontend.Api.ApiClient;
import com.example.androidfrontend.Api.ApiService;
import com.example.androidfrontend.Model.Content;
import com.example.androidfrontend.Model.contentlistResponse;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class content extends AppCompatActivity {

    private RecyclerView subjectsRecyclerView;
    private SubjectsAdapter adapter;
    private LottieAnimationView lottieLoader;
    private LinearLayout contentLayout;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Material");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        lottieLoader = findViewById(R.id.lottieLoader);
        contentLayout = findViewById(R.id.contentLayout);
        subjectsRecyclerView = findViewById(R.id.subjectsRecyclerView);
        subjectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("studentId", -1);

        if (studentId == -1) {
            Toast.makeText(this, "Student ID not found. Please login.", Toast.LENGTH_LONG).show();
            return;
        }

        lottieLoader.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);

        // 👇 Show Lottie for 2.5 seconds then load content
        lottieLoader.playAnimation();
        new Handler().postDelayed(() -> {
            lottieLoader.setVisibility(View.GONE);
            contentLayout.setVisibility(View.VISIBLE);
            fetchCourseContents(studentId);
        }, 2500); // 2.5 seconds
    }

    private void fetchCourseContents(int studentId) {
        ApiService apiService = ApiClient.getApiService();
        Call<contentlistResponse> call = apiService.getCourseContents(studentId);

        call.enqueue(new Callback<contentlistResponse>() {
            @Override
            public void onResponse(Call<contentlistResponse> call, Response<contentlistResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Content> contentList = response.body().getContent();
                    adapter = new SubjectsAdapter(content.this, contentList);
                    subjectsRecyclerView.setAdapter(adapter);

                    if (contentList.isEmpty()) {
                        Toast.makeText(content.this, "No content available.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(content.this, "Failed to load content", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<contentlistResponse> call, Throwable t) {
                Toast.makeText(content.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
