package com.nhom5.shelhive.ui.admin.hoadon;

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
import com.nhom5.shelhive.adapter.MotelAdapter;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetMotelResponse;
import com.nhom5.shelhive.ui.admin.quanly.Admin_SuaNhaTroActivity;
import com.nhom5.shelhive.ui.model.Motel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_MotelListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNhaTro;
    private MotelAdapter motelAdapter;
    private List<Motel> nhaTroList = new ArrayList<>();
    private TextView tvNoMotels;
    private String adminEmail; // đưa vào đây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_hoadon_nhatro);

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

        motelAdapter.setOnItemClickListener(maDay -> openRoomList(maDay));

        motelAdapter.setOnItemActionListener(new MotelAdapter.OnItemActionListener() {
            @Override
            public void onEdit(Motel motel) {
                Intent intent = new Intent(Admin_MotelListActivity.this, Admin_SuaNhaTroActivity.class);
                intent.putExtra("MA_DAY", motel.getMaday());
                startActivity(intent);
            }

            @Override
            public void onDelete(Motel motel) {
                Toast.makeText(Admin_MotelListActivity.this, "Xoá: " + motel.getName(), Toast.LENGTH_SHORT).show();
            }
        });

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
                            Toast.makeText(Admin_MotelListActivity.this, "Không tải được danh sách!", Toast.LENGTH_SHORT).show();
                            updateEmptyState();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GetMotelResponse>> call, Throwable t) {
                        Log.e("API_ERROR", t.getMessage(), t);
                        Toast.makeText(Admin_MotelListActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
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

    private void openRoomList(int maDay) {
        Intent intent = new Intent(this, Admin_RoomListActivity.class);
        intent.putExtra("MA_DAY", maDay);
        startActivity(intent);
    }
}

