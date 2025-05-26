package com.nhom5.shelhive.ui.admin.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.ui.common.adapter.BillAdapter;
import com.nhom5.shelhive.ui.model.Bill;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_RoomBillDetailActivity extends AppCompatActivity {

    private int roomId;
    private String tenantName;
    private TextView tvRoomInfo;
    private ImageView btnBack, addBillButton;
    private RecyclerView recyclerViewBills;
    private TextView emptyView;
    private BillAdapter billAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_chitiet_hdp);

        // Nhận dữ liệu từ Intent
        roomId = getIntent().getIntExtra("ROOM_NUMBER", -1);
        tenantName = getIntent().getStringExtra("TENANT_NAME");

        // Ánh xạ view
        tvRoomInfo = findViewById(R.id.tv_room_info);
        btnBack = findViewById(R.id.btn_back);
        addBillButton = findViewById(R.id.add_bill_button);
        recyclerViewBills = findViewById(R.id.recycler_bills);
        emptyView = findViewById(R.id.tv_no_bills);

        tvRoomInfo.setText("Phòng " + roomId + " - " + tenantName);

        // RecyclerView setup
        recyclerViewBills.setLayoutManager(new LinearLayoutManager(this));
        billAdapter = new BillAdapter(this, bill -> {
            Intent intent = new Intent(this, Admin_ViewBillDetailActivity.class);
            intent.putExtra("BILL_ID", bill.getId());
            startActivity(intent);
        });
        recyclerViewBills.setAdapter(billAdapter);

        btnBack.setOnClickListener(v -> finish());

        addBillButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_CreateBillActivity.class);
            intent.putExtra("ROOM_NUMBER", roomId);
            startActivity(intent);
        });

        loadBillsForRoom(roomId);
    }

    private void loadBillsForRoom(int roomId) {
        ApiService.apiService.getInvoicesByRoom(roomId).enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Bill> bills = response.body();
                    if (!bills.isEmpty()) {
                        billAdapter.setBills(bills);
                        recyclerViewBills.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    } else {
                        recyclerViewBills.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                } else {
                    recyclerViewBills.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi khi lấy hóa đơn: " + t.getMessage());
                recyclerViewBills.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        });
    }
}
