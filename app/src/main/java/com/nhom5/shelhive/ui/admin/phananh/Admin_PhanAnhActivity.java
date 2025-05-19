package com.nhom5.shelhive.ui.admin.phananh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.adapter.PhanAnhAdapter;
import com.nhom5.shelhive.model.PhanAnh;
import com.nhom5.shelhive.ui.admin.Admin_TrangChuActivity;

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
        ImageView btnBack = findViewById(R.id.btn_back);
        rvUnresolved = findViewById(R.id.rv_unresolved_feedback);
        rvResolved = findViewById(R.id.rv_resolved_feedback);

        // Thay đổi sự kiện nút back để chuyển sang Admin_PhanAnhNhaTroActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_PhanAnhActivity.this, Admin_PhanAnh_NhaTro.class); // thay bằng Activity chính admin của bạn
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
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
