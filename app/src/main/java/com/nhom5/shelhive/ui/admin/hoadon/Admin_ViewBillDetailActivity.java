package com.nhom5.shelhive.ui.admin.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;

public class Admin_ViewBillDetailActivity extends AppCompatActivity {
    private String roomNumber, billMonth, billType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_xem_hdp);

        roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
        billMonth = getIntent().getStringExtra("BILL_MONTH");
        billType = getIntent().getStringExtra("BILL_TYPE");

        TextView tvRoomNumber = findViewById(R.id.tv_room_number);
        tvRoomNumber.setText("Phòng " + roomNumber);

        Button btnRemind = findViewById(R.id.btn_remind);
        Button btnExtend = findViewById(R.id.btn_extend);
        Button btnEdit = findViewById(R.id.btn_edit);
        ImageView btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        btnRemind.setOnClickListener(v -> Toast.makeText(this, "Đã gửi nhắc nhở thanh toán!", Toast.LENGTH_SHORT).show());

        btnExtend.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_ExtendBillActivity.class);
            intent.putExtra("ROOM_NUMBER", roomNumber);
            intent.putExtra("BILL_MONTH", billMonth);
            startActivity(intent);
        });

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_EditBillActivity.class);
            intent.putExtra("ROOM_NUMBER", roomNumber);
            intent.putExtra("BILL_MONTH", billMonth);
            intent.putExtra("BILL_TYPE", billType);
            startActivity(intent);
        });
    }
}
