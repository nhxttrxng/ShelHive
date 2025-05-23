package com.nhom5.shelhive.ui.admin.phananh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

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

    private RecyclerView rvUnresolved, rvResolved;
    private PhanAnhAdapter unresolvedAdapter, resolvedAdapter;
    private List<PhanAnh> unresolvedList = new ArrayList<>();
    private List<PhanAnh> resolvedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String email = getIntent().getStringExtra("EMAIL");
        if (email == null) {
            SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
            email = prefs.getString("email", null);
        }

        if (email == null) {
            Log.e("Admin_PhanAnh", "Không có email trong SharedPreferences!");
            // Có thể hiển thị thông báo hoặc quay lại màn hình trước
            finish();
            return;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_phananh);

        ImageView btnBack = findViewById(R.id.btn_back);
        rvUnresolved = findViewById(R.id.rv_unresolved_feedback);
        rvResolved = findViewById(R.id.rv_resolved_feedback);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_PhanAnhActivity.this, Admin_PhanAnh_NhaTro.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        unresolvedAdapter = new PhanAnhAdapter(unresolvedList);
        resolvedAdapter = new PhanAnhAdapter(resolvedList);

        rvUnresolved.setLayoutManager(new LinearLayoutManager(this));
        rvUnresolved.setAdapter(unresolvedAdapter);

        rvResolved.setLayoutManager(new LinearLayoutManager(this));
        rvResolved.setAdapter(resolvedAdapter);

        // Gọi API lấy danh sách phản ánh chưa xử lý
        fetchPhanAnh("chưa xử lí");
        fetchPhanAnh("đã xử lí");

        unresolvedAdapter.setOnItemClickListener(new PhanAnhAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PhanAnh phanAnh) {
                Intent intent = new Intent(Admin_PhanAnhActivity.this, Admin_XuLiPhanAnhActivity.class);
                intent.putExtra("ma_phan_anh", phanAnh.getMaPhanAnh());// <-- THÊM DÒNG NÀY
                intent.putExtra("ma_phong", phanAnh.getMaPhong());// <-- THÊM DÒNG NÀY
                intent.putExtra("tieu_de", phanAnh.getTieuDe());
                intent.putExtra("loai_van_de", phanAnh.getLoaiSuCo());
                intent.putExtra("tinh_trang", phanAnh.getTinhTrang());
                intent.putExtra("noi_dung", phanAnh.getNoiDung());

                startActivity(intent);
            }
        });
    }

    private void fetchPhanAnh(String tinh_trang) {

        ApiService.apiService.getPhanAnhByTinhTrang(tinh_trang).enqueue(new Callback<List<PhanAnh>>() {
            @Override
            public void onResponse(Call<List<PhanAnh>> call, Response<List<PhanAnh>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", new Gson().toJson(response.body()));
                    if (tinh_trang.equalsIgnoreCase("chưa xử lí")) {
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
                Log.d("URL", call.request().url().toString());
            }

            @Override
            public void onFailure(Call<List<PhanAnh>> call, Throwable t) {
                Toast.makeText(Admin_PhanAnhActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
