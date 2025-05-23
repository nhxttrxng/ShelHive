package com.nhom5.shelhive.ui.admin.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.common.adapter.RoomAdapter;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetRoom2Response;
import com.nhom5.shelhive.api.GetRoomInfoResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_RoomListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;
    private List<GetRoomInfoResponse> roomInfoList = new ArrayList<>();
    private TextView tvMotelName;
    private int maDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_hoadon_phong);

        recyclerView = findViewById(R.id.recyclerViewRooms); // bạn cần thêm ID này trong layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvMotelName = findViewById(R.id.tv_motel_name);
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        maDay = getIntent().getIntExtra("MA_DAY", -1);
        tvMotelName.setText("Dãy số " + maDay);

        roomAdapter = new RoomAdapter(this, roomInfoList);
        recyclerView.setAdapter(roomAdapter);

        roomAdapter.setOnItemClickListener(room -> {
            Intent intent = new Intent(Admin_RoomListActivity.this, Admin_RoomBillDetailActivity.class);
            intent.putExtra("ROOM_NUMBER", room.getRoom().getMa_phong());
            intent.putExtra("TENANT_NAME", room.getUser() != null ? room.getUser().getHoTen() : "Chưa có");
            intent.putExtra("MA_DAY", maDay);
            startActivity(intent);
        });

        loadRooms(maDay);
    }

    private void loadRooms(int maDay) {
        ApiService.apiService.getRoomsByMaDay(maDay).enqueue(new Callback<List<GetRoom2Response>>() {
            @Override
            public void onResponse(Call<List<GetRoom2Response>> call, Response<List<GetRoom2Response>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GetRoom2Response> rawRooms = response.body();
                    roomInfoList.clear();

                    for (GetRoom2Response room : rawRooms) {
                        // Gọi API để lấy thông tin phòng + người thuê
                        ApiService.apiService.getRoomInfoByMaPhong(Integer.parseInt(room.getMa_phong())).enqueue(new Callback<GetRoomInfoResponse>() {
                                    @Override
                            public void onResponse(Call<GetRoomInfoResponse> call, Response<GetRoomInfoResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    roomInfoList.add(response.body());
                                    roomAdapter.setData(roomInfoList); // cập nhật adapter mỗi lần có dữ liệu mới
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

            @Override
            public void onFailure(Call<List<GetRoom2Response>> call, Throwable t) {
                Log.e("ROOM_LIST_API", t.getMessage());
            }
        });
    }
}
