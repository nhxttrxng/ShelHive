package com.nhom5.shelhive.ui.admin.thongtin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetAdminByEmailResponse;
import com.nhom5.shelhive.ui.auth.DangNhapActivity;
import com.nhom5.shelhive.ui.common.customviews.HexagonImageView;
import com.nhom5.shelhive.ui.admin.Admin_TrangChuActivity;
import com.nhom5.shelhive.ui.admin.thongtin.Admin_DoiMatKhauActivity;
import com.nhom5.shelhive.ui.admin.thongtin.Admin_EditThongTinActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_ThongTinActivity extends AppCompatActivity {

    private TextView textFullName, textEmail, textPhoneNumber;
    private View btnEdit, btnChangePassword;
    private ImageView btnBack;
    private HexagonImageView imgAdminAvatar;
    private LinearLayout navHome, navLogout;
    private View popupLayout;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_thongtincanhan);

        // Lấy email từ intent hoặc SharedPreferences
        email = getIntent().getStringExtra("email");
        if (email == null) {
            SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
            email = prefs.getString("email", null);
        }

        // Ánh xạ view
        textFullName = findViewById(R.id.txt_admin_fullname);
        textEmail = findViewById(R.id.txt_admin_email);
        textPhoneNumber = findViewById(R.id.txt_admin_phone);

        btnEdit = findViewById(R.id.btn_admin_edit);
        btnChangePassword = findViewById(R.id.btn_admin_changepass);

        imgAdminAvatar = findViewById(R.id.img_admin_avatar);

        btnBack = findViewById(R.id.btn_admin_back);
        navHome = findViewById(R.id.nav_user_home);
        navLogout = findViewById(R.id.nav_logout);

        popupLayout = findViewById(R.id.popup_logout);

        // Bấm back hoặc home đều finish
        btnBack.setOnClickListener(v -> finish());
        navHome.setOnClickListener(v -> finish());

        // Edit thông tin
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_ThongTinActivity.this, Admin_EditThongTinActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        // Đổi mật khẩu
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_ThongTinActivity.this, Admin_DoiMatKhauActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        // Đăng xuất show popup
        navLogout.setOnClickListener(v -> showPopupLogout());

        // Xử lý trong popup logout
        if (popupLayout != null) {
            ImageView closeBtn = popupLayout.findViewById(R.id.close_button);
            ImageView logoutBtn = popupLayout.findViewById(R.id.logout_button);

            if (closeBtn != null) {
                closeBtn.setOnClickListener(v -> {
                    popupLayout.setVisibility(View.GONE);
                });
            }
            if (logoutBtn != null) {
                logoutBtn.setOnClickListener(v -> {
                    SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("email");
                    editor.remove("password");
                    editor.remove("remember");
                    editor.apply();

                    startActivity(new Intent(Admin_ThongTinActivity.this, DangNhapActivity.class));
                    finish();
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdminInfo();
    }

    private void loadAdminInfo() {
        ApiService.apiService.getAdminByEmail(email).enqueue(new Callback<GetAdminByEmailResponse>() {
            @Override
            public void onResponse(Call<GetAdminByEmailResponse> call, Response<GetAdminByEmailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetAdminByEmailResponse admin = response.body();
                    textFullName.setText(admin.getHo_ten());
                    textEmail.setText(admin.getEmail());
                    textPhoneNumber.setText(admin.getSdt());
                    // Load avatar nếu có
                    String avtPath = admin.getAvt();
                    if (avtPath != null && !avtPath.isEmpty()) {
                        String fullAvtUrl = "http://221.132.33.173:3000" + avtPath;
                        Glide.with(Admin_ThongTinActivity.this).load(fullAvtUrl).placeholder(R.drawable.avatar).into(imgAdminAvatar);
                    } else {
                        imgAdminAvatar.setImageResource(R.drawable.avatar);
                    }
                } else {
                    showToast("Lỗi tải thông tin admin!");
                }
            }

            @Override
            public void onFailure(Call<GetAdminByEmailResponse> call, Throwable t) {
                showToast("Lỗi kết nối!");
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
