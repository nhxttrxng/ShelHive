package com.nhom5.shelhive.api;

import com.nhom5.shelhive.ui.model.*;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    // --- AUTH ---
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

    @POST("auth/resend-otp")
    Call<ResponseBody> resendOtp(@Body ResendOtpRequest request);

    @GET("auth/check-verify")
    Call<ResponseBody> checkVerify(@Body CheckVerifyRequest request);

    @POST("auth/change-password")
    Call<ResponseBody> changePassword(@Body ChangePasswordRequest request);

    // --- USER / ADMIN ---
    @GET("users/{email}")
    Call<GetUserResponse> getUserByEmail(@Path("email") String email);

    @GET("users/phong/{email}")
    Call<FullUserInfoResponse> getFullInfoByEmail(@Path("email") String email);

    @PUT("users/{email}")
    Call<ResponseBody> updateUser(@Path("email") String email, @Body UpdateUserRequest request);

    @Multipart
    @POST("users/upload-avt/{email}")
    Call<ResponseBody> uploadAvatar(@Path("email") String email, @Part MultipartBody.Part image);

    @GET("admins/{email}")
    Call<GetAdminByEmailResponse> getAdminByEmail(@Path("email") String email);

    @PUT("admins/{email}")
    Call<ResponseBody> updateAdmin(@Path("email") String email, @Body UpdateAdminRequest request);

    @Multipart
    @POST("admins/upload-avt/{email}")
    Call<ResponseBody> uploadAdminAvatar(@Path("email") String email, @Part MultipartBody.Part image);

    // --- DAY TRO / MOTEL ---
    @GET("motels/{email}")
    Call<List<GetMotelResponse>> getDayTroByAdminEmail(@Path("email") String email);

    @GET("motels/day/{ma_day}")
    Call<GetMotelByIdResponse> getMotelById(@Path("ma_day") int maDay);

    @POST("motels/")
    Call<ResponseBody> createDayTro(@Body CreateDayTroRequest createDayTroRequest);

    @PUT("motels/{maday}")
    Call<ResponseBody> updateDayTro(@Path("maday") int maDay, @Body UpdateMotelRequest request);

    @DELETE("motels/{ma_day}")
    Call<ResponseBody> deleteDayTro(@Path("ma_day") int maDay);

    // --- ROOM ---
    @GET("rooms/day/{ma_day}")
    Call<List<GetRoomResponse>> getRoomsByMaDay(@Path("ma_day") int ma_day);

    @GET("rooms/{ma_phong}")
    Call<GetRoomInfoResponse> getRoomInfoByMaPhong(@Path("ma_phong") int maPhong);

    @POST("rooms/")
    Call<ResponseBody> createRoom(@Body CreateRoomRequest request);

    @PUT("rooms/{ma_phong}")
    Call<ResponseBody> updateRoom(@Path("ma_phong") int maPhong, @Body Map<String, Object> request);

    // --- INVOICE ---
    @GET("invoices/{invoiceId}")
    Call<Bill> getInvoiceById(@Path("invoiceId") int invoiceId);

    @GET("invoices/room/{ma_phong}")
    Call<List<GetBillByRoomResponse>> getBillsByRoom(@Path("ma_phong") int maPhong);

    @POST("invoices")
    Call<ResponseBody> createInvoice(@Body CreateBillRequest request);

    @PATCH("invoices/{id}/status")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> updateInvoiceStatus(@Path("id") int invoiceId, @Body Map<String, String> statusBody);

    @DELETE("invoices/{id}")
    Call<Void> deleteInvoice(@Path("id") int invoiceId);

    @PUT("invoices/{id}")
    Call<ResponseBody> updateInvoice(
            @Path("id") int billId,
            @Body UpdateBillRequest request
    );

    @GET("invoices/room/{roomId}/latest-meter")
    Call<GetLatestMeterResponse> getLatestMeterIndexesByRoom(@Path("roomId") int roomId);

    // --- GIA HẠN HÓA ĐƠN (EXTENSION) ---
    @POST("extensions/")
    Call<ResponseBody> createExtension(@Body CreateExtensionRequest request);

    @GET("extensions/latest-approved/{invoiceId}")
    Call<GetExtensionByBillResponse> getExtensionByBillId(@Path("invoiceId") int invoiceId);

    @GET("extensions/pending-by-room/{roomId}")
    Call<List<Extension>> getPendingExtensionsByRoomId(@Path("roomId") int roomId);

    @GET("extensions/{id}")
    Call<Extension> getExtensionById(@Path("id") int id);

    @PATCH("extensions/{id}/approve")
    Call<ResponseBody> approveExtension(@Path("id") int id);

    @PATCH("extensions/{id}/reject")
    Call<ResponseBody> rejectExtension(@Path("id") int id);

    // --- REPORT ---
    @GET("reports/{tinh_trang}/{ma_day}")
    Call<List<PhanAnh>> getPhanAnhByTinhTrang(@Path("tinh_trang") String tinhTrang, @Path("ma_day") int maDay);

    @POST("reports/")
    Call<ResponseBody> createPhanAnh(@Body PhanAnhRequest phanAnhRequest);

    @PUT("reports/{ma_phan_anh}")
    Call<ResponseBody> updateTinhTrang(@Path("ma_phan_anh") int id, @Body Map<String, String> body);

    // --- NOTIFICATION ---
    @GET("notifications/phong/{ma_phong}")
    Call<ThongBaoResponse1> getThongBaoChungTheoPhong(@Path("ma_phong") int ma_phong);

    @GET("notifications/day/{ma_day}")
    Call<ThongBaoResponse> getThongBaoByMaDay(@Path("ma_day") int maDay);

    @GET("notifications/{user_id}")
    Call<List<ThongBao>> getThongBaoTheoUser(@Path("user_id") String userId);

    @POST("notifications/")
    Call<ThongBaoRequest> taoThongBao(@Body ThongBaoRequest thongBao);

    @DELETE("notifications/{ma_thong_bao}")
    Call<Void> xoaThongBao(@Path("ma_thong_bao") int maThongBao);

    @PUT("notifications/{ma_thong_bao}")
    Call<Void> suaThongBao(@Path("ma_thong_bao") int maThongBao, @Body Map<String, Object> requestBody);

    // --- INVOICE NOTIFICATION ---
    @POST("invoice-notifications/")
    Call<ResponseBody> createInvoiceNotification(@Body CreateInvoiceNotificationRequest req);

    @GET("invoice-notifications/phong/{ma_phong}")
    Call<ThongBaoHoaDonResponse1> getThongBaoHoaDonTheoPhong(@Path("ma_phong") int ma_phong);

    @GET("invoice-notifications/daytro/{ma_day}")
    Call<List<ThongBaoHoaDon>> getThongBaoHoaDonByMaDay(@Path("ma_day") int maDay);

    @PUT("invoice-notifications/{ma_thong_bao_hoa_don}")
    Call<Void> suaThongBaoHoaDon(@Path("ma_thong_bao_hoa_don") int maThongBaoHD, @Body Map<String, Object> requestBody);

    @DELETE("invoice-notifications/{ma_thong_bao_hoa_don}")
    Call<Void> xoaThongBaoHoaDon(@Path("ma_thong_bao_hoa_don") int maThongBaoHD);

    // --- THANH TOÁN VNPay ---
    @POST("vnpay/create_payment")
    Call<ResponseBody> createPayment(@Body Map<String, Object> body);

    // --- STATISTIC ---
    @GET("/api/stats/electric-money/{ma_phong}/{fromMonth}/{fromYear}/{toMonth}/{toYear}")
    Call<List<ThongKeDienResponse>> getThongKeDien(
            @Path("ma_phong") int maPhong,
            @Path("fromMonth") int fromMonth,
            @Path("fromYear") int fromYear,
            @Path("toMonth") int toMonth,
            @Path("toYear") int toYear
    );

    @GET("/api/stats/water-money/{ma_phong}/{fromMonth}/{fromYear}/{toMonth}/{toYear}")
    Call<List<ThongKeNuocResponse>> getThongKeNuoc(
            @Path("ma_phong") int maPhong,
            @Path("fromMonth") int fromMonth,
            @Path("fromYear") int fromYear,
            @Path("toMonth") int toMonth,
            @Path("toYear") int toYear
    );

    @GET("/api/stats/electric-profit/{ma_day}/{fromMonth}/{fromYear}/{toMonth}/{toYear}")
    Call<List<ThongKeE_Profit>> getThongKeLoiDien(
            @Path("ma_day") int maDay,
            @Path("fromMonth") int fromMonth,
            @Path("fromYear") int fromYear,
            @Path("toMonth") int toMonth,
            @Path("toYear") int toYear
    );

    @GET("/api/stats/water-profit/{ma_day}/{fromMonth}/{fromYear}/{toMonth}/{toYear}")
    Call<List<ThongKeW_Profit>> getThongKeLoiNuoc(
            @Path("ma_day") int maDay,
            @Path("fromMonth") int fromMonth,
            @Path("fromYear") int fromYear,
            @Path("toMonth") int toMonth,
            @Path("toYear") int toYear
    );

    @GET("/api/stats/rent-money/{ma_day}/{month}/{year}")
    Call<List<ThongKeRentMoney>> getThongKeTienTro(
            @Path("ma_day") int maDay,
            @Path("month") int month,
            @Path("year") int year
    );

    @GET("/api/stats/room_count/{ma_day}/{month}/{year}")
    Call<List<ThongKeRoomCount>> getThongKeSoPhong(
            @Path("ma_day") int maDay,
            @Path("month") int month,
            @Path("year") int year
    );

    // --- Singleton Instance ---
    ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
}
