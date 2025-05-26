package com.nhom5.shelhive.ui.user.thongtin;

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
import com.nhom5.shelhive.api.GetUserResponse;
import com.nhom5.shelhive.ui.auth.DangNhapActivity;
import com.nhom5.shelhive.ui.common.customviews.HexagonImageView;
import com.nhom5.shelhive.ui.user.thongtin.User_DoiMatKhauActivity;
import com.nhom5.shelhive.ui.user.thongtin.User_EditThongTinActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_ThongTinActivity extends AppCompatActivity {

    private TextView textFullName, textDob, textSex, textHometown, textAddress, textIdNumber, textEmail, textPhoneNumber;
    private View btnEdit, btnChangePassword;
    private ImageView btnBack;
    private HexagonImageView imgUserAvatar;
    private LinearLayout navHome, navLogout;
    private View popupLayout;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_thongtincanhan);

        // Lấy email truyền từ trước
        email = getIntent().getStringExtra("email");

        // Ánh xạ view
        textFullName = findViewById(R.id.txt_user_fullname);
        textDob = findViewById(R.id.txt_user_dob);
        textSex = findViewById(R.id.txt_sex);
        textHometown = findViewById(R.id.txt_user_hometown);
        textAddress = findViewById(R.id.txt_user_address);
        textIdNumber = findViewById(R.id.txt_user_idnumber);
        textEmail = findViewById(R.id.txt_user_email);
        textPhoneNumber = findViewById(R.id.txt_user_phone);

        btnEdit = findViewById(R.id.btn_user_edit);
        btnChangePassword = findViewById(R.id.btn_user_changepass);

        imgUserAvatar = findViewById(R.id.img_user_avatar);

        btnBack = findViewById(R.id.btn_user_back);
        navHome = findViewById(R.id.nav_user_home);
        navLogout = findViewById(R.id.nav_logout);

        popupLayout = findViewById(R.id.popup_logout);

        // Bấm back hoặc home đều finish
        btnBack.setOnClickListener(v -> finish());
        navHome.setOnClickListener(v -> finish());

        // Edit thông tin
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(User_ThongTinActivity.this, User_EditThongTinActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        // Đổi mật khẩu
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(User_ThongTinActivity.this, User_DoiMatKhauActivity.class);
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

                    startActivity(new Intent(User_ThongTinActivity.this, DangNhapActivity.class));
                    finish();
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
    }

    private void loadUserInfo() {
        ApiService.apiService.getUserByEmail(email).enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetUserResponse user = response.body();
                    textFullName.setText(user.getHo_ten());
                    textDob.setText(formatDob(user.getNgay_sinh()));
                    textSex.setText(user.getGioi_tinh());
                    textHometown.setText(user.getQue_quan());
                    textAddress.setText(user.getDia_chi());
                    textIdNumber.setText(user.getCccd());
                    textEmail.setText(user.getEmail());
                    textPhoneNumber.setText(user.getSdt());
                    // Load avatar nếu có
                    String avtPath = user.getAvt();
                    if (avtPath != null && !avtPath.isEmpty()) {
                        String fullAvtUrl = "http://221.132.33.173:3000" + avtPath;
                        Glide.with(User_ThongTinActivity.this).load(fullAvtUrl).placeholder(R.drawable.default_avatar).into(imgUserAvatar);
                    } else {
                        imgUserAvatar.setImageResource(R.drawable.default_avatar);
                    }
                } else {
                    showToast("Lỗi tải thông tin người dùng!");
                }
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                showToast("Lỗi kết nối!");
            }
        });
    }

    // Chuyển yyyy-MM-dd(T...) thành dd/MM/yyyy
    private String formatDob(String raw) {
        if (raw == null) return "";
        String datePart = raw.split("T")[0];
        String[] parts = datePart.split("-");
        if (parts.length == 3) {
            return parts[2] + "/" + parts[1] + "/" + parts[0];
        } else {
            return raw;
        }
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
