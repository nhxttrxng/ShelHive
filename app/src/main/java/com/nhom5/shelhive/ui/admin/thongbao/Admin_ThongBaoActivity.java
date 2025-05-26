package com.nhom5.shelhive.ui.admin.thongbao;

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
import com.nhom5.shelhive.api.ThongBaoResponse;
import com.nhom5.shelhive.ui.common.adapter.ThongBaoAdapter;
import com.nhom5.shelhive.ui.common.adapter.ThongBaoHDAdapter;
import com.nhom5.shelhive.ui.model.ThongBao;
import com.nhom5.shelhive.ui.model.ThongBaoHoaDon;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_ThongBaoActivity extends AppCompatActivity {

    private TextView tabThongBaoChung, tabThongBaoHoaDon;
    private RecyclerView recyclerView;

    private ThongBaoAdapter thongBaoAdapter;
    private ThongBaoHDAdapter thongBaoHDAdapter;

    private List<ThongBao> thongBaoChungList = new ArrayList<>();
    private List<ThongBaoHoaDon> thongBaoHoaDonList = new ArrayList<>();

    private int maDay;
    private boolean isTabChung = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_thongbao);

        maDay = getIntent().getIntExtra("MA_DAY", -1);
        if (maDay == -1) {
            Toast.makeText(this, "Không tìm thấy mã dãy", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tabThongBaoChung = findViewById(R.id.tab_thong_bao_chung_admin);
        tabThongBaoHoaDon = findViewById(R.id.tab_thong_bao_hoa_don_admin);
        recyclerView = findViewById(R.id.recycler_view_thong_bao);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        thongBaoAdapter = new ThongBaoAdapter(new ArrayList<>(), this, this::xuLyClickThongBaoChung);
        thongBaoHDAdapter = new ThongBaoHDAdapter(new ArrayList<>(), this, this::xuLyClickThongBaoHoaDon);

        recyclerView.setAdapter(thongBaoAdapter);  // Default là tab chung

        ImageView btnThem = findViewById(R.id.btn_bottom_image);
        ImageView btnBack = findViewById(R.id.btn_back);

        btnThem.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_TaoThongBao.class);
            intent.putExtra("MA_DAY", maDay);
            startActivityForResult(intent, 100);
        });

        btnBack.setOnClickListener(v -> finish());

        tabThongBaoChung.setOnClickListener(v -> chuyenTab(true));
        tabThongBaoHoaDon.setOnClickListener(v -> chuyenTab(false));

        loadThongBaoChung();
        loadThongBaoHoaDon();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (isTabChung) {
                loadThongBaoChung();
            } else {
                loadThongBaoHoaDon();
            }
        }
    }

    private void chuyenTab(boolean laTabChung) {
        isTabChung = laTabChung;

        if (laTabChung) {
            tabThongBaoChung.setBackgroundResource(R.drawable.bg_tab_chung);
            tabThongBaoChung.setTextColor(Color.WHITE);
            tabThongBaoHoaDon.setBackgroundResource(R.drawable.bg_tab_hoa_don);
            tabThongBaoHoaDon.setTextColor(Color.parseColor("#755200"));
            recyclerView.setAdapter(thongBaoAdapter);
            thongBaoAdapter.capNhatDuLieu(thongBaoChungList);
        } else {
            tabThongBaoHoaDon.setBackgroundResource(R.drawable.bg_tab_hoa_don_1);
            tabThongBaoHoaDon.setTextColor(Color.WHITE);
            tabThongBaoChung.setBackgroundResource(R.drawable.bg_tab_chung_1);
            tabThongBaoChung.setTextColor(Color.parseColor("#755200"));
            recyclerView.setAdapter(thongBaoHDAdapter);
            thongBaoHDAdapter.capNhatDuLieu(thongBaoHoaDonList);
        }
    }

    private void loadThongBaoChung() {
        ApiService.apiService.getThongBaoByMaDay(maDay).enqueue(new Callback<ThongBaoResponse>() {
            @Override
            public void onResponse(Call<ThongBaoResponse> call, Response<ThongBaoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    thongBaoChungList = response.body().getNotifications();
                    if (isTabChung) {
                        thongBaoAdapter.capNhatDuLieu(thongBaoChungList);
                    }
                } else {
                    Toast.makeText(Admin_ThongBaoActivity.this, "Không có dữ liệu thông báo chung", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ThongBaoResponse> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi tải thông báo chung: " + t.getMessage());
                Toast.makeText(Admin_ThongBaoActivity.this, "Lỗi tải thông báo chung", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadThongBaoHoaDon() {
        ApiService.apiService.getThongBaoHoaDonByMaDay(maDay).enqueue(new Callback<List<ThongBaoHoaDon>>() {
            @Override
            public void onResponse(Call<List<ThongBaoHoaDon>> call, Response<List<ThongBaoHoaDon>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    thongBaoHoaDonList = response.body();
                    if (!isTabChung) {
                        // Đảm bảo gán adapter nếu chưa
                        recyclerView.setAdapter(thongBaoHDAdapter);
                        thongBaoHDAdapter.capNhatDuLieu(thongBaoHoaDonList);
                    }
                } else {
                    Toast.makeText(Admin_ThongBaoActivity.this, "Không có dữ liệu thông báo hóa đơn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ThongBaoHoaDon>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi tải thông báo hóa đơn: " + t.getMessage());
                Toast.makeText(Admin_ThongBaoActivity.this, "Lỗi tải thông báo hóa đơn", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void xuLyClickThongBaoChung(ThongBao thongBao) {
        Intent intent = new Intent(this, Admin_XoaThongBaoActivity.class);
        intent.putExtra("ma_thong_bao", thongBao.getMaThongBao());
        intent.putExtra("ma_day", thongBao.getMaDay());
        intent.putExtra("noi_dung", thongBao.getNoiDung());
        intent.putExtra("ngay_tao", thongBao.getNgayTao());
        startActivityForResult(intent, 100);
    }

    private void xuLyClickThongBaoHoaDon(ThongBaoHoaDon thongBaoHD) {
        Intent intent = new Intent(this, Admin_XoaThongBaoActivity.class);
        intent.putExtra("ma_thong_bao", thongBaoHD.getMa_thong_bao_hoa_don());
        intent.putExtra("ma_day", thongBaoHD.getMa_day());
        intent.putExtra("noi_dung", thongBaoHD.getNoi_dung());
        intent.putExtra("ngay_tao", thongBaoHD.getNgay_tao().toString());
        startActivityForResult(intent, 100);
    }
}
