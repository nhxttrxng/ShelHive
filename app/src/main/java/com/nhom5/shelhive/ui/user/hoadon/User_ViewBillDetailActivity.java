package com.nhom5.shelhive.ui.user.hoadon;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.ui.model.Bill;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_ViewBillDetailActivity extends AppCompatActivity {

    private int billId;

    private TextView tvRoomNumber, tvElectricityTotal, tvWaterTotal, tvServiceTotal, tvInterestTotal, tvTotal;
    private EditText edMonth, edOriginalDueDate, edNewDueDate, edElectricityOld, edElectricityNew,
            edWaterOld, edWaterNew, edInterestRate, edNote;
    private CheckBox cbElectricity, cbWater, cbRoom, cbInterest, cbLateFee;

    private Button btnRemind, btnExtend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_xem_hdp);

        billId = getIntent().getIntExtra("BILL_ID", -1);

        initViews();

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        btnRemind.setOnClickListener(v -> {
            // Xử lý logic nhắc nhở
        });

        btnExtend.setOnClickListener(v -> {
            // Xử lý logic gia hạn
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
        edNote = findViewById(R.id.ed_note);

        cbElectricity = findViewById(R.id.cb_electricity);
        cbWater = findViewById(R.id.cb_water);
        cbRoom = findViewById(R.id.cb_room);
        cbInterest = findViewById(R.id.cb_interest);
        cbLateFee = findViewById(R.id.cb_late_fee);

        btnRemind = findViewById(R.id.btn_remind);
        btnExtend = findViewById(R.id.btn_extend);
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
        tvRoomNumber.setText("Phòng " + bill.getRoomId());
        edMonth.setText(bill.getBillMonthYear());
        edOriginalDueDate.setText(bill.getDueDate());
        edNewDueDate.setText(bill.getExtendedDueDate());
        edElectricityOld.setText(String.valueOf(bill.getElectricityOldIndex()));
        edElectricityNew.setText(String.valueOf(bill.getElectricityNewIndex()));
        edWaterOld.setText(String.valueOf(bill.getWaterOldIndex()));
        edWaterNew.setText(String.valueOf(bill.getWaterNewIndex()));
        edInterestRate.setText(String.valueOf(bill.getExtensionFee()));
        edNote.setText(""); // Giả sử có ghi chú từ server thì set ở đây

        tvElectricityTotal.setText(String.format("%,.0f đ", bill.getElectricityAmount()));
        tvWaterTotal.setText(String.format("%,.0f đ", bill.getWaterAmount()));
        tvServiceTotal.setText(String.format("%,.0f đ", bill.getRoomAmount()));
        tvInterestTotal.setText(String.format("%,.0f đ", bill.getExtensionFee()));
        tvTotal.setText(String.format("%,.0f đ", bill.getAmount()));

        cbElectricity.setChecked(bill.getElectricityAmount() > 0);
        cbWater.setChecked(bill.getWaterAmount() > 0);
        cbRoom.setChecked(bill.getRoomAmount() > 0);
        cbInterest.setChecked(bill.getExtensionFee() > 0);
        cbLateFee.setChecked(bill.getExtensionDays() > 0);
    }
}
