package com.nhom5.shelhive.ui.admin.thongbao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.ThongBaoRequest;
import com.nhom5.shelhive.ui.model.ThongBao;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_TaoThongBao extends AppCompatActivity {

    private EditText edtThongBao;
    private Button btnTaoThongBao;
    private int maDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_themthongbao);
        maDay = getIntent().getIntExtra("MA_DAY", -1);
        if (maDay == -1) {
            Toast.makeText(this, "Không tìm thấy mã dãy", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Initialize views
        edtThongBao = findViewById(R.id.edt_noi_dung_thong_bao);
        btnTaoThongBao = findViewById(R.id.btn_gui_thong_bao);
        ImageView btnBack=findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_TaoThongBao.this, Admin_ThongBaoActivity.class);
            intent.putExtra("MA_DAY", maDay); // TRUYỀN LẠI MA_DAY
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Set button click listener
        btnTaoThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thongBaoContent = edtThongBao.getText().toString().trim();

                // Check if the notification content is not empty
                if (!thongBaoContent.isEmpty()) {
                    // Handle the notification creation (e.g., send it to the server or display it in the app)
                    createThongBao(thongBaoContent);
                } else {
                    // Show error message if content is empty
                    Toast.makeText(Admin_TaoThongBao.this, "Vui lòng nhập thông báo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to handle notification creation logic
    private void createThongBao(String noi_dung) {
        Log.d("TAO_THONG_BAO", "Bắt đầu tạo thông báo. maDay=" + maDay + ", noi_dung=" + noi_dung);

        ThongBaoRequest thongBao = new ThongBaoRequest(maDay, noi_dung);

        ApiService.apiService.taoThongBao(thongBao).enqueue(new Callback<ThongBaoRequest>() {
            @Override
            public void onResponse(Call<ThongBaoRequest> call, Response<ThongBaoRequest> response) {
                if (response.isSuccessful()) {
                    Log.d("TAO_THONG_BAO", "Tạo thông báo thành công. Response: " + response.body());
                    Toast.makeText(Admin_TaoThongBao.this, "Tạo thông báo thành công!", Toast.LENGTH_SHORT).show();
                    edtThongBao.setText("");
                } else {
                    Log.e("TAO_THONG_BAO", "Tạo thông báo thất bại. Mã lỗi: " + response.code());
                    Toast.makeText(Admin_TaoThongBao.this, "Tạo thông báo thất bại", Toast.LENGTH_SHORT).show();
                }

                setResult(RESULT_OK); // Thông báo kết quả thành công
                finish(); // Quay lại activity trước
            }

            @Override
            public void onFailure(Call<ThongBaoRequest> call, Throwable t) {
                Log.e("TAO_THONG_BAO", "Lỗi kết nối API: " + t.getMessage(), t);
                Toast.makeText(Admin_TaoThongBao.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
