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
import com.nhom5.shelhive.ui.common.adapter.ThongBaoAdapter;
import com.nhom5.shelhive.ui.model.ThongBao;
import com.nhom5.shelhive.ui.user.User_TrangChuActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_ThongBaoActivity extends AppCompatActivity {

    private TextView tabThongBaoChung, tabThongBaoHoaDon;
    private RecyclerView recyclerView;
    private ThongBaoAdapter adapter;
    private ImageView btnBack;

    private List<ThongBao> thongBaoChungList = new ArrayList<>();
    private List<ThongBao> thongBaoHoaDonList = new ArrayList<>();

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

        // Khởi tạo adapter với danh sách rỗng, callback xử lý click thông báo
        adapter = new ThongBaoAdapter(new ArrayList<>(), this, this::xuLyClickThongBao);
        recyclerView.setAdapter(adapter);

        // Load dữ liệu thông báo từ API
        fetchThongBaoTheoPhong(maPhong);

        // Gán sự kiện click tab
        tabThongBaoChung.setOnClickListener(v -> chuyenTab(true));
        tabThongBaoHoaDon.setOnClickListener(v -> chuyenTab(false));
    }

    private void fetchThongBaoTheoPhong(int maPhong) {
        // Giả sử ApiService có 2 API:
        // getThongBaoChungTheoPhong(maPhong)
        // getThongBaoHoaDonTheoPhong(maPhong)

        // Call API lấy thông báo chung
        ApiService.apiService.getThongBaoChungTheoPhong(maPhong).enqueue(new Callback<List<ThongBao>>() {
            @Override
            public void onResponse(Call<List<ThongBao>> call, Response<List<ThongBao>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    thongBaoChungList.clear();
                    thongBaoChungList.addAll(response.body());
                    // Hiển thị mặc định tab thông báo chung
                    adapter.capNhatDuLieu(thongBaoChungList);
                    chuyenTab(true);
                } else {
                    Toast.makeText(User_ThongBaoActivity.this, "Không có dữ liệu thông báo chung", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ThongBao>> call, Throwable t) {
                Toast.makeText(User_ThongBaoActivity.this, "Lỗi khi lấy thông báo chung: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Call API lấy thông báo hóa đơn
        ApiService.apiService.getThongBaoHoaDonTheoPhong(maPhong).enqueue(new Callback<List<ThongBao>>() {
            @Override
            public void onResponse(Call<List<ThongBao>> call, Response<List<ThongBao>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    thongBaoHoaDonList.clear();
                    thongBaoHoaDonList.addAll(response.body());
                } else {
                    Toast.makeText(User_ThongBaoActivity.this, "Không có dữ liệu thông báo hóa đơn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ThongBao>> call, Throwable t) {
                Log.e("API_ERROR", "onFailure: " + t.getMessage());
                Toast.makeText(User_ThongBaoActivity.this, "Lỗi khi lấy thông báo hóa đơn: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chuyenTab(boolean laTabChung) {
        if (laTabChung) {
            tabThongBaoChung.setBackgroundResource(R.drawable.bg_tab_chung);
            tabThongBaoChung.setTextColor(getResources().getColor(android.R.color.white));
            tabThongBaoHoaDon.setBackgroundResource(R.drawable.bg_tab_hoa_don);
            tabThongBaoHoaDon.setTextColor(Color.parseColor("#755200"));
            adapter.capNhatDuLieu(thongBaoChungList);
        } else {
            tabThongBaoHoaDon.setBackgroundResource(R.drawable.bg_tab_hoa_don_1);
            tabThongBaoHoaDon.setTextColor(getResources().getColor(android.R.color.white));
            tabThongBaoChung.setBackgroundResource(R.drawable.bg_tab_chung_1);
            tabThongBaoChung.setTextColor(Color.parseColor("#755200"));
            adapter.capNhatDuLieu(thongBaoHoaDonList);
        }
    }

    private void xuLyClickThongBao(ThongBao thongBao) {
        // Xử lý click thông báo: ví dụ hiện Toast hoặc mở Activity chi tiết
        Toast.makeText(this, "Bạn chọn: " + thongBao.getNoiDung(), Toast.LENGTH_SHORT).show();

        // Nếu cần mở chi tiết có thể làm như này:
        // Intent intent = new Intent(this, User_ChiTietThongBaoActivity.class);
        // intent.putExtra("thongBao", thongBao);
        // startActivity(intent);
    }
}
