package com.nhom5.shelhive.ui.admin.thongbao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetMotelResponse;
import com.nhom5.shelhive.ui.admin.Admin_TrangChuActivity;
import com.nhom5.shelhive.ui.admin.hoadon.Admin_MotelListActivity;
import com.nhom5.shelhive.ui.common.adapter.MotelAdapter;
import com.nhom5.shelhive.ui.model.Motel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_ThongBao_NhaTro extends AppCompatActivity {
    private RecyclerView recyclerViewNhaTro;
    private MotelAdapter motelAdapter;
    private List<Motel> nhaTroList = new ArrayList<>();
    private TextView tvNoMotels;
    private String adminEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_thongbao_nhatro);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        if (email == null) {
            SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
            email = prefs.getString("email", null);
        }

        adminEmail = email;

        if (adminEmail == null) {
            Toast.makeText(this, "Không tìm thấy email!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerViewNhaTro = findViewById(R.id.recyclerViewNhaTro);
        tvNoMotels = findViewById(R.id.tv_no_motels);
        recyclerViewNhaTro.setLayoutManager(new LinearLayoutManager(this));

        ImageView btnBack = findViewById(R.id.mingcute_arrow_up);
        btnBack.setOnClickListener(v -> finish());

        motelAdapter = new MotelAdapter(this, nhaTroList);
        recyclerViewNhaTro.setAdapter(motelAdapter);

        // Chỉ click vào dãy để xem danh sách phòng, không có edit/xóa
        motelAdapter.setOnItemClickListener((maDay, tenTro) -> openRoomList(maDay, tenTro));

        fetchMotelsFromApi();
    }

    private void fetchMotelsFromApi() {
        ApiService.apiService.getDayTroByAdminEmail(adminEmail)
                .enqueue(new Callback<List<GetMotelResponse>>() {
                    @Override
                    public void onResponse(Call<List<GetMotelResponse>> call, Response<List<GetMotelResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            nhaTroList.clear();
                            for (GetMotelResponse item : response.body()) {
                                nhaTroList.add(new Motel(
                                        item.getMa_day(),
                                        item.getTen_tro(),
                                        item.getDia_chi(),
                                        item.getSo_phong(),
                                        item.getGia_dien(),
                                        item.getGia_nuoc()
                                ));
                            }
                            motelAdapter.notifyDataSetChanged();
                            updateEmptyState();
                        } else {
                            Toast.makeText(Admin_ThongBao_NhaTro.this, "Không tải được danh sách!", Toast.LENGTH_SHORT).show();
                            updateEmptyState();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GetMotelResponse>> call, Throwable t) {
                        Log.e("API_ERROR", t.getMessage(), t);
                        Toast.makeText(Admin_ThongBao_NhaTro.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        updateEmptyState();
                    }
                });
    }

    private void updateEmptyState() {
        if (nhaTroList.isEmpty()) {
            recyclerViewNhaTro.setVisibility(View.GONE);
            tvNoMotels.setVisibility(View.VISIBLE);
        } else {
            recyclerViewNhaTro.setVisibility(View.VISIBLE);
            tvNoMotels.setVisibility(View.GONE);
        }
    }
    // Sửa hàm này:
    private void openRoomList(int maDay, String tenTro) {
        Intent intent = new Intent(this, Admin_ThongBaoActivity.class);
        intent.putExtra("MA_DAY", maDay);
        intent.putExtra("MOTEL_NAME", tenTro); // Nếu cần truyền tên nhà trọ cho màn sau
        startActivity(intent);
    }
}