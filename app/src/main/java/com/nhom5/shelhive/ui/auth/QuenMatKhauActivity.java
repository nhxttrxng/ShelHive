package com.nhom5.shelhive.ui.auth;

import android.app.ProgressDialog;
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
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.ForgotPasswordRequest;
import com.nhom5.shelhive.api.ResetPasswordRequest;
import com.nhom5.shelhive.api.ResendOtpRequest;
import com.nhom5.shelhive.api.VerifyOtpRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuenMatKhauActivity extends AppCompatActivity {

    private String currentEmail;
    private String currentOtp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        loadEmailScreen();
    }

    private void loadEmailScreen() {
        setContentView(R.layout.quenmatkhau);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, DangNhapActivity.class));
            finish();
        });

        TextInputLayout emailLayout = findViewById(R.id.email);
        EditText emailEditText = emailLayout.getEditText();
        TextView emailError = findViewById(R.id.email_error);
        emailError.setVisibility(View.GONE);

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateEmailField(emailEditText, emailError);
        });

        findViewById(R.id.tieptuc_button).setOnClickListener(v -> {
            if (!validateEmailField(emailEditText, emailError)) return;
            currentEmail = emailEditText.getText().toString().trim();
            // Gọi API forgot-password
            progressDialog.setMessage("Đang gửi yêu cầu...");
            progressDialog.show();
            ApiService.apiService
                    .forgotPassword(new ForgotPasswordRequest(currentEmail))
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> res) {
                            progressDialog.dismiss();
                            if (res.isSuccessful()) {
                                Toast.makeText(QuenMatKhauActivity.this,
                                        "OTP đã được gửi vào email", Toast.LENGTH_SHORT).show();
                                loadOTPScreen();
                            } else {
                                emailError.setText("Gửi OTP thất bại");
                                emailError.setVisibility(View.VISIBLE);
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressDialog.dismiss();
                            emailError.setText("Lỗi kết nối: " + t.getMessage());
                            emailError.setVisibility(View.VISIBLE);
                        }
                    });
        });
    }

    private boolean validateEmailField(EditText emailEditText, TextView emailError) {
        String email = emailEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailError.setText("Vui lòng nhập email");
            emailError.setVisibility(View.VISIBLE);
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.setText("Email không hợp lệ");
            emailError.setVisibility(View.VISIBLE);
            return false;
        }
        emailError.setVisibility(View.INVISIBLE);
        return true;
    }

    private void loadOTPScreen() {
        setContentView(R.layout.quenmatkhau_otp);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> loadEmailScreen());

        TextView otpError = findViewById(R.id.otp_error);
        otpError.setVisibility(View.INVISIBLE);

        EditText otp1 = findViewById(R.id.otp_1);
        EditText otp2 = findViewById(R.id.otp_2);
        EditText otp3 = findViewById(R.id.otp_3);
        EditText otp4 = findViewById(R.id.otp_4);
        EditText otp5 = findViewById(R.id.otp_5);
        EditText otp6 = findViewById(R.id.otp_6);

        setupOTPInputs(otp1, otp2, otp3, otp4, otp5, otp6, otpError);

        TextView resendNow = findViewById(R.id.resend_now);
        startResendTimer(resendNow);

        resendNow.setOnClickListener(v -> {
            if ("Gửi lại ngay".equalsIgnoreCase(resendNow.getText().toString())) {
                // Gọi API resend-otp
                progressDialog.setMessage("Đang gửi lại OTP...");
                progressDialog.show();
                ApiService.apiService
                        .resendOtp(new ResendOtpRequest(currentEmail))
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> res) {
                                progressDialog.dismiss();
                                if (res.isSuccessful()) {
                                    Toast.makeText(QuenMatKhauActivity.this,
                                            "Đã gửi OTP mới", Toast.LENGTH_SHORT).show();
                                    startResendTimer(resendNow);
                                } else {
                                    Toast.makeText(QuenMatKhauActivity.this,
                                            "Gửi lại thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(QuenMatKhauActivity.this,
                                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void setupOTPInputs(EditText otp1,
                                EditText otp2,
                                EditText otp3,
                                EditText otp4,
                                EditText otp5,
                                EditText otp6,
                                TextView otpErrorText) {

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
                    // Khi đủ 6 ký tự thì gom lại và gọi API verifyOtp
                    String otpEntered = otp1.getText().toString().trim()
                            + otp2.getText().toString().trim()
                            + otp3.getText().toString().trim()
                            + otp4.getText().toString().trim()
                            + otp5.getText().toString().trim()
                            + otp6.getText().toString().trim();

                    if (otpEntered.length() == 6) {
                        progressDialog.setMessage("Đang xác thực OTP...");
                        progressDialog.show();
                        ApiService.apiService
                                .verifyOtp(new VerifyOtpRequest(currentEmail, otpEntered))
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> res) {
                                        progressDialog.dismiss();
                                        if (res.isSuccessful()) {
                                            currentOtp = otpEntered;
                                            loadResetPasswordScreen();
                                        } else {
                                            otpErrorText.setText("OTP không đúng");
                                            otpErrorText.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        progressDialog.dismiss();
                                        otpErrorText.setText("Lỗi kết nối");
                                        otpErrorText.setVisibility(View.VISIBLE);
                                    }
                                });
                    }
                } else if (s.length() == 0) {
                    otp5.requestFocus();
                }
            }
        });
    }

    private void startResendTimer(final TextView resendNow) {
        resendNow.setClickable(false);
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                resendNow.setText("Gửi lại sau " + (millisUntilFinished/1000) + "s");
            }
            public void onFinish() {
                resendNow.setText("Gửi lại ngay");
                resendNow.setClickable(true);
            }
        }.start();
    }

    private void loadResetPasswordScreen() {
        setContentView(R.layout.quenmatkhau_mkm);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> loadOTPScreen());

        TextInputLayout newPassLayout = findViewById(R.id.newpassword);
        EditText newPassEdit = newPassLayout.getEditText();
        TextView newPassError = findViewById(R.id.newpassword_error);

        TextInputLayout confirmLayout = findViewById(R.id.confirm_newpassword);
        EditText confirmEdit = confirmLayout.getEditText();
        TextView confirmError = findViewById(R.id.confirm_newpassword_error);

        newPassError.setVisibility(View.INVISIBLE);
        confirmError.setVisibility(View.INVISIBLE);

        newPassEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateNewPassword(newPassEdit, newPassError);
        });
        confirmEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateConfirmPassword(newPassEdit, confirmEdit, confirmError);
        });

        findViewById(R.id.tieptuc_button).setOnClickListener(v -> {
            boolean valid1 = validateNewPassword(newPassEdit, newPassError);
            boolean valid2 = validateConfirmPassword(newPassEdit, confirmEdit, confirmError);
            if (!valid1 || !valid2) return;

            String newPass = newPassEdit.getText().toString().trim();
            // Gọi API reset-password
            progressDialog.setMessage("Đang đặt lại mật khẩu...");
            progressDialog.show();
            ApiService.apiService
                    .resetPassword(new ResetPasswordRequest(currentEmail, currentOtp, newPass))
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> res) {
                            progressDialog.dismiss();
                            if (res.isSuccessful()) {
                                Toast.makeText(QuenMatKhauActivity.this,
                                        "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                finish(); // Trở về login
                            } else {
                                Toast.makeText(QuenMatKhauActivity.this,
                                        "Thất bại, thử lại", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(QuenMatKhauActivity.this,
                                    "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private boolean validateNewPassword(EditText passEdit, TextView errorView) {
        String p = passEdit.getText().toString().trim();
        if (p.isEmpty()) {
            errorView.setText("Vui lòng nhập mật khẩu mới");
            errorView.setVisibility(View.VISIBLE);
            return false;
        } else if (p.length() < 6) {
            errorView.setText("Mật khẩu phải từ 6 ký tự");
            errorView.setVisibility(View.VISIBLE);
            return false;
        }
        errorView.setVisibility(View.INVISIBLE);
        return true;
    }

    private boolean validateConfirmPassword(EditText newPass, EditText confirmEdit, TextView errorView) {
        String c = confirmEdit.getText().toString().trim();
        if (c.isEmpty()) {
            errorView.setText("Vui lòng xác nhận mật khẩu");
            errorView.setVisibility(View.VISIBLE);
            return false;
        } else if (!c.equals(newPass.getText().toString().trim())) {
            errorView.setText("Mật khẩu không khớp");
            errorView.setVisibility(View.VISIBLE);
            return false;
        }
        errorView.setVisibility(View.INVISIBLE);
        return true;
    }

    private abstract class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
        @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
        public abstract void afterTextChanged(Editable s);
    }
}
