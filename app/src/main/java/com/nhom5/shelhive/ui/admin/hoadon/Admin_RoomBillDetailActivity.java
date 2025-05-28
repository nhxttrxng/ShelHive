package com.nhom5.shelhive.ui.admin.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
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
import com.nhom5.shelhive.ui.common.adapter.ExtensionAdapter;
import com.nhom5.shelhive.ui.model.Bill;
import com.nhom5.shelhive.ui.model.Extension;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_RoomBillDetailActivity extends AppCompatActivity {

    private String maPhong;
    private TextView tvRoomInfo, tvDate;
    private ImageView btnBack, addBillButton;
    private RecyclerView recyclerViewBills;
    private TextView emptyView;
    private BillAdapter billAdapter;
    private ExtensionAdapter extensionAdapter;

    // Tab TextViews
    private TextView tabYeuCau, tabTreHan, tabChuaThanhToan, tabDaThanhToan;

    // Chọn tháng/năm
    private RelativeLayout dateSelector;
    private int selectedMonth = -1;
    private int selectedYear = -1;
    private int maDay = -1;

    // Danh sách dữ liệu
    private List<Bill> allBillsOrigin = new ArrayList<>(); // luôn giữ bản gốc cho filter lại
    private List<Extension> allExtensions = new ArrayList<>();
    private Map<Integer, Bill> extensionBillMap = new HashMap<>(); // Map<billId, Bill>
    private String currentTab = "Chưa thanh toán";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_chitiet_hdp);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        maPhong = intent.getStringExtra("MA_PHONG");
        if (maPhong == null) maPhong = intent.getStringExtra("ROOM_NUMBER");
        maDay = intent.getIntExtra("MA_DAY", -1);

        // Ánh xạ view
        tvRoomInfo = findViewById(R.id.tv_room_info);
        btnBack = findViewById(R.id.btn_back);
        addBillButton = findViewById(R.id.add_bill_button);
        recyclerViewBills = findViewById(R.id.recycler_bills);
        emptyView = findViewById(R.id.tv_no_bills);

        tabYeuCau = findViewById(R.id.tab_yeucau_giahan);
        tabTreHan = findViewById(R.id.tab_trehan);
        tabChuaThanhToan = findViewById(R.id.tab_chua_thanhtoan);
        tabDaThanhToan = findViewById(R.id.tab_da_thanhtoan);

        dateSelector = findViewById(R.id.date_selector);
        tvDate = findViewById(R.id.tv_date);

        // Hiển thị thông tin phòng
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
        billAdapter = new BillAdapter(this, bill -> {
            Intent intent1 = new Intent(this, Admin_ViewBillDetailActivity.class);
            intent1.putExtra("BILL_ID", bill.getId());
            intent1.putExtra("MA_PHONG", maPhong);
            intent1.putExtra("MA_DAY", maDay);
            startActivity(intent1);
        });
        extensionAdapter = new ExtensionAdapter(this, (extension, bill) -> {
            Intent i = new Intent(this, Admin_ExtendBillActivity.class);
            i.putExtra("BILL_ID", bill != null ? extension.getBillId() : -1);
            i.putExtra("EXTENSION_ID", extension.getId());
            i.putExtra("ROOM_ID", maPhong); // truyền string, nếu cần int thì ép kiểu ở activity nhận
            startActivity(i);
        });
        recyclerViewBills.setAdapter(billAdapter);

        btnBack.setOnClickListener(v -> finish());
        addBillButton.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, Admin_CreateBillActivity.class);
            intent2.putExtra("MA_PHONG", maPhong);
            intent2.putExtra("MA_DAY", maDay);
            startActivity(intent2);
        });

        // TAB sự kiện
        View.OnClickListener tabClickListener = v -> {
            resetTabSelected();
            v.setSelected(true);
            if (v == tabYeuCau) currentTab = "Yêu cầu gia hạn";
            else if (v == tabTreHan) currentTab = "Trễ hạn";
            else if (v == tabChuaThanhToan) currentTab = "Chưa thanh toán";
            else if (v == tabDaThanhToan) currentTab = "Đã thanh toán";
            showListByTab(currentTab);
        };
        tabYeuCau.setOnClickListener(tabClickListener);
        tabTreHan.setOnClickListener(tabClickListener);
        tabChuaThanhToan.setOnClickListener(tabClickListener);
        tabDaThanhToan.setOnClickListener(tabClickListener);

        // Mặc định chọn tab "Chưa thanh toán"
        tabChuaThanhToan.setSelected(true);

        // Xử lý chọn tháng/năm
        dateSelector.setOnClickListener(v -> showMonthYearPicker());

        // Tải dữ liệu ban đầu
        reloadAllData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadAllData();
        if (selectedMonth <= 0 || selectedYear <= 0) {
            tvDate.setText("Toàn bộ");
        } else {
            tvDate.setText(String.format(Locale.getDefault(), "%02d/%d", selectedMonth, selectedYear));
        }

    }

    private void reloadAllData() {
        if (maPhong != null) {
            try {
                int roomIdInt = Integer.parseInt(maPhong);
                loadBillsForRoom(roomIdInt);
                loadExtensionsForRoom(roomIdInt);
            } catch (Exception e) {
                Log.e("ROOM_ID_PARSE", "Không chuyển được maPhong sang số: " + maPhong);
                recyclerViewBills.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("Không xác định được phòng!");
            }
        } else {
            recyclerViewBills.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("Không xác định được phòng!");
        }
    }

    private void resetTabSelected() {
        tabYeuCau.setSelected(false);
        tabTreHan.setSelected(false);
        tabChuaThanhToan.setSelected(false);
        tabDaThanhToan.setSelected(false);
    }

    // Chọn tháng/năm
    private void showMonthYearPicker() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_month_year_picker, null);

        NumberPicker monthPicker = dialogView.findViewById(R.id.picker_month);
        NumberPicker yearPicker = dialogView.findViewById(R.id.picker_year);

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
                    showListByTab(currentTab);
                })
                .setNeutralButton("Bỏ lọc", (dialog, which) -> {
                    // RESET filter về toàn bộ
                    selectedMonth = -1;
                    selectedYear = -1;
                    tvDate.setText("Toàn bộ");
                    showListByTab(currentTab);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ======= LOAD API =======
    private void loadBillsForRoom(int roomId) {
        ApiService.apiService.getBillsByRoom(roomId).enqueue(new Callback<List<GetBillByRoomResponse>>() {
            @Override
            public void onResponse(Call<List<GetBillByRoomResponse>> call, Response<List<GetBillByRoomResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allBillsOrigin = Bill.fromGetBillByRoomResponseList(response.body());
                    showListByTab(currentTab);
                } else {
                    allBillsOrigin.clear();
                    billAdapter.setBills(new ArrayList<>());
                    recyclerViewBills.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText("Không lấy được dữ liệu hóa đơn.");
                }
            }
            @Override
            public void onFailure(Call<List<GetBillByRoomResponse>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi khi lấy hóa đơn: " + t.getMessage());
                allBillsOrigin.clear();
                billAdapter.setBills(new ArrayList<>());
                recyclerViewBills.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("Lỗi khi kết nối lấy hóa đơn.");
            }
        });
    }

    private void loadExtensionsForRoom(int roomId) {
        ApiService.apiService.getPendingExtensionsByRoomId(roomId).enqueue(new Callback<List<Extension>>() {
            @Override
            public void onResponse(Call<List<Extension>> call, Response<List<Extension>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allExtensions = response.body();
                    Log.d("EXT_DEBUG", "Tải extension thành công, size = " + allExtensions.size());
                    extensionBillMap.clear();
                    if (!allExtensions.isEmpty()) {
                        loadBillsForExtensions(0);
                    } else {
                        showListByTab(currentTab);
                    }
                } else {
                    Log.e("EXT_DEBUG", "Extension response not success or body null! Code: " + response.code());
                    allExtensions.clear();
                    extensionBillMap.clear();
                    showListByTab(currentTab);
                }
            }
            @Override
            public void onFailure(Call<List<Extension>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi khi lấy extension: " + t.getMessage());
                allExtensions.clear();
                extensionBillMap.clear();
                showListByTab(currentTab);
            }
        });
    }

    // Đệ quy gọi lần lượt lấy Bill cho từng Extension
    private void loadBillsForExtensions(int index) {
        if (index >= allExtensions.size()) {
            showListByTab(currentTab);
            return;
        }
        Extension ext = allExtensions.get(index);
        int billId = ext.getBillId();
        ApiService.apiService.getInvoiceById(billId).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                if (response.isSuccessful() && response.body() != null) {
                    extensionBillMap.put(billId, response.body());
                } else {
                    Log.e("EXT_DEBUG", "Không lấy được Bill cho extension billId = " + billId);
                }
                loadBillsForExtensions(index + 1);
            }
            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                Log.e("EXT_DEBUG", "Lỗi khi lấy Bill cho extension billId = " + billId + ": " + t.getMessage());
                loadBillsForExtensions(index + 1);
            }
        });
    }

    // ======= HIỂN THỊ THEO TAB =======
    private void showListByTab(String tab) {
        if ("Yêu cầu gia hạn".equals(tab)) {
            recyclerViewBills.setAdapter(extensionAdapter);
            List<Extension> filteredExt = new ArrayList<>();
            List<Bill> extBills = new ArrayList<>();
            for (Extension ext : allExtensions) {
                Bill bill = extensionBillMap.get(ext.getBillId());
                if (bill != null) {
                    boolean matchMonthYear = true;
                    if (selectedMonth > 0 && selectedYear > 0) {
                        String billMonthYear = bill.getBillMonthYear();
                        if (billMonthYear != null && billMonthYear.length() >= 7) {
                            try {
                                String[] arr = billMonthYear.split("-");
                                int billYear = Integer.parseInt(arr[0]);
                                int billMonth = Integer.parseInt(arr[1]);
                                matchMonthYear = (billMonth == selectedMonth && billYear == selectedYear);
                            } catch (Exception e) {
                                matchMonthYear = false;
                            }
                        } else {
                            matchMonthYear = false;
                        }
                    }
                    if (matchMonthYear) {
                        filteredExt.add(ext);
                        extBills.add(bill);
                    }
                }
            }
            extensionAdapter.setExtensions(filteredExt, extBills);
            if (filteredExt.isEmpty()) {
                recyclerViewBills.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("Không có yêu cầu gia hạn nào!");
            } else {
                recyclerViewBills.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
        } else {
            recyclerViewBills.setAdapter(billAdapter);
            List<Bill> filtered = new ArrayList<>();
            for (Bill bill : allBillsOrigin) { // luôn filter từ bản gốc allBillsOrigin!
                String status = bill.getStatus() != null ? bill.getStatus().trim().toLowerCase() : "";
                boolean statusOk = false;
                switch (tab) {
                    case "Trễ hạn":
                        statusOk = "trễ hạn".equalsIgnoreCase(status); break;
                    case "Chưa thanh toán":
                        statusOk = "chưa thanh toán".equalsIgnoreCase(status); break;
                    case "Đã thanh toán":
                        statusOk = "đã thanh toán".equalsIgnoreCase(status); break;
                }
                if (statusOk) {
                    if (selectedMonth > 0 && selectedYear > 0) {
                        String billMonthYear = bill.getBillMonthYear();
                        if (billMonthYear != null && billMonthYear.length() >= 7) {
                            try {
                                String[] arr = billMonthYear.split("-");
                                int billYear = Integer.parseInt(arr[0]);
                                int billMonth = Integer.parseInt(arr[1]);
                                if (billMonth == selectedMonth && billYear == selectedYear) {
                                    filtered.add(bill);
                                }
                            } catch (Exception ex) { }
                        }
                    } else {
                        filtered.add(bill);
                    }
                }
            }
            billAdapter.setBills(filtered);
            if (filtered.isEmpty()) {
                recyclerViewBills.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("Không có hóa đơn nào!");
            } else {
                recyclerViewBills.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
        }
    }
}
