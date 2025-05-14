package com.nhom5.shelhive.ui.admin;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ScrollView;
import android.widget.Button;
import android.widget.CheckBox;

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

public class Admin_HoaDonActivity extends AppCompatActivity {
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

    // Thêm các biến instance mới
    private TextView tvNoExtensionBills, tvNoOverdueBills, tvNoPaidBills;
    private View extensionHeader, overdueHeader, paidHeader;

    // Thêm biến để kiểm soát chế độ xem chi tiết hóa đơn
    private boolean isRoomListMode = false;
    private boolean isRoomBillDetailMode = false;
    private boolean isViewBillDetailMode = false;

    // Thêm biến cho các checkbox
    private CheckBox cbElectricity, cbWater, cbRoom, cbInterest, cbLateFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Kiểm tra xem đang ở chế độ nào
        if (getIntent().hasExtra("CREATE_BILL")) {
            // Chế độ tạo mới hóa đơn
            setContentView(R.layout.admin_taomoi_hdp);

            // Thiết lập Edge-to-Edge
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            initCreateBillViews();
            setupCreateBillListeners();
            loadCreateBillData();
        } else if (getIntent().hasExtra("EXTEND_BILL")) {
            // Chế độ gia hạn hóa đơn
            setContentView(R.layout.admin_giahan_hdp);

            // Thiết lập Edge-to-Edge
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            initExtendBillViews();
            setupExtendBillListeners();
            loadExtendBillData();
        } else if (getIntent().hasExtra("EDIT_BILL")) {
            // Chế độ sửa hóa đơn
            setContentView(R.layout.admin_sua_hdp);

            // Thiết lập Edge-to-Edge
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            initEditBillViews();
            setupEditBillListeners();
            loadEditBillData();
        } else if (getIntent().hasExtra("VIEW_BILL_DETAIL")) {
            // Chế độ xem chi tiết một hóa đơn cụ thể
            isViewBillDetailMode = true;
            isRoomBillDetailMode = false;
            isRoomListMode = false;
            setContentView(R.layout.admin_xem_hdp);

            // Thiết lập Edge-to-Edge
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            initViewBillDetailViews();
            setupViewBillDetailListeners();
            loadViewBillDetailData();
        } else if (getIntent().hasExtra("ROOM_NUMBER") && getIntent().hasExtra("TENANT_NAME")) {
            // Chế độ chi tiết hóa đơn phòng
            isRoomBillDetailMode = true;
            isRoomListMode = false;
            isViewBillDetailMode = false;
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
            isViewBillDetailMode = false;
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
            isViewBillDetailMode = false;
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

    // Khởi tạo view cho màn hình xem chi tiết hóa đơn
    private void initViewBillDetailViews() {
        // Header
        btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvRoomNumber = findViewById(R.id.tv_room_number);

        // Lấy thông tin phòng và tháng từ intent
        String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
        String billMonth = getIntent().getStringExtra("BILL_MONTH");
        String billType = getIntent().getStringExtra("BILL_TYPE");

        if (roomNumber != null) {
            tvRoomNumber.setText("Phòng " + roomNumber);
        }

        // Các trường thông tin hóa đơn
        TextView tvMonth = findViewById(R.id.ed_month);
        TextView tvOriginalDueDate = findViewById(R.id.ed_original_due_date);
        TextView tvNewDueDate = findViewById(R.id.ed_new_due_date);

        // Điện
        TextView tvElectricityOld = findViewById(R.id.ed_electricity_old);
        TextView tvElectricityNew = findViewById(R.id.ed_electricity_new);
        TextView tvElectricityTotal = findViewById(R.id.tv_electricity_total);

        // Nước
        TextView tvWaterOld = findViewById(R.id.ed_water_old);
        TextView tvWaterNew = findViewById(R.id.ed_water_new);
        TextView tvWaterTotal = findViewById(R.id.tv_water_total);

        // Tiền phòng
        TextView tvRoomPrice = findViewById(R.id.tv_room_price);

        // Lãi suất
        TextView tvInterestRate = findViewById(R.id.ed_interest_rate);
        TextView tvLateFee = findViewById(R.id.tv_late_fee);

        // Tổng tiền
        TextView tvServiceTotal = findViewById(R.id.tv_service_total);
        TextView tvInterestTotal = findViewById(R.id.tv_interest_total);
        TextView tvTotal = findViewById(R.id.tv_total);

        // Ghi chú
        TextView tvNote = findViewById(R.id.ed_note);

        // Các nút dưới cùng
        Button btnRemind = findViewById(R.id.btn_remind);
        Button btnExtend = findViewById(R.id.btn_extend);
        Button btnEdit = findViewById(R.id.btn_edit);

        // Khởi tạo checkboxes và xử lý sự kiện
        initCheckboxes();
    }

    // Hàm khởi tạo và xử lý sự kiện checkboxes
    private void initCheckboxes() {
        // Khởi tạo các checkboxes
        cbElectricity = findViewById(R.id.cb_electricity);
        cbWater = findViewById(R.id.cb_water);
        cbRoom = findViewById(R.id.cb_room);
        cbInterest = findViewById(R.id.cb_interest);
        cbLateFee = findViewById(R.id.cb_late_fee);

        // Áp dụng custom drawable cho các checkbox
        applyCustomCheckboxStyle(cbElectricity);
        applyCustomCheckboxStyle(cbWater);
        applyCustomCheckboxStyle(cbRoom);
        applyCustomCheckboxStyle(cbInterest);
        applyCustomCheckboxStyle(cbLateFee);

        // Thiết lập lắng nghe sự kiện cho checkbox tiền điện
        if (cbElectricity != null) {
            cbElectricity.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Cập nhật UI dựa trên trạng thái checkbox
                updateServiceTotal();
            });
        }

