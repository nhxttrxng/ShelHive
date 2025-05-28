package com.nhom5.shelhive.ui.admin.hoadon;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetExtensionByBillResponse;
import com.nhom5.shelhive.api.GetMotelByIdResponse;
import com.nhom5.shelhive.api.UpdateBillRequest;
import com.nhom5.shelhive.ui.model.Bill;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Admin_EditBillActivity extends AppCompatActivity {
    private int billId, maDay;
    private double giaDien = 0, giaNuoc = 0, giaPhong = 0;
    private Bill currentBill;
    private boolean isExtensionApproved = false;
    private double interestRate = 0;

    private TextView tvRoomNumber, tvElectricityTotal, tvWaterTotal, tvServiceTotal, tvLateFee, tvInterestTotal, tvTotal, tvDays;
    private EditText edMonth, edOriginalDueDate, edNewDueDate, edElectricityOld, edElectricityNew, edWaterOld, edWaterNew, edInterestRate;
    private CheckBox cbElectricity, cbWater, cbRoom;
    private Button btnCancel, btnSave;
    private RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_sua_hdp);

        billId = getIntent().getIntExtra("BILL_ID", -1);
        maDay = getIntent().getIntExtra("MA_DAY", -1);

        initViews();

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Không cho sửa tháng, hạn cũ, hạn mới
        edMonth.setEnabled(false);
        edOriginalDueDate.setEnabled(false);
        edNewDueDate.setEnabled(false);

        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> onSaveBill());

        // Xử lý click ngoài vùng EditText để clear focus + ẩn bàn phím
        rootLayout.setOnClickListener(v -> {
            clearAllEditTextFocus();
        });

        loadBillDetail(billId);
        loadElectricWaterPriceFromMaDay();
    }

    private void initViews() {
        tvRoomNumber = findViewById(R.id.tv_room_number);
        tvElectricityTotal = findViewById(R.id.tv_electricity_total);
        tvWaterTotal = findViewById(R.id.tv_water_total);
        tvServiceTotal = findViewById(R.id.tv_service_total);
        tvLateFee = findViewById(R.id.tv_late_fee);
        tvInterestTotal = findViewById(R.id.tv_interest_total);
        tvTotal = findViewById(R.id.tv_total);
        tvDays = findViewById(R.id.tv_days);

        edMonth = findViewById(R.id.ed_month);
        edOriginalDueDate = findViewById(R.id.ed_original_due_date);
        edNewDueDate = findViewById(R.id.ed_new_due_date);
        edElectricityOld = findViewById(R.id.ed_electricity_old);
        edElectricityNew = findViewById(R.id.ed_electricity_new);
        edWaterOld = findViewById(R.id.ed_water_old);
        edWaterNew = findViewById(R.id.ed_water_new);
        edInterestRate = findViewById(R.id.ed_interest_rate);

        cbElectricity = findViewById(R.id.cb_electricity);
        cbWater = findViewById(R.id.cb_water);
        cbRoom = findViewById(R.id.cb_room);

        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);

        rootLayout = findViewById(R.id.root_layout); // root_layout là id của RelativeLayout cha
    }

    private void clearAllEditTextFocus() {
        edElectricityNew.clearFocus();
        edWaterNew.clearFocus();
        edInterestRate.clearFocus();
        edMonth.clearFocus();
        edOriginalDueDate.clearFocus();
        edNewDueDate.clearFocus();
        // Hide keyboard
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if (imm != null && currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    private void loadBillDetail(int billId) {
        ApiService.apiService.getInvoiceById(billId).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentBill = response.body();
                    populateBillDetails(currentBill);
                    // Check nếu đã duyệt gia hạn thì lấy lãi suất từ gia hạn gần nhất
                    if (currentBill.isExtensionApproved()) {
                        isExtensionApproved = true;
                        loadLatestExtension(billId);
                    }
                }
            }
            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                Toast.makeText(Admin_EditBillActivity.this, "Lỗi tải chi tiết hóa đơn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadElectricWaterPriceFromMaDay() {
        if (maDay > 0) {
            ApiService.apiService.getMotelById(maDay).enqueue(new Callback<GetMotelByIdResponse>() {
                @Override
                public void onResponse(Call<GetMotelByIdResponse> call, Response<GetMotelByIdResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        giaDien = response.body().getGiaDien();
                        giaNuoc = response.body().getGiaNuoc();
                        // Nếu đã có bill, tính lại tiền luôn
                        if (currentBill != null) updateAllMoneyFields();
                    }
                }
                @Override
                public void onFailure(Call<GetMotelByIdResponse> call, Throwable t) {}
            });
        }
    }

    // Lấy lãi suất từ gia hạn gần nhất
    private void loadLatestExtension(int billId) {
        ApiService.apiService.getExtensionByBillId(billId).enqueue(new Callback<GetExtensionByBillResponse>() {
            @Override
            public void onResponse(Call<GetExtensionByBillResponse> call, Response<GetExtensionByBillResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    interestRate = response.body().getLaiSuat(); // chỉ lấy lãi suất
                    edInterestRate.setText(String.valueOf(interestRate));
                    // Số ngày trễ hạn luôn lấy từ currentBill
                    if (currentBill != null) {
                        tvDays.setText(currentBill.getExtensionDays() + " ngày");
                    }
                    updateAllMoneyFields();
                }
            }
            @Override
            public void onFailure(Call<GetExtensionByBillResponse> call, Throwable t) {}
        });
    }

    // Đổ dữ liệu bill vào view, giống ViewBillActivity
    private void populateBillDetails(Bill bill) {
        String roomIdStr = String.valueOf(bill.getRoomId());
        String last2 = roomIdStr.length() > 2 ? roomIdStr.substring(roomIdStr.length() - 2) : roomIdStr;
        tvRoomNumber.setText("Phòng " + last2);

        edMonth.setText(formatMonthYear(bill.getBillMonthYear()));
        edOriginalDueDate.setText(formatDate(bill.getDueDate()));
        edNewDueDate.setText(formatDate(bill.getExtendedDueDate()));

        edElectricityOld.setText(String.valueOf(bill.getElectricityOldIndex()));
        edElectricityNew.setText(String.valueOf(bill.getElectricityNewIndex()));
        edWaterOld.setText(String.valueOf(bill.getWaterOldIndex()));
        edWaterNew.setText(String.valueOf(bill.getWaterNewIndex()));
        edInterestRate.setText("0"); // Default, sẽ update sau nếu có gia hạn

        cbElectricity.setChecked(bill.getElectricityAmount() > 0);
        cbWater.setChecked(bill.getWaterAmount() > 0);
        cbRoom.setChecked(bill.getRoomAmount() > 0);

        // Tiền phòng default từ bill
        giaPhong = bill.getRoomAmount();

        updateAllMoneyFields();
        setEditListeners();
    }

    // Gắn event khi nhập số mới sẽ tự động tính lại và validate
    private void setEditListeners() {
        edElectricityNew.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateAndUpdateElectricity();
        });
        edWaterNew.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateAndUpdateWater();
        });
        edInterestRate.setOnFocusChangeListener((v, hasFocus) -> { if (!hasFocus) updateAllMoneyFields(); });

        cbElectricity.setOnCheckedChangeListener((buttonView, isChecked) -> updateAllMoneyFields());
        cbWater.setOnCheckedChangeListener((buttonView, isChecked) -> updateAllMoneyFields());
        cbRoom.setOnCheckedChangeListener((buttonView, isChecked) -> updateAllMoneyFields());
    }

    private void validateAndUpdateElectricity() {
        int oldValue = safeParseInt(edElectricityOld.getText().toString());
        int newValue = safeParseInt(edElectricityNew.getText().toString());
        if (newValue < oldValue) {
            edElectricityNew.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            Toast.makeText(this, "Chỉ số điện mới phải lớn hơn hoặc bằng chỉ số cũ!", Toast.LENGTH_SHORT).show();
        } else {
            edElectricityNew.setTextColor(getResources().getColor(R.color.darkbrown));
            updateAllMoneyFields();
        }
    }

    private void validateAndUpdateWater() {
        int oldValue = safeParseInt(edWaterOld.getText().toString());
        int newValue = safeParseInt(edWaterNew.getText().toString());
        if (newValue < oldValue) {
            edWaterNew.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            Toast.makeText(this, "Chỉ số nước mới phải lớn hơn hoặc bằng chỉ số cũ!", Toast.LENGTH_SHORT).show();
        } else {
            edWaterNew.setTextColor(getResources().getColor(R.color.darkbrown));
            updateAllMoneyFields();
        }
    }

    private void updateAllMoneyFields() {
        int oldDien = safeParseInt(edElectricityOld.getText().toString());
        int newDien = safeParseInt(edElectricityNew.getText().toString());
        int soDien = Math.max(0, newDien - oldDien);

        int oldNuoc = safeParseInt(edWaterOld.getText().toString());
        int newNuoc = safeParseInt(edWaterNew.getText().toString());
        int soNuoc = Math.max(0, newNuoc - oldNuoc);

        double tienDien = cbElectricity.isChecked() ? soDien * giaDien : 0;
        double tienNuoc = cbWater.isChecked() ? soNuoc * giaNuoc : 0;

        // Tiền phòng lấy từ bill, không lấy từ motel!
        double tienPhong = currentBill != null ? currentBill.getRoomAmount() : 0;
        if (!cbRoom.isChecked()) tienPhong = 0;

        tvElectricityTotal.setText(formatCurrency(tienDien));
        tvWaterTotal.setText(formatCurrency(tienNuoc));
        tvServiceTotal.setText(formatCurrency(tienDien + tienNuoc + tienPhong));

        // Số ngày trễ hạn lấy từ bill
        int soNgayTre = (currentBill != null) ? currentBill.getExtensionDays() : 0;
        tvDays.setText(soNgayTre + " ngày");

        // Tính tiền lãi gia hạn nếu đã duyệt
        double tienLaiGiaHan = 0;
        if (isExtensionApproved) {
            double ls = safeParseDouble(edInterestRate.getText().toString());
            tienLaiGiaHan = (tienPhong + tienDien + tienNuoc) * (ls / 100) * soNgayTre;
        }
        tvLateFee.setText(formatCurrency(tienLaiGiaHan));
        tvInterestTotal.setText(formatCurrency(tienLaiGiaHan));

        double tongTien = tienPhong + tienDien + tienNuoc + tienLaiGiaHan;
        tvTotal.setText(formatCurrency(tongTien));
    }

    private void onSaveBill() {
        int chiSoDienMoi = safeParseInt(edElectricityNew.getText().toString());
        int chiSoNuocMoi = safeParseInt(edWaterNew.getText().toString());
        int soDien = Math.max(0, chiSoDienMoi - safeParseInt(edElectricityOld.getText().toString()));
        int soNuoc = Math.max(0, chiSoNuocMoi - safeParseInt(edWaterOld.getText().toString()));

        double tienDien = cbElectricity.isChecked() ? soDien * giaDien : 0;
        double tienNuoc = cbWater.isChecked() ? soNuoc * giaNuoc : 0;
        double tienPhong = currentBill != null ? currentBill.getRoomAmount() : 0;
        if (!cbRoom.isChecked()) tienPhong = 0;

        // Số ngày trễ hạn từ bill
        int soNgayTre = currentBill != null ? currentBill.getExtensionDays() : 0;

        double tienLaiGiaHan = 0;
        if (isExtensionApproved) {
            double ls = safeParseDouble(edInterestRate.getText().toString());
            tienLaiGiaHan = (tienPhong + tienDien + tienNuoc) * (ls / 100) * soNgayTre;
        }

        double tongTien = tienPhong + tienDien + tienNuoc + tienLaiGiaHan;

        UpdateBillRequest req = new UpdateBillRequest(
                chiSoDienMoi, chiSoNuocMoi, tienDien, tienNuoc,
                soDien, soNuoc, tienPhong, tongTien, tienLaiGiaHan
        );

        ProgressDialog dlg = ProgressDialog.show(this, null, "Đang lưu...", true, false);

        ApiService.apiService.updateInvoice(billId, req).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dlg.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Parse JSON thủ công
                        String json = response.body().string();
                        JSONObject obj = new JSONObject(json);
                        String message = obj.optString("message");
                        JSONObject invoiceObj = obj.optJSONObject("invoice");
                        if (invoiceObj != null) {
                            // Parse thành Bill nếu muốn (dùng Gson hoặc tự lấy trường)
                            Bill bill = new Gson().fromJson(invoiceObj.toString(), Bill.class);
                        }
                        Toast.makeText(Admin_EditBillActivity.this, "Sửa hóa đơn thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(Admin_EditBillActivity.this, "Lỗi đọc dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Admin_EditBillActivity.this, "Lưu thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dlg.dismiss();
                Toast.makeText(Admin_EditBillActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private int safeParseInt(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return 0; }
    }
    private double safeParseDouble(String s) {
        try { return Double.parseDouble(s.trim()); }
        catch (Exception e) { return 0; }
    }
    private String formatMonthYear(String iso) {
        if (iso == null || iso.isEmpty()) return "";
        try {
            if (iso.length() >= 7) {
                String[] arr = iso.split("-");
                if (arr.length >= 2) return arr[1] + "/" + arr[0];
            }
        } catch (Exception e) {}
        return iso;
    }
    private String formatDate(String iso) {
        if (iso == null || iso.isEmpty()) return "";
        try {
            SimpleDateFormat src = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date d = src.parse(iso.substring(0, 10));
            SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return out.format(d);
        } catch (Exception e) {
            return iso;
        }
    }
    private String formatCurrency(double amount) {
        DecimalFormat df = new DecimalFormat("#,### đ");
        DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        return df.format(amount);
    }
}
