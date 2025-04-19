package com.nhom5.shelhive;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
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

public class DangKyActivity extends AppCompatActivity {

    TextInputEditText nameInput, phoneInput, emailInput, passwordInput, confirmPasswordInput;
    CheckBox checkbox;
    FrameLayout dangKyButton;  // Cập nhật lại FrameLayout

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
        nameInput = findViewById(R.id.name);
        phoneInput = findViewById(R.id.phone);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        confirmPasswordInput = findViewById(R.id.confirm_password);
        checkbox = findViewById(R.id.checkbox);
        dangKyButton = findViewById(R.id.dangky_container);

        // Ánh xạ các TextView để hiển thị lỗi
        TextView errorName = findViewById(R.id.name_error);
        TextView errorPhone = findViewById(R.id.phone_error);
        TextView errorEmail = findViewById(R.id.email_error);
        TextView errorPassword = findViewById(R.id.password_error);
        TextView errorConfirmPassword = findViewById(R.id.confirm_password_error);

        nameInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (nameInput.getText().toString().trim().isEmpty()) {
                    errorName.setText("Tên không được để trống");
                    errorName.setVisibility(View.VISIBLE);
                } else {
                    errorName.setVisibility(View.INVISIBLE);
                }
            }
        });

        phoneInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String phone = phoneInput.getText().toString().trim();
                if (!phone.matches("^[0-9]{10,11}$")) {
                    errorPhone.setText("Số điện thoại không hợp lệ");
                    errorPhone.setVisibility(View.VISIBLE);
                } else {
                    errorPhone.setVisibility(View.INVISIBLE);
                }
            }
        });

        emailInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = emailInput.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    errorEmail.setText("Email không hợp lệ");
                    errorEmail.setVisibility(View.VISIBLE);
                } else {
                    errorEmail.setVisibility(View.INVISIBLE);
                }
            }
        });

        passwordInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String password = passwordInput.getText().toString();
                if (password.isEmpty()) {
                    errorPassword.setText("Mật khẩu không được để trống");
                    errorPassword.setVisibility(View.VISIBLE);
                } else {
                    errorPassword.setVisibility(View.INVISIBLE);
                }
            }
        });

        confirmPasswordInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String confirm = confirmPasswordInput.getText().toString();
                String original = passwordInput.getText().toString();
                if (!confirm.equals(original)) {
                    errorConfirmPassword.setText("Mật khẩu xác nhận không khớp");
                    errorConfirmPassword.setVisibility(View.VISIBLE);
                } else {
                    errorConfirmPassword.setVisibility(View.INVISIBLE);
                }
            }
        });


        // Xử lý quay lại đăng nhập
        findViewById(R.id.back_button).setOnClickListener(v -> {
            Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
            startActivity(intent);
            finish();
        });

        // Xử lý khi nhấn nút đăng ký
        dangKyButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!phone.matches("^[0-9]{10,11}$")) {
                Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checkbox.isChecked()) {
                Toast.makeText(this, "Vui lòng đồng ý với điều khoản", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu vào SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.putString("phone", phone);
            editor.putString("email", email);
            editor.putString("password", password);
            editor.apply();

            // Log dữ liệu đã lưu vào SharedPreferences
            Log.d("DangKyActivity", "Name: " + name);
            Log.d("DangKyActivity", "Phone: " + phone);
            Log.d("DangKyActivity", "Email: " + email);
            Log.d("DangKyActivity", "Password: " + password);

            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

            // Chuyển về màn hình đăng nhập
            startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
            finish();
        });

        // Text điều khoản
        setupTermsTextView();

        // TextView "đã có tài khoản?"
        TextView signinTextView = findViewById(R.id.signin);
        signinTextView.setOnClickListener(v -> {
            Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
            startActivity(intent);
            finish();
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
                        .setMessage("Đây là nội dung Điều khoản sử dụng và Chính sách bảo mật của ứng dụng. Bạn vui lòng đọc kỹ trước khi tiếp tục.")
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
