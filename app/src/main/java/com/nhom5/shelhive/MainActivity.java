package com.nhom5.shelhive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetUserResponse;
import com.nhom5.shelhive.ui.admin.Admin_TrangChuActivity;
import com.nhom5.shelhive.ui.auth.DangNhapActivity;
import com.nhom5.shelhive.ui.user.User_TrangChuActivity;
import com.nhom5.shelhive.ui.user.xacthuc.User_XacThucActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_NAME = "login_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isRemembered = sharedPreferences.getBoolean("remember", false);
        String email = sharedPreferences.getString("email", "");
        String role = sharedPreferences.getString("role", "");

        if (isRemembered && !email.isEmpty() && !role.isEmpty()) {
            if ("admin".equalsIgnoreCase(role)) {
                // Admin -> Trang chủ admin
                Intent intent = new Intent(MainActivity.this, Admin_TrangChuActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            } else {
                // User -> Check CCCD bằng API
                ApiService.apiService.getUserByEmail(email).enqueue(new Callback<GetUserResponse>() {
                    @Override
                    public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String cccd = response.body().getCccd();
                            if (cccd == null || cccd.trim().isEmpty()) {
                                // Chưa có CCCD -> Vào xác thực CCCD
                                Intent intent = new Intent(MainActivity.this, User_XacThucActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            } else {
                                // Đã xác thực -> Vào trang chủ User
                                Intent intent = new Intent(MainActivity.this, User_TrangChuActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // Không lấy được thông tin user -> logout
                            Toast.makeText(MainActivity.this, "Lỗi lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                            startLoginActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetUserResponse> call, Throwable t) {
                        // Lỗi mạng
                        Toast.makeText(MainActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        startLoginActivity();
                    }
                });
            }
        } else {
            // Chưa đăng nhập -> về login
            startLoginActivity();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(MainActivity.this, DangNhapActivity.class);
        startActivity(intent);
        finish();
    }
}
