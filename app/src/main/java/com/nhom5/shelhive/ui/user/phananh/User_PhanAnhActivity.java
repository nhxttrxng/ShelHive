package com.nhom5.shelhive.ui.user.phananh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.PhanAnhRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_PhanAnhActivity extends AppCompatActivity {

    private EditText edtTieuDe, edtMoTa;
    private Spinner spinnerLoaiVanDe;
    private Button btnBaoCao;
    private ImageView btnBack;
    private TextView tvTenPhong;
    private int maPhong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_phananh);

        edtTieuDe = findViewById(R.id.tieu_de_input);
        edtMoTa = findViewById(R.id.mo_ta_input);
        spinnerLoaiVanDe = findViewById(R.id.spinner_loai_van_de);
        btnBaoCao = findViewById(R.id.btn_bao_cao);
        btnBack = findViewById(R.id.btn_back);
        tvTenPhong = findViewById(R.id.tv_motel_name);

        // Danh sách loại sự cố
        String[] loaiVanDe = {"Điện", "Nước", "Wifi", "Vệ sinh", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, loaiVanDe);
        spinnerLoaiVanDe.setAdapter(adapter);

        // Nhận dữ liệu từ intent
        String maPhongStr = getIntent().getStringExtra("maPhong");
        String tenPhong = getIntent().getStringExtra("tenPhong");

        try {
            maPhong = Integer.parseInt(maPhongStr);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: không xác định được mã phòng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (tenPhong != null) {
            tvTenPhong.setText("Phòng " + tenPhong);
        }

        btnBack.setOnClickListener(v -> finish());

        btnBaoCao.setOnClickListener(v -> {
            String tieuDe = edtTieuDe.getText().toString().trim();
            String noiDung = edtMoTa.getText().toString().trim();
            String loaiSuCo = spinnerLoaiVanDe.getSelectedItem().toString();

            if (tieuDe.isEmpty() || noiDung.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tiêu đề và nội dung phản ánh", Toast.LENGTH_SHORT).show();
                return;
            }

            PhanAnhRequest phanAnh = new PhanAnhRequest(maPhong, tieuDe, loaiSuCo, noiDung);


            ApiService.apiService.createPhanAnh(phanAnh).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(User_PhanAnhActivity.this, "Gửi phản ánh thành công!", Toast.LENGTH_LONG).show();
                        edtTieuDe.setText("");
                        edtMoTa.setText("");
                        spinnerLoaiVanDe.setSelection(0);
                    } else {
                        Toast.makeText(User_PhanAnhActivity.this, "Gửi thất bại. Thử lại sau!", Toast.LENGTH_LONG).show();
                        Log.e("PHANANH_API", "Response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(User_PhanAnhActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("PHANANH_API", "onFailure: " + t.getMessage());
                }
            });
        });
    }
}
