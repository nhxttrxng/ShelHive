package com.nhom5.shelhive.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.CheckVerifyRequest;
import com.nhom5.shelhive.api.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XacThucMailActivity extends AppCompatActivity {

    TextView xacThucButton;
    String email; // Email truyền từ DangKyActivity qua để xác thực

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xacthucmail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgot_password), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = getIntent().getStringExtra("email");

        mappingViews();
        setupButtonListeners();
    }

    private void mappingViews() {
        xacThucButton = findViewById(R.id.xacthuc_button);
    }

    private void setupButtonListeners() {
        findViewById(R.id.back_button).setOnClickListener(v -> {
            finish(); // Quay lại đăng ký
        });

        xacThucButton.setOnClickListener(v -> {
            callVerifyApi();
        });
    }

    private void callVerifyApi() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Tạo object CheckVerifyRequest từ email
        CheckVerifyRequest request = new CheckVerifyRequest(email);

        apiService.checkVerify(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(XacThucMailActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(XacThucMailActivity.this, DangNhapActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(XacThucMailActivity.this, "Xác thực thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(XacThucMailActivity.this, "Lỗi kết nối. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
