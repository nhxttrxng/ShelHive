package com.nhom5.shelhive.ui.admin.hoadon;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;

public class Admin_EditBillActivity extends AppCompatActivity {
    private String roomNumber, billMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_sua_hdp);

        roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
        billMonth = getIntent().getStringExtra("BILL_MONTH");

        TextView tvRoomNumber = findViewById(R.id.tv_room_number);
        if (roomNumber != null) {
            tvRoomNumber.setText("Phòng " + roomNumber);
        }

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }
}
