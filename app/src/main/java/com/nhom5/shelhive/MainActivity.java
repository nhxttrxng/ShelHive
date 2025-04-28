package com.nhom5.shelhive;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.ui.admin.Admin_QuanLyActivity;
import com.nhom5.shelhive.ui.auth.DangNhapActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mở trực tiếp HoaDonActivity
        Intent intent = new Intent(MainActivity.this, DangNhapActivity.class);
        startActivity(intent);

        // Đóng MainActivity nếu không cần quay lại
        finish();
    }
}