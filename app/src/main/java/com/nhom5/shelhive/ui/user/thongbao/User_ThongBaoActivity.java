package com.nhom5.shelhive.ui.user.thongbao;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.ThongBaoDonGian;
import com.nhom5.shelhive.api.ThongBaoHDDonGian;
import com.nhom5.shelhive.api.ThongBaoHoaDonResponse1;
import com.nhom5.shelhive.api.ThongBaoResponse1;
import com.nhom5.shelhive.ui.common.adapter.ThongBaoDonGianAdapter;
import com.nhom5.shelhive.ui.common.adapter.ThongBaoHDDonGianAdapter;
import com.nhom5.shelhive.ui.user.User_TrangChuActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_ThongBaoActivity extends AppCompatActivity {

    private TextView tabThongBaoChung, tabThongBaoHoaDon;
    private RecyclerView recyclerView;
    private ThongBaoDonGianAdapter adapter;
    private ThongBaoHDDonGianAdapter adapter2;
    private ImageView btnBack;

    private List<ThongBaoDonGian> thongBaoChungList = new ArrayList<>();
    private List<ThongBaoHDDonGian> thongBaoHoaDonList = new ArrayList<>();

    private int maPhong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_thongbao);

        String maPhongStr = getIntent().getStringExtra("maPhong");

        try {
            maPhong = Integer.parseInt(maPhongStr);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: không xác định được mã phòng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ánh xạ view
        tabThongBaoChung = findViewById(R.id.tab_thong_bao_chung);
        tabThongBaoHoaDon = findViewById(R.id.tab_thong_bao_hoa_don);
        recyclerView = findViewById(R.id.recycler_view_thong_bao);
        btnBack = findViewById(R.id.btn_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(User_ThongBaoActivity.this, User_TrangChuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Khởi tạo adapter (chưa setAdapter vội)
        adapter = new ThongBaoDonGianAdapter(new ArrayList<>(), this, this::xuLyClickThongBao);
        adapter2 = new ThongBaoHDDonGianAdapter(new ArrayList<>(), this, this::xuLyClickThongBaoHD);

        // Gọi API lấy thông báo
        fetchThongBaoTheoPhong(maPhong);

        // Gán sự kiện click cho tab
        tabThongBaoChung.setOnClickListener(v -> chuyenTab(true));
        tabThongBaoHoaDon.setOnClickListener(v -> chuyenTab(false));
    }

    private void fetchThongBaoTheoPhong(int maPhong) {
        // Thông báo chung
        ApiService.apiService.getThongBaoChungTheoPhong(maPhong).enqueue(new Callback<ThongBaoResponse1>() {
            @Override
            public void onResponse(Call<ThongBaoResponse1> call, Response<ThongBaoResponse1> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    thongBaoChungList.clear();
                    thongBaoChungList.addAll(response.body().getData());
                    adapter.capNhatDuLieu(thongBaoChungList);
                    chuyenTab(true); // Hiển thị tab chung mặc định
                } else {
                    Toast.makeText(User_ThongBaoActivity.this, "Không có dữ liệu thông báo chung", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ThongBaoResponse1> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi thông báo chung: " + t.getMessage());
                Toast.makeText(User_ThongBaoActivity.this, "Lỗi khi lấy thông báo chung: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Thông báo hóa đơn
        ApiService.apiService.getThongBaoHoaDonTheoPhong(maPhong).enqueue(new Callback<ThongBaoHoaDonResponse1>() {
            @Override
            public void onResponse(Call<ThongBaoHoaDonResponse1> call, Response<ThongBaoHoaDonResponse1> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    thongBaoHoaDonList.clear();
                    thongBaoHoaDonList.addAll(response.body().getData());
                    adapter2.capNhatDuLieu(thongBaoHoaDonList);
                } else {
                    Toast.makeText(User_ThongBaoActivity.this, "Không có dữ liệu thông báo hóa đơn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ThongBaoHoaDonResponse1> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi thông báo hóa đơn: " + t.getMessage());
                Toast.makeText(User_ThongBaoActivity.this, "Lỗi khi lấy thông báo hóa đơn: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chuyenTab(boolean laTabChung) {
        if (laTabChung) {
            // Thiết lập giao diện tab
            tabThongBaoChung.setBackgroundResource(R.drawable.bg_tab_chung);
            tabThongBaoChung.setTextColor(getResources().getColor(android.R.color.white));
            tabThongBaoHoaDon.setBackgroundResource(R.drawable.bg_tab_hoa_don);
            tabThongBaoHoaDon.setTextColor(Color.parseColor("#755200"));

            // Đổi adapter
            recyclerView.setAdapter(adapter);
            adapter.capNhatDuLieu(thongBaoChungList);
        } else {
            tabThongBaoHoaDon.setBackgroundResource(R.drawable.bg_tab_hoa_don_1);
            tabThongBaoHoaDon.setTextColor(getResources().getColor(android.R.color.white));
            tabThongBaoChung.setBackgroundResource(R.drawable.bg_tab_chung_1);
            tabThongBaoChung.setTextColor(Color.parseColor("#755200"));

            recyclerView.setAdapter(adapter2);
            adapter2.capNhatDuLieu(thongBaoHoaDonList);
        }
    }

    private void xuLyClickThongBao(ThongBaoDonGian thongBao) {
        Toast.makeText(this, "Bạn chọn: " + thongBao.getNoi_dung(), Toast.LENGTH_SHORT).show();
    }

    private void xuLyClickThongBaoHD(ThongBaoHDDonGian thongBaoHD) {
        Toast.makeText(this, "Bạn chọn: " + thongBaoHD.getNoi_dung(), Toast.LENGTH_SHORT).show();
    }
}
