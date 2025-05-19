package com.nhom5.shelhive.ui.admin.thongbao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;

public class Admin_TaoThongBao extends AppCompatActivity {

    private EditText edtThongBao;
    private Button btnTaoThongBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_themthongbao);

        // Initialize views
        edtThongBao = findViewById(R.id.edt_noi_dung_thong_bao);
        btnTaoThongBao = findViewById(R.id.btn_gui_thong_bao);
        ImageView btnBack=findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_TaoThongBao.this, Admin_ThongBaoActivity.class); // thay bằng Activity chính admin của bạn
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
    private void createThongBao(String thongBaoContent) {
        // This is where you would typically send the notification to a backend server or save it in a database.
        // For now, we’ll just show a Toast for simplicity.

        // Example of sending notification logic
        // You can make an API call here to save the notification to your server.
        Toast.makeText(Admin_TaoThongBao.this, "Thông báo đã được tạo: " + thongBaoContent, Toast.LENGTH_SHORT).show();

        // Optionally, you can reset the EditText field after creating the notification.
        edtThongBao.setText("");
    }
}
