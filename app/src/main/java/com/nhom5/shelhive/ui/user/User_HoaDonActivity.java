package com.nhom5.shelhive.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.common.adapter.MotelAdapter;
import com.nhom5.shelhive.ui.model.Motel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class User_HoaDonActivity extends AppCompatActivity {
    // Phần màn hình danh sách nhà trọ
    private RecyclerView recyclerViewNhaTro;
    private MotelAdapter motelAdapter;
    private List<Motel> nhaTroList;

    // Phần màn hình danh sách phòng
    private TextView tvTitle, tvMotelName;
    private ImageView btnBack;
    private EditText etSearch;
    private RelativeLayout room1, room2, room3;

    // Phần màn hình chi tiết hóa đơn phòng
    private RelativeLayout dateSelector, filterSelector;
    private TextView tvRoomInfo, tvDate, tvFilter;
    private ScrollView scrollContainer;
    private RelativeLayout extensionBill, overdueBill1, overdueBill2, paidBill;
    private TextView tvNoUnpaidBills;
    private ImageView addBillButton;

    private boolean isRoomListMode = false;
    private boolean isRoomBillDetailMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Kiểm tra xem đang ở chế độ nào
        if (getIntent().hasExtra("ROOM_NUMBER") && getIntent().hasExtra("TENANT_NAME")) {
            // Chế độ chi tiết hóa đơn phòng
            isRoomBillDetailMode = true;
            isRoomListMode = false;
            setContentView(R.layout.admin_chitiet_hdp);

            // Thiết lập Edge-to-Edge
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            initRoomBillDetailViews();
            setupRoomBillDetailListeners();
            loadRoomBillDetailData();
        } else if (getIntent().hasExtra("MOTEL_NAME")) {
            // Chế độ danh sách phòng của nhà trọ
            isRoomListMode = true;
            isRoomBillDetailMode = false;
            setContentView(R.layout.admin_hoadon_phong);

            // Thiết lập Edge-to-Edge
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            initRoomViews();
            setupRoomListeners();
            loadRoomData();
        } else {
            // Chế độ danh sách nhà trọ
            isRoomListMode = false;
            isRoomBillDetailMode = false;
            setContentView(R.layout.admin_hoadon_nhatro);

            // Thiết lập Edge-to-Edge
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            initMotelViews();
            loadMotelData();
        }
    }

    // Khởi tạo view cho màn hình danh sách nhà trọ
    private void initMotelViews() {
        recyclerViewNhaTro = findViewById(R.id.recyclerViewNhaTro);
        recyclerViewNhaTro.setLayoutManager(new LinearLayoutManager(this));

        // Lấy tham chiếu đến nút quay lại
        ImageView btnBack = findViewById(R.id.mingcute_arrow_up);
        btnBack.setOnClickListener(v -> finish());
    }

    // Khởi tạo view cho màn hình danh sách phòng
    private void initRoomViews() {
        tvTitle = findViewById(R.id.tv_title);
        tvMotelName = findViewById(R.id.tv_motel_name);
        btnBack = findViewById(R.id.btn_back);
        etSearch = findViewById(R.id.et_search);

        room1 = findViewById(R.id.room_1);
        room2 = findViewById(R.id.room_2);
        room3 = findViewById(R.id.room_3);
    }

    // Khởi tạo view cho màn hình chi tiết hóa đơn phòng
    private void initRoomBillDetailViews() {
        // Header
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tv_title);
        tvRoomInfo = findViewById(R.id.tv_room_info);

        // Filter bar
        dateSelector = findViewById(R.id.date_selector);
        filterSelector = findViewById(R.id.filter_selector);

        // Tìm các TextView trong date_selector và filter_selector
        tvDate = findViewById(R.id.tv_date);
        tvFilter = findViewById(R.id.tv_filter);

        // Thiết lập ngày mặc định là ngày 1 của tháng trước
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        // Giảm tháng đi 1 để lấy tháng trước
        calendar.add(Calendar.MONTH, -1);
        // Đặt ngày là ngày 1 của tháng
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        if (tvDate != null) {
            tvDate.setText(dateFormat.format(calendar.getTime()));
        }

        // Thiết lập filter mặc định là "Tất cả"
        if (tvFilter != null) {
            tvFilter.setText("Tất cả");
        }

        // Scroll container
        scrollContainer = findViewById(R.id.scroll_container);

        // Bill containers
        extensionBill = findViewById(R.id.extension_bill);
        overdueBill1 = findViewById(R.id.overdue_bill_1);
        overdueBill2 = findViewById(R.id.overdue_bill_2);
        paidBill = findViewById(R.id.paid_bill);

        // No unpaid bills message
        tvNoUnpaidBills = findViewById(R.id.no_unpaid_bills);

        // Add bill button
        addBillButton = findViewById(R.id.add_bill_button);
    }

    // Thiết lập sự kiện cho màn hình danh sách nhà trọ
    private void setupMotelListeners() {
        if (motelAdapter != null) {
            motelAdapter.setOnItemClickListener((motel, position) -> {
                openRoomList(motel.getName());
            });
        }
    }

    // Thiết lập sự kiện cho màn hình chi tiết hóa đơn phòng
    private void setupRoomBillDetailListeners() {
        btnBack.setOnClickListener(v -> finish());

        if (addBillButton != null) {
            addBillButton.setOnClickListener(v -> {
                Toast.makeText(this, "Thêm hóa đơn mới", Toast.LENGTH_SHORT).show();
                // Mở màn hình thêm hóa đơn mới
                // TODO: Thêm code mở màn hình tạo hóa đơn mới
            });
        }

        if (dateSelector != null) {
            dateSelector.setOnClickListener(v -> {
                // Tạo dialog chọn ngày
                Calendar currentDate = Calendar.getInstance();
                // Lấy ngày hiện tại từ TextView
                try {
                    if (tvDate != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Date date = dateFormat.parse(tvDate.getText().toString());
                        if (date != null) {
                            currentDate.setTime(date);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);

                android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                        this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            // Cập nhật ngày đã chọn vào TextView
                            Calendar selectedDate = Calendar.getInstance();
                            selectedDate.set(selectedYear, selectedMonth, selectedDay);

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            if (tvDate != null) {
                                tvDate.setText(dateFormat.format(selectedDate.getTime()));
                            }

                            // Tải lại dữ liệu với ngày mới
                            loadRoomBillDetailData();
                        },
                        year, month, day
                );

                datePickerDialog.show();
            });
        }

        if (filterSelector != null) {
            filterSelector.setOnClickListener(v -> {
                // Tạo popup menu để chọn bộ lọc
                android.widget.PopupMenu popup = new android.widget.PopupMenu(this, filterSelector);
                popup.getMenu().add("Tất cả");
                popup.getMenu().add("Yêu cầu gia hạn");
                popup.getMenu().add("Trễ hạn");
                popup.getMenu().add("Chưa đóng");
                popup.getMenu().add("Đã đóng");

                popup.setOnMenuItemClickListener(item -> {
                    // Cập nhật filter đã chọn
                    if (tvFilter != null) {
                        tvFilter.setText(item.getTitle());
                    }

                    // Tải lại dữ liệu với filter mới
                    loadRoomBillDetailData();
                    return true;
                });

                popup.show();
            });
        }

        // Bắt sự kiện click vào các bill
        if (extensionBill != null) {
            extensionBill.setOnClickListener(v -> {
                Toast.makeText(this, "Chi tiết hóa đơn yêu cầu gia hạn", Toast.LENGTH_SHORT).show();
                // TODO: Mở dialog xử lý yêu cầu gia hạn
            });
        }

        if (overdueBill1 != null) {
            overdueBill1.setOnClickListener(v -> {
                Toast.makeText(this, "Chi tiết hóa đơn trễ hạn (Tháng 2/2025)", Toast.LENGTH_SHORT).show();
                // TODO: Mở dialog xem chi tiết và xử lý hóa đơn
            });
        }

        if (overdueBill2 != null) {
            overdueBill2.setOnClickListener(v -> {
                Toast.makeText(this, "Chi tiết hóa đơn trễ hạn (Tháng 1/2025)", Toast.LENGTH_SHORT).show();
                // TODO: Mở dialog xem chi tiết và xử lý hóa đơn
            });
        }

        if (paidBill != null) {
            paidBill.setOnClickListener(v -> {
                Toast.makeText(this, "Chi tiết hóa đơn đã đóng", Toast.LENGTH_SHORT).show();
                // TODO: Mở dialog xem chi tiết hóa đơn đã thanh toán
            });
        }
    }

    // Tải dữ liệu nhà trọ và hiển thị lên RecyclerView
    private void loadMotelData() {
        // Tạo dữ liệu mẫu
        nhaTroList = new ArrayList<>();
        nhaTroList.add(new Motel(2,"Nhà trọ Trường Phát 1", "số 41, đường N4, p. Mỹ Phước, tp. Bến Cát, Bình Dương"));
        nhaTroList.add(new Motel(4, "Nhà trọ Trường Phát 2", "số 42, đường N4, p. Mỹ Phước, tp. Bến Cát, Bình Dương"));

        // Hiển thị hoặc ẩn thông báo không có dữ liệu
        TextView tvNoMotels = findViewById(R.id.tv_no_motels);
        if (nhaTroList.isEmpty() && tvNoMotels != null) {
            recyclerViewNhaTro.setVisibility(View.GONE);
            tvNoMotels.setVisibility(View.VISIBLE);
        } else {
            recyclerViewNhaTro.setVisibility(View.VISIBLE);
            if (tvNoMotels != null) {
                tvNoMotels.setVisibility(View.GONE);
            }
        }

        // Khởi tạo và gán adapter
        motelAdapter = new MotelAdapter(this, nhaTroList);
        motelAdapter.setOnItemClickListener((motel, position) -> {
            openRoomList(motel.getName());
        });
        recyclerViewNhaTro.setAdapter(motelAdapter);
    }

    // Thiết lập sự kiện cho màn hình danh sách phòng
    private void setupRoomListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại màn hình trước
            }
        });

        // Xử lý khi nhấn vào phòng 1
        room1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở màn hình chi tiết hoá đơn phòng
                openRoomBillDetail("1", "Ngô Nhựt Trường");
            }
        });

        // Xử lý khi nhấn vào phòng 2
        room2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở màn hình chi tiết hoá đơn phòng
                openRoomBillDetail("2", "Trần Danh Vinh");
            }
        });

        // Xử lý khi nhấn vào phòng 3
        room3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở màn hình chi tiết hoá đơn phòng
                openRoomBillDetail("3", "Trần Thảo Quyên");
            }
        });
    }

    // Mở màn hình danh sách phòng
    private void openRoomList(String motelName) {
        Intent intent = new Intent(User_HoaDonActivity.this, User_HoaDonActivity.class);
        intent.putExtra("MOTEL_NAME", motelName);
        startActivity(intent);
    }

    // Tải dữ liệu cho màn hình danh sách phòng
    private void loadRoomData() {
        // Nhận dữ liệu từ intent
        String motelName = getIntent().getStringExtra("MOTEL_NAME");
        if (motelName != null && !motelName.isEmpty()) {
            tvMotelName.setText(motelName);
        }

        // Trong thực tế, bạn sẽ lấy danh sách phòng từ database dựa vào tên nhà trọ
        // Ví dụ: roomList = databaseHelper.getRoomsByMotelName(motelName);
        // Sau đó cập nhật giao diện dựa trên dữ liệu thực

        // Kiểm tra có phòng nào không
        LinearLayout roomsContainer = findViewById(R.id.rooms_container);
        TextView tvNoRooms = findViewById(R.id.tv_no_rooms);

        // Kiểm tra các phòng trong layout hiện tại
        boolean hasRooms = room1 != null || room2 != null || room3 != null;

        if (!hasRooms && tvNoRooms != null) {
            if (roomsContainer != null) roomsContainer.setVisibility(View.GONE);
            tvNoRooms.setVisibility(View.VISIBLE);
            tvNoRooms.setText("Không có hóa đơn nào!");
        } else {
            if (roomsContainer != null) roomsContainer.setVisibility(View.VISIBLE);
            if (tvNoRooms != null) tvNoRooms.setVisibility(View.GONE);
        }
    }

    // Tải dữ liệu cho màn hình chi tiết hóa đơn phòng
    private void loadRoomBillDetailData() {
        // Nhận dữ liệu từ intent
        String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
        String tenantName = getIntent().getStringExtra("TENANT_NAME");

        if (roomNumber != null) {
            // Hiển thị thông tin phòng
            tvRoomInfo.setText("Phòng " + roomNumber + " - " + tenantName);

            // Lấy bộ lọc hiện tại
            String currentFilter = "";
            if (tvFilter != null) {
                currentFilter = tvFilter.getText().toString();
            }

            // Ẩn tất cả các hóa đơn trước
            if (extensionBill != null) extensionBill.setVisibility(View.GONE);
            if (overdueBill1 != null) overdueBill1.setVisibility(View.GONE);
            if (overdueBill2 != null) overdueBill2.setVisibility(View.GONE);
            if (paidBill != null) paidBill.setVisibility(View.GONE);
            if (tvNoUnpaidBills != null) tvNoUnpaidBills.setVisibility(View.GONE);

            // Biến để theo dõi xem có hiển thị hóa đơn nào không
            boolean anyBillsShown = false;

            // Tùy thuộc vào bộ lọc, hiển thị các hóa đơn tương ứng
            switch (currentFilter) {
                case "Trễ hạn":
                    if (roomNumber.equals("1")) {
                        if (overdueBill1 != null) {
                            overdueBill1.setVisibility(View.VISIBLE);
                            anyBillsShown = true;
                        }
                        if (overdueBill2 != null) {
                            overdueBill2.setVisibility(View.VISIBLE);
                            anyBillsShown = true;
                        }
                    }
                    // Phòng 2 và 3 không có hóa đơn trễ hạn
                    // anyBillsShown sẽ vẫn là false
                    break;
                case "Chưa đóng":
                    if (roomNumber.equals("1")) {
                        if (extensionBill != null) {
                            extensionBill.setVisibility(View.VISIBLE);
                            anyBillsShown = true;
                        }
                    }
                    // Phòng 2 và 3 không có hóa đơn chưa đóng
                    // anyBillsShown sẽ vẫn là false
                    break;
                case "Đã đóng":
                    // Cả 3 phòng đều có hóa đơn đã đóng
                    if (paidBill != null) {
                        paidBill.setVisibility(View.VISIBLE);
                        anyBillsShown = true;
                    }
                    break;
                case "Yêu cầu gia hạn":
                    if (roomNumber.equals("1")) {
                        if (extensionBill != null) {
                            extensionBill.setVisibility(View.VISIBLE);
                            anyBillsShown = true;
                        }
                    }
                    // Phòng 2 và 3 không có yêu cầu gia hạn
                    // anyBillsShown sẽ vẫn là false
                    break;
                default: // "Tất cả" hoặc bất kỳ filter nào khác
                    // Hiển thị tùy theo phòng
                    switch (roomNumber) {
                        case "1": // Phòng 1 - Ngô Nhựt Trường: Có 2 hóa đơn trễ hạn, 1 yêu cầu gia hạn, 1 đã đóng
                            if (extensionBill != null) {
                                extensionBill.setVisibility(View.VISIBLE);
                                anyBillsShown = true;
                            }
                            if (overdueBill1 != null) {
                                overdueBill1.setVisibility(View.VISIBLE);
                                anyBillsShown = true;
                            }
                            if (overdueBill2 != null) {
                                overdueBill2.setVisibility(View.VISIBLE);
                                anyBillsShown = true;
                            }
                            if (paidBill != null) {
                                paidBill.setVisibility(View.VISIBLE);
                                anyBillsShown = true;
                            }
                            break;
                        case "2": // Phòng 2 - Trần Danh Vinh: Chỉ có hóa đơn đã đóng
                            if (paidBill != null) {
                                paidBill.setVisibility(View.VISIBLE);
                                anyBillsShown = true;
                            }
                            break;
                        case "3": // Phòng 3 - Trần Thảo Quyên: Chỉ có hóa đơn đã đóng
                            if (paidBill != null) {
                                paidBill.setVisibility(View.VISIBLE);
                                anyBillsShown = true;
                            }
                            break;
                        default:
                            // Mặc định hiển thị tất cả
                            if (extensionBill != null) {
                                extensionBill.setVisibility(View.VISIBLE);
                                anyBillsShown = true;
                            }
                            if (overdueBill1 != null) {
                                overdueBill1.setVisibility(View.VISIBLE);
                                anyBillsShown = true;
                            }
                            if (overdueBill2 != null) {
                                overdueBill2.setVisibility(View.VISIBLE);
                                anyBillsShown = true;
                            }
                            if (paidBill != null) {
                                paidBill.setVisibility(View.VISIBLE);
                                anyBillsShown = true;
                            }
                            break;
                    }
                    break;
            }

            // Hiển thị thông báo nếu không có hóa đơn nào
            if (!anyBillsShown && tvNoUnpaidBills != null) {
                tvNoUnpaidBills.setVisibility(View.VISIBLE);
                tvNoUnpaidBills.setText("Không có hóa đơn nào!");
            }
        }
    }

    // Mở màn hình chi tiết hóa đơn phòng
    private void openRoomBillDetail(String roomNumber, String tenantName) {
        Intent intent = new Intent(User_HoaDonActivity.this, User_HoaDonActivity.class);
        intent.putExtra("ROOM_NUMBER", roomNumber);
        intent.putExtra("TENANT_NAME", tenantName);
        startActivity(intent);
    }
}