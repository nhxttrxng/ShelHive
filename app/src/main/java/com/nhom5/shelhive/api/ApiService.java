package com.nhom5.shelhive.api;

import com.nhom5.shelhive.ui.model.PhanAnh;
import com.nhom5.shelhive.ui.model.ThongBao;
import com.nhom5.shelhive.ui.model.Bill;
import com.nhom5.shelhive.ui.model.ThongBaoHoaDon;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
    Call<List<GetRoomResponse>> getRoomsByMaDay(@Path("ma_day") int ma_day);

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
    //@GET("/api/reports/")
    //Call<List<PhanAnh>> getAllPhanAnh();

    @PUT("reports/{ma_phan_anh}")
    Call<ResponseBody> updateTinhTrang(@Path("ma_phan_anh") int id, @Body Map<String, String> body);
    public interface NotificationApi {
        @GET("notifications/{user_id}")
        Call<List<ThongBao>> getThongBaoTheoUser(@Path("user_id") String userId);
    }
    @PUT("users/{email}")
    Call<ResponseBody> updateUser(
            @Path("email") String email,
            @Body UpdateUserRequest request
    );

    @PUT("admins/{email}")
    Call<ResponseBody> updateAdmin(
            @Path("email") String email,
            @Body UpdateAdminRequest request
    );

    @Multipart
    @POST("users/upload-avt/{email}")
    Call<ResponseBody> uploadAvatar(
            @Path("email") String email,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("admins/upload-avt/{email}")
    Call<ResponseBody> uploadAdminAvatar(
            @Path("email") String email,
            @Part MultipartBody.Part image
    );

    @POST("auth/change-password")
    Call<ResponseBody> changePassword(@Body ChangePasswordRequest request);
    @GET("notifications/phong/{ma_phong}")
    Call<ThongBaoResponse1> getThongBaoChungTheoPhong(@Path("ma_phong") int ma_phong);
    @GET("invoices/{invoiceId}")
    Call<Bill> getInvoiceById(@Path("invoiceId") int invoiceId);

    @GET("invoices/room/{ma_phong}")
    Call<List<GetBillByRoomResponse>> getBillsByRoom(@Path("ma_phong") int maPhong);
    @GET("invoice-notifications/phong/{ma_phong}")
    Call<ThongBaoHoaDonResponse1> getThongBaoHoaDonTheoPhong(@Path("ma_phong") int ma_phong);
    @POST("notifications/")
    Call<ThongBaoRequest> taoThongBao(@Body ThongBaoRequest thongBao);
    // Xóa thông báo
    @DELETE("notifications/{ma_thong_bao}")
    Call<Void> xoaThongBao(@Path("ma_thong_bao") int maThongBao);

    // Sửa thông báo
    @PUT("notifications/{ma_thong_bao}")
    Call<Void> suaThongBao(
            @Path("ma_thong_bao") int maThongBao,
            @Body Map<String, Object> requestBody
    );
    // Sửa thông báo hd
    @PUT("invoice-notifications/{ma_thong_bao_hoa_don}")
    Call<Void> suaThongBaoHoaDon(
            @Path("ma_thong_bao_hoa_don") int maThongBaoHD,
            @Body Map<String, Object> requestBody
    );
    // Xóa thông báo hd
    @DELETE("invoice-notifications/{ma_thong_bao_hoa_don}")
    Call<Void> xoaThongBaoHoaDon(@Path("ma_thong_bao_hoa_don") int maThongBaoHD);
    @GET("notifications/day/{ma_day}")
    Call<ThongBaoResponse> getThongBaoByMaDay(@Path("ma_day") int maDay);
    @GET("invoice-notifications/day/{ma_day}")
    Call<List<ThongBaoHoaDon>> getThongBaoHoaDonByMaDay(@Path("ma_day") int maDay);

    @GET("motels/{ma_day}")
    Call<GetMotelByIdResponse> getMotelById(@Path("ma_day") int maDay);

    public static ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
}
