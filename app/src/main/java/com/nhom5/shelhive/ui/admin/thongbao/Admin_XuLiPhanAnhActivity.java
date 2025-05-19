package com.nhom5.shelhive.ui.admin.thongbao;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;

public class Admin_XuLiPhanAnhActivity extends AppCompatActivity {

    TextView tvTieuDe, tvLoaiVanDe, tvMoTa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_xuliphananh);

        tvTieuDe = findViewById(R.id.tieu_de_input);
        tvLoaiVanDe = findViewById(R.id.spinner_loai_van_de);
        tvMoTa = findViewById(R.id.mo_ta_input);

        // Nhận dữ liệu từ Intent
        String tieuDe = getIntent().getStringExtra("tieu_de");
        String loaiVanDe = getIntent().getStringExtra("loai_van_de");
        String moTa = getIntent().getStringExtra("mo_ta");

        tvTieuDe.setText(tieuDe);
        tvLoaiVanDe.setText(loaiVanDe);
        tvMoTa.setText(moTa);
    }
}
