package com.nhom5.shelhive.api;

import com.nhom5.shelhive.model.PhanAnh;
import com.nhom5.shelhive.model.ThongBao;

import java.util.List;
import java.util.Map;

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

    @GET("api/users/{email}")
    Call<GetUserResponse> getUserByEmail(@Path("email") String email);

    @GET("api/rooms/day/{ma_day}")
    Call<List<GetRoom2Response>> getRoomsByMaDay(@Path("ma_day") int ma_day);

    @GET("/api/rooms/{ma_phong}")
    Call<GetRoomInfoResponse> getRoomInfoByMaPhong(@Path("ma_phong") int maPhong);

    @PUT("/api/rooms/{ma_phong}")
    Call<ResponseBody> updateRoom(
            @Path("ma_phong") int maPhong,
            @Body Map<String, Object> request
    );

    @GET("/api/reports/{tinh_trang}")
    Call<List<PhanAnh>> getPhanAnhByTinhTrang(@Path("tinh_trang") String tinhTrang);

    @POST("/api/reports/")
    Call<ResponseBody> createPhanAnh(@Body PhanAnhRequest phanAnhRequest);
    //@GET("/api/reports/")
    //Call<List<PhanAnh>> getAllPhanAnh();

    @PUT("/api/reports/{ma_phan_anh}")
    Call<ResponseBody> updateTinhTrang(@Path("ma_phan_anh") int id, @Body Map<String, String> body);
    public interface NotificationApi {
        @GET("notifications/{user_id}")
        Call<List<ThongBao>> getThongBaoTheoUser(@Path("user_id") String userId);
    }

    public static ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
}
