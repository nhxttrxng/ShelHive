package com.nhom5.shelhive.ui.admin.phananh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.common.adapter.PhanAnhAdapter;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.ui.model.PhanAnh;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_PhanAnhActivity extends AppCompatActivity {
    private int maDay;
    private String currentTab = "chưa xử lí";

    private RecyclerView rvUnresolved, rvResolved;
    private PhanAnhAdapter unresolvedAdapter, resolvedAdapter;
    private List<PhanAnh> unresolvedList = new ArrayList<>();
    private List<PhanAnh> resolvedList = new ArrayList<>();

    private TextView tabUnresolved, tabResolved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_phananh); // layout bạn gửi

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String tenTro = intent.getStringExtra("MOTEL_NAME");
        maDay = intent.getIntExtra("MA_DAY", -1);
        if (maDay == -1) {
            Toast.makeText(this, "Không tìm thấy mã dãy", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Gán dữ liệu giao diện
        TextView tvMotelName = findViewById(R.id.tv_motel_name);
        tvMotelName.setText(tenTro != null ? tenTro : "");

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        tabUnresolved = findViewById(R.id.tab_unresolved);
        tabResolved = findViewById(R.id.tab_resolved);

        rvUnresolved = findViewById(R.id.rv_unresolved_feedback);
        rvResolved = findViewById(R.id.rv_resolved_feedback);

        rvUnresolved.setLayoutManager(new LinearLayoutManager(this));
        rvResolved.setLayoutManager(new LinearLayoutManager(this));

        unresolvedAdapter = new PhanAnhAdapter(unresolvedList);
        resolvedAdapter = new PhanAnhAdapter(resolvedList);

        rvUnresolved.setAdapter(unresolvedAdapter);
        rvResolved.setAdapter(resolvedAdapter);

        // Xử lý click từng phản ánh
        unresolvedAdapter.setOnItemClickListener(this::handleItemClick);

        // Bắt sự kiện chuyển tab
        tabUnresolved.setOnClickListener(v -> switchTab("chưa xử lí"));
        tabResolved.setOnClickListener(v -> switchTab("đã xử lí"));

        switchTab("chưa xử lí");
    }

    private void handleItemClick(PhanAnh phanAnh) {
        Intent intent = new Intent(this, Admin_XuLiPhanAnhActivity.class);
        intent.putExtra("ma_phan_anh", phanAnh.getMaPhanAnh());
        intent.putExtra("ma_phong", phanAnh.getMaPhong());
        intent.putExtra("tieu_de", phanAnh.getTieuDe());
        intent.putExtra("loai_van_de", phanAnh.getLoaiSuCo());
        intent.putExtra("tinh_trang", phanAnh.getTinhTrang());
        intent.putExtra("noi_dung", phanAnh.getNoiDung());
        startActivityForResult(intent, 100);
    }

    private void switchTab(String tinhTrang) {
        currentTab = tinhTrang;

        if (tinhTrang.equals("chưa xử lí")) {
            tabUnresolved.setBackgroundResource(R.drawable.bg_tab_chung);
            tabUnresolved.setTextColor(Color.WHITE);
            tabResolved.setBackgroundResource(R.drawable.bg_tab_hoa_don);
            tabResolved.setTextColor(Color.parseColor("#755200"));

            rvUnresolved.setVisibility(View.VISIBLE);
            rvResolved.setVisibility(View.GONE);

            if (unresolvedList.isEmpty()) {
                fetchPhanAnh("chưa xử lí");
            }
        } else {
            tabResolved.setBackgroundResource(R.drawable.bg_tab_hoa_don_1);
            tabResolved.setTextColor(Color.WHITE);
            tabUnresolved.setBackgroundResource(R.drawable.bg_tab_chung_1);
            tabUnresolved.setTextColor(Color.parseColor("#755200"));

            rvResolved.setVisibility(View.VISIBLE);
            rvUnresolved.setVisibility(View.GONE);

            if (resolvedList.isEmpty()) {
                fetchPhanAnh("đã xử lí");
            }
        }
    }

    private void fetchPhanAnh(String tinhTrang) {
        ApiService.apiService.getPhanAnhByTinhTrang(tinhTrang, maDay).enqueue(new Callback<List<PhanAnh>>() {
            @Override
            public void onResponse(Call<List<PhanAnh>> call, Response<List<PhanAnh>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (tinhTrang.equals("chưa xử lí")) {
                        unresolvedList.clear();
                        unresolvedList.addAll(response.body());
                        unresolvedAdapter.notifyDataSetChanged();
                    } else {
                        resolvedList.clear();
                        resolvedList.addAll(response.body());
                        resolvedAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(Admin_PhanAnhActivity.this, "Không có dữ liệu phản ánh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PhanAnh>> call, Throwable t) {
                Toast.makeText(Admin_PhanAnhActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            fetchPhanAnh(currentTab);
        }
    }
}
