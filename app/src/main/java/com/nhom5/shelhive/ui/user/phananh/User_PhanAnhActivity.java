package com.nhom5.shelhive.ui.user.phananh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.admin.thongbao.Admin_ThongBaoActivity;
import com.nhom5.shelhive.ui.admin.thongbao.Admin_ThongBao_NhaTro;
import com.nhom5.shelhive.ui.user.User_TrangChuActivity;

public class User_PhanAnhActivity extends AppCompatActivity {

    private EditText edtTieuDe, edtMoTa;
    private Spinner spinnerLoaiVanDe;
    private Button btnBaoCao;
    private ImageView btnBack;
    private TextView tvTenPhong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_phananh);

        // Ánh xạ view
        edtTieuDe = findViewById(R.id.tieu_de_input);
        edtMoTa = findViewById(R.id.mo_ta_input);
        spinnerLoaiVanDe = findViewById(R.id.spinner_loai_van_de);
        btnBaoCao = findViewById(R.id.btn_bao_cao);
        btnBack = findViewById(R.id.btn_back);
        tvTenPhong = findViewById(R.id.tv_motel_name);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(User_PhanAnhActivity.this, User_TrangChuActivity.class); // thay bằng Activity chính admin của bạn
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        // Thiết lập Spinner
        String[] loaiVanDe = {"Điện", "Nước", "Wifi", "Vệ sinh", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, loaiVanDe);
        spinnerLoaiVanDe.setAdapter(adapter);

        // Set tên phòng (có thể truyền từ intent)
        String tenPhong = getIntent().getStringExtra("tenPhong");
        if (tenPhong != null) {
            tvTenPhong.setText(tenPhong);
        }

        // Nút back
        btnBack.setOnClickListener(v -> finish());

        // Xử lý báo cáo
        btnBaoCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tieuDe = edtTieuDe.getText().toString().trim();
                String loaiVanDe = spinnerLoaiVanDe.getSelectedItem().toString();
                String moTa = edtMoTa.getText().toString().trim();

                if (tieuDe.isEmpty() || moTa.isEmpty()) {
                    Toast.makeText(User_PhanAnhActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // TODO: Gửi dữ liệu đến server qua API
                // Ở đây chỉ hiển thị Toast mẫu
                Toast.makeText(User_PhanAnhActivity.this,
                        "Đã gửi phản ánh:\nTiêu đề: " + tieuDe +
                                "\nLoại: " + loaiVanDe +
                                "\nMô tả: " + moTa,
                        Toast.LENGTH_LONG).show();

                // Reset form nếu muốn
                edtTieuDe.setText("");
                edtMoTa.setText("");
                spinnerLoaiVanDe.setSelection(0);
            }
        });
    }
}

