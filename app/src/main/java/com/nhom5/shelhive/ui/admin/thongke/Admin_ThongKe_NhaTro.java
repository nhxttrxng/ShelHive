package com.nhom5.shelhive.ui.admin.thongke;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.admin.Admin_TrangChuActivity;
import com.nhom5.shelhive.ui.common.adapter.MotelAdapter;
import com.nhom5.shelhive.ui.model.Motel;

import java.util.ArrayList;
import java.util.List;

public class Admin_ThongKe_NhaTro extends AppCompatActivity {
    private RecyclerView recyclerViewNhaTro;
    private MotelAdapter motelAdapter;
    private List<Motel> nhaTroList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_thongke_nhatro);

        recyclerViewNhaTro = findViewById(R.id.recyclerViewNhaTro);
        recyclerViewNhaTro.setLayoutManager(new LinearLayoutManager(this));
        ImageView btnBack = findViewById(R.id.mingcute_arrow_up);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_ThongKe_NhaTro.this, Admin_TrangChuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

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
        // Sửa callback: nhận đủ 2 tham số
        motelAdapter.setOnItemClickListener((maDay, tenTro) -> openRoomList(maDay, tenTro));
        recyclerViewNhaTro.setAdapter(motelAdapter);
    }

    // Sửa lại: Nhận cả mã dãy và tên trọ
    private void openRoomList(int maDay, String tenTro) {
        Intent intent = new Intent(this, Admin_ThongKeActivity.class);
        intent.putExtra("MA_DAY", maDay);
        intent.putExtra("MOTEL_NAME", tenTro); // Nếu muốn truyền tiếp tên nhà trọ
        startActivity(intent);
    }
}
