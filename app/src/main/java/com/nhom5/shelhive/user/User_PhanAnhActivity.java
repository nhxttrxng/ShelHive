package com.nhom5.shelhive.user;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;

public class User_PhanAnhActivity extends AppCompatActivity {

    Spinner spinnerLoaiVanDe;
    String[] loaiVanDeList = {"Hư hỏng thiết bị", "Internet yếu", "Điện bị ngắt", "Khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_phananh); // Gắn layout XML ở đây

        spinnerLoaiVanDe = findViewById(R.id.spinner_loai_van_de);

        // Gán dữ liệu cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                loaiVanDeList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiVanDe.setAdapter(adapter);

    }
}
