package com.nhom5.shelhive.ui.admin.hoadon;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetBillByRoomResponse;
import com.nhom5.shelhive.api.GetRoomResponse;
import com.nhom5.shelhive.api.GetRoomInfoResponse;
import com.nhom5.shelhive.ui.common.adapter.RoomBillAdapter;
import com.nhom5.shelhive.ui.model.Room;
import com.nhom5.shelhive.ui.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_RoomListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RoomBillAdapter roomBillAdapter;
    private List<Room> roomList = new ArrayList<>();
    private List<Room> filteredRoomList = new ArrayList<>();
    private TextView tvMotelName;
    private int maDay;
    private String motelName; // Biến lưu tên nhà trọ
    private EditText edtSearchRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_hoadon_phong);

        recyclerView = findViewById(R.id.recyclerViewRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        edtSearchRoom = findViewById(R.id.et_search);

        tvMotelName = findViewById(R.id.tv_motel_name);
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        maDay = getIntent().getIntExtra("MA_DAY", -1);
        motelName = getIntent().getStringExtra("MOTEL_NAME");

        // Set tên nhà trọ lên TextView
        if (motelName != null && !motelName.isEmpty()) {
            tvMotelName.setText(motelName);
        } else {
            tvMotelName.setText("Dãy số " + maDay);
        }

        roomBillAdapter = new RoomBillAdapter(this, filteredRoomList);
        recyclerView.setAdapter(roomBillAdapter);

        // Click chỉ khi có người thuê
        roomBillAdapter.setOnItemClickListener(room -> {
            if (room.getDa_thue() != null && room.getDa_thue()) {
                Intent intent = new Intent(Admin_RoomListActivity.this, Admin_RoomBillDetailActivity.class);
                intent.putExtra("ROOM_NUMBER", room.getMa_phong());
                intent.putExtra("MA_PHONG", room.getMa_phong());
                intent.putExtra("TENANT_EMAIL", room.getEmail_user() != null ? room.getEmail_user() : "Chưa có");
                intent.putExtra("MA_DAY", maDay);
                intent.putExtra("MOTEL_NAME", motelName);
                startActivity(intent);
            }
        });

        loadRooms(maDay);

        // TextWatcher tìm kiếm
        edtSearchRoom.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRooms(s.toString());
            }
            @Override public void afterTextChanged(Editable s) { }
        });
    }

    private void loadRooms(int maDay) {
        ApiService.apiService.getRoomsByMaDay(maDay).enqueue(new Callback<List<GetRoomResponse>>() {
            @Override
            public void onResponse(Call<List<GetRoomResponse>> call, Response<List<GetRoomResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GetRoomResponse> rawRooms = response.body();
                    roomList.clear();
                    filteredRoomList.clear();

                    for (GetRoomResponse roomRes : rawRooms) {
                        String maPhong = roomRes.getMa_phong();
                        loadRoomInfoAndBills(maPhong);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<GetRoomResponse>> call, Throwable t) {
                Log.e("ROOM_LIST_API", t.getMessage());
            }
        });
    }

    private void loadRoomInfoAndBills(String maPhong) {
        ApiService.apiService.getRoomInfoByMaPhong(Integer.parseInt(maPhong)).enqueue(new Callback<GetRoomInfoResponse>() {
            @Override
            public void onResponse(Call<GetRoomInfoResponse> call, Response<GetRoomInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Room room = response.body().getRoom();
                    User user = response.body().getUser();

                    // Nếu không có người thuê thì không hiện unpaidBills & status, cũng không click
                    if (room.getDa_thue() != null && room.getDa_thue()) {
                        ApiService.apiService.getBillsByRoom(Integer.parseInt(maPhong)).enqueue(new Callback<List<GetBillByRoomResponse>>() {
                            @Override
                            public void onResponse(Call<List<GetBillByRoomResponse>> call, Response<List<GetBillByRoomResponse>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    List<GetBillByRoomResponse> bills = response.body();
                                    int unpaid = 0;
                                    int overdue = 0;

                                    for (GetBillByRoomResponse bill : bills) {
                                        String status = bill.getTrangThai() != null ? bill.getTrangThai().trim().toLowerCase() : "";
                                        if ("chưa thanh toán".equals(status) || "trễ hạn".equals(status)) {
                                            unpaid++;
                                        }
                                        if ("trễ hạn".equals(status)) {
                                            overdue++;
                                        }
                                    }

                                    String payStatus;
                                    if (unpaid == 0) payStatus = "Đã đóng";
                                    else if (overdue > 0) payStatus = "Trễ hạn";
                                    else payStatus = "Chưa đóng";

                                    room.setUnpaidBills(unpaid);
                                    room.setPayStatus(payStatus);
                                    room.setTenNguoiThue(user != null ? user.getHoTen() : "");
                                }
                                addAndSort(room);
                            }

                            @Override
                            public void onFailure(Call<List<GetBillByRoomResponse>> call, Throwable t) {
                                Log.e("BILL_LIST_API", t.getMessage());
                                addAndSort(room);
                            }
                        });
                    } else {
                        // Không có người thuê, để các trường này = 0/null
                        room.setUnpaidBills(0);
                        room.setPayStatus(null);
                        room.setTenNguoiThue(""); // Hoặc null tuỳ ý
                        addAndSort(room);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetRoomInfoResponse> call, Throwable t) {
                Log.e("ROOM_INFO_API", t.getMessage());
            }
        });
    }

    // Thêm phòng vào list và sort lại
    private void addAndSort(Room room) {
        roomList.add(room);
        sortRoomList(roomList);

        // Filter lại theo keyword hiện tại nếu có
        String keyword = edtSearchRoom.getText().toString();
        filterRooms(keyword);
    }

    // Sắp xếp theo mã phòng tăng dần (so sánh số)
    private void sortRoomList(List<Room> list) {
        Collections.sort(list, new Comparator<Room>() {
            @Override
            public int compare(Room r1, Room r2) {
                try {
                    int m1 = Integer.parseInt(r1.getMa_phong());
                    int m2 = Integer.parseInt(r2.getMa_phong());
                    return Integer.compare(m1, m2);
                } catch (Exception e) {
                    return r1.getMa_phong().compareTo(r2.getMa_phong());
                }
            }
        });
    }

    // Lọc list dựa trên mã phòng hoặc tên người thuê (không phân biệt hoa thường)
    private void filterRooms(String keyword) {
        filteredRoomList.clear();
        if (keyword == null || keyword.trim().isEmpty()) {
            filteredRoomList.addAll(roomList);
        } else {
            String search = keyword.toLowerCase().trim();
            for (Room room : roomList) {
                boolean match = false;
                if (room.getMa_phong() != null && room.getMa_phong().toLowerCase().contains(search)) {
                    match = true;
                } else if (room.getTenNguoiThue() != null && room.getTenNguoiThue().toLowerCase().contains(search)) {
                    match = true;
                }
                if (match) filteredRoomList.add(room);
            }
        }
        // Sort sau filter cũng được, cho chắc luôn đúng
        sortRoomList(filteredRoomList);
        roomBillAdapter.setData(new ArrayList<>(filteredRoomList));
    }

    private boolean isOverdue(String hanDongTien) {
        if (hanDongTien == null || hanDongTien.isEmpty()) return false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            Date han = sdf.parse(hanDongTien);
            Date now = new Date();
            return han != null && han.before(now);
        } catch (ParseException e) {
            return false;
        }
    }
}
