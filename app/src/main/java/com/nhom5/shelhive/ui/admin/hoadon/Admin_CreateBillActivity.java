package com.nhom5.shelhive.ui.admin.hoadon;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.CreateBillRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

public class Admin_CreateBillActivity extends AppCompatActivity {

    private EditText edMonth, edDueDate, edElectricOld, edElectricNew, edWaterOld, edWaterNew;
    private TextView tvRoomPrice;
    private Button btnCreate;
    private String maPhong;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_taomoi_hdp);

        // Khởi tạo view từ layout
        edMonth = findViewById(R.id.ed_month);
        edDueDate = findViewById(R.id.ed_due_date);
        edElectricOld = findViewById(R.id.ed_electricity_old);
        edElectricNew = findViewById(R.id.ed_electricity_new);
        edWaterOld = findViewById(R.id.ed_water_old);
        edWaterNew = findViewById(R.id.ed_water_new);
        tvRoomPrice = findViewById(R.id.tv_room_price);
        btnCreate = findViewById(R.id.btn_create);

        // Lấy mã phòng từ intent truyền qua
        if (getIntent() != null) {
            maPhong = getIntent().getStringExtra("ma_phong");
        } else {
            Toast.makeText(this, "Không tìm thấy mã phòng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        apiService = ApiService.apiService;

        // Xử lý chọn tháng (MonthPicker)
        edMonth.setOnClickListener(v -> showMonthYearPicker());

        // Xử lý chọn hạn thanh toán (DatePicker)
        edDueDate.setOnClickListener(v -> showDatePicker());

        btnCreate.setOnClickListener(v -> {
            String month = edMonth.getText().toString().trim();
            String dueDate = edDueDate.getText().toString().trim();
            String electricOld = edElectricOld.getText().toString().trim();
            String electricNew = edElectricNew.getText().toString().trim();
            String waterOld = edWaterOld.getText().toString().trim();
            String waterNew = edWaterNew.getText().toString().trim();
            String roomPrice = tvRoomPrice.getText().toString().replaceAll("[^\\d]", "");

            // Kiểm tra dữ liệu nhập
            if (month.isEmpty() || dueDate.isEmpty() || electricOld.isEmpty() || electricNew.isEmpty() ||
                    waterOld.isEmpty() || waterNew.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ các trường bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo request
            CreateBillRequest request = new CreateBillRequest(
                    maPhong,
                    Integer.parseInt(electricOld),
                    Integer.parseInt(electricNew),
                    Integer.parseInt(waterOld),
                    Integer.parseInt(waterNew),
                    Double.parseDouble(roomPrice),
                    convertToIsoDate(dueDate)
            );

            // Gọi API
            apiService.createInvoice(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Admin_CreateBillActivity.this, "Tạo hóa đơn thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Admin_CreateBillActivity.this, Admin_RoomBillDetailActivity.class);
                        intent.putExtra("ma_phong", maPhong);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Admin_CreateBillActivity.this, "Tạo hóa đơn thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(Admin_CreateBillActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
    }

    private String convertToIsoDate(String ddMMyyyy) {
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdfInput.parse(ddMMyyyy);
            SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdfOutput.format(date);
        } catch (ParseException e) {
            return ddMMyyyy; // Nếu lỗi, trả về nguyên
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                    edDueDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showMonthYearPicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog monthYearPicker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedMonthYear = String.format("%02d/%d", month + 1, year);
                    edMonth.setText(selectedMonthYear);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        // Ẩn ngày
        try {
            monthYearPicker.getDatePicker().findViewById(
                            getResources().getIdentifier("day", "id", "android"))
                    .setVisibility(android.view.View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        monthYearPicker.show();
    }
}
