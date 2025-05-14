package com.nhom5.shelhive.ui.admin.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;

public class Admin_RoomBillDetailActivity extends AppCompatActivity {
    private TextView tvRoomInfo, tvDate, tvFilter;
    private RelativeLayout extensionBill, overdueBill1, overdueBill2, paidBill;
    private ImageView btnBack, addBillButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_chitiet_hdp);

        tvRoomInfo = findViewById(R.id.tv_room_info);
        tvDate = findViewById(R.id.tv_date);
        tvFilter = findViewById(R.id.tv_filter);
        extensionBill = findViewById(R.id.extension_bill);
        overdueBill1 = findViewById(R.id.overdue_bill_1);
        overdueBill2 = findViewById(R.id.overdue_bill_2);
        paidBill = findViewById(R.id.paid_bill);
        btnBack = findViewById(R.id.btn_back);
        addBillButton = findViewById(R.id.add_bill_button);

        String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
        String tenantName = getIntent().getStringExtra("TENANT_NAME");
        tvRoomInfo.setText("Phòng " + roomNumber + " - " + tenantName);

        btnBack.setOnClickListener(v -> finish());

        addBillButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_CreateBillActivity.class);
            intent.putExtra("ROOM_NUMBER", roomNumber);
            startActivity(intent);
        });

        extensionBill.setOnClickListener(v -> openViewBillDetail(roomNumber, "02/2025", "extension"));
        overdueBill1.setOnClickListener(v -> openViewBillDetail(roomNumber, "02/2025", "overdue"));
        overdueBill2.setOnClickListener(v -> openViewBillDetail(roomNumber, "01/2025", "overdue"));
        paidBill.setOnClickListener(v -> openViewBillDetail(roomNumber, "12/2024", "paid"));
    }

    private void openViewBillDetail(String roomNumber, String billMonth, String billType) {
        Intent intent = new Intent(this, Admin_ViewBillDetailActivity.class);
        intent.putExtra("ROOM_NUMBER", roomNumber);
        intent.putExtra("BILL_MONTH", billMonth);
        intent.putExtra("BILL_TYPE", billType);
        startActivity(intent);
    }
}
