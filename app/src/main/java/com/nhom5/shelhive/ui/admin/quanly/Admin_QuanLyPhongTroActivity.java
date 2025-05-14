package com.nhom5.shelhive.ui.admin.quanly;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetRoom2Response;
import com.nhom5.shelhive.ui.common.adapter.Room2Adapter;
import com.nhom5.shelhive.ui.model.Room2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_QuanLyPhongTroActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRooms;
    private Room2Adapter room2Adapter;
    private List<Room2> roomList;
    private int maDay;
    private String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanly_phongtro);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        maDay = intent.getIntExtra("ma_day", -1);
        email = intent.getStringExtra("email");

        // Ánh xạ View
        recyclerViewRooms = findViewById(R.id.recyclerViewRooms);
        recyclerViewRooms.setLayoutManager(new LinearLayoutManager(this));

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> finish());

        roomList = new ArrayList<>();
        room2Adapter = new Room2Adapter(this, roomList);
        recyclerViewRooms.setAdapter(room2Adapter);

        loadRoomList();
    }

    private void loadRoomList() {
        ApiService.apiService.getRoomsByMaDay(maDay).enqueue(new Callback<List<GetRoom2Response>>() {
            @Override
            public void onResponse(Call<List<GetRoom2Response>> call, Response<List<GetRoom2Response>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    roomList.clear();
                    for (GetRoom2Response dto : response.body()) {
                        roomList.add(new Room2(
                                dto.getMa_phong(),
                                dto.getMa_day(),
                                dto.getEmail_user(),
                                dto.getTrang_thai_phong(),
                                dto.getNgay_bat_dau(),
                                dto.getNgay_ket_thuc(),
                                dto.getGia_thue()
                        ));
                    }
                    room2Adapter.updateList(roomList);
                } else {
                    Log.e("QLPhong", "Không lấy được danh sách phòng: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<GetRoom2Response>> call, Throwable t) {
                Log.e("QLPhong", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}