package com.nhom5.shelhive.ui.admin.thongbao;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;

public class Admin_XoaThongBaoActivity extends AppCompatActivity {

    EditText edtNoiDung;
    Button btnXoa, btnSua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_xoa_thong_bao);

        edtNoiDung = findViewById(R.id.edt_notification_content); // Giờ là EditText
        btnXoa = findViewById(R.id.btn_delete);
        btnSua = findViewById(R.id.btn_edit);
        ImageView btnBack=findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_XoaThongBaoActivity.this, Admin_ThongBaoActivity.class); // thay bằng Activity chính admin của bạn
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        String noiDung = getIntent().getStringExtra("noiDungThongBao");
        edtNoiDung.setText(noiDung);

        // Xử lý nút Xóa
        btnXoa.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá thông báo này không?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        // TODO: Xử lý xóa khỏi database hoặc danh sách (tuỳ bạn cài đặt)
                        Toast.makeText(this, "Đã xoá thông báo", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại màn trước
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });

        // Xử lý nút Sửa
        btnSua.setOnClickListener(v -> {
            String noiDungMoi = edtNoiDung.getText().toString();
            // TODO: Cập nhật lại dữ liệu mới trong database hoặc danh sách (tuỳ bạn cài đặt)
            Toast.makeText(this, "Đã cập nhật nội dung thông báo", Toast.LENGTH_SHORT).show();
            finish(); // Có thể cho quay lại hoặc không tuỳ ý
        });
    }
}
