package com.nhom5.shelhive.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/auth/register")
    Call<ResponseBody> register(@Body RegisterRequest registerRequest);

    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/api/auth/forgot-password")
    Call<ResponseBody> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("/api/auth/reset-password")
    Call<ResponseBody> resetPassword(@Body ResetPasswordRequest request);

    @POST("/api/auth/verify-otp")
    Call<ResponseBody> verifyOtp(@Body VerifyOtpRequest request);

    @POST("/api/auth/resend-otp")
    Call<ResponseBody> resendOtp(@Body ResendOtpRequest request);

    public static ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
}
