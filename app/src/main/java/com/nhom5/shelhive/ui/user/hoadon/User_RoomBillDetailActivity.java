package com.nhom5.shelhive.ui.user.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetBillByRoomResponse;
import com.nhom5.shelhive.ui.common.adapter.BillAdapter;
import com.nhom5.shelhive.ui.model.Bill;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_RoomBillDetailActivity extends AppCompatActivity {

    private String maPhong;
    private int maDay = -1;
    private TextView tvRoomInfo, tvDate, tvNoBills;
    private ImageView btnBack;
    private RecyclerView recyclerViewBills;
    private BillAdapter billAdapter;

    // Tab TextViews
    private TextView tabTreHan, tabChuaThanhToan, tabDaThanhToan;
    private RelativeLayout dateSelector;

    private int selectedMonth = -1;
    private int selectedYear = -1;

    private List<Bill> allBills = new ArrayList<>();
    private String currentTab = "Chưa thanh toán";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_chitiet_hdp);

        // Nhận dữ liệu từ Intent (chỉ nhận đúng "maPhong")
        Intent intent = getIntent();
        maPhong = intent.getStringExtra("maPhong");
        maDay = intent.getIntExtra("maDay", -1);

        // Ánh xạ view
        tvRoomInfo = findViewById(R.id.tv_room_info);
        btnBack = findViewById(R.id.btn_back);
        recyclerViewBills = findViewById(R.id.recycler_bills);
        tvNoBills = findViewById(R.id.tv_no_bills);

        tabTreHan = findViewById(R.id.tab_trehan);
        tabChuaThanhToan = findViewById(R.id.tab_chua_thanhtoan);
        tabDaThanhToan = findViewById(R.id.tab_da_thanhtoan);

        dateSelector = findViewById(R.id.date_selector);
        tvDate = findViewById(R.id.tv_date);

        // Hiển thị mã phòng (2 số cuối hoặc full nếu ngắn)
        String phongText = "Phòng ";
        if (maPhong != null && maPhong.length() > 2) {
            phongText += maPhong.substring(maPhong.length() - 2);
        } else if (maPhong != null) {
            phongText += maPhong;
        } else {
            phongText += "N/A";
        }
        tvRoomInfo.setText(phongText);

        // RecyclerView setup
        recyclerViewBills.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBills.setNestedScrollingEnabled(false);
        billAdapter = new BillAdapter(this, bill -> {
            Intent intent1 = new Intent(this, User_ViewBillDetailActivity.class);
            intent1.putExtra("BILL_ID", bill.getId());
            intent1.putExtra("maDay", maDay);
            startActivity(intent1);
        });
        recyclerViewBills.setAdapter(billAdapter);

        btnBack.setOnClickListener(v -> finish());

        // TAB sự kiện
        View.OnClickListener tabClickListener = v -> {
            resetTabSelected();
            v.setSelected(true);
            if (v == tabTreHan) currentTab = "Trễ hạn";
            else if (v == tabChuaThanhToan) currentTab = "Chưa thanh toán";
            else if (v == tabDaThanhToan) currentTab = "Đã thanh toán";
            showBillListByTab(currentTab);
        };
        tabTreHan.setOnClickListener(tabClickListener);
        tabChuaThanhToan.setOnClickListener(tabClickListener);
        tabDaThanhToan.setOnClickListener(tabClickListener);

        // Mặc định chọn tab "Chưa thanh toán"
        tabChuaThanhToan.setSelected(true);

        // Xử lý chọn tháng/năm
        dateSelector.setOnClickListener(v -> showMonthYearPicker());

        // Kiểm tra maPhong và load hóa đơn
        if (maPhong != null && !maPhong.trim().isEmpty() && !maPhong.equals("null")) {
            try {
                int roomIdInt = Integer.parseInt(maPhong);
                loadBillsForRoom(roomIdInt);
            } catch (Exception e) {
                recyclerViewBills.setVisibility(View.GONE);
                tvNoBills.setVisibility(View.VISIBLE);
                tvNoBills.setText("Không xác định được phòng!");
            }
        } else {
            recyclerViewBills.setVisibility(View.GONE);
            tvNoBills.setVisibility(View.VISIBLE);
            tvNoBills.setText("Không xác định được phòng!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (maPhong != null && !maPhong.trim().isEmpty() && !maPhong.equals("null")) {
            try {
                int roomIdInt = Integer.parseInt(maPhong);
                loadBillsForRoom(roomIdInt);
            } catch (Exception e) {
                recyclerViewBills.setVisibility(View.GONE);
                tvNoBills.setVisibility(View.VISIBLE);
                tvNoBills.setText("Không xác định được phòng!");
            }
        }
        // Hiển thị chữ "Toàn bộ" nếu không chọn tháng/năm
        if (selectedMonth <= 0 || selectedYear <= 0) {
            tvDate.setText("Toàn bộ");
        } else {
            tvDate.setText(String.format(Locale.getDefault(), "%02d/%d", selectedMonth, selectedYear));
        }

    }

    private void resetTabSelected() {
        tabTreHan.setSelected(false);
        tabChuaThanhToan.setSelected(false);
        tabDaThanhToan.setSelected(false);
    }

    // Hàm chọn tháng/năm custom
    private void showMonthYearPicker() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_month_year_picker, null);

        android.widget.NumberPicker monthPicker = dialogView.findViewById(R.id.picker_month);
        android.widget.NumberPicker yearPicker = dialogView.findViewById(R.id.picker_year);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(selectedMonth > 0 ? selectedMonth : Calendar.getInstance().get(Calendar.MONTH) + 1);

        int yearNow = Calendar.getInstance().get(Calendar.YEAR);
        yearPicker.setMinValue(2022);
        yearPicker.setMaxValue(yearNow + 2);
        yearPicker.setValue(selectedYear > 0 ? selectedYear : yearNow);

        new android.app.AlertDialog.Builder(this)
                .setTitle("Chọn tháng & năm")
                .setView(dialogView)
                .setPositiveButton("Chọn", (dialog, which) -> {
                    selectedMonth = monthPicker.getValue();
                    selectedYear = yearPicker.getValue();
                    tvDate.setText(String.format(Locale.getDefault(), "%02d/%d", selectedMonth, selectedYear));
                    showBillListByTab(currentTab);
                })
                .setNeutralButton("Bỏ lọc", (dialog, which) -> {
                    selectedMonth = -1;
                    selectedYear = -1;
                    tvDate.setText("Toàn bộ");
                    showBillListByTab(currentTab);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // LỌC bill theo tab & tháng/năm (dạng yyyy-MM-ddTHH:mm:ss)
    private void showBillListByTab(String tab) {
        List<Bill> filtered = new ArrayList<>();
        for (Bill bill : allBills) {
            String status = bill.getStatus() != null ? bill.getStatus().trim().toLowerCase() : "";
            boolean statusOk = false;
            switch (tab) {
                case "Trễ hạn":
                    statusOk = "trễ hạn".equalsIgnoreCase(status);
                    break;
                case "Chưa thanh toán":
                    statusOk = "chưa thanh toán".equalsIgnoreCase(status);
                    break;
                case "Đã thanh toán":
                    statusOk = "đã thanh toán".equalsIgnoreCase(status);
                    break;
            }
            if (statusOk) {
                if (selectedMonth > 0 && selectedYear > 0) {
                    String billMonthYear = bill.getBillMonthYear();
                    if (billMonthYear != null && billMonthYear.length() >= 7) {
                        try {
                            String yearMonth = billMonthYear.substring(0, 7);
                            String[] arr = yearMonth.split("-");
                            if (arr.length == 2) {
                                int billYear = Integer.parseInt(arr[0]);
                                int billMonth = Integer.parseInt(arr[1]);
                                if (billMonth == selectedMonth && billYear == selectedYear) {
                                    filtered.add(bill);
                                }
                            }
                        } catch (Exception ex) {
                            // skip nếu lỗi format
                        }
                    }
                } else {
                    filtered.add(bill);
                }
            }
        }
        billAdapter.setBills(filtered);
        if (filtered.isEmpty()) {
            recyclerViewBills.setVisibility(View.GONE);
            tvNoBills.setVisibility(View.VISIBLE);
            tvNoBills.setText("Không có hóa đơn nào!");
        } else {
            recyclerViewBills.setVisibility(View.VISIBLE);
            tvNoBills.setVisibility(View.GONE);
        }
    }

    private void loadBillsForRoom(int roomId) {
        ApiService.apiService.getBillsByRoom(roomId).enqueue(new Callback<List<GetBillByRoomResponse>>() {
            @Override
            public void onResponse(Call<List<GetBillByRoomResponse>> call, Response<List<GetBillByRoomResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allBills = Bill.fromGetBillByRoomResponseList(response.body());
                    showBillListByTab(currentTab);
                } else {
                    allBills.clear();
                    billAdapter.setBills(new ArrayList<>());
                    recyclerViewBills.setVisibility(View.GONE);
                    tvNoBills.setVisibility(View.VISIBLE);
                    tvNoBills.setText("Không lấy được dữ liệu hóa đơn.");
                }
            }
            @Override
            public void onFailure(Call<List<GetBillByRoomResponse>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi khi lấy hóa đơn: " + t.getMessage());
                allBills.clear();
                billAdapter.setBills(new ArrayList<>());
                recyclerViewBills.setVisibility(View.GONE);
                tvNoBills.setVisibility(View.VISIBLE);
                tvNoBills.setText("Lỗi khi kết nối lấy hóa đơn.");
            }
        });
    }
}
