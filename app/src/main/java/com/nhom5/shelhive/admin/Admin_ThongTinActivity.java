package com.nhom5.shelhive.admin;

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

public class Admin_ThongTinActivity extends AppCompatActivity {
    
    private static final String TAG = "Admin_ThongTinActivity";

    // Trạng thái màn hình
    private boolean isEditMode = false;
    private boolean isChangePasswordMode = false;
    
    // Biến cho màn hình xem thông tin
    private ImageView btnBack;
    private FrameLayout btnChangePassword, btnEdit;
    private TextView textFullName, textDob, textCity, textProvince, 
                     textIdNumber, textEmail, textPhoneNumber;
    
    // Biến cho màn hình sửa thông tin
    private ImageView btnBackEdit;
    private FrameLayout btnSaveUser, btnCancel;
    private EditText editFullName, editDob, editQueQuan, editDiaChi, 
                     editCCCD, editEmail, editPhoneNumber;
    
    // Biến cho màn hình đổi mật khẩu
    private ImageView btnBackChangePass;
    private EditText edtCurrentPassword, edtNewPassword, edtConfirmPassword;
    private FrameLayout btnSavePassword, btnCancelChangePass;
    
    // Biến cho thanh điều hướng
    private LinearLayout navHome, navProfile, navLogout;
    
