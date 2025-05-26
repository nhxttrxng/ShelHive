package com.nhom5.shelhive.ui.user.thongtin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.ChangePasswordRequest;
import com.nhom5.shelhive.ui.auth.DangNhapActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_DoiMatKhauActivity extends AppCompatActivity {
    private EditText edtCurrentPassword, edtNewPassword, edtConfirmPassword;
    private View btnSave, btnCancel;
    private ImageView btnBack;
    private LinearLayout navHome, navProfile, navLogout;
    private View popupLayout;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_quenmatkhau);

        // Nhận email từ intent hoặc SharedPreferences
        email = getIntent().getStringExtra("email");
        if (email == null) {
            SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
            email = prefs.getString("email", null);
        }

        // Ánh xạ view
        edtCurrentPassword = findViewById(R.id.edt_user_current_password);
        edtNewPassword     = findViewById(R.id.edt_user_new_password);
        edtConfirmPassword = findViewById(R.id.edt_user_confirm_password);

        btnSave   = findViewById(R.id.btn_user_save);
        btnCancel = findViewById(R.id.btn_user_cancel);
        btnBack   = findViewById(R.id.btn_user_back);

        navHome    = findViewById(R.id.nav_user_home);
        navProfile = findViewById(R.id.nav_user_profile);
        navLogout  = findViewById(R.id.nav_user_logout);

        popupLayout = findViewById(R.id.popup_logout);

        // Các sự kiện cơ bản
        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());
        navHome.setOnClickListener(v -> finish());
        navProfile.setOnClickListener(v -> {/* Ở lại trang hoặc show toast */});

        navLogout.setOnClickListener(v -> showPopupLogout());

        // Đăng xuất trong popup
        if (popupLayout != null) {
            ImageView closeBtn = popupLayout.findViewById(R.id.close_button);
            ImageView logoutBtn = popupLayout.findViewById(R.id.logout_button);

            if (closeBtn != null) closeBtn.setOnClickListener(v -> popupLayout.setVisibility(View.GONE));
            if (logoutBtn != null) logoutBtn.setOnClickListener(v -> {
                SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
                prefs.edit().clear().apply();
                startActivity(new Intent(this, DangNhapActivity.class));
                finish();
            });
        }

        // Sự kiện Lưu (đổi mật khẩu)
        btnSave.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String currentPwd = edtCurrentPassword.getText().toString().trim();
        String newPwd     = edtNewPassword.getText().toString().trim();
        String confirmPwd = edtConfirmPassword.getText().toString().trim();

        // Validate
        if (TextUtils.isEmpty(currentPwd) || TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confirmPwd)) {
            showToast("Vui lòng nhập đủ thông tin!");
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            showToast("Mật khẩu xác nhận không khớp!");
            return;
        }
        if (newPwd.length() < 6) {
            showToast("Mật khẩu mới tối thiểu 6 ký tự!");
            return;
        }
        // Tạo request
        ChangePasswordRequest req = new ChangePasswordRequest(email, currentPwd, newPwd);
        ApiService.apiService.changePassword(req).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showToast("Đổi mật khẩu thành công!");
                    finish();
                } else {
                    showToast("Đổi mật khẩu thất bại! Mật khẩu cũ không đúng?");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showToast("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void showPopupLogout() {
        if (popupLayout != null) {
            popupLayout.setVisibility(View.VISIBLE);
            popupLayout.bringToFront();
        } else {
            showToast("Không tìm thấy popup logout!");
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
