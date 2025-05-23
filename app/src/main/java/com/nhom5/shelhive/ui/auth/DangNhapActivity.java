package com.nhom5.shelhive.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetUserResponse;
import com.nhom5.shelhive.api.LoginRequest;
import com.nhom5.shelhive.api.LoginResponse;
import com.nhom5.shelhive.ui.admin.Admin_TrangChuActivity;
import com.nhom5.shelhive.ui.user.User_TrangChuActivity;
import com.nhom5.shelhive.ui.user.xacthuc.User_XacThucActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangNhapActivity extends AppCompatActivity {

    private TextInputEditText edtEmail, edtPassword;
    private TextView emailError, passwordError, btnLogin, btnSignup;
    private CheckBox checkBoxRemember;
    private TextView tvForgotPassword;
    private ImageView imgButton;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "login_prefs";

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

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        loadSavedLogin();

        edtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = edtEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    emailError.setText("Email không được để trống");
                    emailError.setVisibility(View.VISIBLE);
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError.setText("Email không hợp lệ");
                    emailError.setVisibility(View.VISIBLE);
                } else {
                    emailError.setVisibility(View.INVISIBLE);
                }
            }
        });

        edtPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String password = edtPassword.getText().toString().trim();
                if (password.isEmpty()) {
                    passwordError.setText("Mật khẩu không được để trống");
                    passwordError.setVisibility(View.VISIBLE);
                } else if (password.length() < 6) {
                    passwordError.setText("Mật khẩu phải có ít nhất 6 ký tự");
                    passwordError.setVisibility(View.VISIBLE);
                } else {
                    passwordError.setVisibility(View.INVISIBLE);
                }
            }
        });

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

    private void loadSavedLogin() {
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");
        boolean isRemembered = sharedPreferences.getBoolean("remember", false);

        if (isRemembered) {
            edtEmail.setText(savedEmail);
            edtPassword.setText(savedPassword);
            checkBoxRemember.setChecked(true);
        }
    }

    private void saveLoginInfo(String email, String password, boolean remember, String role) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (remember) {
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putBoolean("remember", true);
            editor.putString("role", role);
        } else {
            editor.clear();
        }
        editor.apply();
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

                    saveLoginInfo(email, password, checkBoxRemember.isChecked(), role);

                    // Nếu là admin => vào luôn trang chủ admin
                    if ("admin".equalsIgnoreCase(role)) {
                        Intent intent = new Intent(DangNhapActivity.this, Admin_TrangChuActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                        return;
                    }

                    // Nếu là user thì gọi API lấy chi tiết user, kiểm tra cccd
                    ApiService.apiService.getUserByEmail(email).enqueue(new Callback<GetUserResponse>() {
                        @Override
                        public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String cccd = response.body().getCccd();

                                if (cccd == null || cccd.trim().isEmpty()) {
                                    // Chuyển tới activity xác thực
                                    Intent xacThucIntent = new Intent(DangNhapActivity.this, User_XacThucActivity.class);
                                    xacThucIntent.putExtra("email", email); // nếu cần truyền email
                                    startActivity(xacThucIntent);
                                    finish();
                                } else {
                                    // Đã có CCCD, chuyển vào trang chủ user
                                    Intent intent = new Intent(DangNhapActivity.this, User_TrangChuActivity.class);
                                    intent.putExtra("email", email);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                passwordError.setText("Lỗi kiểm tra CCCD người dùng!");
                                passwordError.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<GetUserResponse> call, Throwable t) {
                            passwordError.setText("Lỗi kết nối khi lấy thông tin người dùng!");
                            passwordError.setVisibility(View.VISIBLE);
                        }
                    });

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
