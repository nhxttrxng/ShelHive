package com.nhom5.shelhive.ui.admin.thongbao;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_XoaThongBaoActivity extends AppCompatActivity {

    EditText edtNoiDung;
    Button btnXoa, btnSua;
    int maThongBao;
    int maDay;
    String ngayTao;  // Nếu bạn muốn sử dụng ngày tạo
    EditText edtNewContent;
    boolean isEditing = false;

    private static final String TAG = "Admin_XoaThongBao";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_xoa_thong_bao);

        edtNoiDung = findViewById(R.id.edt_notification_content);
        btnXoa = findViewById(R.id.btn_reject);
        btnSua = findViewById(R.id.btn_agree);
        RelativeLayout editCard = findViewById(R.id.edit_notification_card); // Thêm dòng này nếu chưa có

        ImageView btnBack = findViewById(R.id.btn_back);
        edtNewContent = findViewById(R.id.edt_new_notification_content);


        maThongBao = getIntent().getIntExtra("ma_thong_bao", -1);
        maDay = getIntent().getIntExtra("ma_day", -1);
        String noiDung = getIntent().getStringExtra("noi_dung"); // Sửa key này thành "noi_dung"
        ngayTao = getIntent().getStringExtra("ngay_tao"); // Nếu bạn cần dùng ngày tạo
        if (maThongBao == -1 || maDay == -1) {
            Toast.makeText(this, "Dữ liệu thông báo không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edtNoiDung.setText(noiDung);

        Log.d(TAG, "onCreate: maThongBao = " + maThongBao + ", maDay = " + maDay + ", noiDung = " + noiDung + ", ngayTao = " + ngayTao);

        btnBack.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            // Trở về Admin_ThongBaoActivity, giữ lại maDay nếu cần
            Intent intent = new Intent(Admin_XoaThongBaoActivity.this, Admin_ThongBaoActivity.class);
            intent.putExtra("MA_DAY", maDay);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        btnXoa.setOnClickListener(v -> {
            Log.d(TAG, "Xoá button clicked");
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá thông báo này không?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        Log.d(TAG, "Gửi yêu cầu xoá thông báo với ID: " + maThongBao);
                        ApiService.apiService.xoaThongBao(maThongBao).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.d(TAG, "onResponse xoá: code = " + response.code());
                                if (response.isSuccessful()) {
                                    Toast.makeText(Admin_XoaThongBaoActivity.this, "Đã xoá thông báo", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    Toast.makeText(Admin_XoaThongBaoActivity.this, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e(TAG, "onFailure xoá: " + t.getMessage(), t);
                                Toast.makeText(Admin_XoaThongBaoActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });


        btnSua.setOnClickListener(v -> {
            if (!isEditing) {
                // Hiển thị khung sửa
                editCard.setVisibility(View.VISIBLE); // ✅ sửa ở đây
                edtNewContent.setText(edtNoiDung.getText().toString());
                btnSua.setText("Lưu");
                isEditing = true;
            } else {
                String noiDungMoi = edtNewContent.getText().toString().trim();
                if (noiDungMoi.isEmpty()) {
                    Toast.makeText(this, "Nội dung không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> requestMap = new HashMap<>();
                requestMap.put("ma_day", maDay);
                requestMap.put("noi_dung", noiDungMoi);

                ApiService.apiService.suaThongBao(maThongBao, requestMap).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(Admin_XoaThongBaoActivity.this, "Đã cập nhật nội dung", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(Admin_XoaThongBaoActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(Admin_XoaThongBaoActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }
}
