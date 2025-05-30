package com.nhom5.shelhive.ui.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetAdminByEmailResponse;
import com.nhom5.shelhive.ui.admin.hoadon.Admin_MotelListActivity;
import com.nhom5.shelhive.ui.admin.phananh.Admin_PhanAnh_NhaTro;
import com.nhom5.shelhive.ui.admin.quanly.Admin_QuanLyActivity;
import com.nhom5.shelhive.ui.admin.thongbao.Admin_ThongBao_NhaTro;
import com.nhom5.shelhive.ui.admin.thongke.Admin_ThongKe_NhaTro;
import com.nhom5.shelhive.ui.admin.thongtin.Admin_ThongTinActivity;
import com.nhom5.shelhive.ui.auth.DangNhapActivity;
import com.nhom5.shelhive.ui.common.customviews.CustomTypefaceSpan;
import com.nhom5.shelhive.ui.common.customviews.HexagonImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_TrangChuActivity extends AppCompatActivity {
    private String finalEmail;
    private HexagonImageView avt;
    private TextView helloText;
    private Typeface dancing;
    private Typeface nunito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_trangchu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.admin_trangchu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo view và font
        helloText = findViewById(R.id.hello);
        dancing = ResourcesCompat.getFont(this, R.font.dancing_script_bold);
        nunito = ResourcesCompat.getFont(this, R.font.nunito_semibold);
        avt = findViewById(R.id.avatar);

        // Lấy email
        String email = getIntent().getStringExtra("email");
        if (email == null) {
            SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
            email = prefs.getString("email", null);
        }
        finalEmail = email;

        // Gọi lần đầu khi khởi tạo
        if (finalEmail != null) {
            loadAdminInfo(finalEmail);
        } else {
            helloText.setText("Xin chào!");
        }

        // Sự kiện chuyển các màn
        FrameLayout btnQuanLy = findViewById(R.id.quanly);
        FrameLayout btnHoaDon = findViewById(R.id.hoadon);
        FrameLayout btnThongBao = findViewById(R.id.thongbao);
        FrameLayout btnPhanAnh = findViewById(R.id.phananh);
        FrameLayout btnThongKe = findViewById(R.id.thongke);

        LinearLayout btnNavProfile = findViewById(R.id.nav_profile);
        LinearLayout btnNavLogout = findViewById(R.id.nav_logout);

        btnQuanLy.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_QuanLyActivity.class);
            intent.putExtra("email", finalEmail);
            startActivity(intent);
        });

        btnHoaDon.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_MotelListActivity.class);
            intent.putExtra("email", finalEmail);
            startActivity(intent);
        });

        btnThongBao.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_ThongBao_NhaTro.class);
            intent.putExtra("email", finalEmail);
            startActivity(intent);
        });

        btnPhanAnh.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_PhanAnh_NhaTro.class);
            intent.putExtra("email", finalEmail);
            startActivity(intent);
        });

        btnThongKe.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_ThongKe_NhaTro.class);
            intent.putExtra("email", finalEmail);
            startActivity(intent);
        });

        btnNavProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_ThongTinActivity.class);
            intent.putExtra("email", finalEmail);
            startActivity(intent);
        });

        btnNavLogout.setOnClickListener(v -> {
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

    @Override
    protected void onResume() {
        super.onResume();
        if (finalEmail != null) {
            loadAdminInfo(finalEmail);
        }
    }

    // Đưa đoạn load thông tin admin vào method này
    private void loadAdminInfo(String email) {
        ApiService.apiService.getAdminByEmail(email).enqueue(new Callback<GetAdminByEmailResponse>() {
            @Override
            public void onResponse(Call<GetAdminByEmailResponse> call, Response<GetAdminByEmailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String name = response.body().getHo_ten();
                    String fullText = "Xin chào, " + name + "!";
                    SpannableString spannable = new SpannableString(fullText);
                    int start = fullText.indexOf(name);
                    int end = start + name.length();

                    String avtPath = response.body().getAvt();
                    if (avtPath != null && !avtPath.isEmpty()) {
                        String fullAvtUrl = "http://221.132.33.173:3000" + avtPath;
                        Glide.with(Admin_TrangChuActivity.this).load(fullAvtUrl).placeholder(R.drawable.default_avatar).into(avt);
                    } else {
                        avt.setImageResource(R.drawable.default_avatar);
                    }

                    spannable.setSpan(new CustomTypefaceSpan(dancing), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new ForegroundColorSpan(getColor(R.color.darkyellow)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    helloText.setText(spannable);
                    helloText.setTypeface(nunito);
                    helloText.setTextColor(getColor(R.color.brown));
                } else {
                    helloText.setText("Xin chào!");
                }
            }

            @Override
            public void onFailure(Call<GetAdminByEmailResponse> call, Throwable t) {
                helloText.setText("Xin chào!");
            }
        });
    }
}
