package com.nhom5.shelhive.ui.user.hoadon;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.CreateExtensionRequest;
import com.nhom5.shelhive.ui.model.Bill;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public class User_GiaHanActivity extends AppCompatActivity {
    private int billId;
    private Bill billDetail;

    private EditText edNewDueDate, edInterestRate;
    private TextView edMonth, edOriginalDueDate, tvRoomNumber;
    private TextView tvServiceTotal, tvElectricityTotal, tvWaterTotal, tvRoomTotal;
    private TextView tvDays, tvLateFee, tvInterestTotal, tvTotal;
    private Button btnSave, btnCancel;

    // Thông tin dịch vụ phụ
    private TextView tvElectricityPrice, tvWaterPrice, tvRoomPrice;
    private EditText edElectricityOld, edElectricityNew, edWaterOld, edWaterNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_giahan_hdp);

        // Nhận billId
        Intent intent = getIntent();
        billId = intent.getIntExtra("billId", -1);

        // Lấy view
        edNewDueDate = findViewById(R.id.ed_new_due_date);
        edInterestRate = findViewById(R.id.ed_interest_rate);
        edMonth = findViewById(R.id.ed_month);
        edOriginalDueDate = findViewById(R.id.ed_original_due_date);
        tvRoomNumber = findViewById(R.id.tv_room_number);

        // Tổng dịch vụ & từng thành phần
        tvServiceTotal = findViewById(R.id.tv_service_total);
        tvElectricityTotal = findViewById(R.id.tv_electricity_total);
        tvWaterTotal = findViewById(R.id.tv_water_total);

        tvElectricityPrice = findViewById(R.id.tv_electricity_price);
        tvWaterPrice = findViewById(R.id.tv_water_price);
        tvRoomPrice = findViewById(R.id.tv_room_price);

        edElectricityOld = findViewById(R.id.ed_electricity_old);
        edElectricityNew = findViewById(R.id.ed_electricity_new);
        edWaterOld = findViewById(R.id.ed_water_old);
        edWaterNew = findViewById(R.id.ed_water_new);

        tvDays = findViewById(R.id.tv_days); // có thể đổi lại id nếu layout khác
        tvLateFee = findViewById(R.id.tv_late_fee);
        tvInterestTotal = findViewById(R.id.tv_interest_total);
        tvTotal = findViewById(R.id.tv_total);

        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Lấy lại detail bill theo billId
        if (billId != -1) {
            ApiService.apiService.getInvoiceById(billId).enqueue(new Callback<Bill>() {
                @Override
                public void onResponse(Call<Bill> call, Response<Bill> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        billDetail = response.body();
                        fillBillDetail();
                    } else {
                        Toast.makeText(User_GiaHanActivity.this, "Không lấy được thông tin hóa đơn!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                @Override
                public void onFailure(Call<Bill> call, Throwable t) {
                    Toast.makeText(User_GiaHanActivity.this, "Lỗi mạng!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        // Sự kiện chọn ngày
        edNewDueDate.setOnClickListener(v -> showDatePicker());
        findViewById(R.id.ic_calendar).setOnClickListener(v -> showDatePicker());

        // Sự kiện nhập lãi suất
        edInterestRate.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recalculate();
            }
        });

        // Sự kiện nhập/chọn ngày hạn mới
        edNewDueDate.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recalculate();
            }
        });

        btnCancel.setOnClickListener(view -> finish());
        btnSave.setOnClickListener(view -> sendExtensionRequest());
    }

    private void fillBillDetail() {
        if (billDetail == null) return;

        // Lấy 2 số cuối của roomId
        String roomIdStr = String.valueOf(billDetail.getRoomId());
        String lastTwoDigits = roomIdStr.length() >= 2
                ? roomIdStr.substring(roomIdStr.length() - 2)
                : roomIdStr; // Nếu < 2 ký tự thì giữ nguyên
        tvRoomNumber.setText("Phòng " + lastTwoDigits);
        edMonth.setText(formatMonthYear(billDetail.getBillMonthYear()));
        edOriginalDueDate.setText(formatDate(billDetail.getDueDate()));

        // Điền giá trị dịch vụ
        // Tiền phòng
        tvRoomPrice.setText(formatCurrency(billDetail.getRoomAmount()) + "/tháng");
        // Tiền điện
        tvElectricityPrice.setText(formatCurrency(billDetail.getElectricityAmount()) + "/kwh");
        // Tiền nước
        tvWaterPrice.setText(formatCurrency(billDetail.getWaterAmount()) + "/m³");

        // Chỉ số điện nước
        edElectricityOld.setText(String.valueOf(billDetail.getElectricityOldIndex()));
        edElectricityNew.setText(String.valueOf(billDetail.getElectricityNewIndex()));
        edWaterOld.setText(String.valueOf(billDetail.getWaterOldIndex()));
        edWaterNew.setText(String.valueOf(billDetail.getWaterNewIndex()));

        // Hiển thị từng thành phần dịch vụ
        tvElectricityTotal.setText(formatCurrency(billDetail.getElectricityAmount()));
        tvWaterTotal.setText(formatCurrency(billDetail.getWaterAmount()));
        tvServiceTotal.setText(formatCurrency(billDetail.getRoomAmount() + billDetail.getElectricityAmount() + billDetail.getWaterAmount()));

        // Giá trị mặc định cho lãi suất
        edInterestRate.setText("0.5");

        // Set hạn mới mặc định là trống cho user chọn
        edNewDueDate.setText("");

        recalculate();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String dateStr = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    edNewDueDate.setText(dateStr);
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void sendExtensionRequest() {
        String newDueDateStr = edNewDueDate.getText().toString().trim();
        String interestRateStr = edInterestRate.getText().toString().trim();

        if (billId == -1 || newDueDateStr.isEmpty() || interestRateStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập hạn mới và lãi suất!", Toast.LENGTH_SHORT).show();
            return;
        }

        String isoDate = convertDateToISO8601(newDueDateStr);
        double interestRate;
        try {
            interestRate = Double.parseDouble(interestRateStr);
        } catch (Exception e) {
            Toast.makeText(this, "Lãi suất không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        CreateExtensionRequest request = new CreateExtensionRequest(billId, isoDate, interestRate);

        btnSave.setEnabled(false);

        ApiService.apiService.createExtension(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                btnSave.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(User_GiaHanActivity.this, "Đã gửi yêu cầu gia hạn thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(User_GiaHanActivity.this, "Gửi yêu cầu thất bại!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                btnSave.setEnabled(true);
                Toast.makeText(User_GiaHanActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void recalculate() {
        if (billDetail == null) return;
        String goc = edOriginalDueDate.getText().toString().trim();
        String moi = edNewDueDate.getText().toString().trim();
        String interestStr = edInterestRate.getText().toString().trim();

        int days = getDateDiffInDays(goc, moi);
        tvDays.setText(days + " ngày");

        // Tính tổng dịch vụ: phòng + điện + nước
        double serviceTotal = billDetail.getRoomAmount() + billDetail.getElectricityAmount() + billDetail.getWaterAmount();

        double interest = 0;
        try { interest = Double.parseDouble(interestStr); } catch (Exception e) {}

        double lateFee = serviceTotal * (interest / 100.0) * days;
        tvLateFee.setText(formatCurrency(lateFee));
        tvInterestTotal.setText(formatCurrency(lateFee));
        double total = serviceTotal + lateFee;
        tvTotal.setText(formatCurrency(total));
    }

    private int getDateDiffInDays(String from, String to) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date dateFrom = sdf.parse(from);
            Date dateTo = sdf.parse(to);
            if (dateFrom == null || dateTo == null) return 0;
            long diff = dateTo.getTime() - dateFrom.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            return 0;
        }
    }

    private String formatCurrency(double amount) {
        DecimalFormat df = new DecimalFormat("#,### đ");
        DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        return df.format(amount);
    }

    private String formatMonthYear(String iso) {
        if (iso == null || iso.isEmpty()) return "";
        try {
            if (iso.length() >= 7) {
                String[] arr = iso.split("-");
                if (arr.length >= 2) {
                    return arr[1] + "/" + arr[0];
                }
            }
        } catch (Exception e) { }
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

    private String convertDateToISO8601(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdf.parse(dateStr);
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'", Locale.getDefault());
            return output.format(date);
        } catch (Exception e) {
            return dateStr;
        }
    }

    public abstract static class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
    }
}
