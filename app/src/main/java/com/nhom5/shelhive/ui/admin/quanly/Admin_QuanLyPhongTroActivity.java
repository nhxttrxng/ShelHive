package com.nhom5.shelhive.ui.admin.quanly;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetRoomResponse;
import com.nhom5.shelhive.ui.common.adapter.RoomManagementAdapter;
import com.nhom5.shelhive.ui.model.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_QuanLyPhongTroActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRooms;
    private RoomManagementAdapter roomManagementAdapter;
    private List<Room> roomList;
    private List<Room> allRooms;
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

        String tenTro = intent.getStringExtra("ten_tro");
        TextView nameNhaTro = findViewById(R.id.name_nhatro);
        if (tenTro != null && nameNhaTro != null) {
            nameNhaTro.setText(tenTro);
        }

        recyclerViewRooms = findViewById(R.id.recyclerViewRooms);
        recyclerViewRooms.setLayoutManager(new LinearLayoutManager(this));

        EditText editSearchRoom = findViewById(R.id.editSearchRoom);
        editSearchRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRooms(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> finish());

        // Nút thêm phòng
        ImageView addRoomButton = findViewById(R.id.add_room_button);
        addRoomButton.setOnClickListener(v -> {
            Intent intentAdd = new Intent(Admin_QuanLyPhongTroActivity.this, Admin_TaoPhongTroActivity.class);
            intentAdd.putExtra("ma_day", maDay);
            startActivity(intentAdd);
        });

        roomList = new ArrayList<>();
        allRooms = new ArrayList<>();

        // Adapter truyền đúng với trạng thái mới
        roomManagementAdapter = new RoomManagementAdapter(this, roomList, room -> {
            int maPhong = -1;
            try {
                maPhong = Integer.parseInt(room.getMa_phong());
            } catch (Exception e) {
                // Có thể log lỗi hoặc hiện Toast ở đây nếu cần
            }
            Intent intentRoom = new Intent(Admin_QuanLyPhongTroActivity.this, Admin_XemPhongTroActivity.class);
            intentRoom.putExtra("ma_phong", maPhong);
            startActivity(intentRoom);
        });
        recyclerViewRooms.setAdapter(roomManagementAdapter);

        loadRoomList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRoomList();  // Luôn reload lại danh sách phòng khi quay lại
    }

    private void loadRoomList() {
        ApiService.apiService.getRoomsByMaDay(maDay).enqueue(new Callback<List<GetRoomResponse>>() {
            @Override
            public void onResponse(Call<List<GetRoomResponse>> call, Response<List<GetRoomResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    roomList.clear();
                    allRooms.clear();
                    for (GetRoomResponse dto : response.body()) {
                        Room room = new Room(
                                dto.getMa_phong(),
                                dto.getMa_day(),
                                dto.getEmail_user(),
                                dto.getDa_thue(),
                                dto.getNgay_bat_dau(),
                                dto.getNgay_ket_thuc(),
                                dto.getGia_thue()
                        );
                        roomList.add(room);
                        allRooms.add(room);
                    }
                    // Sắp xếp theo ma_phong tăng dần
                    Comparator<Room> roomComparator = (a, b) -> {
                        try {
                            return Integer.compare(Integer.parseInt(a.getMa_phong()), Integer.parseInt(b.getMa_phong()));
                        } catch (Exception e) {
                            return a.getMa_phong().compareTo(b.getMa_phong());
                        }
                    };
                    Collections.sort(roomList, roomComparator);
                    Collections.sort(allRooms, roomComparator);

                    roomManagementAdapter.updateList(roomList);
                } else {
                    Log.e("QLPhong", "Không lấy được danh sách phòng: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<GetRoomResponse>> call, Throwable t) {
                Log.e("QLPhong", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void filterRooms(String keyword) {
        List<Room> filtered = new ArrayList<>();
        for (Room room : allRooms) {
            String maPhong = room.getMa_phong();
            String soPhongDisplay = maPhong.length() >= 2 ? maPhong.substring(maPhong.length() - 2) : maPhong;
            // Có thể dùng field trạng thái mới để filter (nếu muốn), ví dụ: "Đã thuê", "Trống"
            String trangThai = (room.getDa_thue() != null && room.getDa_thue()) ? "Đã thuê" : "Trống";

            if (soPhongDisplay.toLowerCase().contains(keyword.toLowerCase()) ||
                    trangThai.toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(room);
            }
        }
        // Sắp xếp lại filtered luôn cho đúng thứ tự
        filtered.sort((a, b) -> {
            try {
                return Integer.compare(Integer.parseInt(a.getMa_phong()), Integer.parseInt(b.getMa_phong()));
            } catch (Exception e) {
                return a.getMa_phong().compareTo(b.getMa_phong());
            }
        });
        roomManagementAdapter.updateList(filtered);
    }
}
