package com.nhom5.shelhive.ui.admin.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.CreateInvoiceNotificationRequest;
import com.nhom5.shelhive.ui.model.Bill;
import com.nhom5.shelhive.ui.model.Extension;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;

public class Admin_ExtendBillActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvRoomNumber, tvTitle;
    private EditText edMonth, edOriginalDueDate, edNewDueDate, edInterestRate;
    private EditText edElectricityOld, edElectricityNew, edWaterOld, edWaterNew;
    private TextView edElectricityPrice, tvElectricityTotal;
    private TextView edWaterPrice, tvWaterTotal;
    private TextView tvRoomPrice;
    private TextView tvLateDays, tvLateFee;
    private TextView tvServiceTotal, tvInterestTotal, tvTotal;
    private CheckBox cbElectricity, cbWater, cbRoom;
    private Button btnAgree, btnReject;

    private Bill bill;
    private Extension extension;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_giahan_hdp);

        // Ánh xạ view
        btnBack = findViewById(R.id.btn_back);
        tvRoomNumber = findViewById(R.id.tv_room_number);
        tvTitle = findViewById(R.id.tv_title);
        edMonth = findViewById(R.id.ed_month);
        edOriginalDueDate = findViewById(R.id.ed_original_due_date);
        edNewDueDate = findViewById(R.id.ed_new_due_date);
        edInterestRate = findViewById(R.id.ed_interest_rate);
        edElectricityOld = findViewById(R.id.ed_electricity_old);
        edElectricityNew = findViewById(R.id.ed_electricity_new);
        edWaterOld = findViewById(R.id.ed_water_old);
        edWaterNew = findViewById(R.id.ed_water_new);
        edElectricityPrice = findViewById(R.id.ed_electricity_price);
        tvElectricityTotal = findViewById(R.id.tv_electricity_total);
        edWaterPrice = findViewById(R.id.ed_water_price);
        tvWaterTotal = findViewById(R.id.tv_water_total);
        tvRoomPrice = findViewById(R.id.tv_room_price);
        tvLateDays = findViewById(R.id.tv_late_days);
        tvLateFee = findViewById(R.id.tv_late_fee);
        tvServiceTotal = findViewById(R.id.tv_service_total);
        tvInterestTotal = findViewById(R.id.tv_interest_total);
        tvTotal = findViewById(R.id.tv_total);
        cbElectricity = findViewById(R.id.cb_electricity);
        cbWater = findViewById(R.id.cb_water);
        cbRoom = findViewById(R.id.cb_room);
        btnAgree = findViewById(R.id.btn_agree);
        btnReject = findViewById(R.id.btn_reject);

        // Lấy dữ liệu từ intent
        Intent intent = getIntent();
        int billId = intent.getIntExtra("BILL_ID", -1);
        int extensionId = intent.getIntExtra("EXTENSION_ID", -1);
        String roomId = intent.getStringExtra("ROOM_ID");

        tvRoomNumber.setText(roomId != null ? "Phòng " + roomId : "Phòng ?");

        loadExtensionAndBill(extensionId, billId);

        btnBack.setOnClickListener(v -> finish());

        btnAgree.setOnClickListener(v -> {
            if (extension == null || bill == null) return;
            ApiService.apiService.approveExtension(extension.getId())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                // Tạo nội dung thông báo duyệt
                                String msg = "Yêu cầu gia hạn hoá đơn tháng " +
                                        getMonthYearShort(bill.getBillMonthYear()) +
                                        " tới ngày " + formatDate(extension.getNewDueDate()) +
                                        " của phòng " + getLastTwoDigits(roomId) +
                                        " đã được duyệt.";
                                sendInvoiceNotification(bill.getId(), msg);
                                Toast.makeText(Admin_ExtendBillActivity.this, "Duyệt yêu cầu thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(Admin_ExtendBillActivity.this, "Duyệt yêu cầu thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(Admin_ExtendBillActivity.this, "Lỗi duyệt: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnReject.setOnClickListener(v -> {
            if (extension == null || bill == null) return;
            ApiService.apiService.rejectExtension(extension.getId())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                // Tạo nội dung thông báo từ chối
                                String msg = "Yêu cầu gia hạn hoá đơn tháng " +
                                        getMonthYearShort(bill.getBillMonthYear()) +
                                        " tới ngày " + formatDate(extension.getNewDueDate()) +
                                        " của phòng " + getLastTwoDigits(roomId) +
                                        " đã bị từ chối.";
                                sendInvoiceNotification(bill.getId(), msg);
                                Toast.makeText(Admin_ExtendBillActivity.this, "Đã từ chối yêu cầu!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(Admin_ExtendBillActivity.this, "Từ chối yêu cầu thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(Admin_ExtendBillActivity.this, "Lỗi từ chối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void loadExtensionAndBill(int extensionId, int billId) {
        ApiService.apiService.getExtensionById(extensionId).enqueue(new Callback<Extension>() {
            @Override
            public void onResponse(Call<Extension> call, Response<Extension> response) {
                if (response.isSuccessful() && response.body() != null) {
                    extension = response.body();
                    ApiService.apiService.getInvoiceById(billId).enqueue(new Callback<Bill>() {
                        @Override
                        public void onResponse(Call<Bill> call, Response<Bill> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                bill = response.body();
                                showAllData();
                            }
                        }
                        @Override
                        public void onFailure(Call<Bill> call, Throwable t) { }
                    });
                }
            }
            @Override
            public void onFailure(Call<Extension> call, Throwable t) { }
        });
    }

    private void showAllData() {
        if (bill == null || extension == null) return;
        edMonth.setText(formatMonthYear(bill.getBillMonthYear()));
        edOriginalDueDate.setText(formatDate(bill.getDueDate()));
        edNewDueDate.setText(formatDate(extension.getNewDueDate()));
        edInterestRate.setText(formatPercent(extension.getInterestRate()));
        edElectricityOld.setText(String.valueOf(bill.getElectricityOldIndex()));
        edElectricityNew.setText(String.valueOf(bill.getElectricityNewIndex()));
        edElectricityPrice.setText(formatCurrency(bill.getElectricityAmount()) + "/kWh");
        tvElectricityTotal.setText(formatCurrency(bill.getElectricityAmount()));
        edWaterOld.setText(String.valueOf(bill.getWaterOldIndex()));
        edWaterNew.setText(String.valueOf(bill.getWaterNewIndex()));
        edWaterPrice.setText(formatCurrency(bill.getWaterAmount()) + "/m³");
        tvWaterTotal.setText(formatCurrency(bill.getWaterAmount()));
        tvRoomPrice.setText(formatCurrency(bill.getRoomAmount()) + "/tháng");
        int lateDays = calcLateDays(bill.getDueDate(), extension.getNewDueDate());
        tvLateDays.setText(lateDays + " ngày");
        tvLateFee.setText(formatCurrency(extension.getExpectedInterest()));
        tvInterestTotal.setText(formatCurrency(extension.getExpectedInterest()));
        double totalService = bill.getElectricityAmount() + bill.getWaterAmount() + bill.getRoomAmount();
        tvServiceTotal.setText(formatCurrency(totalService));
        double total = totalService + extension.getExpectedInterest();
        tvTotal.setText(formatCurrency(total));
    }

    // ========== Hàm tiện ích ==========
    private String formatMonthYear(String thangNam) {
        if (thangNam == null) return "Tháng ?";
        try {
            SimpleDateFormat src = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
            SimpleDateFormat dest = new SimpleDateFormat("'Tháng' MM/yyyy", Locale.getDefault());
            return dest.format(src.parse(thangNam));
        } catch (Exception e) {
            return "Tháng " + thangNam;
        }
    }
    private String getMonthYearShort(String thangNam) {
        // trả về dạng MM/yyyy
        if (thangNam == null) return "?";
        try {
            String[] arr = thangNam.split("-");
            if (arr.length >= 2) return arr[1] + "/" + arr[0];
            return thangNam;
        } catch (Exception e) { return thangNam; }
    }
    private String formatDate(String isoDate) {
        if (isoDate == null) return "";
        try {
            String[] arr = isoDate.split("T");
            SimpleDateFormat src = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat dest = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return dest.format(src.parse(arr[0]));
        } catch (Exception e) {
            return isoDate;
        }
    }
    private String getLastTwoDigits(String roomId) {
        if (roomId == null) return "";
        if (roomId.length() <= 2) return roomId;
        return roomId.substring(roomId.length() - 2);
    }
    private String formatCurrency(double amount) {
        NumberFormat vnFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        return vnFormat.format(amount) + " đ";
    }
    private String formatPercent(double percent) {
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(percent);
    }
    private int calcLateDays(String from, String to) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String fromDay = from.split("T")[0];
            String toDay = to.split("T")[0];
            long start = sdf.parse(fromDay).getTime();
            long end = sdf.parse(toDay).getTime();
            long diff = end - start;
            return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return 0;
        }
    }

    private void sendInvoiceNotification(int billId, String message) {
        String ngayTao = getCurrentDateString();
        CreateInvoiceNotificationRequest req =
                new CreateInvoiceNotificationRequest(billId, message, ngayTao);
        ApiService.apiService.createInvoiceNotification(req)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) { }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) { }
                });
    }
    private String getCurrentDateString() {
        // yyyy-MM-dd HH:mm:ss (hoặc yyyy-MM-dd nếu BE chỉ cần ngày)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