    // Dữ liệu người dùng
    private String fullName = "";
    private String dob = "";
    private String city = "";
    private String province = "";
    private String idNumber = "";
    private String email = "";
    private String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            showViewMode(); // Bắt đầu với chế độ xem mặc định
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Lỗi khi khởi tạo", e);
            showToast("Lỗi: " + e.getMessage());
        }
    }
    
    // ====== CHUYỂN ĐỔI GIỮA CÁC CHẾ ĐỘ ======
    
    private void showViewMode() {
        try {
            setContentView(R.layout.admin_thongtincanhan);
            
            isEditMode = false;
            isChangePasswordMode = false;
            
            initViewModeViews();
            setViewModeListeners();
            
            // Hiển thị dữ liệu người dùng nếu có
            displayUserData();
            
        } catch (Exception e) {
            Log.e(TAG, "showViewMode: Lỗi khi đặt giao diện", e);
            showToast("Lỗi khi hiển thị thông tin: " + e.getMessage());
        }
    }
    
    private void showEditMode() {
        try {
            // Lưu dữ liệu từ giao diện xem nếu có
            saveDataFromViewMode();
            
            setContentView(R.layout.admin_suathongtincanhan);
            
            isEditMode = true;
            isChangePasswordMode = false;
            
            initEditModeViews();
            setEditModeListeners();
            
            // Điền dữ liệu vào form
            fillEditFormData();
            
        } catch (Exception e) {
            Log.e(TAG, "showEditMode: Lỗi khi đặt giao diện", e);
            showToast("Lỗi khi hiển thị giao diện sửa: " + e.getMessage());
            isEditMode = false;
            showViewMode();
        }
    }
    
    private void showChangePasswordMode() {
        try {
            setContentView(R.layout.admin_quenmatkhau);
            
            isEditMode = false;
            isChangePasswordMode = true;
            
            initChangePasswordViews();
            setChangePasswordListeners();
            
        } catch (Exception e) {
            Log.e(TAG, "showChangePasswordMode: Lỗi khi đặt giao diện", e);
            showToast("Lỗi khi hiển thị giao diện đổi mật khẩu: " + e.getMessage());
            showViewMode();
        }
    }
    
    // ====== KHỞI TẠO GIAO DIỆN ======
    
    private void initViewModeViews() {
        // Ánh xạ các nút
        btnBack = findViewSafely(R.id.btn_admin_back);
        btnChangePassword = findViewSafely(R.id.btn_admin_changepass);
        btnEdit = findViewSafely(R.id.btn_admin_edit);
        
        // Ánh xạ các TextView
        textFullName = findViewSafely(R.id.txt_admin_fullname);
        textEmail = findViewSafely(R.id.txt_admin_email);
        textPhoneNumber = findViewSafely(R.id.txt_admin_phone);
        
        // Ánh xạ thanh điều hướng
        initNavigation();
    }
    
    private void initEditModeViews() {
        // Ánh xạ các nút
        btnBackEdit = findViewSafely(R.id.btn_admin_back);
        btnSaveUser = findViewSafely(R.id.btn_admin_save);
        btnCancel = findViewSafely(R.id.btn_admin_cancel);
        
        // Ánh xạ các EditText
        editFullName = findViewSafely(R.id.edt_admin_fullname);
        editEmail = findViewSafely(R.id.edt_admin_email);
        editPhoneNumber = findViewSafely(R.id.edt_admin_phone);
        
        // Ánh xạ thanh điều hướng nếu có
        initNavigation();
    }
    
    private void initChangePasswordViews() {
        // Ánh xạ các nút
        btnBackChangePass = findViewSafely(R.id.btn_admin_back);
        btnSavePassword = findViewSafely(R.id.btn_admin_save_user);
        btnCancelChangePass = findViewSafely(R.id.btn_admin_cancel);
        
        // Ánh xạ các EditText
        edtCurrentPassword = findViewSafely(R.id.edt_admin_current_password);
        edtNewPassword = findViewSafely(R.id.edt_admin_new_password);
        edtConfirmPassword = findViewSafely(R.id.edt_admin_confirm_password);
        
        // Ánh xạ thanh điều hướng nếu có
        initNavigation();
    }
    
    private void initNavigation() {
        // Ánh xạ thanh điều hướng
        navHome = findViewSafely(R.id.nav_admin_home);
        navProfile = findViewSafely(R.id.nav_admin_profile);
        navLogout = findViewSafely(R.id.nav_admin_logout);
    }
    
    // ====== XỬ LÝ DỮ LIỆU ======
    
    private void saveDataFromViewMode() {
        if (textFullName != null) fullName = textFullName.getText().toString();
        if (textDob != null) dob = textDob.getText().toString();
        if (textCity != null) city = textCity.getText().toString();
        if (textProvince != null) province = textProvince.getText().toString();
        if (textIdNumber != null) idNumber = textIdNumber.getText().toString();
        if (textEmail != null) email = textEmail.getText().toString();
        if (textPhoneNumber != null) phoneNumber = textPhoneNumber.getText().toString();
    }
    
    private void displayUserData() {
        if (textFullName != null && !fullName.isEmpty()) textFullName.setText(fullName);
        if (textDob != null && !dob.isEmpty()) textDob.setText(dob);
        if (textCity != null && !city.isEmpty()) textCity.setText(city);
        if (textProvince != null && !province.isEmpty()) textProvince.setText(province);
        if (textIdNumber != null && !idNumber.isEmpty()) textIdNumber.setText(idNumber);
        if (textEmail != null && !email.isEmpty()) textEmail.setText(email);
        if (textPhoneNumber != null && !phoneNumber.isEmpty()) textPhoneNumber.setText(phoneNumber);
    }
    
    private void fillEditFormData() {
        if (editFullName != null) editFullName.setText(fullName);
        if (editDob != null) editDob.setText(dob);
        if (editQueQuan != null) editQueQuan.setText(city);
        if (editDiaChi != null) editDiaChi.setText(province);
        if (editCCCD != null) editCCCD.setText(idNumber);
        if (editEmail != null) editEmail.setText(email);
        if (editPhoneNumber != null) editPhoneNumber.setText(phoneNumber);
    }
    
    private void saveUserData() {
        try {
            // Đọc dữ liệu từ form
            if (editFullName != null) fullName = editFullName.getText().toString();
            if (editDob != null) dob = editDob.getText().toString();
            if (editQueQuan != null) city = editQueQuan.getText().toString();
            if (editDiaChi != null) province = editDiaChi.getText().toString();
            if (editCCCD != null) idNumber = editCCCD.getText().toString();
            if (editEmail != null) email = editEmail.getText().toString();
            if (editPhoneNumber != null) phoneNumber = editPhoneNumber.getText().toString();
            
            // Kiểm tra dữ liệu
            if (fullName.isEmpty()) {
                showToast("Vui lòng nhập họ tên");
                return;
            }
            
            if (email.isEmpty()) {
                showToast("Vui lòng nhập email");
                return;
            }
            
            if (phoneNumber.isEmpty()) {
                showToast("Vui lòng nhập số điện thoại");
                return;
            }
            
            // TODO: Lưu dữ liệu vào cơ sở dữ liệu
            
            showToast("Đã lưu thông tin");
            
            // Quay về chế độ xem
            showViewMode();
            
        } catch (Exception e) {
            Log.e(TAG, "saveUserData: Lỗi khi lưu dữ liệu", e);
            showToast("Lỗi khi lưu: " + e.getMessage());
        }
    }
    
    private void savePassword() {
        try {
            // Đọc dữ liệu từ form
            String currentPassword = edtCurrentPassword != null ? edtCurrentPassword.getText().toString().trim() : "";
            String newPassword = edtNewPassword != null ? edtNewPassword.getText().toString().trim() : "";
            String confirmPassword = edtConfirmPassword != null ? edtConfirmPassword.getText().toString().trim() : "";
            
            // Kiểm tra dữ liệu
            if (currentPassword.isEmpty()) {
                showToast("Vui lòng nhập mật khẩu hiện tại");
                return;
            }
            
            if (newPassword.isEmpty()) {
                showToast("Vui lòng nhập mật khẩu mới");
                return;
            }
            
            if (confirmPassword.isEmpty()) {
                showToast("Vui lòng xác nhận mật khẩu mới");
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                showToast("Mật khẩu xác nhận không khớp");
                return;
            }
            
            // TODO: Kiểm tra mật khẩu hiện tại và lưu mật khẩu mới
            
            showToast("Đổi mật khẩu thành công");
            
            // Quay về chế độ xem
            showViewMode();
            
        } catch (Exception e) {
            Log.e(TAG, "savePassword: Lỗi khi lưu mật khẩu", e);
            showToast("Lỗi khi đổi mật khẩu: " + e.getMessage());
        }
    }
    
    // ====== THIẾT LẬP SỰ KIỆN ======
    
    private void setViewModeListeners() {
        // Nút quay lại
        setOnClickListener(btnBack, v -> onBackPressed());
        
        // Nút đổi mật khẩu
        setOnClickListener(btnChangePassword, v -> showChangePasswordMode());
        
        // Nút sửa thông tin
        setOnClickListener(btnEdit, v -> showEditMode());
        
        // Thiết lập sự kiện cho thanh điều hướng
        setupNavigationListeners();
    }
    
    private void setEditModeListeners() {
        // Nút quay lại và hủy
        setOnClickListener(btnBackEdit, v -> showViewMode());
        setOnClickListener(btnCancel, v -> showViewMode());
        
        // Nút lưu
        setOnClickListener(btnSaveUser, v -> saveUserData());
        
        // Thiết lập sự kiện cho thanh điều hướng
        setupNavigationListeners();
    }
    
    private void setChangePasswordListeners() {
        // Nút quay lại và hủy
        setOnClickListener(btnBackChangePass, v -> {
            showViewMode();
            Log.d(TAG, "Nút quay lại được nhấn");
        });
        
        setOnClickListener(btnCancelChangePass, v -> {
            showViewMode();
            Log.d(TAG, "Nút hủy được nhấn");
        });
        
        // Nút lưu
        setOnClickListener(btnSavePassword, v -> {
            savePassword();
            Log.d(TAG, "Nút lưu mật khẩu được nhấn");
        });
        
        // Thiết lập sự kiện cho thanh điều hướng
        setupNavigationListeners();
    }
    
    private void setupNavigationListeners() {
        // Nút trang chủ
        setOnClickListener(navHome, v -> {
            try {
                Intent intent = new Intent(Admin_ThongTinActivity.this, Admin_TrangChuActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Log.e(TAG, "Lỗi khi mở trang chủ", e);
                showToast("Không thể mở trang chủ: " + e.getMessage());
            }
        });
        
        // Nút hồ sơ - đã ở trang này nên chỉ cần về chế độ xem
        setOnClickListener(navProfile, v -> showViewMode());
        
        // Nút đăng xuất
        setOnClickListener(navLogout, v -> {
            showToast("Đăng xuất");
            // TODO: Thực hiện đăng xuất
            // FirebaseAuth.getInstance().signOut();
            // Intent intent = new Intent(Admin_ThongTinActivity.this, DangNhapActivity.class);
            // startActivity(intent);
            // finish();
        });
    }
    
    // ====== TIỆN ÍCH ======
    
    @Override
    public void onBackPressed() {
        if (isEditMode || isChangePasswordMode) {
            showViewMode();
        } else {
            super.onBackPressed();
        }
    }
    
    private <T extends View> T findViewSafely(int id) {
        try {
            return findViewById(id);
        } catch (Exception e) {
            Log.e(TAG, "findViewSafely: Không tìm thấy view với id: " + id, e);
            return null;
        }
    }
    
    private void setOnClickListener(View view, View.OnClickListener listener) {
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }
    
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
