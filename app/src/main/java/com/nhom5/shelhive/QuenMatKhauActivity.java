package com.nhom5.shelhive;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class QuenMatKhauActivity extends AppCompatActivity {

    // Giả sử email đã đăng ký và OTP mẫu
    private static final String REGISTERED_EMAIL = "user@example.com";
    private static final String SAMPLE_OTP = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadEmailScreen();
    }

    /**
     * Màn hình nhập email (quenmatkhau.xml)
     */
    private void loadEmailScreen() {
        setContentView(R.layout.quenmatkhau);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(QuenMatKhauActivity.this, DangNhapActivity.class);
            startActivity(intent);
            finish(); // Đóng activity hiện tại
        });

        // Lấy EditText từ TextInputLayout có id "email"
        TextInputLayout emailLayout = findViewById(R.id.email);
        EditText emailEditText = emailLayout.getEditText();

        // Lấy TextView để hiển thị lỗi
        TextView emailErrorTextView = findViewById(R.id.email_error);
        emailErrorTextView.setVisibility(View.GONE);  // Ẩn lỗi ban đầu

        // Xử lý khi mất focus từ EditText email
        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String emailInput = emailEditText.getText().toString().trim();
                if (TextUtils.isEmpty(emailInput)) {
                    emailErrorTextView.setText("Vui lòng nhập email");
                    emailErrorTextView.setVisibility(View.VISIBLE);
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    emailErrorTextView.setText("Email không hợp lệ");
                    emailErrorTextView.setVisibility(View.VISIBLE);
                } else {
                    emailErrorTextView.setVisibility(View.INVISIBLE); // Ẩn lỗi nếu email hợp lệ
                }
            }
        });

        // Nút tiếp tục nằm trong FrameLayout với id "tieptuc_button"
        TextView continueButton = findViewById(R.id.tieptuc_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = emailEditText.getText().toString().trim();

                // Kiểm tra email khi nhấn "Tiếp tục"
                if (TextUtils.isEmpty(emailInput)) {
                    emailErrorTextView.setText("Vui lòng nhập email");
                    emailErrorTextView.setVisibility(View.VISIBLE);
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    emailErrorTextView.setText("Email không hợp lệ");
                    emailErrorTextView.setVisibility(View.VISIBLE);
                    return;
                }
                if (!emailInput.equalsIgnoreCase(REGISTERED_EMAIL)) {
                    emailErrorTextView.setText("Email không tồn tại");
                    emailErrorTextView.setVisibility(View.VISIBLE);
                    return;
                }

                // Email hợp lệ, chuyển sang màn hình OTP
                emailErrorTextView.setVisibility(View.INVISIBLE); // Ẩn lỗi nếu email hợp lệ
                loadOTPScreen();
            }
        });
    }

    /**
     * Màn hình nhập OTP (quenmatkhau_otp.xml)
     */
    private void loadOTPScreen() {
        setContentView(R.layout.quenmatkhau_otp);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> loadEmailScreen());
        TextView otpErrorText = findViewById(R.id.otp_error);
        otpErrorText.setVisibility(View.INVISIBLE); // Ẩn lỗi ban đầu


        // Lấy các ô OTP (6 EditText)
        final EditText otp1 = findViewById(R.id.otp_1);
        final EditText otp2 = findViewById(R.id.otp_2);
        final EditText otp3 = findViewById(R.id.otp_3);
        final EditText otp4 = findViewById(R.id.otp_4);
        final EditText otp5 = findViewById(R.id.otp_5);
        final EditText otp6 = findViewById(R.id.otp_6);

        // Thiết lập chuyển focus tự động và tự kiểm tra khi ô thứ 6 được nhập
        setupOTPInputs(otp1, otp2, otp3, otp4, otp5, otp6);

        // Xử lý chức năng "Gửi lại OTP" với đếm ngược 60 giây
        final TextView resendNow = findViewById(R.id.resend_now);
        startResendTimer(resendNow);

        resendNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resendNow.getText().toString().equalsIgnoreCase("Gửi lại ngay")) {
                    Toast.makeText(QuenMatKhauActivity.this, "Đã gửi OTP mới", Toast.LENGTH_SHORT).show();
                    startResendTimer(resendNow);
                }
            }
        });
    }

    /**
     * Thiết lập chuyển focus tự động giữa các ô OTP và tự kiểm tra khi hoàn thành ô thứ 6
     */
    private abstract class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public abstract void afterTextChanged(Editable s);
    }

    private void setupOTPInputs(final EditText otp1, final EditText otp2, final EditText otp3,
                                final EditText otp4, final EditText otp5, final EditText otp6) {
        otp1.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otp2.requestFocus();
                }
            }
        });
        otp2.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otp3.requestFocus();
                } else if (s.length() == 0) {
                    otp1.requestFocus();
                }
            }
        });
        otp3.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otp4.requestFocus();
                } else if (s.length() == 0) {
                    otp2.requestFocus();
                }
            }
        });
        otp4.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otp5.requestFocus();
                } else if (s.length() == 0) {
                    otp3.requestFocus();
                }
            }
        });
        otp5.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otp6.requestFocus();
                } else if (s.length() == 0) {
                    otp4.requestFocus();
                }
            }
        });
        otp6.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    String otpEntered = otp1.getText().toString().trim()
                            + otp2.getText().toString().trim()
                            + otp3.getText().toString().trim()
                            + otp4.getText().toString().trim()
                            + otp5.getText().toString().trim()
                            + otp6.getText().toString().trim();

                    if (otpEntered.length() == 6) {
                        TextView otpErrorText = findViewById(R.id.otp_error);

                        if (otpEntered.equals(SAMPLE_OTP)) {
                            otpErrorText.setVisibility(View.INVISIBLE);
                            loadResetPasswordScreen();
                        } else {
                            otpErrorText.setText("Mã OTP không đúng");
                            otpErrorText.setVisibility(View.VISIBLE);
                        }
                    }
                } else if (s.length() == 0) {
                    otp5.requestFocus();
                }
            }
        });
    }

    /**
     * Hàm đếm ngược 60 giây cho chức năng "Gửi lại OTP"
     */
    private void startResendTimer(final TextView resendNow) {
        resendNow.setClickable(false);
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendNow.setText("Gửi lại sau " + (millisUntilFinished / 1000) + "s");
            }
            @Override
            public void onFinish() {
                resendNow.setText("Gửi lại ngay");
                resendNow.setClickable(true);
            }
        }.start();
    }

    /**
     * Màn hình đặt lại mật khẩu (quenmatkhau_mkm.xml)
     */
    private void loadResetPasswordScreen() {
        setContentView(R.layout.quenmatkhau_mkm);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> loadOTPScreen());

        // Lấy mật khẩu mới và mật khẩu xác nhận từ các TextInputLayout
        TextInputLayout newPassLayout = findViewById(R.id.newpassword);
        TextInputEditText newPassEdit = (TextInputEditText) newPassLayout.getEditText();
        TextView newPassError = findViewById(R.id.newpassword_error);

        TextInputLayout confirmPassLayout = findViewById(R.id.confirm_newpassword);
        TextInputEditText confirmPassEdit = (TextInputEditText) confirmPassLayout.getEditText();
        TextView confirmPassError = findViewById(R.id.confirm_newpassword_error);

        // Ẩn lỗi ban đầu
        newPassError.setVisibility(View.INVISIBLE);
        confirmPassError.setVisibility(View.INVISIBLE);

        // Khi mất focus ở ô nhập mật khẩu mới
        newPassEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String newPass = newPassEdit.getText().toString().trim();
                if (newPass.isEmpty()) {
                    newPassError.setText("Vui lòng nhập mật khẩu mới");
                    newPassError.setVisibility(View.VISIBLE);
                } else if (newPass.length() < 6) {
                    newPassError.setText("Mật khẩu phải từ 6 ký tự trở lên");
                    newPassError.setVisibility(View.VISIBLE);
                } else {
                    newPassError.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Khi mất focus ở ô xác nhận mật khẩu
        confirmPassEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String newPass = newPassEdit.getText().toString().trim();
                String confirmPass = confirmPassEdit.getText().toString().trim();
                if (confirmPass.isEmpty()) {
                    confirmPassError.setText("Vui lòng xác nhận mật khẩu");
                    confirmPassError.setVisibility(View.VISIBLE);
                } else if (!confirmPass.equals(newPass)) {
                    confirmPassError.setText("Mật khẩu không khớp");
                    confirmPassError.setVisibility(View.VISIBLE);
                } else {
                    confirmPassError.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Nút xác nhận
        TextView resetContinueButton = findViewById(R.id.tieptuc_button);
        resetContinueButton.setOnClickListener(v -> {
            String newPass = newPassEdit.getText().toString().trim();
            String confirmPass = confirmPassEdit.getText().toString().trim();
            boolean isValid = true;

            if (newPass.isEmpty()) {
                newPassError.setText("Vui lòng nhập mật khẩu mới");
                newPassError.setVisibility(View.VISIBLE);
                isValid = false;
            } else if (newPass.length() < 6) {
                newPassError.setText("Mật khẩu phải từ 6 ký tự trở lên");
                newPassError.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                newPassError.setVisibility(View.INVISIBLE);
            }

            if (confirmPass.isEmpty()) {
                confirmPassError.setText("Vui lòng xác nhận mật khẩu");
                confirmPassError.setVisibility(View.VISIBLE);
                isValid = false;
            } else if (!confirmPass.equals(newPass)) {
                confirmPassError.setText("Mật khẩu không khớp");
                confirmPassError.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                confirmPassError.setVisibility(View.INVISIBLE);
            }

            if (isValid) {
                Toast.makeText(QuenMatKhauActivity.this, "Đặt lại mật khẩu thành công", Toast.LENGTH_SHORT).show();
                finish(); // Quay về trang trước (hoặc có thể chuyển về login tùy ý)
            }
        });
    }
}




