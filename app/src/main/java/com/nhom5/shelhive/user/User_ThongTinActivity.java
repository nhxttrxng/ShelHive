package com.nhom5.shelhive.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;

public class User_ThongTinActivity extends AppCompatActivity {
    
    private static final String TAG = "User_ThongTinActivity";

    // Biến cho màn hình xem thông tin
    private ImageView btnBack;
    private FrameLayout btnChangePassword, btnEdit;
    private LinearLayout navHome, navProfile, navLogout;
    private TextView textFullName, textDob, textCity, textProvince, 
                     textIdNumber, textEmail, textPhoneNumber;
    
    // Biến cho màn hình sửa thông tin
    private boolean isEditMode = false;
    private ImageView btnBackEdit;
    private FrameLayout btnSaveUser, btnCancel;
    private EditText editFullName, editDob, editQueQuan, editDiaChi, 
                     editCCCD, editEmail, editPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.d(TAG, "onCreate: Bắt đầu khởi tạo User_ThongTinActivity");
            showViewMode(); // Bắt đầu với chế độ xem mặc định
            Log.d(TAG, "onCreate: Đã khởi tạo xong User_ThongTinActivity");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Lỗi khi khởi tạo", e);
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void showViewMode() {
        try {
            Log.d(TAG, "showViewMode: Đang đặt giao diện user_thongtincanhan.xml");
            setContentView(R.layout.user_thongtincanhan);
            Log.d(TAG, "showViewMode: Đã đặt giao diện thành công");
            
            initViewModeViews();
            setViewModeListeners();
        } catch (Exception e) {
            Log.e(TAG, "showViewMode: Lỗi khi đặt giao diện", e);
            Toast.makeText(this, "Lỗi khi hiển thị thông tin: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void showEditMode() {
        try {
            Log.d(TAG, "showEditMode: Đang đặt giao diện user_suathongtincanhan.xml");
            setContentView(R.layout.user_suathongtincanhan);
            Log.d(TAG, "showEditMode: Đã đặt giao diện thành công");
            
            initEditModeViews();
            setEditModeListeners();
        } catch (Exception e) {
            Log.e(TAG, "showEditMode: Lỗi khi đặt giao diện", e);
            Toast.makeText(this, "Lỗi khi hiển thị giao diện sửa: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void initViewModeViews() {
        try {
            Log.d(TAG, "initViewModeViews: Đang ánh xạ các view");
            
            btnBack = findViewById(R.id.container_group);
            btnChangePassword = findViewById(R.id.btn_changepass_container);
            btnEdit = findViewById(R.id.btn_edit_container);
            
            navHome = findViewById(R.id.nav_home);
            navProfile = findViewById(R.id.nav_profile);
            navLogout = findViewById(R.id.nav_logout);
            
            // Ánh xạ các TextView
            textFullName = findViewById(R.id.text_full_name);
            textDob = findViewById(R.id.text_dob);
            textCity = findViewById(R.id.text_city);
            textProvince = findViewById(R.id.text_province);
            textIdNumber = findViewById(R.id.text_id_number);
            textEmail = findViewById(R.id.text_email);
            textPhoneNumber = findViewById(R.id.text_phone_number);
            
            Log.d(TAG, "initViewModeViews: Đã ánh xạ xong các view");
        } catch (Exception e) {
            Log.e(TAG, "initViewModeViews: Lỗi khi ánh xạ view", e);
            throw e; // ném lại lỗi để xử lý ở cấp cao hơn
        }
    }
    
    private void initEditModeViews() {
        try {
            Log.d(TAG, "initEditModeViews: Đang ánh xạ các view");
            
            btnBackEdit = findViewById(R.id.btn_back);
            btnSaveUser = findViewById(R.id.btn_save_user);
            btnCancel = findViewById(R.id.btn_cancel);
    
            // Ánh xạ các EditText
            editFullName = findViewById(R.id.edit_full_name);
            editDob = findViewById(R.id.edit_dob);
            editQueQuan = findViewById(R.id.edit_que_quan);
            editDiaChi = findViewById(R.id.edit_dia_chi);
            editCCCD = findViewById(R.id.edit_cccd);
            editEmail = findViewById(R.id.edit_email);
            editPhoneNumber = findViewById(R.id.edit_phone_number);
            
            Log.d(TAG, "initEditModeViews: Đã ánh xạ xong các view");
            
            // Đọc dữ liệu từ Intent
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                Log.d(TAG, "initEditModeViews: Đọc dữ liệu từ Intent");
                // Nếu có dữ liệu từ Intent, sử dụng nó
                editFullName.setText(extras.getString("fullName", ""));
                editDob.setText(extras.getString("dob", ""));
                editQueQuan.setText(extras.getString("city", ""));
                editDiaChi.setText(extras.getString("province", ""));
                editCCCD.setText(extras.getString("idNumber", ""));
                editEmail.setText(extras.getString("email", ""));
                editPhoneNumber.setText(extras.getString("phoneNumber", ""));
            } else if (textFullName != null) {
                Log.d(TAG, "initEditModeViews: Đọc dữ liệu từ TextView");
                // Nếu không có dữ liệu từ Intent nhưng có dữ liệu từ màn hình xem
                editFullName.setText(textFullName.getText());
                editDob.setText(textDob.getText());
                editQueQuan.setText(textCity.getText());
                editDiaChi.setText(textProvince.getText());
                editCCCD.setText(textIdNumber.getText());
                editEmail.setText(textEmail.getText());
                editPhoneNumber.setText(textPhoneNumber.getText());
            }
            
            Log.d(TAG, "initEditModeViews: Đã thiết lập dữ liệu cho EditText");
        } catch (Exception e) {
            Log.e(TAG, "initEditModeViews: Lỗi khi ánh xạ view", e);
            throw e; // ném lại lỗi để xử lý ở cấp cao hơn
        }
    }
    
    private void saveUserData() {
        try {
            Log.d(TAG, "saveUserData: Bắt đầu lưu dữ liệu");
            
            // Lưu dữ liệu người dùng vào cơ sở dữ liệu
            String fullName = editFullName.getText().toString();
            String dob = editDob.getText().toString();
            String queQuan = editQueQuan.getText().toString();
            String diaChi = editDiaChi.getText().toString();
            String cccd = editCCCD.getText().toString();
            String email = editEmail.getText().toString();
            String phoneNumber = editPhoneNumber.getText().toString();
            
            // TODO: Thêm code lưu vào cơ sở dữ liệu
            
            Toast.makeText(this, "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "saveUserData: Đã lưu thông tin thành công");
            
            // Quay về chế độ xem và cập nhật thông tin hiển thị
            isEditMode = false;
            showViewMode();
        } catch (Exception e) {
            Log.e(TAG, "saveUserData: Lỗi khi lưu dữ liệu", e);
            Toast.makeText(this, "Lỗi khi lưu: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void setViewModeListeners() {
        try {
            Log.d(TAG, "setViewModeListeners: Thiết lập các listener");
            
            // Nút quay lại
            btnBack.setOnClickListener(v -> {
                Log.d(TAG, "Đã nhấn nút quay lại");
                onBackPressed();
            });
    
            // Nút đổi mật khẩu
            btnChangePassword.setOnClickListener(v -> {
                Log.d(TAG, "Đã nhấn nút đổi mật khẩu");
                Toast.makeText(this, "Đổi mật khẩu", Toast.LENGTH_SHORT).show();
                // Thêm code chuyển đến màn hình đổi mật khẩu
                // Intent intent = new Intent(User_ThongTinActivity.this, User_DoiMatKhauActivity.class);
                // startActivity(intent);
            });
    
            // Nút sửa thông tin
            btnEdit.setOnClickListener(v -> {
                Log.d(TAG, "Đã nhấn nút sửa, chuyển sang chế độ chỉnh sửa");
                isEditMode = true;
                showEditMode();
            });
    
            // Bottom Navigation
            navHome.setOnClickListener(v -> {
                Log.d(TAG, "Đã nhấn nút trang chủ");
                try {
                    Intent intent = new Intent(User_ThongTinActivity.this, User_TrangChuActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.e(TAG, "Lỗi khi mở trang chủ", e);
                    Toast.makeText(this, "Không thể mở trang chủ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    
            navProfile.setOnClickListener(v -> {
                Log.d(TAG, "Đã nhấn nút hồ sơ (đã ở trang hồ sơ)");
                // Đã ở trang profile nên không cần làm gì
            });
    
            navLogout.setOnClickListener(v -> {
                Log.d(TAG, "Đã nhấn nút đăng xuất");
                Toast.makeText(this, "Đăng xuất", Toast.LENGTH_SHORT).show();
                // Thêm code đăng xuất
                // FirebaseAuth.getInstance().signOut();
                // Intent intent = new Intent(User_ThongTinActivity.this, DangNhapActivity.class);
                // startActivity(intent);
                // finish();
            });
            
            Log.d(TAG, "setViewModeListeners: Đã thiết lập xong các listener");
        } catch (Exception e) {
            Log.e(TAG, "setViewModeListeners: Lỗi khi thiết lập listener", e);
            throw e; // ném lại lỗi để xử lý ở cấp cao hơn
        }
    }
    
    private void setEditModeListeners() {
        try {
            Log.d(TAG, "setEditModeListeners: Thiết lập các listener");
            
            // Nút quay lại
            btnBackEdit.setOnClickListener(v -> {
                Log.d(TAG, "Đã nhấn nút quay lại, chuyển về chế độ xem");
                isEditMode = false;
                showViewMode();
            });
    
            // Nút hủy
            btnCancel.setOnClickListener(v -> {
                Log.d(TAG, "Đã nhấn nút hủy, chuyển về chế độ xem");
                isEditMode = false;
                showViewMode();
            });
    
            // Nút lưu
            btnSaveUser.setOnClickListener(v -> {
                Log.d(TAG, "Đã nhấn nút lưu");
                saveUserData();
            });
            
            Log.d(TAG, "setEditModeListeners: Đã thiết lập xong các listener");
        } catch (Exception e) {
            Log.e(TAG, "setEditModeListeners: Lỗi khi thiết lập listener", e);
            throw e; // ném lại lỗi để xử lý ở cấp cao hơn
        }
    }
    
    @Override
    public void onBackPressed() {
        if (isEditMode) {
            Log.d(TAG, "onBackPressed: Đang ở chế độ chỉnh sửa, chuyển về chế độ xem");
            isEditMode = false;
            showViewMode();
        } else {
            Log.d(TAG, "onBackPressed: Đang ở chế độ xem, thoát activity");
            super.onBackPressed();
        }
    }
} 