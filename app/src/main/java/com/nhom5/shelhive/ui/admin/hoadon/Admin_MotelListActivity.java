package com.nhom5.shelhive.ui.admin.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.common.adapter.MotelAdapter;
import com.nhom5.shelhive.ui.model.Motel;

import java.util.ArrayList;
import java.util.List;

public class Admin_MotelListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNhaTro;
    private MotelAdapter motelAdapter;
    private List<Motel> nhaTroList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_hoadon_nhatro);

        recyclerViewNhaTro = findViewById(R.id.recyclerViewNhaTro);
        recyclerViewNhaTro.setLayoutManager(new LinearLayoutManager(this));

        ImageView btnBack = findViewById(R.id.mingcute_arrow_up);
        btnBack.setOnClickListener(v -> finish());

        loadMotelData();
    }

    private void loadMotelData() {
        nhaTroList = new ArrayList<>();
        nhaTroList.add(new Motel(2, "Nhà trọ Trường Phát 1", "41 N4, Mỹ Phước, Bến Cát"));
        nhaTroList.add(new Motel(4, "Nhà trọ Trường Phát 2", "42 N4, Mỹ Phước, Bến Cát"));

        TextView tvNoMotels = findViewById(R.id.tv_no_motels);
        if (nhaTroList.isEmpty()) {
            recyclerViewNhaTro.setVisibility(View.GONE);
            tvNoMotels.setVisibility(View.VISIBLE);
        } else {
            recyclerViewNhaTro.setVisibility(View.VISIBLE);
            tvNoMotels.setVisibility(View.GONE);
        }

        motelAdapter = new MotelAdapter(this, nhaTroList);
        motelAdapter.setOnItemClickListener(maDay -> openRoomList(maDay)); // sửa ở đây
        recyclerViewNhaTro.setAdapter(motelAdapter);
    }

    private void openRoomList(int maDay) { // sửa kiểu tham số
        Intent intent = new Intent(this, Admin_RoomListActivity.class);
        intent.putExtra("MA_DAY", maDay); // truyền đúng key
        startActivity(intent);
    }
}
