package com.nhom5.shelhive.ui.admin.thongke;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.*;

import java.lang.reflect.Field;
import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// ... phần import giữ nguyên

public class Admin_ThongKeActivity extends AppCompatActivity {
    private BarChart barChartTienTro, barChartDien, barChartNuoc;
    private PieChart pieChartSoPhong;
    private int maDay;

    private EditText etElectricFrom, etElectricTo, etWaterFrom, etWaterTo, etDayRoom, etThanhToan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_thongke);

        maDay = getIntent().getIntExtra("MA_DAY", -1);
        if (maDay == -1) {
            Toast.makeText(this, "Không tìm thấy mã dãy", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ánh xạ view
        barChartTienTro = findViewById(R.id.barChartTienTro);
        barChartDien = findViewById(R.id.barChartDien);
        barChartNuoc = findViewById(R.id.barChartNuoc);
        pieChartSoPhong = findViewById(R.id.pieChartThanhToan);
        etElectricFrom = findViewById(R.id.edDienFrom);
        etElectricTo = findViewById(R.id.edDienTo);
        etWaterFrom = findViewById(R.id.edNuocFrom);
        etWaterTo = findViewById(R.id.edNuocTo);
        etDayRoom = findViewById(R.id.et_tro);
        etThanhToan = findViewById(R.id.et_thanh_toan);

        setupMonthYearPicker(etElectricFrom);
        setupMonthYearPicker(etElectricTo);
        setupMonthYearPicker(etWaterFrom);
        setupMonthYearPicker(etWaterTo);
        setupMonthYearPicker(etDayRoom);
        setupMonthYearPicker(etThanhToan);

        Calendar c = Calendar.getInstance();
        String currentMonthYear = String.format(Locale.getDefault(), "%02d/%d", c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
        etElectricFrom.setText(currentMonthYear);
        etElectricTo.setText(currentMonthYear);
        etWaterFrom.setText(currentMonthYear);
        etWaterTo.setText(currentMonthYear);
        etDayRoom.setText(currentMonthYear);
        etThanhToan.setText(currentMonthYear);
        ImageView btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        updateAllCharts();
    }

    private void updateAllCharts() {
        int[] dienFrom = parseMonthYear(etElectricFrom.getText().toString());
        int[] dienTo = parseMonthYear(etElectricTo.getText().toString());
        int[] nuocFrom = parseMonthYear(etWaterFrom.getText().toString());
        int[] nuocTo = parseMonthYear(etWaterTo.getText().toString());
        int[] rentMonthYear = parseMonthYear(etDayRoom.getText().toString());
        int[] roomCountMonthYear = parseMonthYear(etThanhToan.getText().toString());

        fetchTienTro(rentMonthYear[0], rentMonthYear[1]);
        fetchSoPhong(roomCountMonthYear[0], roomCountMonthYear[1]);
        fetchElectric(dienFrom, dienTo);
        fetchWater(nuocFrom, nuocTo);
    }

    private void fetchTienTro(int month, int year) {
        ApiService.apiService.getThongKeTienTro(maDay, month, year).enqueue(new Callback<List<ThongKeRentMoney>>() {
            @Override
            public void onResponse(Call<List<ThongKeRentMoney>> call, Response<List<ThongKeRentMoney>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    ThongKeRentMoney data = response.body().get(0);
                    List<BarEntry> entries = new ArrayList<>();
                    entries.add(new BarEntry(0, data.getTotal_paid_rent()));
                    entries.add(new BarEntry(1, data.getTotal_unpaid_rent()));

                    BarDataSet dataSet = new BarDataSet(entries, "Tiền trọ");
                    dataSet.setColors(getColor(R.color.green), getColor(R.color.red));
                    barChartTienTro.setData(new BarData(dataSet));
                    barChartTienTro.getXAxis().setEnabled(false);
                    barChartTienTro.getAxisRight().setEnabled(false);
                    barChartTienTro.setDescription(new Description() {{ setText(month + "/" + year); }});
                    barChartTienTro.animateY(1000);
                    barChartTienTro.invalidate();
                }
            }

            @Override
            public void onFailure(Call<List<ThongKeRentMoney>> call, Throwable t) {
                Log.e("TienTro", t.getMessage());
            }
        });
    }

    private void fetchSoPhong(int month, int year) {
        ApiService.apiService.getThongKeSoPhong(maDay, month, year).enqueue(new Callback<List<ThongKeRoomCount>>() {
            @Override
            public void onResponse(Call<List<ThongKeRoomCount>> call, Response<List<ThongKeRoomCount>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    ThongKeRoomCount data = response.body().get(0);
                    List<PieEntry> entries = new ArrayList<>();
                    entries.add(new PieEntry(data.getPaid_room_count(), "Đã đóng"));
                    entries.add(new PieEntry(data.getOverdue_room_count(), "Trễ hạn"));
                    entries.add(new PieEntry(data.getUnpaid_room_count(), "Chưa đóng"));

                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setColors(getColor(R.color.green), getColor(R.color.orange), getColor(R.color.red));
                    pieChartSoPhong.setData(new PieData(dataSet));
                    pieChartSoPhong.setDescription(new Description() {{ setText(month + "/" + year); }});
                    pieChartSoPhong.setDrawHoleEnabled(false);
                    pieChartSoPhong.animateY(1000);
                    pieChartSoPhong.invalidate();
                }
            }

            @Override
            public void onFailure(Call<List<ThongKeRoomCount>> call, Throwable t) {
                Log.e("RoomCount", t.getMessage());
            }
        });
    }

    private void fetchElectric(int[] from, int[] to) {
        ApiService.apiService.getThongKeLoiDien(maDay, from[0], from[1], to[0], to[1])
                .enqueue(new Callback<List<ThongKeE_Profit>>() {
                    @Override
                    public void onResponse(Call<List<ThongKeE_Profit>> call, Response<List<ThongKeE_Profit>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<BarEntry> entries = new ArrayList<>();
                            List<String> labels = new ArrayList<>();
                            int i = 0;
                            for (ThongKeE_Profit item : response.body()) {
                                entries.add(new BarEntry(i, Float.parseFloat(item.getElectric_profit())));
                                labels.add(item.getMonth() + "/" + item.getYear());
                                i++;
                            }
                            drawBarChart(barChartDien, entries, labels, "Lợi nhuận điện", R.color.yellow);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ThongKeE_Profit>> call, Throwable t) {
                        Log.e("Electric", t.getMessage());
                    }
                });
    }

    private void fetchWater(int[] from, int[] to) {
        ApiService.apiService.getThongKeLoiNuoc(maDay, from[0], from[1], to[0], to[1])
                .enqueue(new Callback<List<ThongKeW_Profit>>() {
                    @Override
                    public void onResponse(Call<List<ThongKeW_Profit>> call, Response<List<ThongKeW_Profit>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<BarEntry> entries = new ArrayList<>();
                            List<String> labels = new ArrayList<>();
                            int i = 0;
                            for (ThongKeW_Profit item : response.body()) {
                                entries.add(new BarEntry(i, Float.parseFloat(item.getWater_profit())));
                                labels.add(item.getMonth() + "/" + item.getYear());
                                i++;
                            }
                            drawBarChart(barChartNuoc, entries, labels, "Lợi nhuận nước", R.color.blue);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ThongKeW_Profit>> call, Throwable t) {
                        Log.e("Water", t.getMessage());
                    }
                });
    }

    private void drawBarChart(BarChart chart, List<BarEntry> entries, List<String> labels, String label, int colorRes) {
        BarDataSet dataSet = new BarDataSet(entries, label);
        dataSet.setColor(getResources().getColor(colorRes));
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);

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

        chart.setData(data);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void setupMonthYearPicker(EditText editText) {
        editText.setFocusable(false);
        editText.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                editText.setText(String.format(Locale.getDefault(), "%02d/%d", month + 1, year));
                updateAllCharts();
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

            try {
                Field[] fields = dpd.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mDatePicker".equals(field.getName())) {
                        field.setAccessible(true);
                        DatePicker dp = (DatePicker) field.get(dpd);
                        Field[] dpFields = dp.getClass().getDeclaredFields();
                        for (Field dpField : dpFields) {
                            if ("mDaySpinner".equals(dpField.getName()) || "mDayPicker".equals(dpField.getName())) {
                                dpField.setAccessible(true);
                                ((android.view.View) dpField.get(dp)).setVisibility(android.view.View.GONE);
                            }
                        }
                    }
                }
            } catch (Exception ignored) {}
            dpd.show();
        });
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
}