        // Thiết lập lắng nghe sự kiện cho checkbox tiền nước
        if (cbWater != null) {
            cbWater.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Cập nhật UI dựa trên trạng thái checkbox
                updateServiceTotal();
            });
        }

        // Thiết lập lắng nghe sự kiện cho checkbox tiền phòng
        if (cbRoom != null) {
            cbRoom.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Cập nhật UI dựa trên trạng thái checkbox
                updateServiceTotal();
            });
        }

        // Thiết lập lắng nghe sự kiện cho checkbox lãi suất
        if (cbInterest != null) {
            cbInterest.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Cập nhật UI dựa trên trạng thái checkbox
                updateInterestTotal();
            });
        }

        // Thiết lập lắng nghe sự kiện cho checkbox tiền lãi
        if (cbLateFee != null) {
            cbLateFee.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Cập nhật UI dựa trên trạng thái checkbox
                updateInterestTotal();
            });
        }
    }

    // Hàm áp dụng style tùy chỉnh cho checkbox
    private void applyCustomCheckboxStyle(CheckBox checkBox) {
        if (checkBox != null) {
            // Không cần đặt lại buttonDrawable vì đã được xử lý qua style
            // Đảm bảo không có tint mặc định
            checkBox.setButtonTintList(null);
        }
    }

    // Cập nhật tổng tiền dịch vụ
    private void updateServiceTotal() {
        TextView tvServiceTotal = findViewById(R.id.tv_service_total);
        TextView tvTotal = findViewById(R.id.tv_total);

        if (tvServiceTotal == null || tvTotal == null) return;

        // Tính tổng tiền dịch vụ dựa trên checkbox được chọn
        int totalService = 0;

        // Tiền điện
        TextView tvElectricityTotal = findViewById(R.id.tv_electricity_total);
        if (tvElectricityTotal != null && cbElectricity != null && cbElectricity.isChecked()) {
            totalService += parseAmount(tvElectricityTotal.getText().toString());
        }

        // Tiền nước
        TextView tvWaterTotal = findViewById(R.id.tv_water_total);
        if (tvWaterTotal != null && cbWater != null && cbWater.isChecked()) {
            totalService += parseAmount(tvWaterTotal.getText().toString());
        }

        // Tiền phòng: 1.100.000 (giá cố định)
        if (cbRoom != null && cbRoom.isChecked()) {
            totalService += 1100000;
        }

        // Cập nhật UI
        tvServiceTotal.setText(formatCurrency(totalService) + " đồng");

        // Cập nhật tổng tiền
        updateTotalAmount();
    }

    // Cập nhật tổng tiền lãi
    private void updateInterestTotal() {
        TextView tvInterestTotal = findViewById(R.id.tv_interest_total);
        TextView tvTotal = findViewById(R.id.tv_total);

        if (tvInterestTotal == null || tvTotal == null) return;

        // Tính tổng tiền lãi
        int totalInterest = 0;

        // Tiền lãi: 427.750 nếu checkbox được chọn
        if (cbLateFee != null && cbLateFee.isChecked() && cbInterest != null && cbInterest.isChecked()) {
            totalInterest += 427750;
        }

        // Cập nhật UI
        tvInterestTotal.setText(formatCurrency(totalInterest) + " đồng");

        // Cập nhật tổng tiền
        updateTotalAmount();
    }

    // Cập nhật tổng tiền
    private void updateTotalAmount() {
        TextView tvServiceTotal = findViewById(R.id.tv_service_total);
        TextView tvInterestTotal = findViewById(R.id.tv_interest_total);
        TextView tvTotal = findViewById(R.id.tv_total);

        if (tvServiceTotal == null || tvInterestTotal == null || tvTotal == null) return;

        // Lấy giá trị tiền dịch vụ và tiền lãi
        int serviceAmount = parseAmount(tvServiceTotal.getText().toString());
        int interestAmount = parseAmount(tvInterestTotal.getText().toString());

        // Tính tổng tiền
        int totalAmount = serviceAmount + interestAmount;

        // Cập nhật UI
        tvTotal.setText(formatCurrency(totalAmount) + " đồng");
    }

    // Hàm hỗ trợ định dạng tiền tệ
    private String formatCurrency(int amount) {
        // Định dạng số tiền với dấu phẩy ngăn cách hàng nghìn
        return String.format("%,d", amount).replace(",", ".");
    }

    // Hàm hỗ trợ phân tích số tiền từ chuỗi
    private int parseAmount(String amountString) {
        try {
            // Loại bỏ định dạng tiền tệ và đơn vị
            String cleaned = amountString.replaceAll("[^0-9]", "");
            return Integer.parseInt(cleaned);
        } catch (Exception e) {
            return 0;
        }
    }

    // Thiết lập sự kiện cho màn hình xem chi tiết hóa đơn
    private void setupViewBillDetailListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Nút nhắc nhở
        Button btnRemind = findViewById(R.id.btn_remind);
        if (btnRemind != null) {
            btnRemind.setOnClickListener(v -> {
                Toast.makeText(this, "Đã gửi nhắc nhở thanh toán!", Toast.LENGTH_SHORT).show();
            });
        }

        // Nút gia hạn
        Button btnExtend = findViewById(R.id.btn_extend);
        if (btnExtend != null) {
            btnExtend.setOnClickListener(v -> {
                // Mở màn hình gia hạn
                String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
                String billMonth = getIntent().getStringExtra("BILL_MONTH");
                openExtendBillScreen(roomNumber, billMonth);
            });
        }

        // Nút sửa
        Button btnEdit = findViewById(R.id.btn_edit);
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                // Mở màn hình sửa hóa đơn
                String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
                String billMonth = getIntent().getStringExtra("BILL_MONTH");
                String billType = getIntent().getStringExtra("BILL_TYPE");

                // Log thông tin để debug
                android.util.Log.d("Admin_HoaDonActivity", "Bấm nút sửa - Room: " + roomNumber + ", Month: " + billMonth + ", Type: " + billType);

                // Thu thập dữ liệu từ màn hình hiện tại
                String electricityOld = "";
                String electricityNew = "";
                String waterOld = "";
                String waterNew = "";
                String interestRate = "";
                String note = "";

                // Lấy dữ liệu từ các TextView trong màn hình xem hóa đơn
                TextView tvElectricityOld = findViewById(R.id.ed_electricity_old);
                TextView tvElectricityNew = findViewById(R.id.ed_electricity_new);
                TextView tvWaterOld = findViewById(R.id.ed_water_old);
                TextView tvWaterNew = findViewById(R.id.ed_water_new);
                TextView tvInterestRate = findViewById(R.id.ed_interest_rate);
                TextView tvNote = findViewById(R.id.ed_note);

                if (tvElectricityOld != null) electricityOld = tvElectricityOld.getText().toString();
                if (tvElectricityNew != null) electricityNew = tvElectricityNew.getText().toString();
                if (tvWaterOld != null) waterOld = tvWaterOld.getText().toString();
                if (tvWaterNew != null) waterNew = tvWaterNew.getText().toString();
                if (tvInterestRate != null) interestRate = tvInterestRate.getText().toString();
                if (tvNote != null) note = tvNote.getText().toString();

                android.util.Log.d("Admin_HoaDonActivity", "Dữ liệu lấy được: ElecOld=" + electricityOld +
                        ", ElecNew=" + electricityNew +
                        ", WaterOld=" + waterOld +
                        ", WaterNew=" + waterNew);

                // Chuyển sang màn hình sửa với dữ liệu đầy đủ
                openEditBillScreen(roomNumber, billMonth, billType, electricityOld, electricityNew, waterOld, waterNew, interestRate, note);
            });
        }
    }

    // Tải dữ liệu cho màn hình xem chi tiết hóa đơn
    private void loadViewBillDetailData() {
        // Lấy thông tin từ intent
        String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
        String billMonth = getIntent().getStringExtra("BILL_MONTH");
        String billType = getIntent().getStringExtra("BILL_TYPE");

        // Trong thực tế, bạn sẽ truy vấn database để lấy chi tiết hóa đơn
        // Ví dụ: Bill bill = databaseHelper.getBillDetails(roomNumber, billMonth);

        // Hiện tại chỉ hiển thị dữ liệu mẫu
        TextView tvMonth = findViewById(R.id.ed_month);
        TextView tvOriginalDueDate = findViewById(R.id.ed_original_due_date);
        TextView tvNewDueDate = findViewById(R.id.ed_new_due_date);

        if (billMonth != null && tvMonth != null) {
            tvMonth.setText(billMonth);
        }

        // Thiết lập các giá trị khác
        TextView tvElectricityOld = findViewById(R.id.ed_electricity_old);
        TextView tvElectricityNew = findViewById(R.id.ed_electricity_new);
        TextView tvWaterOld = findViewById(R.id.ed_water_old);
        TextView tvWaterNew = findViewById(R.id.ed_water_new);
        TextView tvInterestRate = findViewById(R.id.ed_interest_rate);
        TextView tvNote = findViewById(R.id.ed_note);

        // Thiết lập tiêu đề và các nút
        TextView tvTitle = findViewById(R.id.tv_title);
        if (tvTitle != null) {
            tvTitle.setText("Hóa đơn");
        }

        // Cập nhật UI dựa vào loại hóa đơn
        if (billType != null) {
            switch (billType) {
                case "extension":
                    // Hiển thị thông tin hóa đơn gia hạn
                    if (tvNewDueDate != null) {
                        tvNewDueDate.setText("07/05/2025");
                    }
                    break;
                case "overdue":
                    // Hiển thị thông tin hóa đơn trễ hạn
                    break;
                case "paid":
                    // Hiển thị thông tin hóa đơn đã thanh toán
                    break;
            }
        }

        // Cập nhật hiển thị các nút
        Button btnRemind = findViewById(R.id.btn_remind);
        Button btnExtend = findViewById(R.id.btn_extend);
        Button btnEdit = findViewById(R.id.btn_edit);

        if (btnRemind != null) {
            btnRemind.setText("Nhắc nhở");
        }

        if (btnExtend != null) {
            btnExtend.setText("Gia hạn");
        }

        if (btnEdit != null) {
            btnEdit.setText("Sửa");
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

        // Add bill button
        addBillButton = findViewById(R.id.add_bill_button);

        // Thêm tham chiếu đến các header
        extensionHeader = findViewById(R.id.extension_header);
        overdueHeader = findViewById(R.id.overdue_header);
        paidHeader = findViewById(R.id.paid_header);

        // Thêm tham chiếu đến các TextView thông báo trống
        tvNoExtensionBills = findViewById(R.id.no_extension_bills);
        tvNoOverdueBills = findViewById(R.id.no_overdue_bills);
        tvNoPaidBills = findViewById(R.id.no_paid_bills);
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
                String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
                // Mở màn hình tạo hóa đơn mới
                openCreateBillScreen(this, roomNumber);
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
                // Mở màn hình chi tiết hóa đơn yêu cầu gia hạn
                String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
                openViewBillDetail(roomNumber, "02/2025", "extension");
            });
        }

        if (overdueBill1 != null) {
            overdueBill1.setOnClickListener(v -> {
                // Mở màn hình chi tiết hóa đơn trễ hạn tháng 2
                String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
                openViewBillDetail(roomNumber, "02/2025", "overdue");
            });
        }

        if (overdueBill2 != null) {
            overdueBill2.setOnClickListener(v -> {
                // Mở màn hình chi tiết hóa đơn trễ hạn tháng 1
                String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
                openViewBillDetail(roomNumber, "01/2025", "overdue");
            });
        }

        if (paidBill != null) {
            paidBill.setOnClickListener(v -> {
                // Mở màn hình chi tiết hóa đơn đã đóng
                String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
                openViewBillDetail(roomNumber, "12/2024", "paid");
            });
        }
    }

    // Mở màn hình xem chi tiết hóa đơn
    private void openViewBillDetail(String roomNumber, String billMonth, String billType) {
        Intent intent = new Intent(Admin_HoaDonActivity.this, Admin_HoaDonActivity.class);
        intent.putExtra("VIEW_BILL_DETAIL", true);
        intent.putExtra("ROOM_NUMBER", roomNumber);
        intent.putExtra("BILL_MONTH", billMonth);
        intent.putExtra("BILL_TYPE", billType);
        startActivity(intent);
    }

    // Tải dữ liệu nhà trọ và hiển thị lên RecyclerView
    private void loadMotelData() {
        // Tạo dữ liệu mẫu
        nhaTroList = new ArrayList<>();
        nhaTroList.add(new Motel("Nhà trọ Trường Phát 1", "số 41, đường N4, p. Mỹ Phước, tp. Bến Cát, Bình Dương"));
        nhaTroList.add(new Motel("Nhà trọ Trường Phát 2", "số 42, đường N4, p. Mỹ Phước, tp. Bến Cát, Bình Dương"));

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
        Intent intent = new Intent(Admin_HoaDonActivity.this, Admin_HoaDonActivity.class);
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

        if (roomNumber == null) return;

        // Hiển thị thông tin phòng
        tvRoomInfo.setText("Phòng " + roomNumber + " - " + tenantName);

        // Lấy bộ lọc hiện tại
        String currentFilter = "";
        if (tvFilter != null) {
            currentFilter = tvFilter.getText().toString();
        }

        // Ẩn tất cả các hóa đơn và thông báo
        hideAllBillsAndMessages();

        // Xử lý theo filter
        switch (currentFilter) {
            case "Yêu cầu gia hạn":
                handleExtensionFilter(roomNumber);
                break;
            case "Trễ hạn":
                handleOverdueFilter(roomNumber);
                break;
            case "Đã đóng":
                handlePaidFilter(roomNumber);
                break;
            default: // "Tất cả"
                handleAllFilter(roomNumber);
                break;
        }
    }

    // Xử lý filter "Yêu cầu gia hạn"
    private void handleExtensionFilter(String roomNumber) {
        // Luôn hiển thị header
        extensionHeader.setVisibility(View.VISIBLE);

        boolean hasExtensionBills = false;

        // Chỉ phòng 1 có yêu cầu gia hạn
        if (roomNumber.equals("1") && extensionBill != null) {
            extensionBill.setVisibility(View.VISIBLE);
            hasExtensionBills = true;
        }

        // Hiển thị thông báo nếu không có hóa đơn
        if (!hasExtensionBills && tvNoExtensionBills != null) {
            tvNoExtensionBills.setVisibility(View.VISIBLE);
        }
    }

    // Xử lý filter "Trễ hạn"
    private void handleOverdueFilter(String roomNumber) {
        // Luôn hiển thị header
        overdueHeader.setVisibility(View.VISIBLE);

        boolean hasOverdueBills = false;

        // Chỉ phòng 1 có hóa đơn trễ hạn
        if (roomNumber.equals("1")) {
            if (overdueBill1 != null) {
                overdueBill1.setVisibility(View.VISIBLE);
                hasOverdueBills = true;
            }
            if (overdueBill2 != null) {
                overdueBill2.setVisibility(View.VISIBLE);
                hasOverdueBills = true;
            }
        }

        // Hiển thị thông báo nếu không có hóa đơn
        if (!hasOverdueBills && tvNoOverdueBills != null) {
            tvNoOverdueBills.setVisibility(View.VISIBLE);
        }
    }

    // Xử lý filter "Đã đóng"
    private void handlePaidFilter(String roomNumber) {
        // Luôn hiển thị header
        paidHeader.setVisibility(View.VISIBLE);

        boolean hasPaidBills = false;

        // Tất cả các phòng đều có hóa đơn đã đóng
        if (paidBill != null) {
            paidBill.setVisibility(View.VISIBLE);
            hasPaidBills = true;
        }

        // Hiển thị thông báo nếu không có hóa đơn
        if (!hasPaidBills && tvNoPaidBills != null) {
            tvNoPaidBills.setVisibility(View.VISIBLE);
        }
    }

    // Xử lý filter "Tất cả"
    private void handleAllFilter(String roomNumber) {
        // Chỉ hiển thị các section có dữ liệu thực tế

        // Xử lý section "Yêu cầu gia hạn"
        if (roomNumber.equals("1") && extensionBill != null) {
            extensionHeader.setVisibility(View.VISIBLE);
            extensionBill.setVisibility(View.VISIBLE);
        }

        // Xử lý section "Trễ hạn"
        if (roomNumber.equals("1") && (overdueBill1 != null || overdueBill2 != null)) {
            overdueHeader.setVisibility(View.VISIBLE);
            if (overdueBill1 != null) overdueBill1.setVisibility(View.VISIBLE);
            if (overdueBill2 != null) overdueBill2.setVisibility(View.VISIBLE);
        }

        // Xử lý section "Đã đóng"
        if (paidBill != null) {
            paidHeader.setVisibility(View.VISIBLE);
            paidBill.setVisibility(View.VISIBLE);
        }

        // Khi filter là "Tất cả", không hiển thị thông báo "Không có..." cho các section
    }

    // Ẩn tất cả các hóa đơn, header và thông báo
    private void hideAllBillsAndMessages() {
        // Ẩn tất cả các hóa đơn
        if (extensionBill != null) extensionBill.setVisibility(View.GONE);
        if (overdueBill1 != null) overdueBill1.setVisibility(View.GONE);
        if (overdueBill2 != null) overdueBill2.setVisibility(View.GONE);
        if (paidBill != null) paidBill.setVisibility(View.GONE);

        // Ẩn tất cả các header
        if (extensionHeader != null) extensionHeader.setVisibility(View.GONE);
        if (overdueHeader != null) overdueHeader.setVisibility(View.GONE);
        if (paidHeader != null) paidHeader.setVisibility(View.GONE);

        // Ẩn tất cả các thông báo trống
        if (tvNoExtensionBills != null) tvNoExtensionBills.setVisibility(View.GONE);
        if (tvNoOverdueBills != null) tvNoOverdueBills.setVisibility(View.GONE);
        if (tvNoPaidBills != null) tvNoPaidBills.setVisibility(View.GONE);
    }

    // Mở màn hình chi tiết hóa đơn phòng
    private void openRoomBillDetail(String roomNumber, String tenantName) {
        Intent intent = new Intent(Admin_HoaDonActivity.this, Admin_HoaDonActivity.class);
        intent.putExtra("ROOM_NUMBER", roomNumber);
        intent.putExtra("TENANT_NAME", tenantName);
        startActivity(intent);
    }

    // Khởi tạo giao diện cho màn hình gia hạn hóa đơn
    private void initExtendBillViews() {
        // Header
        btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvRoomNumber = findViewById(R.id.tv_room_number);

        // Lấy thông tin phòng từ intent
        String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
        String billMonth = getIntent().getStringExtra("BILL_MONTH");

        if (roomNumber != null) {
            tvRoomNumber.setText("Phòng " + roomNumber);
        }

        // Khởi tạo các trường dữ liệu
        EditText edMonth = findViewById(R.id.ed_month);
        EditText edOriginalDueDate = findViewById(R.id.ed_original_due_date);
        EditText edNewDueDate = findViewById(R.id.ed_new_due_date);

        // Các checkbox
        cbElectricity = findViewById(R.id.cb_electricity);
        cbWater = findViewById(R.id.cb_water);
        cbRoom = findViewById(R.id.cb_room);
        cbInterest = findViewById(R.id.cb_interest);
        cbLateFee = findViewById(R.id.cb_late_fee);

        // Áp dụng style cho checkbox
        applyCustomCheckboxStyle(cbElectricity);
        applyCustomCheckboxStyle(cbWater);
        applyCustomCheckboxStyle(cbRoom);
        applyCustomCheckboxStyle(cbInterest);
        applyCustomCheckboxStyle(cbLateFee);

        // Thiết lập trường hạn thanh toán mới mặc định trống
        if (edNewDueDate != null) {
            // Để trống và hint thay vì setText
            edNewDueDate.setText("");
            edNewDueDate.setHint("DD/MM/YYYY");

            // Thiết lập sự kiện click vào calendar icon
            ImageView iconCalendar = findViewById(R.id.ic_calendar);
            if (iconCalendar != null) {
                iconCalendar.setOnClickListener(v -> {
                    showDatePickerDialog(edNewDueDate);
                });
            }

            // Thiết lập focus change cho edNewDueDate để hiển thị date picker khi focus
            edNewDueDate.setOnClickListener(v -> {
                showDatePickerDialog(edNewDueDate);
            });
        }
    }

    // Thiết lập sự kiện cho màn hình gia hạn hóa đơn
    private void setupExtendBillListeners() {
        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Nút Hủy
        Button btnCancel = findViewById(R.id.btn_cancel);
        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> finish());
        }

        // Nút Lưu
        Button btnSave = findViewById(R.id.btn_save);
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                // Lấy các trường dữ liệu
                EditText edNewDueDate = findViewById(R.id.ed_new_due_date);
                EditText edOriginalDueDate = findViewById(R.id.ed_original_due_date);

                // Kiểm tra nếu người dùng không nhập ngày hạn mới
                if (edNewDueDate != null && edOriginalDueDate != null) {
                    String newDueDateStr = edNewDueDate.getText().toString().trim();
                    String originalDueDateStr = edOriginalDueDate.getText().toString().trim();

                    if (newDueDateStr.isEmpty()) {
                        // Nếu không chọn ngày hạn mới, gán bằng ngày hạn gốc
                        edNewDueDate.setText(originalDueDateStr);

                        // Cập nhật lại số ngày và tiền lãi
                        calculateExtensionDays();

                        // Thông báo cho người dùng
                        Toast.makeText(this, "Hạn thanh toán mới được đặt bằng hạn thanh toán gốc", Toast.LENGTH_SHORT).show();
                    }

                    // Lưu thông tin gia hạn
                    Toast.makeText(this, "Đã lưu thông tin gia hạn!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Thêm sự kiện cho EditText lãi suất khi thay đổi giá trị
        EditText edInterestRate = findViewById(R.id.ed_interest_rate);
        if (edInterestRate != null) {
            edInterestRate.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Không cần xử lý
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Không cần xử lý
                }

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    // Tính lại số ngày và tiền lãi sau khi lãi suất thay đổi
                    calculateExtensionDays();
                }
            });
        }
    }

    // Tải dữ liệu cho màn hình gia hạn hóa đơn
    private void loadExtendBillData() {
        // Lấy thông tin từ intent
        String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
        String billMonth = getIntent().getStringExtra("BILL_MONTH");

        // Hiển thị dữ liệu từ hóa đơn cần gia hạn
        EditText edMonth = findViewById(R.id.ed_month);
        EditText edOriginalDueDate = findViewById(R.id.ed_original_due_date);

        if (billMonth != null && edMonth != null) {
            edMonth.setText(billMonth);
        }

        if (edOriginalDueDate != null) {
            edOriginalDueDate.setText("07/02/2025"); // Ngày hạn gốc mẫu
        }

        // Không cần tính và cập nhật số ngày gia hạn vì chưa có ngày hạn mới
        // calculateAndUpdateExtendDays();

        // Cập nhật các giá trị khác
        TextView tvServiceTotal = findViewById(R.id.tv_service_total);
        if (tvServiceTotal != null) {
            tvServiceTotal.setText("1.450.000 đồng");
        }

        // Reset giá trị liên quan đến ngày
        TextView tvDays = findViewById(R.id.tv_days);
        TextView tvLateFee = findViewById(R.id.tv_late_fee);
        TextView tvInterestTotal = findViewById(R.id.tv_interest_total);
        TextView tvTotal = findViewById(R.id.tv_total);

        if (tvDays != null) tvDays.setText("0 ngày");
        if (tvLateFee != null) tvLateFee.setText("0 đồng");
        if (tvInterestTotal != null) tvInterestTotal.setText("0 đồng");

        // Tổng tiền ban đầu chỉ bằng tiền dịch vụ, chưa có lãi
        if (tvTotal != null && tvServiceTotal != null) {
            tvTotal.setText(tvServiceTotal.getText());
        }
    }

    // Hiển thị DatePickerDialog cho trường ngày tháng
    private void showDatePickerDialog(EditText dateField) {
        Calendar calendar = Calendar.getInstance();

        // Nếu trường đã có giá trị ngày, sử dụng nó làm giá trị mặc định
        if (dateField.getText() != null && !dateField.getText().toString().isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = dateFormat.parse(dateField.getText().toString());
                if (date != null) {
                    calendar.setTime(date);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Tạo Calendar cho ngày được chọn
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);

                    // Kiểm tra nếu là ngày hạn thanh toán mới và đang ở màn hình gia hạn
                    if (dateField.getId() == R.id.ed_new_due_date && getIntent().hasExtra("EXTEND_BILL")) {
                        // Lấy ngày hạn thanh toán gốc
                        EditText edOriginalDueDate = findViewById(R.id.ed_original_due_date);
                        if (edOriginalDueDate != null && !edOriginalDueDate.getText().toString().isEmpty()) {
                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                Date originalDueDate = dateFormat.parse(edOriginalDueDate.getText().toString());

                                // So sánh với ngày được chọn
                                if (originalDueDate != null) {
                                    // Kiểm tra xem ngày mới có trước ngày gốc không
                                    if (selectedDate.getTime().before(originalDueDate)) {
                                        // Hiển thị thông báo lỗi nếu ngày mới trước ngày gốc
                                        Toast.makeText(Admin_HoaDonActivity.this,
                                                "Ngày hạn thanh toán mới phải sau ngày hạn thanh toán gốc",
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    // Tính số ngày gia hạn
                                    long diffInMillis = selectedDate.getTime().getTime() - originalDueDate.getTime();
                                    int diffInDays = (int) (diffInMillis / (24 * 60 * 60 * 1000));

                                    // Kiểm tra xem có vượt quá 30 ngày không
                                    if (diffInDays > 30) {
                                        Toast.makeText(Admin_HoaDonActivity.this,
                                                "Chỉ được gia hạn tối đa 30 ngày",
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // Cập nhật ngày đã chọn vào EditText
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    dateField.setText(dateFormat.format(selectedDate.getTime()));

                    // Nếu đang ở màn hình gia hạn, tính số ngày gia hạn và tiền lãi
                    if (getIntent().hasExtra("EXTEND_BILL")) {
                        calculateExtensionDays();
                    }
                },
                year, month, day
        );

        // Thêm giới hạn ngày cho trường hạn thanh toán mới
        if (dateField.getId() == R.id.ed_new_due_date && getIntent().hasExtra("EXTEND_BILL")) {
            // Lấy ngày hạn thanh toán gốc làm ngày tối thiểu
            EditText edOriginalDueDate = findViewById(R.id.ed_original_due_date);
            if (edOriginalDueDate != null && !edOriginalDueDate.getText().toString().isEmpty()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date originalDueDate = dateFormat.parse(edOriginalDueDate.getText().toString());
                    if (originalDueDate != null) {
                        // Thiết lập ngày tối thiểu là ngày hạn thanh toán gốc
                        datePickerDialog.getDatePicker().setMinDate(originalDueDate.getTime());

                        // Thiết lập ngày tối đa là 30 ngày sau ngày hạn thanh toán gốc
                        Calendar maxDate = Calendar.getInstance();
                        maxDate.setTime(originalDueDate);
                        maxDate.add(Calendar.DAY_OF_MONTH, 30);
                        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        datePickerDialog.show();
    }

    // Tính số ngày gia hạn và tiền lãi
    private void calculateExtensionDays() {
        // Lấy các trường dữ liệu cần thiết
        EditText edOriginalDueDate = findViewById(R.id.ed_original_due_date);
        EditText edNewDueDate = findViewById(R.id.ed_new_due_date);
        TextView tvDays = findViewById(R.id.tv_days);
        TextView tvLateFee = findViewById(R.id.tv_late_fee);
        TextView tvInterestTotal = findViewById(R.id.tv_interest_total);
        TextView tvTotal = findViewById(R.id.tv_total);
        EditText edInterestRate = findViewById(R.id.ed_interest_rate);

        // Kiểm tra các trường có tồn tại không
        if (edOriginalDueDate == null || edNewDueDate == null || tvDays == null ||
                tvLateFee == null || tvInterestTotal == null || tvTotal == null || edInterestRate == null) {
            return;
        }

        // Lấy giá trị ngày và kiểm tra
        String originalDueDateStr = edOriginalDueDate.getText().toString();
        String newDueDateStr = edNewDueDate.getText().toString();

        if (originalDueDateStr.isEmpty() || newDueDateStr.isEmpty()) {
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date originalDueDate = dateFormat.parse(originalDueDateStr);
            Date newDueDate = dateFormat.parse(newDueDateStr);

            if (originalDueDate == null || newDueDate == null) {
                return;
            }

            // Tính số ngày gia hạn
            long diffInMillis = newDueDate.getTime() - originalDueDate.getTime();
            int diffInDays = (int) (diffInMillis / (24 * 60 * 60 * 1000));

            // Kiểm tra nếu số ngày vượt quá 30
            if (diffInDays > 30) {
                Toast.makeText(this, "Chỉ được gia hạn tối đa 30 ngày", Toast.LENGTH_SHORT).show();
                // Reset lại ngày mới
                Calendar maxDate = Calendar.getInstance();
                maxDate.setTime(originalDueDate);
                maxDate.add(Calendar.DAY_OF_MONTH, 30);
                edNewDueDate.setText(dateFormat.format(maxDate.getTime()));

                // Cập nhật lại số ngày
                diffInDays = 30;
            }

            // Hiển thị số ngày
            tvDays.setText(diffInDays + " ngày");

            // Lấy tỷ lệ lãi suất
            double interestRate = 0.5; // Mặc định 0.5%/ngày
            try {
                String interestRateStr = edInterestRate.getText().toString();
                if (!interestRateStr.isEmpty()) {
                    interestRate = Double.parseDouble(interestRateStr);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            // Lấy tiền dịch vụ
            TextView tvServiceTotal = findViewById(R.id.tv_service_total);
            int serviceAmount = parseAmount(tvServiceTotal.getText().toString());

            // Tính tiền lãi: Số tiền * Lãi suất * Số ngày
            double lateFeeAmount = serviceAmount * (interestRate / 100) * diffInDays;

            // Hiển thị tiền lãi
            tvLateFee.setText(formatCurrency((int)lateFeeAmount) + " đồng");
            tvInterestTotal.setText(formatCurrency((int)lateFeeAmount) + " đồng");

            // Cập nhật tổng tiền
            int totalAmount = serviceAmount + (int)lateFeeAmount;
            tvTotal.setText(formatCurrency(totalAmount) + " đồng");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mở màn hình gia hạn hóa đơn
    private void openExtendBillScreen(String roomNumber, String billMonth) {
        Intent intent = new Intent(Admin_HoaDonActivity.this, Admin_HoaDonActivity.class);
        intent.putExtra("EXTEND_BILL", true);
        intent.putExtra("ROOM_NUMBER", roomNumber);
        intent.putExtra("BILL_MONTH", billMonth);
        startActivity(intent);
    }

    // Mở màn hình sửa hóa đơn với dữ liệu đầy đủ
    private void openEditBillScreen(String roomNumber, String billMonth, String billType,
                                    String electricityOld, String electricityNew,
                                    String waterOld, String waterNew,
                                    String interestRate, String note) {
        Intent intent = new Intent(Admin_HoaDonActivity.this, Admin_HoaDonActivity.class);
        intent.putExtra("EDIT_BILL", true);
        intent.putExtra("ROOM_NUMBER", roomNumber);
        intent.putExtra("BILL_MONTH", billMonth);
        intent.putExtra("BILL_TYPE", billType);

        // Truyền thêm dữ liệu chi tiết
        intent.putExtra("ELECTRICITY_OLD", electricityOld);
        intent.putExtra("ELECTRICITY_NEW", electricityNew);
        intent.putExtra("WATER_OLD", waterOld);
        intent.putExtra("WATER_NEW", waterNew);
        intent.putExtra("INTEREST_RATE", interestRate);
        intent.putExtra("NOTE", note);

        android.util.Log.d("Admin_HoaDonActivity", "Chuyển sang màn hình sửa hóa đơn với dữ liệu đầy đủ");
        startActivity(intent);
    }

    // Phương thức cũ để tương thích ngược
    private void openEditBillScreen(String roomNumber, String billMonth, String billType) {
        openEditBillScreen(roomNumber, billMonth, billType, "", "", "", "", "", "");
    }

    // Tải dữ liệu cho màn hình sửa hóa đơn
    private void loadEditBillData() {
        // Lấy thông tin từ intent
        String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
        String billMonth = getIntent().getStringExtra("BILL_MONTH");
        String billType = getIntent().getStringExtra("BILL_TYPE");

        // Lấy dữ liệu bổ sung nếu có
        String electricityOld = getIntent().getStringExtra("ELECTRICITY_OLD");
        String electricityNew = getIntent().getStringExtra("ELECTRICITY_NEW");
        String waterOld = getIntent().getStringExtra("WATER_OLD");
        String waterNew = getIntent().getStringExtra("WATER_NEW");
        String interestRateStr = getIntent().getStringExtra("INTEREST_RATE");
        String noteText = getIntent().getStringExtra("NOTE");

        android.util.Log.d("Admin_HoaDonActivity", "Tải dữ liệu cho màn hình sửa - Room: " + roomNumber +
                ", Month: " + billMonth +
                ", ElecOld: " + electricityOld +
                ", ElecNew: " + electricityNew +
                ", WaterOld: " + waterOld +
                ", WaterNew: " + waterNew);

        // Trong thực tế, bạn sẽ truy vấn database để lấy chi tiết hóa đơn
        // Ví dụ: Bill bill = databaseHelper.getBillDetails(roomNumber, billMonth);

        // Hiện tại, chỉ hiển thị dữ liệu mẫu từ layout hoặc dữ liệu được truyền từ màn hình trước
        EditText edMonth = findViewById(R.id.ed_month);
        EditText edOriginalDueDate = findViewById(R.id.ed_original_due_date);
        EditText edNewDueDate = findViewById(R.id.ed_new_due_date);
        EditText edElectricityOld = findViewById(R.id.ed_electricity_old);
        EditText edElectricityNew = findViewById(R.id.ed_electricity_new);
        EditText edWaterOld = findViewById(R.id.ed_water_old);
        EditText edWaterNew = findViewById(R.id.ed_water_new);
        EditText edInterestRate = findViewById(R.id.ed_interest_rate);
        EditText edNote = findViewById(R.id.ed_note);

        // Đặt dữ liệu cơ bản
        if (billMonth != null && edMonth != null) {
            edMonth.setText(billMonth);
        }

        // Thiết lập giá trị mặc định cho các ngày nếu cần
        if (edOriginalDueDate != null) {
            edOriginalDueDate.setText("07/02/2025"); // Giữ ngày hạn gốc mẫu
        }

        if (edNewDueDate != null) {
            edNewDueDate.setText("07/04/2025"); // Giữ ngày hạn mới mẫu
        }

        // Sử dụng dữ liệu truyền từ màn hình trước nếu có
        if (electricityOld != null && !electricityOld.isEmpty() && edElectricityOld != null) {
            edElectricityOld.setText(electricityOld);
        }

        if (electricityNew != null && !electricityNew.isEmpty() && edElectricityNew != null) {
            edElectricityNew.setText(electricityNew);
        }

        if (waterOld != null && !waterOld.isEmpty() && edWaterOld != null) {
            edWaterOld.setText(waterOld);
        }

        if (waterNew != null && !waterNew.isEmpty() && edWaterNew != null) {
            edWaterNew.setText(waterNew);
        }

        if (interestRateStr != null && !interestRateStr.isEmpty() && edInterestRate != null) {
            edInterestRate.setText(interestRateStr);
        }

        if (noteText != null && !noteText.isEmpty() && edNote != null) {
            edNote.setText(noteText);
        }

        // Tính toán và cập nhật giá trị ban đầu
        updateElectricityTotal();
        updateWaterTotal();
        updateServiceTotal();
        updateInterestTotal();
        updateTotalAmount();
    }

    // Khởi tạo giao diện cho màn hình sửa hóa đơn
    private void initEditBillViews() {
        // Header
        btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvRoomNumber = findViewById(R.id.tv_room_number);

        // Cập nhật tiêu đề
        if (tvTitle != null) {
            tvTitle.setText("Sửa hóa đơn");
        }

        // Lấy thông tin phòng và tháng từ intent
        String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
        String billMonth = getIntent().getStringExtra("BILL_MONTH");

        if (roomNumber != null && tvRoomNumber != null) {
            tvRoomNumber.setText("Phòng " + roomNumber);
        }

        // Khởi tạo các trường dữ liệu
        EditText edMonth = findViewById(R.id.ed_month);
        EditText edOriginalDueDate = findViewById(R.id.ed_original_due_date);
        EditText edNewDueDate = findViewById(R.id.ed_new_due_date);

        // Khởi tạo các trường số điện
        EditText edElectricityOld = findViewById(R.id.ed_electricity_old);
        EditText edElectricityNew = findViewById(R.id.ed_electricity_new);

        // Khởi tạo các trường số nước
        EditText edWaterOld = findViewById(R.id.ed_water_old);
        EditText edWaterNew = findViewById(R.id.ed_water_new);

        // Lãi suất
        EditText edInterestRate = findViewById(R.id.ed_interest_rate);

        // Ghi chú
        EditText edNote = findViewById(R.id.ed_note);

        // Đảm bảo tất cả các trường được kích hoạt để chỉnh sửa
        if (edElectricityOld != null) edElectricityOld.setEnabled(true);
        if (edElectricityNew != null) edElectricityNew.setEnabled(true);
        if (edWaterOld != null) edWaterOld.setEnabled(true);
        if (edWaterNew != null) edWaterNew.setEnabled(true);
        if (edInterestRate != null) edInterestRate.setEnabled(true);
        if (edNote != null) edNote.setEnabled(true);

        // Khởi tạo checkboxes và xử lý sự kiện
        initCheckboxes();

        // Thêm TextWatcher để cập nhật thành tiền khi giá trị thay đổi
        setupTextWatchers();
    }

    // Thiết lập sự kiện cho màn hình sửa hóa đơn
    private void setupEditBillListeners() {
        // Nút quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Nút Hủy
        Button btnCancel = findViewById(R.id.btn_remind);
        if (btnCancel != null) {
            btnCancel.setText("Hủy");
            btnCancel.setOnClickListener(v -> finish());
        }

        // Nút Lưu
        Button btnSave = findViewById(R.id.btn_edit);
        if (btnSave != null) {
            btnSave.setText("Lưu");
            btnSave.setOnClickListener(v -> {
                // Lưu thông tin đã chỉnh sửa
                saveEditedBillData();
            });
        }

        // Ẩn nút Gia hạn
        Button btnExtend = findViewById(R.id.btn_extend);
        if (btnExtend != null) {
            btnExtend.setVisibility(View.GONE);
        }
    }

    // Thiết lập TextWatcher cho các trường dữ liệu
    private void setupTextWatchers() {
        // Thiết lập TextWatcher cho số điện cũ và mới
        EditText edElectricityOld = findViewById(R.id.ed_electricity_old);
        EditText edElectricityNew = findViewById(R.id.ed_electricity_new);

        if (edElectricityOld != null) {
            edElectricityOld.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    updateElectricityTotal();
                }
            });
        }

        if (edElectricityNew != null) {
            edElectricityNew.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    updateElectricityTotal();
                }
            });
        }

        // Thiết lập TextWatcher cho số nước cũ và mới
        EditText edWaterOld = findViewById(R.id.ed_water_old);
        EditText edWaterNew = findViewById(R.id.ed_water_new);

        if (edWaterOld != null) {
            edWaterOld.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    updateWaterTotal();
                }
            });
        }

        if (edWaterNew != null) {
            edWaterNew.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    updateWaterTotal();
                }
            });
        }

        // Thiết lập TextWatcher cho lãi suất
        EditText edInterestRate = findViewById(R.id.ed_interest_rate);

        if (edInterestRate != null) {
            edInterestRate.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    updateInterestTotal();
                }
            });
        }
    }

    // Cập nhật tổng tiền điện
    private void updateElectricityTotal() {
        EditText edElectricityOld = findViewById(R.id.ed_electricity_old);
        EditText edElectricityNew = findViewById(R.id.ed_electricity_new);
        TextView tvElectricityTotal = findViewById(R.id.tv_electricity_total);

        if (edElectricityOld == null || edElectricityNew == null || tvElectricityTotal == null) return;

        try {
            // Lấy số cũ và số mới
            int oldValue = 0;
            int newValue = 0;

            if (!edElectricityOld.getText().toString().isEmpty()) {
                oldValue = Integer.parseInt(edElectricityOld.getText().toString());
            }

            if (!edElectricityNew.getText().toString().isEmpty()) {
                newValue = Integer.parseInt(edElectricityNew.getText().toString());
            }

            // Tính tiền điện: (số mới - số cũ) * đơn giá
            int usage = newValue - oldValue;
            if (usage < 0) usage = 0; // Đảm bảo không âm

            int unitPrice = 3500; // 3.500 đ/kwh
            int total = usage * unitPrice;

            // Cập nhật UI
            tvElectricityTotal.setText(formatCurrency(total) + " đ");

            // Cập nhật tổng tiền dịch vụ
            updateServiceTotal();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật tổng tiền nước
    private void updateWaterTotal() {
        EditText edWaterOld = findViewById(R.id.ed_water_old);
        EditText edWaterNew = findViewById(R.id.ed_water_new);
        TextView tvWaterTotal = findViewById(R.id.tv_water_total);

        if (edWaterOld == null || edWaterNew == null || tvWaterTotal == null) return;

        try {
            // Lấy số cũ và số mới
            int oldValue = 0;
            int newValue = 0;

            if (!edWaterOld.getText().toString().isEmpty()) {
                oldValue = Integer.parseInt(edWaterOld.getText().toString());
            }

            if (!edWaterNew.getText().toString().isEmpty()) {
                newValue = Integer.parseInt(edWaterNew.getText().toString());
            }

            // Tính tiền nước: (số mới - số cũ) * đơn giá
            int usage = newValue - oldValue;
            if (usage < 0) usage = 0; // Đảm bảo không âm

            int unitPrice = 7000; // 7.000 đ/m³
            int total = usage * unitPrice;

            // Cập nhật UI
            tvWaterTotal.setText(formatCurrency(total) + " đ");

            // Cập nhật tổng tiền dịch vụ
            updateServiceTotal();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Lưu dữ liệu đã chỉnh sửa
    private void saveEditedBillData() {
        // Trong thực tế, bạn sẽ lưu dữ liệu vào database
        // Ví dụ: databaseHelper.updateBill(bill);

        // Hiện tại chỉ hiển thị thông báo thành công
        Toast.makeText(this, "Đã lưu thông tin hóa đơn!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Khởi tạo giao diện cho màn hình tạo mới hóa đơn
    private void initCreateBillViews() {
        // Header
        btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvRoomNumber = findViewById(R.id.tv_room_number);

        // Lấy thông tin phòng từ intent
        String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");

        if (roomNumber != null && tvRoomNumber != null) {
            tvRoomNumber.setText("Phòng " + roomNumber);
        }

        // Khởi tạo các trường dữ liệu
        EditText edMonth = findViewById(R.id.ed_month);
        EditText edDueDate = findViewById(R.id.ed_due_date);

        // Khởi tạo icon lịch để chọn ngày
        ImageView icCalendarMonth = findViewById(R.id.ic_calendar_month);
        ImageView icCalendarDue = findViewById(R.id.ic_calendar_due);

        // Khởi tạo các trường số điện
        EditText edElectricityOld = findViewById(R.id.ed_electricity_old);
        EditText edElectricityNew = findViewById(R.id.ed_electricity_new);

        // Khởi tạo các trường số nước
        EditText edWaterOld = findViewById(R.id.ed_water_old);
        EditText edWaterNew = findViewById(R.id.ed_water_new);

        // Ghi chú
        EditText edNote = findViewById(R.id.ed_note);

        // Khởi tạo checkboxes và xử lý sự kiện
        initCheckboxes();
    }

    // Thiết lập sự kiện cho màn hình tạo mới hóa đơn
    private void setupCreateBillListeners() {
        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Sự kiện chọn tháng
        EditText edMonth = findViewById(R.id.ed_month);
        ImageView icCalendarMonth = findViewById(R.id.ic_calendar_month);

        if (edMonth != null && icCalendarMonth != null) {
            View.OnClickListener monthClickListener = v -> {
                showMonthPickerDialog(edMonth);
            };

            edMonth.setOnClickListener(monthClickListener);
            icCalendarMonth.setOnClickListener(monthClickListener);
        }

        // Sự kiện chọn hạn thanh toán
        EditText edDueDate = findViewById(R.id.ed_due_date);
        ImageView icCalendarDue = findViewById(R.id.ic_calendar_due);

        if (edDueDate != null && icCalendarDue != null) {
            View.OnClickListener dueDateClickListener = v -> {
                showDatePickerDialog(edDueDate);
            };

            edDueDate.setOnClickListener(dueDateClickListener);
            icCalendarDue.setOnClickListener(dueDateClickListener);
        }

        // Thiết lập TextWatcher cho các trường số
        setupCreateBillTextWatchers();

        // Nút Tạo
        Button btnCreate = findViewById(R.id.btn_create);
        if (btnCreate != null) {
            btnCreate.setOnClickListener(v -> {
                // Kiểm tra dữ liệu đầu vào
                if (validateCreateBillData()) {
                    // Lưu thông tin hóa đơn mới
                    saveNewBillData();
                }
            });
        }
    }

    // Thiết lập TextWatcher cho các trường số trong màn hình tạo mới hóa đơn
    private void setupCreateBillTextWatchers() {
        // TextWatcher cho số điện
        EditText edElectricityOld = findViewById(R.id.ed_electricity_old);
        EditText edElectricityNew = findViewById(R.id.ed_electricity_new);

        if (edElectricityOld != null) {
            edElectricityOld.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    updateElectricityTotal();
                }
            });
        }

        if (edElectricityNew != null) {
            edElectricityNew.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    updateElectricityTotal();
                }
            });
        }

        // TextWatcher cho số nước
        EditText edWaterOld = findViewById(R.id.ed_water_old);
        EditText edWaterNew = findViewById(R.id.ed_water_new);

        if (edWaterOld != null) {
            edWaterOld.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    updateWaterTotal();
                }
            });
        }

        if (edWaterNew != null) {
            edWaterNew.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    updateWaterTotal();
                }
            });
        }

        // TextWatcher cho checkbox
        CheckBox cbElectricity = findViewById(R.id.cb_electricity);
        CheckBox cbWater = findViewById(R.id.cb_water);
        CheckBox cbRoom = findViewById(R.id.cb_room);

        if (cbElectricity != null) {
            cbElectricity.setOnCheckedChangeListener((buttonView, isChecked) -> {
                updateServiceTotal();
            });
        }

        if (cbWater != null) {
            cbWater.setOnCheckedChangeListener((buttonView, isChecked) -> {
                updateServiceTotal();
            });
        }

        if (cbRoom != null) {
            cbRoom.setOnCheckedChangeListener((buttonView, isChecked) -> {
                updateServiceTotal();
            });
        }
    }

    // Tải dữ liệu mặc định cho màn hình tạo mới hóa đơn
    private void loadCreateBillData() {
        // Lấy thông tin phòng từ intent
        String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");

        // Thiết lập giá trị mặc định cho tháng: tháng hiện tại
        EditText edMonth = findViewById(R.id.ed_month);
        if (edMonth != null) {
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
            edMonth.setText(monthFormat.format(new Date()));
        }

        // Thiết lập giá trị mặc định cho hạn thanh toán: ngày 10 của tháng hiện tại
        EditText edDueDate = findViewById(R.id.ed_due_date);
        if (edDueDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 10);  // Ngày 10 hàng tháng

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            edDueDate.setText(dateFormat.format(calendar.getTime()));
        }

        // Mô phỏng việc lấy số cũ từ tháng trước
        // Trong thực tế, bạn nên lấy dữ liệu từ hóa đơn trước đó để thiết lập giá trị mặc định
        // cho số cũ của điện/nước
        EditText edElectricityOld = findViewById(R.id.ed_electricity_old);
        EditText edWaterOld = findViewById(R.id.ed_water_old);

        // Giả lập lấy số cũ từ tháng trước - trong thực tế sẽ lấy từ database
        if (edElectricityOld != null) {
            // Giả lập dữ liệu
            if (roomNumber != null) {
                switch (roomNumber) {
                    case "1":
                        edElectricityOld.setText("120"); // Giả sử phòng 1 tháng trước có số điện là 120
                        break;
                    case "2":
                        edElectricityOld.setText("95"); // Giả sử phòng 2 tháng trước có số điện là 95
                        break;
                    case "3":
                        edElectricityOld.setText("150"); // Giả sử phòng 3 tháng trước có số điện là 150
                        break;
                    default:
                        edElectricityOld.setText("0");
                        break;
                }
            } else {
                edElectricityOld.setText("0");
            }
        }

        if (edWaterOld != null) {
            // Giả lập dữ liệu
            if (roomNumber != null) {
                switch (roomNumber) {
                    case "1":
                        edWaterOld.setText("8"); // Giả sử phòng 1 tháng trước có số nước là 8
                        break;
                    case "2":
                        edWaterOld.setText("5"); // Giả sử phòng 2 tháng trước có số nước là 5
                        break;
                    case "3":
                        edWaterOld.setText("12"); // Giả sử phòng 3 tháng trước có số nước là 12
                        break;
                    default:
                        edWaterOld.setText("0");
                        break;
                }
            } else {
                edWaterOld.setText("0");
            }
        }

        // Cập nhật UI
        updateElectricityTotal();
        updateWaterTotal();
        updateServiceTotal();
        updateTotalAmount();
    }

    // Hiển thị dialog chọn tháng
    private void showMonthPickerDialog(EditText monthField) {
        Calendar calendar = Calendar.getInstance();

        // Nếu đã có giá trị, phân tích giá trị hiện tại
        if (monthField.getText() != null && !monthField.getText().toString().isEmpty()) {
            try {
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
                Date date = monthFormat.parse(monthField.getText().toString());
                if (date != null) {
                    calendar.setTime(date);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        // Tạo DatePickerDialog chỉ hiển thị tháng/năm
        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                this,
                android.app.DatePickerDialog.THEME_HOLO_LIGHT,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Cập nhật giá trị vào EditText
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, 1);

                    SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
                    monthField.setText(monthFormat.format(selectedDate.getTime()));

                    // Cập nhật hạn thanh toán tự động (ngày 10 của tháng được chọn)
                    Calendar dueDate = Calendar.getInstance();
                    dueDate.set(selectedYear, selectedMonth, 10);

                    // Kiểm tra nếu ngày 10 của tháng đã qua, thì đặt hạn là ngày 10 tháng sau
                    Calendar today = Calendar.getInstance();
                    if (dueDate.before(today)) {
                        dueDate.add(Calendar.MONTH, 1);
                        dueDate.set(Calendar.DAY_OF_MONTH, 10);
                    }

                    EditText edDueDate = findViewById(R.id.ed_due_date);
                    if (edDueDate != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        edDueDate.setText(dateFormat.format(dueDate.getTime()));
                    }

                    // Tự động cập nhật số điện và số nước cũ dựa trên tháng được chọn
                    updateOldValuesForSelectedMonth(selectedYear, selectedMonth);
                },
                year, month, 1
        );

        // Ẩn phần chọn ngày, chỉ hiển thị tháng/năm
        try {
            datePickerDialog.getDatePicker().findViewById(
                    Resources.getSystem().getIdentifier("day", "id", "android")
            ).setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        datePickerDialog.show();
    }

    // Phương thức mới để cập nhật số cũ dựa trên tháng được chọn
    private void updateOldValuesForSelectedMonth(int year, int month) {
        // Lấy thông tin phòng từ intent
        String roomNumber = getIntent().getStringExtra("ROOM_NUMBER");
        if (roomNumber == null) return;
        
        // Trong thực tế, bạn sẽ truy vấn dữ liệu từ database dựa vào roomNumber, year và month
        // Ví dụ: Bill lastBill = databaseHelper.getBillByRoomAndMonth(roomNumber, year, month - 1);
        
        // Hiện tại sử dụng giả lập đơn giản
        EditText edElectricityOld = findViewById(R.id.ed_electricity_old);
        EditText edWaterOld = findViewById(R.id.ed_water_old);
        
        // Ví dụ: tạo dữ liệu giả lập dựa vào tháng được chọn (chỉ cho mục đích demo)
        if (edElectricityOld != null) {
            // Tạo dữ liệu giả lập - chỉ dùng cho demo
            int basedElecValue = 0;
            switch (roomNumber) {
                case "1": basedElecValue = 100; break;
                case "2": basedElecValue = 80; break;
                case "3": basedElecValue = 130; break;
                default: basedElecValue = 0; break;
            }
            
            // Thêm vào basedValue một lượng dựa vào tháng để mô phỏng sự thay đổi
            int monthVariance = (month % 3) * 5;
            edElectricityOld.setText(String.valueOf(basedElecValue + monthVariance));
        }
        
        if (edWaterOld != null) {
            // Tạo dữ liệu giả lập - chỉ dùng cho demo
            int basedWaterValue = 0;
            switch (roomNumber) {
                case "1": basedWaterValue = 7; break;
                case "2": basedWaterValue = 4; break;
                case "3": basedWaterValue = 10; break;
                default: basedWaterValue = 0; break;
            }
            
            // Thêm vào basedValue một lượng dựa vào tháng để mô phỏng sự thay đổi
            int monthVariance = (month % 3);
            edWaterOld.setText(String.valueOf(basedWaterValue + monthVariance));
        }
        
        // Cập nhật UI sau khi thay đổi số cũ
        updateElectricityTotal();
        updateWaterTotal();
        updateServiceTotal();
        updateTotalAmount();
    }
    
    // Kiểm tra dữ liệu đầu vào khi tạo hóa đơn mới
    private boolean validateCreateBillData() {
        boolean isValid = true;
        
        // Kiểm tra tháng
        EditText edMonth = findViewById(R.id.ed_month);
        if (edMonth != null && (edMonth.getText() == null || edMonth.getText().toString().isEmpty())) {
            edMonth.setError("Vui lòng chọn tháng");
            isValid = false;
        }
        
        // Kiểm tra hạn thanh toán
        EditText edDueDate = findViewById(R.id.ed_due_date);
        if (edDueDate != null && (edDueDate.getText() == null || edDueDate.getText().toString().isEmpty())) {
            edDueDate.setError("Vui lòng chọn hạn thanh toán");
            isValid = false;
        }
        
        // Kiểm tra số điện
        CheckBox cbElectricity = findViewById(R.id.cb_electricity);
        EditText edElectricityOld = findViewById(R.id.ed_electricity_old);
        EditText edElectricityNew = findViewById(R.id.ed_electricity_new);
        
        if (cbElectricity != null && cbElectricity.isChecked()) {
            if (edElectricityOld != null && (edElectricityOld.getText() == null || edElectricityOld.getText().toString().isEmpty())) {
                edElectricityOld.setError("Vui lòng nhập số cũ");
                isValid = false;
            }
            if (edElectricityNew != null && (edElectricityNew.getText() == null || edElectricityNew.getText().toString().isEmpty())) {
                edElectricityNew.setError("Vui lòng nhập số mới");
                isValid = false;
            }
            
            // Kiểm tra số mới >= số cũ
            if (edElectricityOld != null && edElectricityNew != null && !edElectricityOld.getText().toString().isEmpty() 
                && !edElectricityNew.getText().toString().isEmpty()) {
                int oldValue = Integer.parseInt(edElectricityOld.getText().toString());
                int newValue = Integer.parseInt(edElectricityNew.getText().toString());
                
                if (newValue < oldValue) {
                    edElectricityNew.setError("Số mới phải lớn hơn hoặc bằng số cũ");
                    isValid = false;
                }
            }
        }
        
        // Kiểm tra số nước
        CheckBox cbWater = findViewById(R.id.cb_water);
        EditText edWaterOld = findViewById(R.id.ed_water_old);
        EditText edWaterNew = findViewById(R.id.ed_water_new);
        
        if (cbWater != null && cbWater.isChecked()) {
            if (edWaterOld != null && (edWaterOld.getText() == null || edWaterOld.getText().toString().isEmpty())) {
                edWaterOld.setError("Vui lòng nhập số cũ");
                isValid = false;
            }
            if (edWaterNew != null && (edWaterNew.getText() == null || edWaterNew.getText().toString().isEmpty())) {
                edWaterNew.setError("Vui lòng nhập số mới");
                isValid = false;
            }
            
            // Kiểm tra số mới >= số cũ
            if (edWaterOld != null && edWaterNew != null && !edWaterOld.getText().toString().isEmpty() 
                && !edWaterNew.getText().toString().isEmpty()) {
                int oldValue = Integer.parseInt(edWaterOld.getText().toString());
                int newValue = Integer.parseInt(edWaterNew.getText().toString());
                
                if (newValue < oldValue) {
                    edWaterNew.setError("Số mới phải lớn hơn hoặc bằng số cũ");
                    isValid = false;
                }
            }
        }
        
        return isValid;
    }

    // Lưu thông tin hóa đơn mới
    private void saveNewBillData() {
        // Trong thực tế, bạn sẽ lưu dữ liệu vào database
        // Ví dụ: databaseHelper.createBill(bill);

        // Hiện tại chỉ hiển thị thông báo thành công
        Toast.makeText(this, "Đã tạo hóa đơn mới!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Mở màn hình tạo mới hóa đơn
    public static void openCreateBillScreen(Context context, String roomNumber) {
        Intent intent = new Intent(context, Admin_HoaDonActivity.class);
        intent.putExtra("CREATE_BILL", true);
        intent.putExtra("ROOM_NUMBER", roomNumber);
        context.startActivity(intent);
    }
}