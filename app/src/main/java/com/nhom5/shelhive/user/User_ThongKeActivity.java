package com.nhom5.shelhive.user;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.nhom5.shelhive.R;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class User_ThongKeActivity extends AppCompatActivity {

    private BarChart barChartDien, barChartNuoc;
    private TextView tvTitle, tvMotelName;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_thongke); // Assumes the XML layout is named activity_user_thongke.xml

        // Initialize Views
        tvTitle = findViewById(R.id.tv_title);
        tvMotelName = findViewById(R.id.tv_motel_name);
        btnBack = findViewById(R.id.btn_back);

        barChartDien = findViewById(R.id.barChartDien);
        barChartNuoc = findViewById(R.id.barChartNuoc);

        // Set up Title and Motel Name
        tvTitle.setText("Thống kê");
        tvMotelName.setText("Phòng 1");

        // Setup back button click listener
        btnBack.setOnClickListener(v -> onBackPressed());

        // Setup charts with data from invoices
        setupCharts();
    }

    private void setupCharts() {
        // Get data for electricity (dien) and water (nuoc) from the invoices
        ArrayList<BarEntry> dienEntries = getElectricityData();
        ArrayList<BarEntry> nuocEntries = getWaterData();

        // Create BarDataSets for each chart
        BarDataSet dienDataSet = new BarDataSet(dienEntries, "Tiền điện");
        dienDataSet.setColor(getResources().getColor(R.color.blue));  // Set color for electricity bars

        BarDataSet nuocDataSet = new BarDataSet(nuocEntries, "Tiền nước");
        nuocDataSet.setColor(getResources().getColor(R.color.green));  // Set color for water bars

        // Create BarData objects
        BarData dienBarData = new BarData(dienDataSet);
        BarData nuocBarData = new BarData(nuocDataSet);

        // Set data for the BarCharts
        barChartDien.setData(dienBarData);
        barChartNuoc.setData(nuocBarData);

        // Configure chart appearance
        configureChart(barChartDien);
        configureChart(barChartNuoc);
    }

    private ArrayList<BarEntry> getElectricityData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        // Simulate data from invoices (replace with actual database query)
        // Let's assume we're showing data for 6 months
        entries.add(new BarEntry(0f, 100)); // Jan - 100
        entries.add(new BarEntry(1f, 120)); // Feb - 120
        entries.add(new BarEntry(2f, 150)); // Mar - 150
        entries.add(new BarEntry(3f, 130)); // Apr - 130
        entries.add(new BarEntry(4f, 110)); // May - 110
        entries.add(new BarEntry(5f, 140)); // Jun - 140

        return entries;
    }

    private ArrayList<BarEntry> getWaterData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        // Simulate data from invoices (replace with actual database query)
        // Let's assume we're showing data for 6 months
        entries.add(new BarEntry(0f, 50));  // Jan - 50
        entries.add(new BarEntry(1f, 60));  // Feb - 60
        entries.add(new BarEntry(2f, 70));  // Mar - 70
        entries.add(new BarEntry(3f, 80));  // Apr - 80
        entries.add(new BarEntry(4f, 75));  // May - 75
        entries.add(new BarEntry(5f, 85));  // Jun - 85

        return entries;
    }

    private void configureChart(BarChart chart) {
        // Customize the chart appearance
        chart.getDescription().setEnabled(false);  // Disable chart description
        chart.setTouchEnabled(true); // Enable touch gestures
        chart.setDragEnabled(true); // Enable dragging to scroll the chart
        chart.setScaleEnabled(true); // Enable scaling
    }
}
