package com.nhom5.shelhive.ui.user.thongke;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.ThongKeDienResponse;
import com.nhom5.shelhive.api.ThongKeNuocResponse;
import com.nhom5.shelhive.ui.user.User_TrangChuActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_ThongKeActivity extends AppCompatActivity {

    private BarChart barChartDien, barChartNuoc;
    private EditText etElectricFrom, etElectricTo, etWaterFrom, etWaterTo;
    private int maPhong; // hoặc lấy từ intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_thongke);
        String maPhongStr = getIntent().getStringExtra("maPhong");

        try {
            maPhong = Integer.parseInt(maPhongStr);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: không xác định được mã phòng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        barChartDien = findViewById(R.id.barChartDien);
        barChartNuoc = findViewById(R.id.barChartNuoc);
        etElectricFrom = findViewById(R.id.et_electric_from);
        etElectricTo = findViewById(R.id.et_electric_to);
        etWaterFrom = findViewById(R.id.et_water_from);
        etWaterTo = findViewById(R.id.et_water_to);

        findViewById(R.id.btn_back).setOnClickListener(v -> {
            startActivity(new Intent(this, User_TrangChuActivity.class));
            finish();
        });

        setupMonthYearPicker(etElectricFrom);
        setupMonthYearPicker(etElectricTo);
        setupMonthYearPicker(etWaterFrom);
        setupMonthYearPicker(etWaterTo);

        Calendar c = Calendar.getInstance();
        String currentMonthYear = String.format(Locale.getDefault(), "%02d/%d", c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
        etElectricFrom.setText(currentMonthYear);
        etElectricTo.setText(currentMonthYear);
        etWaterFrom.setText(currentMonthYear);
        etWaterTo.setText(currentMonthYear);

        updateCharts();
    }

    private void setupMonthYearPicker(EditText editText) {
        editText.setFocusable(false);
        editText.setOnClickListener(v -> showMonthYearPicker(editText));
    }

    private void showMonthYearPicker(EditText targetEditText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String formatted = String.format(Locale.getDefault(), "%02d/%d", month + 1, year);
            targetEditText.setText(formatted);
            updateCharts();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        try {
            Field[] fields = dpd.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mDatePicker".equals(field.getName())) {
                    field.setAccessible(true);
                    DatePicker datePicker = (DatePicker) field.get(dpd);
                    Field[] dpFields = datePicker.getClass().getDeclaredFields();
                    for (Field dpField : dpFields) {
                        if ("mDaySpinner".equals(dpField.getName()) || "mDayPicker".equals(dpField.getName())) {
                            dpField.setAccessible(true);
                            ((android.view.View) dpField.get(datePicker)).setVisibility(android.view.View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ignored) {}

        dpd.show();
    }

    private void updateCharts() {
        int[] dienFrom = parseMonthYear(etElectricFrom.getText().toString());
        int[] dienTo = parseMonthYear(etElectricTo.getText().toString());
        int[] nuocFrom = parseMonthYear(etWaterFrom.getText().toString());
        int[] nuocTo = parseMonthYear(etWaterTo.getText().toString());

        fetchAndDrawElectricData(dienFrom, dienTo);
        fetchAndDrawWaterData(nuocFrom, nuocTo);

    }

    private int[] parseMonthYear(String text) {
        try {
            String[] parts = text.split("/");
            return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
        } catch (Exception e) {
            Calendar c = Calendar.getInstance();
            return new int[]{c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)};
        }
    }



    private void fetchAndDrawElectricData(int[] from, int[] to) {
        ApiService.apiService.getThongKeDien(maPhong, from[0], from[1], to[0], to[1])
                .enqueue(new Callback<List<ThongKeDienResponse>>() {
                    @Override
                    public void onResponse(Call<List<ThongKeDienResponse>> call, Response<List<ThongKeDienResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<BarEntry> entries = new ArrayList<>();
                            List<String> labels = new ArrayList<>();
                            int index = 0;
                            for (ThongKeDienResponse item : response.body()) {
                                float money = Float.parseFloat(item.getElectric_money());
                                String label = item.getMonth() + "/" + item.getYear();
                                Log.d("ElectricData", "Month-Year: " + label + " | Money: " + money);
                                entries.add(new BarEntry(index, money));
                                labels.add(label);
                                index++;
                            }
                            drawChart(barChartDien, entries, labels, "Tiền điện", R.color.yellow);
                            Log.d("ThongKe", "maPhong: " + maPhong + ", from: " + from[0] + "/" + from[1] + ", to: " + to[0] + "/" + to[1]);

                        } else {
                            Toast.makeText(User_ThongKeActivity.this, "Không có dữ liệu điện", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ThongKeDienResponse>> call, Throwable t) {
                        Toast.makeText(User_ThongKeActivity.this, "Lỗi khi gọi API điện", Toast.LENGTH_SHORT).show();
                        Log.e("ElectricChart", "Error: " + t.getMessage());
                    }
                });
    }

    private void fetchAndDrawWaterData(int[] from, int[] to) {
        ApiService.apiService.getThongKeNuoc(maPhong, from[0], from[1], to[0], to[1])
                .enqueue(new Callback<List<ThongKeNuocResponse>>() {
                    @Override
                    public void onResponse(Call<List<ThongKeNuocResponse>> call, Response<List<ThongKeNuocResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<BarEntry> entries = new ArrayList<>();
                            List<String> labels = new ArrayList<>();
                            int index = 0;
                            for (ThongKeNuocResponse item : response.body()) {
                                float money = Float.parseFloat(item.getWater_money());
                                String label = item.getMonth() + "/" + item.getYear();
                                Log.d("WaterData", "Month-Year: " + label + " | Money: " + money);
                                entries.add(new BarEntry(index, money));
                                labels.add(label);
                                index++;
                            }
                            drawChart(barChartNuoc, entries, labels, "Tiền nước", R.color.blue);
                            Log.d("ThongKe", "maPhong: " + maPhong + ", from: " + from[0] + "/" + from[1] + ", to: " + to[0] + "/" + to[1]);

                        } else {
                            Toast.makeText(User_ThongKeActivity.this, "Không có dữ liệu nước", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ThongKeNuocResponse>> call, Throwable t) {
                        Toast.makeText(User_ThongKeActivity.this, "Lỗi khi gọi API nước", Toast.LENGTH_SHORT).show();
                        Log.e("WaterChart", "Error: " + t.getMessage());
                    }
                });
    }


    private void drawChart(BarChart chart, List<BarEntry> entries, List<String> labels, String label, int colorRes) {
        BarDataSet dataSet = new BarDataSet(entries, label);
        dataSet.setColor(getResources().getColor(colorRes));
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                return (index >= 0 && index < labels.size()) ? labels.get(index) : "";
            }
        });

        chart.setData(barData);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(true);
        chart.animateY(1000);
        chart.invalidate();
    }
}
