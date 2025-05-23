package com.nhom5.shelhive.ui.admin.phananh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.ui.common.adapter.PhanAnhAdapter;
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
    private int maDay;

    private final ActivityResultLauncher<Intent> xuLiPhanAnhLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // ✅ Load lại danh sách nếu phản ánh đã được xử lý
                    fetchPhanAnhTheoDay(maDay);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_phananh);

        maDay = getIntent().getIntExtra("MA_DAY", -1);
        if (maDay == -1) {
            Toast.makeText(this, "Không tìm thấy mã dãy", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageView btnBack = findViewById(R.id.btn_back);
        rvUnresolved = findViewById(R.id.rv_unresolved_feedback);
        rvResolved = findViewById(R.id.rv_resolved_feedback);

        btnBack.setOnClickListener(v -> finish());

        unresolvedAdapter = new PhanAnhAdapter(unresolvedList);
        resolvedAdapter = new PhanAnhAdapter(resolvedList);

        rvUnresolved.setLayoutManager(new LinearLayoutManager(this));
        rvUnresolved.setAdapter(unresolvedAdapter);

        rvResolved.setLayoutManager(new LinearLayoutManager(this));
        rvResolved.setAdapter(resolvedAdapter);

        fetchPhanAnhTheoDay(maDay);

        unresolvedAdapter.setOnItemClickListener(phanAnh -> {
            Intent intent = new Intent(Admin_PhanAnhActivity.this, Admin_XuLiPhanAnhActivity.class);
            intent.putExtra("ma_phan_anh", phanAnh.getMaPhanAnh());
            intent.putExtra("ma_phong", phanAnh.getMaPhong());
            intent.putExtra("tieu_de", phanAnh.getTieuDe());
            intent.putExtra("loai_van_de", phanAnh.getLoaiSuCo());
            intent.putExtra("tinh_trang", phanAnh.getTinhTrang());
            intent.putExtra("noi_dung", phanAnh.getNoiDung());

            // ✅ Mở activity xử lý và chờ kết quả
            xuLiPhanAnhLauncher.launch(intent);
        });
    }

    private void fetchPhanAnhTheoDay(int maDay) {
        ApiService.apiService.getByMaDay(maDay).enqueue(new Callback<List<PhanAnh>>() {
            @Override
            public void onResponse(Call<List<PhanAnh>> call, Response<List<PhanAnh>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    unresolvedList.clear();
                    resolvedList.clear();
                    for (PhanAnh item : response.body()) {
                        if (item.getTinhTrang().equalsIgnoreCase("chưa xử lí")) {
                            unresolvedList.add(item);
                        } else {
                            resolvedList.add(item);
                        }
                    }
                    unresolvedAdapter.notifyDataSetChanged();
                    resolvedAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Admin_PhanAnhActivity.this, "Không có dữ liệu phản ánh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PhanAnh>> call, Throwable t) {
                Toast.makeText(Admin_PhanAnhActivity.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
