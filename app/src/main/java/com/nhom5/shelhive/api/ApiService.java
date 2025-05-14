package com.nhom5.shelhive.api;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

    @GET("/api/auth/check-verify")
    Call<ResponseBody> checkVerify(@Body CheckVerifyRequest request);

    @GET("/api/users/phong/{email}")
    Call<FullUserInfoResponse> getFullInfoByEmail(@Path("email") String email);

    @GET("/api/admins/{email}")
    Call<GetAdminByEmailResponse> getAdminByEmail(@Path("email") String email);

    @GET("/api/motels/{email}")
    Call<List<GetDayTroResponse>> getDayTroByAdminEmail(@Path("email") String email);

    @POST("/api/motels/")
    Call<ResponseBody> createDayTro(@Body CreateDayTroRequest createDayTroRequest);

    @DELETE("api/motels/{ma_day}")
    Call<ResponseBody> deleteDayTro(@Path("ma_day") int maDay);

    @PUT("api/motels/{maday}")
    Call<ResponseBody> updateDayTro(
            @Path("maday") int maDay,
            @Body UpdateDayTroRequest request
    );
    public static ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
}
