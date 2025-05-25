package com.nhom5.shelhive.ui.admin.thongtin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class Admin_DoiMatKhauActivity extends AppCompatActivity {

    private EditText edtCurrentPassword, edtNewPassword, edtConfirmPassword;
    private View btnSave, btnCancel;
    private LinearLayout navHome, navProfile, navLogout;
    private FrameLayout popupLayout;
    private ImageView btnBack;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_quenmatkhau);

        // Nhận email từ intent hoặc SharedPreferences
        email = getIntent().getStringExtra("email");
        if (email == null) {
            SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
            email = prefs.getString("email", null);
        }

        // Ánh xạ view
        edtCurrentPassword = findViewById(R.id.edt_admin_current_password);
        edtNewPassword = findViewById(R.id.edt_admin_new_password);
        edtConfirmPassword = findViewById(R.id.edt_admin_confirm_password);

        btnSave = findViewById(R.id.btn_admin_save_user);
        btnCancel = findViewById(R.id.btn_admin_cancel);

        navHome = findViewById(R.id.nav_user_home);
        navProfile = findViewById(R.id.nav_user_profile);
        navLogout = findViewById(R.id.nav_logout);

        btnBack = findViewById(R.id.btn_admin_back);

        popupLayout = findViewById(R.id.popup_layout);

        // Nút back, cancel về finish
        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());
        navHome.setOnClickListener(v -> finish());
        navProfile.setOnClickListener(v -> { /* Ở lại trang này hoặc show toast */ });

        // Đăng xuất - hiện popup
        navLogout.setOnClickListener(v -> showPopupLogout());

        if (popupLayout != null) {
            ImageView closeBtn = popupLayout.findViewById(R.id.close_button);
            ImageView logoutBtn = popupLayout.findViewById(R.id.logout_button);

            if (closeBtn != null) {
                closeBtn.setOnClickListener(v -> popupLayout.setVisibility(View.GONE));
            }
            if (logoutBtn != null) {
                logoutBtn.setOnClickListener(v -> {
                    SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("email");
                    editor.remove("password");
                    editor.remove("remember");
                    editor.apply();

                    startActivity(new Intent(this, DangNhapActivity.class));
                    finish();
                });
            }
        }

        // Đổi mật khẩu
        btnSave.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String oldPass = edtCurrentPassword.getText().toString().trim();
        String newPass = edtNewPassword.getText().toString().trim();
        String confirm = edtConfirmPassword.getText().toString().trim();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            showToast("Vui lòng nhập đầy đủ thông tin");
            return;
        }
        if (newPass.length() < 6) {
            showToast("Mật khẩu mới phải có ít nhất 6 ký tự");
            return;
        }
        if (!newPass.equals(confirm)) {
            showToast("Xác nhận mật khẩu không khớp");
            return;
        }
        if (oldPass.equals(newPass)) {
            showToast("Mật khẩu mới không được trùng với mật khẩu cũ");
            return;
        }

        ChangePasswordRequest req = new ChangePasswordRequest(email, oldPass, newPass);

        ApiService.apiService.changePassword(req).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showToast("Đổi mật khẩu thành công!");
                    finish();
                } else {
                    // Lấy message lỗi trả về từ BE (nếu có)
                    String errorMsg = "Đổi mật khẩu thất bại!";
                    try {
                        String body = response.errorBody() != null ? response.errorBody().string() : "";
                        if (body.contains("Mật khẩu cũ không đúng")) {
                            errorMsg = "Sai mật khẩu cũ!";
                        } else if (body.contains("Không tìm thấy tài khoản")) {
                            errorMsg = "Tài khoản không tồn tại!";
                        }
                    } catch (Exception ignored) {}
                    showToast(errorMsg);
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
