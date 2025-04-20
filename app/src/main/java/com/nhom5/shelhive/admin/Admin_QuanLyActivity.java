package com.nhom5.shelhive.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.adapter.MotelAdapter;
import com.nhom5.shelhive.model.Motel;

import java.util.ArrayList;
import java.util.List;

public class Admin_QuanLyActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMotels;
    private MotelAdapter motelAdapter;
    private List<Motel> motelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanly_nhatro); // dùng đúng layout vợ đã tạo

        recyclerViewMotels = findViewById(R.id.recyclerViewMotels);
        recyclerViewMotels.setLayoutManager(new LinearLayoutManager(this));

        // Giả lập danh sách nhà trọ
        motelList = new ArrayList<>();
        motelList.add(new Motel("Nhà trọ Trường Phát 1", "42 đường N4, Mỹ Phước, tp. Bến Cát, Bình Dương", 1));
        motelList.add(new Motel("Nhà trọ Trường Phát 2", "42 đường N4, Mỹ Phước", 2));
        motelList.add(new Motel("Nhà trọ Trường Phát 3", "44 đường D1, Mỹ Phước", 5));

        motelAdapter = new MotelAdapter(this, motelList);
        recyclerViewMotels.setAdapter(motelAdapter);

        // Xử lý nút quay lại
        ImageView btnBack = findViewById(R.id.mingcute_arrow_up);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // quay lại màn trước
            }
        });
    }
}
