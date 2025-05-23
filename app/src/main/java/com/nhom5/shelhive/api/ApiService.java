package com.nhom5.shelhive.api;
import com.nhom5.shelhive.api.RetrofitClient;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    Call<List<GetMotelResponse>> getDayTroByAdminEmail(@Path("email") String email);

    @POST("/api/motels/")
    Call<ResponseBody> createDayTro(@Body CreateDayTroRequest createDayTroRequest);

    @DELETE("api/motels/{ma_day}")
    Call<ResponseBody> deleteDayTro(@Path("ma_day") int maDay);

    @PUT("api/motels/{maday}")
    Call<ResponseBody> updateDayTro(
            @Path("maday") int maDay,
            @Body UpdateMotelRequest request
    );

    @GET("api/users/{email}")
    Call<GetUserResponse> getUserByEmail(@Path("email") String email);

    @GET("api/rooms/day/{ma_day}")
    Call<List<GetRoom2Response>> getRoomsByMaDay(@Path("ma_day") int ma_day);

    @GET("/api/rooms/{ma_phong}")
    Call<GetRoomInfoResponse> getRoomInfoByMaPhong(@Path("ma_phong") int maPhong);

    @POST("/api/rooms/")
    Call<ResponseBody> createRoom(@Body CreateRoomRequest request);

    @PUT("/api/rooms/{ma_phong}")
    Call<ResponseBody> updateRoom(
            @Path("ma_phong") int maPhong,
            @Body Map<String, Object> request
    );

    // Lấy tất cả hóa đơn
    @GET("/api/invoices")
    Call<List<Bill>> getAllInvoices();

    // Lấy hóa đơn theo phòng
    @GET("/api/invoices/room/{roomId}")
    Call<List<Bill>> getInvoicesByRoom(@Path("roomId") String roomId);
    
    // Lấy hóa đơn theo dãy trọ
    @GET("/api/invoices/motel/{motelId}")
    Call<List<Bill>> getInvoicesByMotel(@Path("motelId") int motelId);
    
    // Tạo hóa đơn mới
    @POST("/api/invoices")
    Call<Bill> createInvoice(@Body Map<String, Object> body);
    
    // Sửa hóa đơn
    @PUT("/api/invoices/{id}")
    Call<Bill> updateInvoice(@Path("id") int id, @Body Map<String, Object> body);
    
    // Xóa hóa đơn
    @DELETE("/api/invoices/{id}")
    Call<ResponseBody> deleteInvoice(@Path("id") int id);

    public static ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
}
