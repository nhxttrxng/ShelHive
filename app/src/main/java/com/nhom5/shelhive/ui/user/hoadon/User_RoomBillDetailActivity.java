package com.nhom5.shelhive.ui.user.hoadon;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class User_RoomBillDetailActivity extends AppCompatActivity {
    private TextView tvRoomInfo, tvDate, tvFilter;
    private RelativeLayout extensionBill, overdueBill1, overdueBill2, paidBill;
    private TextView tvNoUnpaidBills;
    private ImageView btnBack;
    private RelativeLayout dateSelector, filterSelector;
    private ScrollView scrollContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_chitiet_hdp);

        tvRoomInfo = findViewById(R.id.tv_room_info);
        tvDate = findViewById(R.id.tv_date);
        tvFilter = findViewById(R.id.tv_filter);
        scrollContainer = findViewById(R.id.scroll_container);
        dateSelector = findViewById(R.id.date_selector);
        filterSelector = findViewById(R.id.filter_selector);
        btnBack = findViewById(R.id.btn_back);

        extensionBill = findViewById(R.id.extension_bill);
        overdueBill1 = findViewById(R.id.overdue_bill_1);
        overdueBill2 = findViewById(R.id.overdue_bill_2);
        paidBill = findViewById(R.id.paid_bill);

        btnBack.setOnClickListener(v -> finish());

        setupDateFilter();
        setupFilterSelector();

        loadBillDetails();
    }

    private void setupDateFilter() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(calendar.getTime()));

        dateSelector.setOnClickListener(v -> Toast.makeText(this, "Chọn ngày", Toast.LENGTH_SHORT).show());
    }

    private void setupFilterSelector() {
        filterSelector.setOnClickListener(v -> {
            android.widget.PopupMenu popup = new android.widget.PopupMenu(this, filterSelector);
            popup.getMenu().add("Tất cả");
            popup.getMenu().add("Chưa đóng");
            popup.getMenu().add("Yêu cầu gia hạn");
            popup.getMenu().add("Trễ hạn");
            popup.getMenu().add("Đã đóng");

            popup.setOnMenuItemClickListener(item -> {
                tvFilter.setText(item.getTitle());
                loadBillDetails();
                return true;
            });

            popup.show();
        });
    }

    private void loadBillDetails() {
        String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
        String tenantName = getIntent().getStringExtra("TENANT_NAME");
        String filter = tvFilter.getText().toString();

        tvRoomInfo.setText("Phòng " + roomNumber + " - " + tenantName);
        extensionBill.setVisibility(View.GONE);
        overdueBill1.setVisibility(View.GONE);
        overdueBill2.setVisibility(View.GONE);
        paidBill.setVisibility(View.GONE);
        tvNoUnpaidBills.setVisibility(View.GONE);

        boolean hasData = false;

        switch (filter) {
            case "Trễ hạn":
                if ("1".equals(roomNumber)) {
                    overdueBill1.setVisibility(View.VISIBLE);
                    overdueBill2.setVisibility(View.VISIBLE);
                    hasData = true;
                }
                break;
            case "Yêu cầu gia hạn":
            case "Đã đóng":
                paidBill.setVisibility(View.VISIBLE);
                hasData = true;
                break;
            default:
                extensionBill.setVisibility(View.VISIBLE);
                overdueBill1.setVisibility(View.VISIBLE);
                overdueBill2.setVisibility(View.VISIBLE);
                paidBill.setVisibility(View.VISIBLE);
                hasData = true;
                break;
        }

        if (!hasData) {
            tvNoUnpaidBills.setVisibility(View.VISIBLE);
            tvNoUnpaidBills.setText("Không có hóa đơn nào!");
        }
    }
}
