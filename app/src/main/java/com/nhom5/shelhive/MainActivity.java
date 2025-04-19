package com.nhom5.shelhive;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nhom5.shelhive.admin.Admin_HoaDonActivity;
import com.nhom5.shelhive.admin.Admin_TrangChuActivity;
import com.nhom5.shelhive.user.User_ThongTinActivity;
import com.nhom5.shelhive.user.User_TrangChuActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mở trực tiếp HoaDonActivity
        Intent intent = new Intent(MainActivity.this, Admin_TrangChuActivity.class);
        startActivity(intent);

        // Đóng MainActivity nếu không cần quay lại
        finish();
    }
}