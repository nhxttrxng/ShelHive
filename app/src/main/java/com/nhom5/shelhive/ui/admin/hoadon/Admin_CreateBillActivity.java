package com.nhom5.shelhive.ui.admin.hoadon;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;

public class Admin_CreateBillActivity extends AppCompatActivity {
    private String roomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_taomoi_hdp);

        roomNumber = getIntent().getStringExtra("ROOM_NUMBER");

        TextView tvRoomNumber = findViewById(R.id.tv_room_number);
        if (roomNumber != null && tvRoomNumber != null) {
            tvRoomNumber.setText("Phòng " + roomNumber);
        }

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }
}
