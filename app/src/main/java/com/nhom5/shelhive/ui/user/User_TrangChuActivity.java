package com.nhom5.shelhive.ui.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.FullUserInfoResponse;
import com.nhom5.shelhive.ui.auth.DangNhapActivity;
import com.nhom5.shelhive.ui.common.customviews.CustomTypefaceSpan;
import com.nhom5.shelhive.ui.common.customviews.HexagonImageView;
import com.nhom5.shelhive.ui.user.hoadon.User_RoomBillDetailActivity;
import com.nhom5.shelhive.ui.user.phananh.User_PhanAnhActivity;
import com.nhom5.shelhive.ui.user.thongbao.User_ThongBaoActivity;
import com.nhom5.shelhive.ui.user.thongke.User_ThongKeActivity;
import com.nhom5.shelhive.ui.user.thongtin.User_ThongTinActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_TrangChuActivity extends AppCompatActivity {

    private String maPhong = null;
    private String email = null;
    private View popupLayout;
    private Integer maDay = null;

    // Các biến ánh xạ view để dùng lại trong onResume
    private TextView helloText, emptyMessage, text1, text2, text3;
    private LinearLayout infoContainer;
    private HexagonImageView avt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_trangchu);

        // Lấy email từ Intent hoặc SharedPreferences
        email = getIntent().getStringExtra("email");
        if (email == null) {
            SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
            email = prefs.getString("email", null);
        }

        // Ánh xạ View (gán vào biến toàn cục để dùng lại trong onResume)
        helloText = findViewById(R.id.hello);
        infoContainer = findViewById(R.id.info_container);
        emptyMessage = findViewById(R.id.empty_message);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        avt = findViewById(R.id.avatar);

        FrameLayout hoadon = findViewById(R.id.hoadon);
        FrameLayout thongbao = findViewById(R.id.thongbao);
        FrameLayout phananh = findViewById(R.id.phananh);
        FrameLayout thongke = findViewById(R.id.thongke);
        LinearLayout nav_profile = findViewById(R.id.nav_profile);
        LinearLayout nav_logout = findViewById(R.id.nav_logout);

        popupLayout = findViewById(R.id.popup_layout);
        ImageView closeButton = findViewById(R.id.close_button);
        if (closeButton != null) {
            closeButton.setOnClickListener(v -> popupLayout.setVisibility(View.GONE));
        }

        // Dùng chung một hàm truyền cả maPhong và email cho tất cả activity
        hoadon.setOnClickListener(v -> startActivityWithPhongVaEmail(User_RoomBillDetailActivity.class));
        thongbao.setOnClickListener(v -> startActivityWithPhongVaEmail(User_ThongBaoActivity.class));
        phananh.setOnClickListener(v -> startActivityWithPhongVaEmail(User_PhanAnhActivity.class));
        thongke.setOnClickListener(v -> startActivityWithPhongVaEmail(User_ThongKeActivity.class));
        nav_profile.setOnClickListener(v -> startActivityWithPhongVaEmail(User_ThongTinActivity.class));

        // Đăng xuất giữ nguyên, không truyền email/maPhong
        nav_logout.setOnClickListener(v -> {
            View popup = findViewById(R.id.popup_logout);
            popup.setVisibility(View.VISIBLE);
            popup.bringToFront();

            ImageView closeBtn = popup.findViewById(R.id.close_button);
            ImageView logoutBtn = popup.findViewById(R.id.logout_button);

            closeBtn.setOnClickListener(v1 -> popup.setVisibility(View.GONE));
            logoutBtn.setOnClickListener(v2 -> {
                SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("email");
                editor.remove("password");
                editor.remove("remember");
                editor.apply();

                startActivity(new Intent(this, DangNhapActivity.class));
                finish();
            });
        });
    }

    /** Dùng cho mọi chuyển activity, truyền luôn cả maPhong + email */
    private void startActivityWithPhongVaEmail(Class<?> activityClass) {
        if (maPhong != null && !maPhong.trim().isEmpty() && !maPhong.equals("null")) {
            Intent intent = new Intent(User_TrangChuActivity.this, activityClass);
            intent.putExtra("maPhong", maPhong);
            intent.putExtra("maDay", maDay);
            if (email != null) {
                intent.putExtra("email", email);
            }
            startActivity(intent);
        } else {
            if (popupLayout != null) {
                popupLayout.setVisibility(View.VISIBLE);
                popupLayout.bringToFront();
            }
        }
    }

    // ---- Thêm hàm này để mỗi lần quay lại màn hình là tự động load lại dữ liệu mới nhất ----
    @Override
    protected void onResume() {
        super.onResume();
        reloadUserInfo();
    }

    private void reloadUserInfo() {
        if (email != null) {
            ApiService.apiService.getFullInfoByEmail(email).enqueue(new Callback<FullUserInfoResponse>() {
                @Override
                public void onResponse(Call<FullUserInfoResponse> call, Response<FullUserInfoResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        FullUserInfoResponse info = response.body();

                        // Xin chào
                        String name = info.getHo_ten();
                        String fullText = "Xin chào, " + name + "!";
                        SpannableString spannable = new SpannableString(fullText);
                        int start = fullText.indexOf(name);
                        int end = start + name.length();
                        spannable.setSpan(new CustomTypefaceSpan(ResourcesCompat.getFont(User_TrangChuActivity.this, R.font.dancing_script_bold)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannable.setSpan(new ForegroundColorSpan(getColor(R.color.darkyellow)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        helloText.setText(spannable);
                        helloText.setTextColor(getColor(R.color.brown));

                        // Ảnh đại diện
                        String avtPath = info.getAvt();
                        if (avtPath != null && !avtPath.isEmpty()) {
                            String fullAvtUrl = "http://221.132.33.173:3000" + avtPath;
                            Glide.with(User_TrangChuActivity.this).load(fullAvtUrl).placeholder(R.drawable.default_avatar).into(avt);
                        } else {
                            avt.setImageResource(R.drawable.default_avatar);
                        }

                        // Thông tin phòng
                        maPhong = String.valueOf(info.getMa_phong());
                        maDay = info.getMa_day();
                        String tenTro = info.getTen_tro();
                        String diaChi = info.getDia_chi();

                        if (maPhong != null && tenTro != null && diaChi != null) {
                            infoContainer.setVisibility(View.VISIBLE);
                            emptyMessage.setVisibility(View.GONE);
                            String soCuoiMaPhong = maPhong.length() >= 2 ? maPhong.substring(maPhong.length() - 2) : maPhong;
                            text1.setText("Phòng " + soCuoiMaPhong);
                            text2.setText(tenTro);
                            text3.setText(diaChi);
                        } else {
                            infoContainer.setVisibility(View.GONE);
                            emptyMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        infoContainer.setVisibility(View.GONE);
                        emptyMessage.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<FullUserInfoResponse> call, Throwable t) {
                    infoContainer.setVisibility(View.GONE);
                    emptyMessage.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}