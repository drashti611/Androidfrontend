package com.example.androidfrontend;

import android.animation.Animator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
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
    private LinearLayout lottieContainer, profileLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subjects);

        // Initialize views
        lottieContainer = findViewById(R.id.lottieContainer);
        profileLayout = findViewById(R.id.subjectLayoutt);
        LottieAnimationView lottieView = findViewById(R.id.lottieSplash);

        // Initially show Lottie, hide profile layout
        lottieContainer.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.GONE);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Subjects");
        }

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerViewSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get studentId
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("studentId", -1);

        // Lottie Animation Listener
        lottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) { }

            @Override
            public void onAnimationEnd(Animator animation) {
                // After animation ends
                lottieContainer.setVisibility(View.GONE);
                profileLayout.setVisibility(View.VISIBLE);
                fetchSubjects(studentId);
            }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
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
