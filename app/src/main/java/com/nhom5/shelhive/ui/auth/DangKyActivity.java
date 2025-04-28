package com.nhom5.shelhive.ui.auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.RegisterRequest;
import com.nhom5.shelhive.api.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangKyActivity extends AppCompatActivity {

    TextInputEditText nameInput, phoneInput, emailInput, passwordInput, confirmPasswordInput;
    TextView errorName, errorPhone, errorEmail, errorPassword, errorConfirmPassword;
    CheckBox checkbox;
    FrameLayout dangKyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dangky), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        mappingViews();

        // Set listener kiểm tra lỗi
        setInputValidation();

        // Set click cho các button
        setupButtonListeners();

        // Setup text Điều khoản
        setupTermsTextView();
    }

    private void mappingViews() {
        nameInput = findViewById(R.id.name);
        phoneInput = findViewById(R.id.phone);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        confirmPasswordInput = findViewById(R.id.confirm_password);

        errorName = findViewById(R.id.name_error);
        errorPhone = findViewById(R.id.phone_error);
        errorEmail = findViewById(R.id.email_error);
        errorPassword = findViewById(R.id.password_error);
        errorConfirmPassword = findViewById(R.id.confirm_password_error);

        checkbox = findViewById(R.id.checkbox);
        dangKyButton = findViewById(R.id.dangky_container);
    }

    private void setInputValidation() {
        nameInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateName();
        });
        phoneInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validatePhone();
        });
        emailInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateEmail();
        });
        passwordInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validatePassword();
        });
        confirmPasswordInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateConfirmPassword();
        });
    }

    private void setupButtonListeners() {
        findViewById(R.id.back_button).setOnClickListener(v -> {
            startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
            finish();
        });

        findViewById(R.id.signin).setOnClickListener(v -> {
            startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
            finish();
        });

        dangKyButton.setOnClickListener(v -> {
            if (validateForm()) {
                callRegisterApi();
            }
        });
    }

    private boolean validateName() {
        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            errorName.setText("Tên không được để trống");
            errorName.setVisibility(View.VISIBLE);
            return false;
        }
        errorName.setVisibility(View.INVISIBLE);
        return true;
    }

    private boolean validatePhone() {
        String phone = phoneInput.getText().toString().trim();
        if (!phone.matches("^[0-9]{10,11}$")) {
            errorPhone.setText("Số điện thoại không hợp lệ");
            errorPhone.setVisibility(View.VISIBLE);
            return false;
        }
        errorPhone.setVisibility(View.INVISIBLE);
        return true;
    }

    private boolean validateEmail() {
        String email = emailInput.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorEmail.setText("Email không hợp lệ");
            errorEmail.setVisibility(View.VISIBLE);
            return false;
        }
        errorEmail.setVisibility(View.INVISIBLE);
        return true;
    }

    private boolean validatePassword() {
        String password = passwordInput.getText().toString();
        if (password.isEmpty()) {
            errorPassword.setText("Mật khẩu không được để trống");
            errorPassword.setVisibility(View.VISIBLE);
            return false;
        }
        errorPassword.setVisibility(View.INVISIBLE);
        return true;
    }

    private boolean validateConfirmPassword() {
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        if (!confirmPassword.equals(password)) {
            errorConfirmPassword.setText("Mật khẩu xác nhận không khớp");
            errorConfirmPassword.setVisibility(View.VISIBLE);
            return false;
        }
        errorConfirmPassword.setVisibility(View.INVISIBLE);
        return true;
    }

    private boolean validateForm() {
        boolean isValid = true;
        isValid &= validateName();
        isValid &= validatePhone();
        isValid &= validateEmail();
        isValid &= validatePassword();
        isValid &= validateConfirmPassword();

        if (!checkbox.isChecked()) {
            Toast.makeText(this, "Vui lòng đồng ý với điều khoản", Toast.LENGTH_SHORT).show();
            return false;
        }
        return isValid;
    }

    private void callRegisterApi() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        RegisterRequest registerRequest = new RegisterRequest(
                emailInput.getText().toString().trim(),
                nameInput.getText().toString().trim(),
                phoneInput.getText().toString().trim(),
                passwordInput.getText().toString().trim()
        );

        apiService.register(registerRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
                    finish();
                } else {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DangKyActivity.this, "Lỗi kết nối. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupTermsTextView() {
        TextView termsTextView = findViewById(R.id.dieukhoan_text);
        String fullText = "Tôi đã đọc và đồng ý với Điều khoản sử dụng & chính sách bảo mật.";
        SpannableString spannableString = new SpannableString(fullText);

        int start = fullText.indexOf("Điều khoản sử dụng & chính sách bảo mật");
        int end = start + "Điều khoản sử dụng & chính sách bảo mật".length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                new AlertDialog.Builder(DangKyActivity.this)
                        .setTitle("Điều khoản & Chính sách")
                        .setMessage("Đây là nội dung Điều khoản sử dụng và Chính sách bảo mật của ứng dụng.")
                        .setPositiveButton("Tôi đã hiểu", null)
                        .show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                int yellowColor = ContextCompat.getColor(DangKyActivity.this, R.color.yellow);
                ds.setColor(yellowColor);
                ds.setUnderlineText(false);
            }
        };

        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsTextView.setText(spannableString);
        termsTextView.setMovementMethod(LinkMovementMethod.getInstance());
        termsTextView.setHighlightColor(Color.TRANSPARENT);
    }
}
