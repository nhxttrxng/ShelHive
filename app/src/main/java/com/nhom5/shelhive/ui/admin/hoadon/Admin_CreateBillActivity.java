package com.nhom5.shelhive.ui.admin.hoadon;

import android.app.DatePickerDialog;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.*;

import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_CreateBillActivity extends AppCompatActivity {
    private EditText edMonth, edOriginalDueDate, edElectricityNew, edWaterNew;
    private EditText edElectricityOld, edWaterOld;
    private TextView edElectricityPrice, edWaterPrice, tvRoomPrice;
    private TextView tvElectricityTotal, tvWaterTotal, tvTotal, tvServiceTotal, tvInterestTotal;
    private Button btnCreate, btnCancel;
    private CheckBox cbElectricity, cbWater, cbRoom;
    private int maPhong = -1;
    private int maDay = -1;

    private int chiSoDienCu = 0;
    private int chiSoNuocCu = 0;
    private double giaDien = 0;
    private double giaNuoc = 0;
    private double giaThuePhong = 1100000;

    private Calendar selectedDueDate = Calendar.getInstance();
    private int selectedMonth = -1, selectedYear = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_taomoi_hdp);

        // ==== Nhận intent chắc chắn đúng kiểu ====
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Object phongObj = extras.get("MA_PHONG");
            Object dayObj = extras.get("MA_DAY");
            if (phongObj instanceof Integer) {
                maPhong = (Integer) phongObj;
            } else if (phongObj instanceof String) {
                try { maPhong = Integer.parseInt((String) phongObj); } catch (Exception ignored) {}
            }
            if (dayObj instanceof Integer) {
                maDay = (Integer) dayObj;
            } else if (dayObj instanceof String) {
                try { maDay = Integer.parseInt((String) dayObj); } catch (Exception ignored) {}
            }
        }
        Log.d("API_DEBUG", "Nhận intent: maPhong=" + maPhong + ", maDay=" + maDay);

        // ==== Ánh xạ view ====
        edMonth = findViewById(R.id.ed_month);
        edOriginalDueDate = findViewById(R.id.ed_original_due_date);
        edElectricityNew = findViewById(R.id.ed_electricity_new);
        edWaterNew = findViewById(R.id.ed_water_new);
        edElectricityOld = findViewById(R.id.ed_electricity_old);
        edWaterOld = findViewById(R.id.ed_water_old);

        edElectricityPrice = findViewById(R.id.ed_electricity_price);
        edWaterPrice = findViewById(R.id.ed_water_price);
        tvRoomPrice = findViewById(R.id.tv_room_price);

        tvElectricityTotal = findViewById(R.id.tv_electricity_total);
        tvWaterTotal = findViewById(R.id.tv_water_total);
        tvServiceTotal = findViewById(R.id.tv_service_total);
        tvInterestTotal = findViewById(R.id.tv_interest_total);
        tvTotal = findViewById(R.id.tv_total);

        cbElectricity = findViewById(R.id.cb_electricity);
        cbWater = findViewById(R.id.cb_water);
        cbRoom = findViewById(R.id.cb_room);

        btnCreate = findViewById(R.id.btn_agree);
        btnCancel = findViewById(R.id.btn_remind);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());
        btnCreate.setOnClickListener(v -> taoHoaDon());

        edMonth.setFocusable(false);
        edMonth.setOnClickListener(v -> showMonthYearPicker());
        edOriginalDueDate.setFocusable(false);
        edOriginalDueDate.setOnClickListener(v -> showDatePicker());

        fetchAllData();
        setupCheckBoxListeners();
        setupEditTextFocusListeners(); // <-- BẮT BUỘC GỌI SAU setupCheckBoxListeners
        setupUIToHideKeyboard(findViewById(R.id.root_layout)); // ID của layout cha ngoài cùng
    }

    private void fetchAllData() {
        if (maPhong > 0) {
            ApiService.apiService.getLatestMeterIndexesByRoom(maPhong).enqueue(new Callback<GetLatestMeterResponse>() {
                @Override
                public void onResponse(Call<GetLatestMeterResponse> call, Response<GetLatestMeterResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        chiSoDienCu = response.body().getChi_so_dien_moi();
                        chiSoNuocCu = response.body().getChi_so_nuoc_moi();
                        edElectricityOld.setText(String.valueOf(chiSoDienCu));
                        edWaterOld.setText(String.valueOf(chiSoNuocCu));
                        Log.d("API_DEBUG", "getLatestMeter: chiSoDienCu=" + chiSoDienCu + ", chiSoNuocCu=" + chiSoNuocCu);
                        tinhTongTien();
                    } else {
                        chiSoDienCu = 0;
                        chiSoNuocCu = 0;
                        edElectricityOld.setText("0");
                        edWaterOld.setText("0");
                        Log.d("API_DEBUG", "getLatestMeter: set chỉ số điện/nước về 0");
                        tinhTongTien();
                    }
                }
                @Override
                public void onFailure(Call<GetLatestMeterResponse> call, Throwable t) {
                    chiSoDienCu = 0;
                    chiSoNuocCu = 0;
                    edElectricityOld.setText("0");
                    edWaterOld.setText("0");
                    Log.e("API_DEBUG", "Không lấy được chỉ số cũ", t);
                    tinhTongTien();
                }
            });
            ApiService.apiService.getRoomInfoByMaPhong(maPhong).enqueue(new Callback<GetRoomInfoResponse>() {
                @Override
                public void onResponse(Call<GetRoomInfoResponse> call, Response<GetRoomInfoResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getRoom() != null) {
                        giaThuePhong = response.body().getRoom().getGia_thue();
                        tvRoomPrice.setText(formatCurrency(giaThuePhong) + " đ/tháng");
                        Log.d("API_DEBUG", "getRoomInfo: giaThuePhong=" + giaThuePhong);
                        tinhTongTien();
                    }
                }
                @Override
                public void onFailure(Call<GetRoomInfoResponse> call, Throwable t) {
                    Log.e("API_DEBUG", "Không lấy được giá phòng", t);
                }
            });
        }
        if (maDay > 0) {
            ApiService.apiService.getMotelById(maDay).enqueue(new Callback<GetMotelByIdResponse>() {
                @Override
                public void onResponse(Call<GetMotelByIdResponse> call, Response<GetMotelByIdResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        giaDien = response.body().getGiaDien();
                        giaNuoc = response.body().getGiaNuoc();
                        edElectricityPrice.setText(formatCurrency(giaDien) + " đ/kwh");
                        edWaterPrice.setText(formatCurrency(giaNuoc) + " đ/m³");
                        Log.d("API_DEBUG", "getMotelById: maDay=" + maDay + ", giaDien=" + giaDien + ", giaNuoc=" + giaNuoc);
                        tinhTongTien();
                    } else {
                        Log.d("API_DEBUG", "Không lấy được giá điện nước, response lỗi hoặc null.");
                        giaDien = 0;
                        giaNuoc = 0;
                        edElectricityPrice.setText("0 đ/kwh");
                        edWaterPrice.setText("0 đ/m³");
                        tinhTongTien();
                    }
                }
                @Override
                public void onFailure(Call<GetMotelByIdResponse> call, Throwable t) {
                    giaDien = 0;
                    giaNuoc = 0;
                    edElectricityPrice.setText("0 đ/kwh");
                    edWaterPrice.setText("0 đ/m³");
                    Log.e("API_DEBUG", "Không lấy được giá điện nước", t);
                    tinhTongTien();
                }
            });
        }
    }

    private void setupCheckBoxListeners() {
        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> tinhTongTien();
        cbElectricity.setOnCheckedChangeListener(listener);
        cbWater.setOnCheckedChangeListener(listener);
        cbRoom.setOnCheckedChangeListener(listener);
    }

    // ================== Ẩn bàn phím khi click ra ngoài EditText ===================
    private void setupUIToHideKeyboard(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard();
                view.clearFocus();
                return false;
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUIToHideKeyboard(innerView);
            }
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }
    // =============================================================================

    // ================== KIỂM TRA CHỈ SỐ KHI EDITTEXT MẤT FOCUS ===================
    private void setupEditTextFocusListeners() {
        edElectricityNew.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                int dienCu = chiSoDienCu;
                int dienMoi = parseIntSafe(edElectricityNew.getText().toString());
                if (dienMoi < dienCu) {
                    edElectricityNew.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    edElectricityNew.setError("Chỉ số điện mới phải lớn hơn hoặc bằng số cũ!");
                    Toast.makeText(this, "Chỉ số điện mới phải lớn hơn hoặc bằng số cũ!", Toast.LENGTH_SHORT).show();
                } else {
                    edElectricityNew.setTextColor(getResources().getColor(R.color.darkbrown));
                    edElectricityNew.setError(null);
                    // Nếu đang check điện thì tính lại tiền
                    if (cbElectricity.isChecked()) tinhTongTien();
                }
            }
        });

        edWaterNew.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                int nuocCu = chiSoNuocCu;
                int nuocMoi = parseIntSafe(edWaterNew.getText().toString());
                if (nuocMoi < nuocCu) {
                    edWaterNew.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    edWaterNew.setError("Chỉ số nước mới phải lớn hơn hoặc bằng số cũ!");
                    Toast.makeText(this, "Chỉ số nước mới phải lớn hơn hoặc bằng số cũ!", Toast.LENGTH_SHORT).show();
                } else {
                    edWaterNew.setTextColor(getResources().getColor(R.color.darkbrown));
                    edWaterNew.setError(null);
                    // Nếu đang check nước thì tính lại tiền
                    if (cbWater.isChecked()) tinhTongTien();
                }
            }
        });
    }


    private void checkElectricityValid() {
        int dienCu = chiSoDienCu;
        int dienMoi = parseIntSafe(edElectricityNew.getText().toString());
        if (dienMoi < dienCu) {
            edElectricityNew.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            edElectricityNew.setError("Chỉ số điện mới phải lớn hơn hoặc bằng số cũ!");
            Toast.makeText(this, "Chỉ số điện mới phải lớn hơn hoặc bằng số cũ!", Toast.LENGTH_SHORT).show();
        } else {
            edElectricityNew.setTextColor(getResources().getColor(R.color.darkbrown));
            edElectricityNew.setError(null);
        }
    }

    private void checkWaterValid() {
        int nuocCu = chiSoNuocCu;
        int nuocMoi = parseIntSafe(edWaterNew.getText().toString());
        if (nuocMoi < nuocCu) {
            edWaterNew.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            edWaterNew.setError("Chỉ số nước mới phải lớn hơn hoặc bằng số cũ!");
            Toast.makeText(this, "Chỉ số nước mới phải lớn hơn hoặc bằng số cũ!", Toast.LENGTH_SHORT).show();
        } else {
            edWaterNew.setTextColor(getResources().getColor(R.color.darkbrown));
            edWaterNew.setError(null);
        }
    }
    // =============================================================================

    private void tinhTongTien() {
        int dienCu = chiSoDienCu;
        int nuocCu = chiSoNuocCu;
        int dienMoi = parseIntSafe(edElectricityNew.getText().toString());
        int nuocMoi = parseIntSafe(edWaterNew.getText().toString());

        int soDien = Math.max(dienMoi - dienCu, 0);
        int soNuoc = Math.max(nuocMoi - nuocCu, 0);

        double tienDien = cbElectricity.isChecked() ? soDien * giaDien : 0;
        double tienNuoc = cbWater.isChecked() ? soNuoc * giaNuoc : 0;
        double tienPhong = cbRoom.isChecked() ? giaThuePhong : 0;

        double tienDichVu = tienDien + tienNuoc + tienPhong;
        double tong = tienDichVu;

        tvElectricityTotal.setText(formatCurrency(tienDien));
        tvWaterTotal.setText(formatCurrency(tienNuoc));
        tvServiceTotal.setText(formatCurrency(tienDichVu));
        tvTotal.setText(formatCurrency(tong));
        tvInterestTotal.setText("0");

        tvRoomPrice.setText(formatCurrency(cbRoom.isChecked() ? giaThuePhong : 0) + " đ/tháng");
    }

    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    private void showMonthYearPicker() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_month_year_picker, null);
        NumberPicker monthPicker = dialogView.findViewById(R.id.picker_month);
        NumberPicker yearPicker = dialogView.findViewById(R.id.picker_year);

        final Calendar now = Calendar.getInstance();
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(selectedMonth > 0 ? selectedMonth : now.get(Calendar.MONTH) + 1);

        int yearNow = now.get(Calendar.YEAR);
        yearPicker.setMinValue(2022);
        yearPicker.setMaxValue(yearNow + 2);
        yearPicker.setValue(selectedYear > 0 ? selectedYear : yearNow);

        new AlertDialog.Builder(this)
                .setTitle("Chọn tháng & năm")
                .setView(dialogView)
                .setPositiveButton("Chọn", (dialog, which) -> {
                    selectedMonth = monthPicker.getValue();
                    selectedYear = yearPicker.getValue();
                    edMonth.setText(String.format(Locale.getDefault(), "%02d/%d", selectedMonth, selectedYear));
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int day = selectedDueDate.get(Calendar.DAY_OF_MONTH);
        int month = selectedDueDate.get(Calendar.MONTH);
        int year = selectedDueDate.get(Calendar.YEAR);
        DatePickerDialog dialog = new DatePickerDialog(this, (view, y, m, d) -> {
            selectedDueDate.set(y, m, d);
            edOriginalDueDate.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y));
        }, year, month, day);
        dialog.show();
    }

    private void taoHoaDon() {
        if (maPhong == -1) {
            Toast.makeText(this, "Thiếu mã phòng!", Toast.LENGTH_SHORT).show();
            return;
        }
        String thangNam = edMonth.getText().toString().trim();
        String hanDongTien = edOriginalDueDate.getText().toString().trim();
        String strDienMoi = edElectricityNew.getText().toString().trim();
        String strNuocMoi = edWaterNew.getText().toString().trim();

        if (thangNam.isEmpty() || hanDongTien.isEmpty() || strDienMoi.isEmpty() || strNuocMoi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        int chiSoDienMoi = parseIntSafe(strDienMoi);
        int chiSoNuocMoi = parseIntSafe(strNuocMoi);

        boolean error = false;
        if (cbElectricity.isChecked() && chiSoDienMoi < chiSoDienCu) {
            edElectricityNew.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            edElectricityNew.setError("Chỉ số điện mới phải lớn hơn hoặc bằng chỉ số điện cũ!");
            Toast.makeText(this, "Chỉ số điện mới phải lớn hơn hoặc bằng chỉ số điện cũ!", Toast.LENGTH_SHORT).show();
            error = true;
        }
        if (cbWater.isChecked() && chiSoNuocMoi < chiSoNuocCu) {
            edWaterNew.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            edWaterNew.setError("Chỉ số nước mới phải lớn hơn hoặc bằng chỉ số nước cũ!");
            Toast.makeText(this, "Chỉ số nước mới phải lớn hơn hoặc bằng chỉ số nước cũ!", Toast.LENGTH_SHORT).show();
            error = true;
        }
        if (error) return;

        edElectricityNew.setTextColor(getResources().getColor(R.color.darkbrown));
        edWaterNew.setTextColor(getResources().getColor(R.color.darkbrown));
        edElectricityNew.setError(null);
        edWaterNew.setError(null);

        double tienDien = cbElectricity.isChecked() ? (Math.max(chiSoDienMoi - chiSoDienCu, 0) * giaDien) : 0;
        double tienNuoc = cbWater.isChecked() ? (Math.max(chiSoNuocMoi - chiSoNuocCu, 0) * giaNuoc) : 0;
        double tienPhong = cbRoom.isChecked() ? giaThuePhong : 0;

        Log.d("API_DEBUG", "Gửi API createInvoice:"
                + "\n  maPhong=" + maPhong
                + "\n  chiSoDienCu=" + chiSoDienCu
                + "\n  chiSoDienMoi=" + chiSoDienMoi
                + "\n  chiSoNuocCu=" + chiSoNuocCu
                + "\n  chiSoNuocMoi=" + chiSoNuocMoi
                + "\n  tienPhong=" + tienPhong
                + "\n  tienDien=" + tienDien
                + "\n  tienNuoc=" + tienNuoc
                + "\n  hanDongTien=" + formatDateForApi(hanDongTien)
                + "\n  thangNam=" + formatMonthForApi(thangNam)
        );

        CreateBillRequest req = new CreateBillRequest(
                maPhong,
                chiSoDienCu,
                chiSoDienMoi,
                chiSoNuocCu,
                chiSoNuocMoi,
                tienPhong,
                tienDien,
                tienNuoc,
                formatDateForApi(hanDongTien),
                formatMonthForApi(thangNam)
        );

        ApiService.apiService.createInvoice(req).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("API_DEBUG", "Tạo hóa đơn thành công!");
                    Toast.makeText(Admin_CreateBillActivity.this, "Tạo hóa đơn thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Không rõ lỗi";
                        Log.e("API_DEBUG", "Tạo thất bại: " + errorBody);
                    } catch (Exception e) { Log.e("API_DEBUG", "Lỗi đọc body khi tạo bill", e); }
                    Toast.makeText(Admin_CreateBillActivity.this, "Tạo thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_DEBUG", "Lỗi kết nối khi tạo hóa đơn", t);
                Toast.makeText(Admin_CreateBillActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatDateForApi(String date) {
        try {
            String[] arr = date.split("/");
            if (arr.length == 3) {
                return arr[2] + "-" + arr[1] + "-" + arr[0];
            }
        } catch (Exception e) {}
        return date;
    }

    private String formatMonthForApi(String monthYear) {
        try {
            String[] arr = monthYear.split("/");
            if (arr.length == 2) {
                return arr[1] + "-" + arr[0] + "-02";
            }
        } catch (Exception e) {}
        return monthYear;
    }

    private String formatCurrency(double amount) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,### đ");
        java.text.DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        return df.format(amount);
    }
}
