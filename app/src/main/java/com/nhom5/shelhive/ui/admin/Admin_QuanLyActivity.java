package com.nhom5.shelhive.ui.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.common.adapter.MotelAdapter;
import com.nhom5.shelhive.ui.model.Motel;

import java.util.ArrayList;
import java.util.List;

public class Admin_QuanLyActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMotels;
    private MotelAdapter motelAdapter;
    private List<Motel> motelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanly_nhatro);

        recyclerViewMotels = findViewById(R.id.recyclerViewMotels);
        recyclerViewMotels.setLayoutManager(new LinearLayoutManager(this));

        ImageView noneMotel = findViewById(R.id.none_motel);
        ImageView noneMotelSearch = findViewById(R.id.none_motel_search); // <-- thêm dòng này
        EditText editSearchMotel = findViewById(R.id.editSearchMotel); // <-- lấy EditText tìm kiếm

// Gắn listener để theo dõi khi người dùng nhập vào ô tìm kiếm
        editSearchMotel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMotels(s.toString(), noneMotelSearch);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });
        // <-- thêm dòng này

        // Danh sách nhà trọ gốc
        motelList = new ArrayList<>();
        motelList.add(new Motel("Nhà trọ Trường Phát 1", "42 đường N4, Mỹ Phước"));
        motelList.add(new Motel("Nhà trọ Trường Phát 2", "42 đường N4, Mỹ Phước"));
        motelList.add(new Motel("Nhà trọ Trường Phát 3", "44 đường D1, Mỹ Phước"));

        motelAdapter = new MotelAdapter(this, motelList);
        recyclerViewMotels.setAdapter(motelAdapter);

        // Hiển thị ban đầu
        if (motelList.isEmpty()) {
            recyclerViewMotels.setVisibility(View.GONE);
            noneMotel.setVisibility(View.VISIBLE);
        } else {
            recyclerViewMotels.setVisibility(View.VISIBLE);
            noneMotel.setVisibility(View.GONE);
        }

        // Nút quay lại
        ImageView btnBack = findViewById(R.id.mingcute_arrow_up);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Hàm lọc danh sách theo tên
    private void filterMotels(String text, ImageView noneMotelSearch) {
        List<Motel> filteredList = new ArrayList<>();
        for (Motel motel : motelList) {
            if (motel.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(motel);
            }
        }

        if (filteredList.isEmpty()) {
            recyclerViewMotels.setVisibility(View.GONE);
            noneMotelSearch.setVisibility(View.VISIBLE);
        } else {
            recyclerViewMotels.setVisibility(View.VISIBLE);
            noneMotelSearch.setVisibility(View.GONE);
        }

        motelAdapter.setFilteredList(filteredList); // Gọi tới adapter để update
    }
}
