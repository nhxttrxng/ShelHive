package com.nhom5.shelhive.ui.admin.hoadon;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.ui.model.Bill;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Admin_ViewBillDetailActivity extends AppCompatActivity {

    private int billId;

    private TextView tvRoomNumber, tvElectricityTotal, tvWaterTotal, tvServiceTotal, tvInterestTotal, tvTotal;
    private EditText edMonth, edOriginalDueDate, edNewDueDate, edElectricityOld, edElectricityNew,
            edWaterOld, edWaterNew, edInterestRate, edNote;
    private CheckBox cbElectricity, cbWater, cbRoom, cbInterest, cbLateFee;

    private Button btnRemind, btnExtend, btnEdit;
    private LinearLayout bottomButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_xem_hdp);

        billId = getIntent().getIntExtra("BILL_ID", -1);

        initViews();

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        btnRemind.setOnClickListener(v -> {
            // Xử lý logic nhắc nhở hóa đơn
        });

        btnExtend.setOnClickListener(v -> {
            // Xử lý logic gia hạn hóa đơn
        });

        btnEdit.setOnClickListener(v -> {
            // Xử lý logic chỉnh sửa hóa đơn
        });

        loadBillDetail(billId);
    }

    private void initViews() {
        tvRoomNumber = findViewById(R.id.tv_room_number);
        tvElectricityTotal = findViewById(R.id.tv_electricity_total);
        tvWaterTotal = findViewById(R.id.tv_water_total);
        tvServiceTotal = findViewById(R.id.tv_service_total);
        tvInterestTotal = findViewById(R.id.tv_interest_total);
        tvTotal = findViewById(R.id.tv_total);

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
        cbInterest = findViewById(R.id.cb_interest);
        cbLateFee = findViewById(R.id.cb_late_fee);

        btnRemind = findViewById(R.id.btn_remind);
        btnExtend = findViewById(R.id.btn_extend);
        btnEdit = findViewById(R.id.btn_edit);

        bottomButtons = findViewById(R.id.bottom_buttons);
    }

    private void loadBillDetail(int billId) {
        ApiService.apiService.getInvoiceById(billId).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bill bill = response.body();
                    populateBillDetails(bill);
                } else {
                    Log.e("API_ERROR", "Không tải được chi tiết hóa đơn");
                }
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi khi tải hóa đơn: " + t.getMessage());
            }
        });
    }

    private void populateBillDetails(Bill bill) {
        // Hiển thị đúng 2 số cuối của mã phòng
        String roomIdStr = String.valueOf(bill.getRoomId());
        String last2 = roomIdStr.length() > 2 ? roomIdStr.substring(roomIdStr.length() - 2) : roomIdStr;
        tvRoomNumber.setText("Phòng " + last2);

        // Tháng (mm/yyyy)
        edMonth.setText(formatMonthYear(bill.getBillMonthYear()));

        // Hạn thanh toán gốc (dd/MM/yyyy)
        edOriginalDueDate.setText(formatDate(bill.getDueDate()));

        // Hạn thanh toán mới (dd/MM/yyyy)
        String newDueDate = bill.getExtendedDueDate();
        edNewDueDate.setText(formatDate(newDueDate));

        // Điện, Nước
        edElectricityOld.setText(String.valueOf(bill.getElectricityOldIndex()));
        edElectricityNew.setText(String.valueOf(bill.getElectricityNewIndex()));
        edWaterOld.setText(String.valueOf(bill.getWaterOldIndex()));
        edWaterNew.setText(String.valueOf(bill.getWaterNewIndex()));

        // Lãi suất gia hạn: dùng lãi suất hằng số hoặc từ backend, ví dụ luôn "0.5"
        edInterestRate.setText("0.5");

        // Tổng tiền điện/nước/phòng/lãi
        tvElectricityTotal.setText(formatCurrency(bill.getElectricityAmount()));
        tvWaterTotal.setText(formatCurrency(bill.getWaterAmount()));
        tvServiceTotal.setText(formatCurrency(bill.getRoomAmount()));
        tvInterestTotal.setText(formatCurrency(bill.getExtensionFee()));
        tvTotal.setText(formatCurrency(bill.getAmount()));

        // Checkbox dịch vụ (có sử dụng hay không)
        cbElectricity.setChecked(bill.getElectricityAmount() > 0);
        cbWater.setChecked(bill.getWaterAmount() > 0);
        cbRoom.setChecked(bill.getRoomAmount() > 0);

        // Checkbox lãi suất gia hạn và tiền lãi (phụ thuộc newDueDate)
        boolean hasExtension = (newDueDate != null && !newDueDate.trim().isEmpty());
        cbInterest.setChecked(hasExtension);
        cbLateFee.setChecked(hasExtension);

        // Ẩn 3 button nếu hóa đơn đã thanh toán
        String status = bill.getStatus() != null ? bill.getStatus().trim().toLowerCase() : "";
        if (status.equals("đã thanh toán")) {
            if (bottomButtons != null) bottomButtons.setVisibility(View.GONE);
        }
    }

    /** Format ISO yyyy-MM-dd hoặc yyyy-MM thành MM/yyyy */
    private String formatMonthYear(String iso) {
        if (iso == null || iso.isEmpty()) return "";
        try {
            if (iso.length() >= 7) {
                String[] arr = iso.split("-");
                if (arr.length >= 2) {
                    return arr[1] + "/" + arr[0];
                }
            }
        } catch (Exception e) {}
        return iso;
    }

    /** Format ISO yyyy-MM-dd thành dd/MM/yyyy */
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

    /** Format số tiền thành x.xxx đ */
    private String formatCurrency(double amount) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,### đ");
        java.text.DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        return df.format(amount);
    }
}
