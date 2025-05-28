package com.nhom5.shelhive.ui.admin.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetExtensionByBillResponse;
import com.nhom5.shelhive.api.GetMotelByIdResponse;
import com.nhom5.shelhive.api.CreateInvoiceNotificationRequest;
import com.nhom5.shelhive.ui.model.Bill;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Admin_ViewBillDetailActivity extends AppCompatActivity {

    private int billId;
    private int maDay = -1;
    private int maPhong = -1;

    private TextView tvRoomNumber, tvElectricityTotal, tvWaterTotal, tvServiceTotal, tvInterestTotal, tvLateFee, tvLateDays, tvTotal;
    private TextView tvRoomPrice, tvElectricityPrice, tvWaterPrice;
    private EditText edMonth, edOriginalDueDate, edNewDueDate, edElectricityOld, edElectricityNew,
            edWaterOld, edWaterNew, edInterestRate;
    private CheckBox cbElectricity, cbWater, cbRoom;

    private Button btnRemind, btnDelete, btnEdit;
    private LinearLayout bottomButtons;

    // Để lưu giá trị bill (tháng năm) cho thông báo remind
    private Bill loadedBill = null;
    private String last2RoomId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_xem_hdp);

        billId = getIntent().getIntExtra("BILL_ID", -1);
        maDay = getIntent().getIntExtra("MA_DAY", -1);
        // Nhận cả MA_PHONG (nếu có)
        maPhong = getIntent().getIntExtra("MA_PHONG", -1);

        initViews();

        // Ưu tiên set tvRoomNumber bằng MA_PHONG từ intent nếu có
        if (maPhong > 0) {
            String maPhongStr = String.valueOf(maPhong);
            last2RoomId = maPhongStr.length() > 2 ? maPhongStr.substring(maPhongStr.length() - 2) : maPhongStr;
            tvRoomNumber.setText("Phòng " + last2RoomId);
        }

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        btnRemind.setOnClickListener(v -> {
            if (billId <= 0) {
                Toast.makeText(this, "Thiếu mã hóa đơn!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy mm/yyyy của hóa đơn
            String thangNam = "";
            if (loadedBill != null && loadedBill.getBillMonthYear() != null) {
                thangNam = formatMonthYear(loadedBill.getBillMonthYear());
            }

            // Lấy 2 số cuối của roomId
            String roomShort = last2RoomId;
            if (roomShort.isEmpty() && loadedBill != null) {
                String roomIdStr = String.valueOf(loadedBill.getRoomId());
                roomShort = roomIdStr.length() > 2 ? roomIdStr.substring(roomIdStr.length() - 2) : roomIdStr;
            }

            // Lấy ngày hiện tại yyyy-MM-dd
            String ngayTao = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            String noiDung = String.format("Hoá đơn tháng %s của phòng %s đã được nhắc nhở để thanh toán!", thangNam, roomShort);

            CreateInvoiceNotificationRequest req = new CreateInvoiceNotificationRequest(
                    billId, noiDung, ngayTao
            );

            ApiService.apiService.createInvoiceNotification(req).enqueue(new Callback<okhttp3.ResponseBody>() {
                @Override
                public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Admin_ViewBillDetailActivity.this, "Đã gửi nhắc nhở hóa đơn!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Admin_ViewBillDetailActivity.this, "Nhắc nhở thất bại!", Toast.LENGTH_SHORT).show();
                        Log.e("REMIND_BILL", "Nhắc nhở thất bại, code: " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                    Toast.makeText(Admin_ViewBillDetailActivity.this, "Lỗi kết nối khi nhắc nhở!", Toast.LENGTH_SHORT).show();
                    Log.e("REMIND_BILL", "Lỗi kết nối khi nhắc nhở: " + t.getMessage(), t);
                }
            });
        });

        btnDelete.setOnClickListener(v -> {
            ApiService.apiService.deleteInvoice(billId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Admin_ViewBillDetailActivity.this, "Xóa hóa đơn thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(Admin_ViewBillDetailActivity.this, "Xóa hóa đơn thất bại!", Toast.LENGTH_SHORT).show();
                        Log.e("DELETE_BILL", "Xóa hóa đơn thất bại, code: " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(Admin_ViewBillDetailActivity.this, "Lỗi kết nối khi xóa hóa đơn!", Toast.LENGTH_SHORT).show();
                    Log.e("DELETE_BILL", "Lỗi kết nối khi xóa hóa đơn: " + t.getMessage(), t);
                }
            });
        });

        btnEdit.setOnClickListener(v -> {
            if (loadedBill == null) {
                Toast.makeText(Admin_ViewBillDetailActivity.this, "Chưa tải xong thông tin hóa đơn!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(Admin_ViewBillDetailActivity.this, Admin_EditBillActivity.class);
            intent.putExtra("BILL_ID", billId);
            intent.putExtra("MA_DAY", maDay);
            intent.putExtra("MA_PHONG", maPhong > 0 ? maPhong : loadedBill.getRoomId());
            intent.putExtra("MOTEL_NAME", getIntent().getStringExtra("MOTEL_NAME"));
            startActivity(intent);
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

        btnDelete = findViewById(R.id.btn_reject);
        btnRemind = findViewById(R.id.btn_remind);
        btnEdit = findViewById(R.id.btn_agree);

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
                    loadedBill = bill; // Lưu lại để dùng cho remind
                    populateBillDetails(bill);

                    // Nếu bill đã duyệt gia hạn thì load lãi suất từ API extension
                    if (bill.isExtensionApproved()) {
                        loadExtensionInterest(billId);
                    } else {
                        edInterestRate.setText(""); // clear nếu không duyệt gia hạn
                    }
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

    // Gọi API để lấy lãi suất từ gia hạn đã duyệt gần nhất
    private void loadExtensionInterest(int billId) {
        ApiService.apiService.getExtensionByBillId(billId).enqueue(new Callback<GetExtensionByBillResponse>() {
            @Override
            public void onResponse(Call<GetExtensionByBillResponse> call, Response<GetExtensionByBillResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double interestRate = response.body().getLaiSuat();
                    edInterestRate.setText(String.valueOf(interestRate));
                }
            }
            @Override
            public void onFailure(Call<GetExtensionByBillResponse> call, Throwable t) {}
        });
    }

    private void populateBillDetails(Bill bill) {
        // Ưu tiên dùng MA_PHONG từ intent để hiển thị phòng, nếu không thì lấy từ bill
        String roomIdStr = maPhong > 0 ? String.valueOf(maPhong) : String.valueOf(bill.getRoomId());
        last2RoomId = roomIdStr.length() > 2 ? roomIdStr.substring(roomIdStr.length() - 2) : roomIdStr;
        tvRoomNumber.setText("Phòng " + last2RoomId);

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

        // Lãi suất gia hạn: (sẽ update sau nếu có)
        edInterestRate.setText("");

        // Tổng tiền điện/nước/phòng/lãi
        tvElectricityTotal.setText(formatCurrency(bill.getElectricityAmount()));
        tvWaterTotal.setText(formatCurrency(bill.getWaterAmount()));
        tvServiceTotal.setText(formatCurrency(bill.getRoomAmount() + bill.getElectricityAmount() + bill.getWaterAmount()));
        tvInterestTotal.setText(formatCurrency(bill.getExtensionFee()));
        tvLateFee.setText(formatCurrency(bill.getExtensionFee()));

        // Số ngày trễ hạn (nếu có)
        tvLateDays.setText(String.valueOf(bill.getExtensionDays()));

        // Tổng hóa đơn
        tvTotal.setText(formatCurrency(bill.getAmount()));

        cbElectricity.setChecked(bill.getElectricityAmount() > 0);
        cbWater.setChecked(bill.getWaterAmount() > 0);
        cbRoom.setChecked(bill.getRoomAmount() > 0);

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
