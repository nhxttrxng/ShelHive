package com.nhom5.shelhive.ui.admin.phananh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;

public class Admin_XuLiPhanAnhActivity extends AppCompatActivity {

    TextView tvTieuDe, tvLoaiVanDe, tvMoTa;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_xuliphananh);

        tvTieuDe = findViewById(R.id.tieu_de_input);
        tvLoaiVanDe = findViewById(R.id.spinner_loai_van_de);
        tvMoTa = findViewById(R.id.mo_ta_input);
        btnBack = findViewById(R.id.btn_back); // Đảm bảo bạn có ImageView này trong layout

        // Nhận dữ liệu từ Intent
        String tieuDe = getIntent().getStringExtra("tieu_de");
        String loaiVanDe = getIntent().getStringExtra("loai_van_de");
        String moTa = getIntent().getStringExtra("mo_ta");

        tvTieuDe.setText(tieuDe);
        tvLoaiVanDe.setText(loaiVanDe);
        tvMoTa.setText(moTa);

        // Xử lý khi bấm nút back tùy chỉnh
        btnBack.setOnClickListener(v -> {
            finish(); // Quay lại activity trước đó (Admin_PhanAnhActivity)
        });
    }

    // Optional: xử lý khi người dùng bấm nút back của thiết bị
}
