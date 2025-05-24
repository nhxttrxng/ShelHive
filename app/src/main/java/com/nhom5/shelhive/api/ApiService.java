package com.nhom5.shelhive.api;

import com.nhom5.shelhive.ui.model.PhanAnh;
import com.nhom5.shelhive.ui.model.ThongBao;

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
    @POST("auth/register")
    Call<ResponseBody> register(@Body RegisterRequest registerRequest);

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("auth/forgot-password")
    Call<ResponseBody> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("auth/reset-password")
    Call<ResponseBody> resetPassword(@Body ResetPasswordRequest request);

    @POST("auth/verify-otp")
    Call<ResponseBody> verifyOtp(@Body VerifyOtpRequest request);

    @POST("/auth/resend-otp")
    Call<ResponseBody> resendOtp(@Body ResendOtpRequest request);

    @GET("auth/check-verify")
    Call<ResponseBody> checkVerify(@Body CheckVerifyRequest request);

    @GET("users/phong/{email}")
    Call<FullUserInfoResponse> getFullInfoByEmail(@Path("email") String email);

    @GET("admins/{email}")
    Call<GetAdminByEmailResponse> getAdminByEmail(@Path("email") String email);

    @GET("motels/{email}")
    Call<List<GetMotelResponse>> getDayTroByAdminEmail(@Path("email") String email);

    @POST("motels/")
    Call<ResponseBody> createDayTro(@Body CreateDayTroRequest createDayTroRequest);

    @DELETE("motels/{ma_day}")
    Call<ResponseBody> deleteDayTro(@Path("ma_day") int maDay);

    @PUT("motels/{maday}")
    Call<ResponseBody> updateDayTro(
            @Path("maday") int maDay,
            @Body UpdateMotelRequest request
    );

    @GET("users/{email}")
    Call<GetUserResponse> getUserByEmail(@Path("email") String email);

    @GET("rooms/day/{ma_day}")
    Call<List<GetRoom2Response>> getRoomsByMaDay(@Path("ma_day") int ma_day);

    @GET("rooms/{ma_phong}")
    Call<GetRoomInfoResponse> getRoomInfoByMaPhong(@Path("ma_phong") int maPhong);

    @POST("rooms/")
    Call<ResponseBody> createRoom(@Body CreateRoomRequest request);

    @PUT("rooms/{ma_phong}")
    Call<ResponseBody> updateRoom(
            @Path("ma_phong") int maPhong,
            @Body Map<String, Object> request
    );

    @GET("reports/{tinh_trang}")
    Call<List<PhanAnh>> getPhanAnhByTinhTrang(@Path("tinh_trang") String tinhTrang);

    @POST("reports/")
    Call<ResponseBody> createPhanAnh(@Body PhanAnhRequest phanAnhRequest);
    @GET("reports/day/{ma_day}")
    Call<List<PhanAnh>> getByMaDay(@Path("ma_day") int maDay);


    @PUT("reports/{ma_phan_anh}")
    Call<ResponseBody> updateTinhTrang(@Path("ma_phan_anh") int id, @Body Map<String, String> body);
    @GET("notifications/phong/{ma_phong}")
    Call<List<ThongBao>> getThongBaoChungTheoPhong(@Path("ma_phong") int ma_phong);

    @GET("invoice-notifications/phong/{ma_phong}")
    Call<List<ThongBao>> getThongBaoHoaDonTheoPhong(@Path("ma_phong") int ma_phong);

    @PUT("users/{email}")
    Call<ResponseBody> updateUser(
            @Path("email") String email,
            @Body UpdateUserRequest request
    );
    @GET("notifications/day/{ma_day}")
    Call<ThongBaoResponse> getThongBaoByMaDay(@Path("ma_day") int maDay);
    @GET("invoice_notifications/day/{ma_day}")
    Call<List<ThongBao>> getThongBaoHoaDonByMaDay(@Path("ma_day") int maDay);

    public static ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
}
