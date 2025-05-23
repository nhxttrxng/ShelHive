package com.nhom5.shelhive.ui.user.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.adapter.RoomAdapter;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetRoom2Response;
import com.nhom5.shelhive.api.GetRoomInfoResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_RoomListActivity extends AppCompatActivity {

    private int maDay;
    private TextView tvMotelName;
    private ImageView btnBack;
    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;
    private List<GetRoomInfoResponse> roomInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_hoadon_phong); // dùng lại layout này đã sửa

        maDay = getIntent().getIntExtra("MA_DAY", -1);
        tvMotelName = findViewById(R.id.tv_motel_name);
        tvMotelName.setText("Dãy số " + maDay);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomAdapter = new RoomAdapter(this, roomInfoList);
        recyclerView.setAdapter(roomAdapter);

        roomAdapter.setOnItemClickListener(roomInfo -> {
            Intent intent = new Intent(this, User_RoomBillDetailActivity.class);
            intent.putExtra("ROOM_NUMBER", roomInfo.getRoom().getMa_phong());
            intent.putExtra("TENANT_NAME", roomInfo.getUser() != null ? roomInfo.getUser().getHoTen() : "Chưa có");
            intent.putExtra("MA_DAY", maDay);
            startActivity(intent);
        });

        fetchRoomsFromApi();
    }

    private void fetchRoomsFromApi() {
        ApiService.apiService.getRoomsByMaDay(maDay).enqueue(new Callback<List<GetRoom2Response>>() {
            @Override
            public void onResponse(Call<List<GetRoom2Response>> call, Response<List<GetRoom2Response>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (GetRoom2Response room : response.body()) {
                        String maPhongStr = room.getMa_phong();
                        if (maPhongStr != null && maPhongStr.matches("\\d+")) {
                            ApiService.apiService.getRoomInfoByMaPhong(Integer.parseInt(maPhongStr))
                                    .enqueue(new Callback<GetRoomInfoResponse>() {
                                        @Override
                                        public void onResponse(Call<GetRoomInfoResponse> call, Response<GetRoomInfoResponse> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                roomInfoList.add(response.body());
                                                roomAdapter.setData(roomInfoList);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<GetRoomInfoResponse> call, Throwable t) {
                                            Log.e("ROOM_INFO_API", t.getMessage());
                                        }
                                    });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetRoom2Response>> call, Throwable t) {
                Log.e("ROOM_LIST_API", t.getMessage());
            }
        });
    }
}
