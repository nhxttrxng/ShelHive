package com.nhom5.shelhive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.ui.admin.Admin_TrangChuActivity;
import com.nhom5.shelhive.ui.admin.quanly.Admin_QuanLyActivity;
import com.nhom5.shelhive.ui.admin.quanly.Admin_QuanLyPhongTroActivity;
import com.nhom5.shelhive.ui.auth.DangNhapActivity;
import com.nhom5.shelhive.ui.user.User_TrangChuActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_NAME = "login_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isRemembered = sharedPreferences.getBoolean("remember", false);
        String email = sharedPreferences.getString("email", "");
        String role = sharedPreferences.getString("role", "");

        Intent intent;

        if (isRemembered && !email.isEmpty() && !role.isEmpty()) {
            if ("admin".equalsIgnoreCase(role)) {
                intent = new Intent(MainActivity.this, Admin_TrangChuActivity.class);
            } else {
                intent = new Intent(MainActivity.this, User_TrangChuActivity.class);
            }
            intent.putExtra("email", email);
        } else {
            intent = new Intent(MainActivity.this, DangNhapActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
