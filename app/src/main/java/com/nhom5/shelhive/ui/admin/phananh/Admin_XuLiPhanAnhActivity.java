package com.nhom5.shelhive.ui.admin.phananh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_XuLiPhanAnhActivity extends AppCompatActivity {

    private TextView tvTieuDe, tvLoaiVanDe, tvMoTa;
    private ImageView btnBack;
    private Button btnXuLi;

    private int maPhanAnh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_xuliphananh);

        initViews();
        getIntentData();
        setupEventHandlers();
    }

    private void initViews() {
        tvTieuDe = findViewById(R.id.tieu_de_input);
        tvLoaiVanDe = findViewById(R.id.spinner_loai_van_de);
        tvMoTa = findViewById(R.id.mo_ta_input);
        btnBack = findViewById(R.id.btn_back);
        btnXuLi = findViewById(R.id.btn_xu_li);
    }

    private void getIntentData() {
        maPhanAnh = getIntent().getIntExtra("ma_phan_anh", -1);
        String tieuDe = getIntent().getStringExtra("tieu_de");
        String loaiVanDe = getIntent().getStringExtra("loai_van_de");
        String moTa = getIntent().getStringExtra("mo_ta");

        tvTieuDe.setText(tieuDe != null ? tieuDe : "");
        tvLoaiVanDe.setText(loaiVanDe != null ? loaiVanDe : "");
        tvMoTa.setText(moTa != null ? moTa : "");
    }

    private void setupEventHandlers() {
        btnBack.setOnClickListener(v -> finish());

        btnXuLi.setOnClickListener(v -> {
            if (maPhanAnh == -1) {
                Toast.makeText(this, "Không có mã phản ánh hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            xuLyPhanAnh(maPhanAnh);
        });
    }

    private void xuLyPhanAnh(int id) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("tinh_trang", "đã xử lí");

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.updateTinhTrang(id, requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Admin_XuLiPhanAnhActivity.this, "Xử lí thành công!", Toast.LENGTH_SHORT).show();

                    // ✅ Gửi kết quả thành công về activity trước đó
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(Admin_XuLiPhanAnhActivity.this, "Không thể cập nhật trạng thái!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Admin_XuLiPhanAnhActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
