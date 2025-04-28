package com.nhom5.shelhive.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.LoginRequest;
import com.nhom5.shelhive.api.LoginResponse;
import com.nhom5.shelhive.ui.admin.Admin_TrangChuActivity;
import com.nhom5.shelhive.ui.user.User_TrangChuActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangNhapActivity extends AppCompatActivity {

    private TextInputEditText edtEmail, edtPassword;
    private TextView emailError, passwordError, btnLogin, btnSignup;
    private CheckBox checkBoxRemember;
    private TextView tvForgotPassword;
    private ImageView imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhap);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        emailError = findViewById(R.id.email_error);
        passwordError = findViewById(R.id.password_error);
        btnLogin = findViewById(R.id.dangnhap_button);
        btnSignup = findViewById(R.id.signup);
        checkBoxRemember = findViewById(R.id.checkbox_remember);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        imgButton = findViewById(R.id.my_image);

        btnLogin.setOnClickListener(view -> login());
        btnSignup.setOnClickListener(view -> {
            Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
            startActivity(intent);
        });
        tvForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(DangNhapActivity.this, QuenMatKhauActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        boolean hasError = false;

        emailError.setVisibility(View.INVISIBLE);
        passwordError.setVisibility(View.INVISIBLE);

        if (email.isEmpty()) {
            emailError.setText("Email không được để trống");
            emailError.setVisibility(View.VISIBLE);
            hasError = true;
        }
        if (password.isEmpty()) {
            passwordError.setText("Mật khẩu không được để trống");
            passwordError.setVisibility(View.VISIBLE);
            hasError = true;
        }

        if (hasError) return;

        LoginRequest loginRequest = new LoginRequest(email, password);

        ApiService.apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    String role = loginResponse.getRole();
                    if ("admin".equalsIgnoreCase(role)) {
                        startActivity(new Intent(DangNhapActivity.this, Admin_TrangChuActivity.class));
                    } else {
                        startActivity(new Intent(DangNhapActivity.this, User_TrangChuActivity.class));
                    }
                    finish();
                } else {
                    passwordError.setText("Email hoặc mật khẩu không đúng");
                    passwordError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                passwordError.setText("Lỗi kết nối. Vui lòng thử lại!");
                passwordError.setVisibility(View.VISIBLE);
                t.printStackTrace();
            }
        });
    }
}
