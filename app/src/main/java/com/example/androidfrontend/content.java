package com.example.androidfrontend;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Enable back button in toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Material");

        }
        subjectsRecyclerView = findViewById(R.id.subjectsRecyclerView);
        subjectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int studentId = prefs.getInt("studentId", -1);

        Log.d("STUDENT_ID", "Fetched studentId: " + studentId);

        if (studentId == -1) {
            Toast.makeText(this, "Student ID not found. Please login.", Toast.LENGTH_LONG).show();
            return;
        }

        fetchCourseContents(studentId);
    }

    private void fetchCourseContents(int studentId) {
        ApiService apiService = ApiClient.getApiService();
        Call<contentlistResponse> call = apiService.getCourseContents(studentId);

        call.enqueue(new Callback<contentlistResponse>() {
            @Override
            public void onResponse(Call<contentlistResponse> call, Response<contentlistResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Raw response: " + new Gson().toJson(response.body()));

                    if (response.body().isSuccess()) {
                        List<Content> contentList = response.body().getContent();
                        Log.d("API_SUCCESS", "Content list size: " + contentList.size());

                        adapter = new SubjectsAdapter(content.this, contentList);
                        subjectsRecyclerView.setAdapter(adapter);

                        if (contentList.isEmpty()) {
                            Toast.makeText(content.this, "No content available.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(content.this, "Response marked as failed", Toast.LENGTH_SHORT).show();
                        Log.e("API_FAILED", "Success flag is false");
                    }
                } else {
                    Toast.makeText(content.this, "Response not successful", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<contentlistResponse> call, Throwable t) {
                Toast.makeText(content.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
