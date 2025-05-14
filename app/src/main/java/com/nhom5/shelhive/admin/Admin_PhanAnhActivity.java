package com.nhom5.shelhive.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.adapter.PhanAnhAdapter;
import com.nhom5.shelhive.model.PhanAnh;

import java.util.ArrayList;
import java.util.List;

public class Admin_PhanAnhActivity extends AppCompatActivity {

    private RecyclerView rvUnresolved, rvResolved;
    private PhanAnhAdapter unresolvedAdapter, resolvedAdapter;
    private List<PhanAnh> allPhanAnh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_phananh);

        rvUnresolved = findViewById(R.id.rv_unresolved_feedback);
        rvResolved = findViewById(R.id.rv_resolved_feedback);
        ImageView btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> onBackPressed());

        allPhanAnh = fakeData(); // lấy dữ liệu giả

        List<PhanAnh> unresolvedList = new ArrayList<>();
        List<PhanAnh> resolvedList = new ArrayList<>();

        for (PhanAnh p : allPhanAnh) {
            if (p.isDaXuLy()) {
                resolvedList.add(p);
            } else {
                unresolvedList.add(p);
            }
        }

        unresolvedAdapter = new PhanAnhAdapter(unresolvedList);
        resolvedAdapter = new PhanAnhAdapter(resolvedList);

        rvUnresolved.setLayoutManager(new LinearLayoutManager(this));
        rvUnresolved.setAdapter(unresolvedAdapter);

        rvResolved.setLayoutManager(new LinearLayoutManager(this));
        rvResolved.setAdapter(resolvedAdapter);
        unresolvedAdapter.setOnItemClickListener(new PhanAnhAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PhanAnh phanAnh) {
                Intent intent = new Intent(Admin_PhanAnhActivity.this, Admin_XuLiPhanAnhActivity.class);
                intent.putExtra("tieu_de", phanAnh.getTieuDe());
                intent.putExtra("loai_van_de", phanAnh.getLoaiVanDe());
                intent.putExtra("mo_ta", phanAnh.getMoTa());
                startActivity(intent);
            }
        });
    }

    private List<PhanAnh> fakeData() {
        List<PhanAnh> list = new ArrayList<>();
        list.add(new PhanAnh("Phòng 101", "Hư bóng đèn", false));
        list.add(new PhanAnh("Phòng 102", "Rò rỉ nước", true));
        list.add(new PhanAnh("Phòng 103", "Hư ổ điện", false));
        list.add(new PhanAnh("Phòng 104", "Tiếng ồn lớn", true));
        return list;
    }

}
