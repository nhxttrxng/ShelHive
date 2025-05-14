package com.nhom5.shelhive.ui.admin.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;

public class Admin_RoomListActivity extends AppCompatActivity {
    private TextView tvMotelName;
    private ImageView btnBack;
    private RelativeLayout room1, room2, room3;
    private int maDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_hoadon_phong);

        tvMotelName = findViewById(R.id.tv_motel_name);
        btnBack = findViewById(R.id.btn_back);
        room1 = findViewById(R.id.room_1);
        room2 = findViewById(R.id.room_2);
        room3 = findViewById(R.id.room_3);

        // Nhận ma_day từ intent
        maDay = getIntent().getIntExtra("MA_DAY", -1);
        tvMotelName.setText("Dãy số " + maDay); // Có thể thay bằng tên từ DB nếu muốn

        btnBack.setOnClickListener(v -> finish());

        room1.setOnClickListener(v -> openRoomBillDetail("1", "Ngô Nhựt Trường"));
        room2.setOnClickListener(v -> openRoomBillDetail("2", "Trần Danh Vinh"));
        room3.setOnClickListener(v -> openRoomBillDetail("3", "Trần Thảo Quyên"));
    }

    private void openRoomBillDetail(String roomNumber, String tenantName) {
        Intent intent = new Intent(this, Admin_RoomBillDetailActivity.class);
        intent.putExtra("ROOM_NUMBER", roomNumber);
        intent.putExtra("TENANT_NAME", tenantName);
        intent.putExtra("MA_DAY", maDay); // Nếu cần dùng ở màn hình tiếp theo
        startActivity(intent);
    }
}
