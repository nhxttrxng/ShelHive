package com.nhom5.shelhive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HoaDonActivity extends AppCompatActivity {
    // Phần màn hình danh sách nhà trọ
    private LinearLayout nhaTro1, nhaTro2;

    // Phần màn hình danh sách phòng
    private TextView tvTitle, tvMotelName;
    private ImageView btnBack;
    private EditText etSearch;
    private RelativeLayout room1, room2, room3;

    private boolean isRoomListMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Kiểm tra xem đang ở chế độ danh sách nhà trọ hay danh sách phòng
        if (getIntent().hasExtra("MOTEL_NAME")) {
            // Hiển thị màn hình danh sách phòng
            isRoomListMode = true;
            setContentView(R.layout.admin_hoadon_phong);

            // Thiết lập Edge-to-Edge sử dụng content view
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            initRoomViews();
            setupRoomListeners();
            loadRoomData();
        } else {
            // Hiển thị màn hình danh sách nhà trọ
            setContentView(R.layout.admin_hoadon_nhatro);

            // Thiết lập Edge-to-Edge sử dụng content view
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            initMotelViews();
            setupMotelListeners();
        }
    }

    // Khởi tạo view cho màn hình danh sách nhà trọ
    private void initMotelViews() {
        nhaTro1 = findViewById(R.id.frame); // LinearLayout của Nhà trọ Trường Phát 1
        nhaTro2 = findViewById(R.id.frame_2); // LinearLayout của Nhà trọ Trường Phát 2
    }

    // Khởi tạo view cho màn hình danh sách phòng
    private void initRoomViews() {
        tvTitle = findViewById(R.id.tv_title);
        tvMotelName = findViewById(R.id.tv_motel_name);
        btnBack = findViewById(R.id.btn_back);
        etSearch = findViewById(R.id.et_search);

        room1 = findViewById(R.id.room_1);
        room2 = findViewById(R.id.room_2);
        room3 = findViewById(R.id.room_3);
    }

    // Thiết lập sự kiện cho màn hình danh sách nhà trọ
    private void setupMotelListeners() {
        nhaTro1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoomList("Nhà trọ Trường Phát 1");
            }
        });

        nhaTro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoomList("Nhà trọ Trường Phát 2");
            }
        });
    }

    // Thiết lập sự kiện cho màn hình danh sách phòng
    private void setupRoomListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại màn hình trước
            }
        });

        // Xử lý khi nhấn vào phòng 1
        room1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HoaDonActivity.this, "Phòng 1 - Ngô Nhựt Trường", Toast.LENGTH_SHORT).show();
                // Ở đây bạn có thể mở màn hình chi tiết hoá đơn của phòng 1
            }
        });

        // Xử lý khi nhấn vào phòng 2
        room2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HoaDonActivity.this, "Phòng 2 - Trần Danh Vinh", Toast.LENGTH_SHORT).show();
                // Ở đây bạn có thể mở màn hình chi tiết hoá đơn của phòng 2
            }
        });

        // Xử lý khi nhấn vào phòng 3
        room3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HoaDonActivity.this, "Phòng 3 - Trần Thảo Quyên", Toast.LENGTH_SHORT).show();
                // Ở đây bạn có thể mở màn hình chi tiết hoá đơn của phòng 3
            }
        });
    }

    // Mở màn hình danh sách phòng
    private void openRoomList(String motelName) {
        Intent intent = new Intent(HoaDonActivity.this, HoaDonActivity.class);
        intent.putExtra("MOTEL_NAME", motelName);
        startActivity(intent);
    }

    // Tải dữ liệu cho màn hình danh sách phòng
    private void loadRoomData() {
        // Nhận dữ liệu từ intent
        String motelName = getIntent().getStringExtra("MOTEL_NAME");
        if (motelName != null && !motelName.isEmpty()) {
            tvMotelName.setText(motelName);
        }

        // Trong thực tế, bạn sẽ lấy danh sách phòng từ database dựa vào tên nhà trọ
        // Ví dụ: roomList = databaseHelper.getRoomsByMotelName(motelName);
        // Sau đó cập nhật giao diện dựa trên dữ liệu thực
    }
}