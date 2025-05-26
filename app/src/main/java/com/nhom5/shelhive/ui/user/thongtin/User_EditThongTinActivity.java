package com.nhom5.shelhive.ui.user.thongtin;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetUserResponse;
import com.nhom5.shelhive.api.UpdateUserRequest;
import com.nhom5.shelhive.ui.auth.DangNhapActivity;
import com.nhom5.shelhive.ui.common.customviews.HexagonImageView;
import com.nhom5.shelhive.ui.common.utils.RealPathUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_EditThongTinActivity extends AppCompatActivity {

    private EditText editFullName, editDob, editSex, editHometown, editAddress, editIdNumber, editPhone, editEmail;
    private HexagonImageView avatarHexagon;
    private ImageView imagePenBold, btnBack;

    private FrameLayout containerPen;
    private LinearLayout navHome, navLogout;
    private View popupLayout, btnSave, btnCancel;

    private String email;
    private String currentAvatarPath = null;
    private Uri selectedAvatarUri = null;

    private boolean waitingForPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_suathongtincanhan);

        // Nhận email
        email = getIntent().getStringExtra("email");

        // Ánh xạ view
        editFullName = findViewById(R.id.edit_user_fullname);
        editDob = findViewById(R.id.edit_user_dob);
        editSex = findViewById(R.id.edit_user_sex);
        editHometown = findViewById(R.id.edit_user_city);
        editAddress = findViewById(R.id.edit_user_province);
        editIdNumber = findViewById(R.id.edit_user_idnumber);
        editEmail = findViewById(R.id.edit_user_email);
        editPhone = findViewById(R.id.edit_user_phone);

        avatarHexagon = findViewById(R.id.avatar_hexagon);
        imagePenBold = findViewById(R.id.image_pen_bold);
        FrameLayout penButton = findViewById(R.id.container_frame6);

        btnBack = findViewById(R.id.btn_back);
        navHome = findViewById(R.id.nav_user_home);
        navLogout = findViewById(R.id.nav_logout);

        btnSave = findViewById(R.id.btn_save_user);
        btnCancel = findViewById(R.id.btn_cancel);

        popupLayout = findViewById(R.id.popup_layout);

        // Quay về
        btnBack.setOnClickListener(v -> finish());
        navHome.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        // Hiện thông tin ban đầu
        loadUserInfo();

        // Đăng xuất show popup
        navLogout.setOnClickListener(v -> showPopupLogout());

        if (popupLayout != null) {
            ImageView closeBtn = popupLayout.findViewById(R.id.close_button);
            ImageView logoutBtn = popupLayout.findViewById(R.id.logout_button);

            if (closeBtn != null) {
                closeBtn.setOnClickListener(v -> popupLayout.setVisibility(View.GONE));
            }
            if (logoutBtn != null) {
                logoutBtn.setOnClickListener(v -> {
                    SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("email");
                    editor.remove("password");
                    editor.remove("remember");
                    editor.apply();

                    startActivity(new Intent(this, DangNhapActivity.class));
                    finish();
                });
            }
        }

        // Đổi avatar
        penButton.setOnClickListener(v -> {
            Log.d("DEBUG_AVATAR", "Pen clicked!");
            pickImageFromGallery();
        });

        // Ngày sinh dùng DatePicker
        editDob.setOnClickListener(v -> showDatePicker());

        // Lưu thông tin
        btnSave.setOnClickListener(v -> saveUserInfo());
    }

    // --- PICK IMAGE GALLERY ---
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        selectedAvatarUri = uri;
                        Glide.with(this).load(uri).into(avatarHexagon);
                        uploadAvatar(uri); // Gọi upload avatar lên server
                    }
                }
            }
    );

    private void pickImageFromGallery() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (!waitingForPermission) {
                waitingForPermission = true;
                ActivityCompat.requestPermissions(this, new String[]{permission}, 101);
            }
            return;
        }
        waitingForPermission = false;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            waitingForPermission = false;
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(intent);
            } else {
                showToast("Bạn cần cấp quyền để chọn ảnh!");
            }
        }
    }


    // --- UPLOAD AVATAR ---
    private void uploadAvatar(Uri imageUri) {
        String realPath = RealPathUtil.getRealPath(this, imageUri);
        if (realPath == null) {
            showToast("Không lấy được đường dẫn ảnh!");
            return;
        }
        File file = new File(realPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        ApiService.apiService.uploadAvatar(email, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showToast("Đổi ảnh đại diện thành công!");
                    // Không cần làm gì thêm, avatar đã được set bằng Glide bên trên.
                } else {
                    showToast("Upload ảnh thất bại!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showToast("Lỗi upload avatar: " + t.getMessage());
            }
        });
    }

    // --- DATE PICKER ---
    private void showDatePicker() {
        // Lấy ngày hiện tại hoặc ngày trên EditText
        String current = editDob.getText().toString();
        int day = 1, month = 0, year = 2000;
        if (current.matches("\\d{2}/\\d{2}/\\d{4}")) {
            String[] p = current.split("/");
            day = Integer.parseInt(p[0]);
            month = Integer.parseInt(p[1]) - 1;
            year = Integer.parseInt(p[2]);
        }
        new android.app.DatePickerDialog(this, (view, y, m, d) -> {
            String formatted = String.format("%02d/%02d/%04d", d, m + 1, y);
            editDob.setText(formatted);
        }, year, month, day).show();
    }

    // --- LOAD USER INFO ---
    private void loadUserInfo() {
        ApiService.apiService.getUserByEmail(email).enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetUserResponse user = response.body();
                    editFullName.setText(user.getHo_ten());
                    editDob.setText(formatDob(user.getNgay_sinh()));
                    editSex.setText(user.getGioi_tinh());
                    editHometown.setText(user.getQue_quan());
                    editAddress.setText(user.getDia_chi());
                    editIdNumber.setText(user.getCccd());
                    editEmail.setText(user.getEmail());
                    editPhone.setText(user.getSdt());
                    String avtPath = user.getAvt();
                    if (avtPath != null && !avtPath.isEmpty()) {
                        String fullAvtUrl = "http://221.132.33.173:3000" + avtPath;
                        Glide.with(User_EditThongTinActivity.this).load(fullAvtUrl).placeholder(R.drawable.default_avatar).into(avatarHexagon);
                        currentAvatarPath = avtPath;
                    } else {
                        avatarHexagon.setImageResource(R.drawable.default_avatar);
                    }
                } else {
                    showToast("Lỗi tải thông tin người dùng!");
                }
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                showToast("Lỗi kết nối!");
            }
        });
    }

    // --- FORMAT DOB (yyyy-mm-dd[T...] -> dd/MM/yyyy) ---
    private String formatDob(String raw) {
        if (raw == null) return "";
        String datePart = raw.split("T")[0];
        String[] parts = datePart.split("-");
        if (parts.length == 3) {
            return parts[2] + "/" + parts[1] + "/" + parts[0];
        } else {
            return raw;
        }
    }

    // --- LƯU USER INFO ---
    private void saveUserInfo() {
        String hoTen = editFullName.getText().toString().trim();
        String ngaySinh = editDob.getText().toString().trim();
        String gioiTinh = editSex.getText().toString().trim();
        String queQuan = editHometown.getText().toString().trim();
        String diaChi = editAddress.getText().toString().trim();
        String cccd = editIdNumber.getText().toString().trim();
        String sdt = editPhone.getText().toString().trim();

        // Convert dd/MM/yyyy -> yyyy-MM-dd
        String ngaySinhApi = ngaySinh;
        if (ngaySinh.matches("\\d{2}/\\d{2}/\\d{4}")) {
            String[] p = ngaySinh.split("/");
            ngaySinhApi = p[2] + "-" + p[1] + "-" + p[0];
        }

        UpdateUserRequest req = new UpdateUserRequest(hoTen, ngaySinhApi, gioiTinh, queQuan, diaChi, cccd, null, sdt);

        ApiService.apiService.updateUser(email, req).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showToast("Cập nhật thành công!");
                    finish();
                } else {
                    showToast("Lỗi cập nhật thông tin!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showToast("Lỗi kết nối cập nhật!");
            }
        });
    }

    private void showPopupLogout() {
        if (popupLayout != null) {
            popupLayout.setVisibility(View.VISIBLE);
            popupLayout.bringToFront();
        } else {
            showToast("Không tìm thấy popup logout!");
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
