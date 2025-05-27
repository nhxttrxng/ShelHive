package com.nhom5.shelhive.ui.admin.hoadon;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetMotelByIdResponse;
import com.nhom5.shelhive.ui.model.Bill;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Admin_ViewBillDetailActivity extends AppCompatActivity {

    private int billId;
    private int maDay = -1; // mã dãy lấy từ intent

    private TextView tvRoomNumber, tvElectricityTotal, tvWaterTotal, tvServiceTotal, tvInterestTotal, tvLateFee, tvLateDays, tvTotal;
    private TextView tvRoomPrice, tvElectricityPrice, tvWaterPrice;
    private EditText edMonth, edOriginalDueDate, edNewDueDate, edElectricityOld, edElectricityNew,
            edWaterOld, edWaterNew, edInterestRate;
    private CheckBox cbElectricity, cbWater, cbRoom;

    private Button btnRemind, btnDelete, btnEdit;
    private LinearLayout bottomButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_xem_hdp);

        billId = getIntent().getIntExtra("BILL_ID", -1);
        maDay = getIntent().getIntExtra("MA_DAY", -1);

        initViews();

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        btnRemind.setOnClickListener(v -> {
            // Xử lý logic nhắc nhở hóa đơn
        });

        btnDelete.setOnClickListener(v -> {
            // Xử lý logic gia hạn hóa đơn
        });

        btnEdit.setOnClickListener(v -> {
            // Xử lý logic chỉnh sửa hóa đơn
        });

        // Lấy giá điện, nước từ mã dãy truyền qua intent
        loadElectricWaterPriceFromMaDay();

        loadBillDetail(billId);
    }

    private void initViews() {
        tvRoomNumber = findViewById(R.id.tv_room_number);
        tvElectricityTotal = findViewById(R.id.tv_electricity_total);
        tvWaterTotal = findViewById(R.id.tv_water_total);
        tvServiceTotal = findViewById(R.id.tv_service_total);
        tvInterestTotal = findViewById(R.id.tv_interest_total);
        tvLateFee = findViewById(R.id.tv_late_fee);
        tvLateDays = findViewById(R.id.tv_late_days);
        tvTotal = findViewById(R.id.tv_total);

        tvRoomPrice = findViewById(R.id.tv_room_price);
        tvElectricityPrice = findViewById(R.id.ed_electricity_price);
        tvWaterPrice = findViewById(R.id.ed_water_price);

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

        btnDelete = findViewById(R.id.btn_delete);
        btnRemind = findViewById(R.id.btn_remind);
        btnEdit = findViewById(R.id.btn_edit);

        bottomButtons = findViewById(R.id.bottom_buttons);
    }

    private void loadElectricWaterPriceFromMaDay() {
        if (maDay > 0) {
            ApiService.apiService.getMotelById(maDay).enqueue(new Callback<GetMotelByIdResponse>() {
                @Override
                public void onResponse(Call<GetMotelByIdResponse> call, Response<GetMotelByIdResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        double giaDien = response.body().getGiaDien();
                        double giaNuoc = response.body().getGiaNuoc();
                        tvElectricityPrice.setText(formatCurrency(giaDien) + " đ/kwh");
                        tvWaterPrice.setText(formatCurrency(giaNuoc) + " đ/m³");
                    } else {
                        tvElectricityPrice.setText("0 đ/kwh");
                        tvWaterPrice.setText("0 đ/m³");
                    }
                }
                @Override
                public void onFailure(Call<GetMotelByIdResponse> call, Throwable t) {
                    tvElectricityPrice.setText("0 đ/kwh");
                    tvWaterPrice.setText("0 đ/m³");
                }
            });
        } else {
            tvElectricityPrice.setText("0 đ/kwh");
            tvWaterPrice.setText("0 đ/m³");
        }
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

        // Tiền phòng
        tvRoomPrice.setText(formatCurrency(bill.getRoomAmount()) + " đ/tháng");

        // Lãi suất gia hạn:
        // edInterestRate.setText("0.0"); // Nếu sau này có API thì lấy bằng getExtensionByMaHoaDon
        edInterestRate.setText(""); // Để trống hoặc ghi chú, vợ muốn thì cho giá trị mặc định

        // Tổng tiền điện/nước/phòng/lãi
        tvElectricityTotal.setText(formatCurrency(bill.getElectricityAmount()));
        tvWaterTotal.setText(formatCurrency(bill.getWaterAmount()));
        tvServiceTotal.setText(formatCurrency(bill.getRoomAmount()+bill.getElectricityAmount()+bill.getWaterAmount())); // Nếu muốn là dịch vụ thì: điện+nước+phòng
        tvInterestTotal.setText(formatCurrency(bill.getExtensionFee()));
        tvLateFee.setText(formatCurrency(bill.getExtensionFee()));

        // Số ngày trễ hạn (nếu có)
        tvLateDays.setText(String.valueOf(bill.getExtensionDays()));

        // Tổng hóa đơn
        tvTotal.setText(formatCurrency(bill.getAmount()));

        // Checkbox dịch vụ (có sử dụng hay không)
        cbElectricity.setChecked(bill.getElectricityAmount() > 0);
        cbWater.setChecked(bill.getWaterAmount() > 0);
        cbRoom.setChecked(bill.getRoomAmount() > 0);

        // Checkbox lãi suất gia hạn và tiền lãi (phụ thuộc newDueDate)
        // Note: Nếu có dữ liệu lãi suất từ BE thì gán vào edInterestRate
        // TODO: Lấy lãi suất từ API getExtensionByMaHoaDon nếu có

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
