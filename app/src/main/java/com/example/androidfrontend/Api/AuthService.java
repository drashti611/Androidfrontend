package com.example.androidfrontend.Api;

import com.example.androidfrontend.Model.LoginRequest;
import com.example.androidfrontend.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("Register/loginStudent")
    Call<LoginResponse> loginStudent(@Body LoginRequest request);

}
