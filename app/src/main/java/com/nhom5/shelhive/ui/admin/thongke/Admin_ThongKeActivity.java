
package com.nhom5.shelhive.ui.admin.thongke;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.admin.phananh.Admin_PhanAnhActivity;
import com.nhom5.shelhive.ui.admin.phananh.Admin_PhanAnh_NhaTro;

import android.graphics.Color;
import android.widget.ImageView;

import java.util.ArrayList;

public class Admin_ThongKeActivity extends AppCompatActivity {

    private BarChart barChartTienTro, barChartDien, barChartNuoc;
    private PieChart pieChartSoPhong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_thongke); // file layout bạn đã tạo

        // Ánh xạ
        barChartTienTro = findViewById(R.id.barChartTienTro);
        pieChartSoPhong = findViewById(R.id.pieChartThanhToan);
        barChartDien = findViewById(R.id.barChartDien);
        barChartNuoc = findViewById(R.id.barChartNuoc);
        ImageView btnBack = findViewById(R.id.btn_back);
        setupBarChartTienTro();
        setupPieChartSoPhong();
        setupBarChartDien();
        setupBarChartNuoc();
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_ThongKeActivity.this, Admin_ThongKe_NhaTro.class); // thay bằng Activity chính admin của bạn
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void setupBarChartTienTro() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 75)); // Đã thanh toán
        entries.add(new BarEntry(1, 35)); // Còn nợ

        BarDataSet dataSet = new BarDataSet(entries, "Tiền trọ");
        dataSet.setColors(Color.parseColor("#A6D4A2"), Color.parseColor("#D35D5D"));
        dataSet.setValueTextSize(14f);

        BarData data = new BarData(dataSet);
        barChartTienTro.setData(data);

        Description desc = new Description();
        desc.setText("03/2025");
        barChartTienTro.setDescription(desc);

        barChartTienTro.getXAxis().setEnabled(false);
        barChartTienTro.getAxisRight().setEnabled(false);
        barChartTienTro.animateY(1000);
        barChartTienTro.invalidate();
    }

    private void setupPieChartSoPhong() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(14f, "Đã đóng"));
        entries.add(new PieEntry(7f, "Trễ hạn"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(
                Color.parseColor("#A6D4A2"),
                Color.parseColor("#D35D5D"),
                Color.parseColor("#F3BC5C")
        );
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        pieChartSoPhong.setData(data);

        Description desc = new Description();
        desc.setText("03/2025");
        pieChartSoPhong.setDescription(desc);

        pieChartSoPhong.setUsePercentValues(false);
        pieChartSoPhong.setDrawHoleEnabled(false);
        pieChartSoPhong.animateY(1000);
        pieChartSoPhong.invalidate();
    }

    private void setupBarChartDien() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 150));
        entries.add(new BarEntry(1, 300));
        entries.add(new BarEntry(2, 220));
        entries.add(new BarEntry(3, 250));

        BarDataSet dataSet = new BarDataSet(entries, "Tiền điện");
        dataSet.setColor(Color.parseColor("#F4B742"));
        dataSet.setValueTextSize(14f);

        BarData data = new BarData(dataSet);
        barChartDien.setData(data);

        Description desc = new Description();
        desc.setText("11/2024 - 03/2025");
        barChartDien.setDescription(desc);

        barChartDien.getXAxis().setDrawGridLines(false);
        barChartDien.getAxisRight().setEnabled(false);
        barChartDien.animateY(1000);
        barChartDien.invalidate();
    }

    private void setupBarChartNuoc() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 100));
        entries.add(new BarEntry(1, 130));
        entries.add(new BarEntry(2, 110));
        entries.add(new BarEntry(3, 160));

        BarDataSet dataSet = new BarDataSet(entries, "Tiền nước");
        dataSet.setColor(Color.parseColor("#4A90E2"));
        dataSet.setValueTextSize(14f);

        BarData data = new BarData(dataSet);
        barChartNuoc.setData(data);

        Description desc = new Description();
        desc.setText("11/2024 - 03/2025");
        barChartNuoc.setDescription(desc);

        barChartNuoc.getXAxis().setDrawGridLines(false);
        barChartNuoc.getAxisRight().setEnabled(false);
        barChartNuoc.animateY(1000);
        barChartNuoc.invalidate();
    }
}
