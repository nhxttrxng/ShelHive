package com.nhom5.shelhive.ui.admin.thongtin;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetAdminByEmailResponse;
import com.nhom5.shelhive.api.GetUserResponse;
import com.nhom5.shelhive.api.UpdateAdminRequest;
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

public class Admin_EditThongTinActivity extends AppCompatActivity {

    private EditText edtFullName, edtEmail, edtPhone;
    private HexagonImageView imgAvatar;
    private FrameLayout avatarEditButton;
    private ImageView btnBack;
    private LinearLayout navHome, navProfile, navLogout;
    private View btnSave, btnCancel;
    private String email;
    private Uri selectedAvatarUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_suathongtincanhan);

        // Nhận email (hoặc lấy từ SharedPreferences nếu quản trị viên)
        email = getIntent().getStringExtra("email");
        if (email == null) {
            SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
            email = prefs.getString("email", null);
        }

        // Ánh xạ view
        edtFullName = findViewById(R.id.edt_admin_fullname);
        edtEmail    = findViewById(R.id.edt_admin_email);
        edtPhone    = findViewById(R.id.edt_admin_phone);

        imgAvatar = findViewById(R.id.img_admin_avatar);
        avatarEditButton = findViewById(R.id.container_admin_avatar_edit);
        btnBack = findViewById(R.id.btn_admin_back);

        btnSave   = findViewById(R.id.btn_admin_save);
        btnCancel = findViewById(R.id.btn_admin_cancel);

        navHome    = findViewById(R.id.nav_user_home);
        navProfile = findViewById(R.id.nav_user_profile);
        navLogout  = findViewById(R.id.nav_logout);

        // Nút back và hủy đều về finish()
        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());
        navHome.setOnClickListener(v -> finish());
        navProfile.setOnClickListener(v -> { /* Ở lại trang này hoặc show toast */ });

        // Đăng xuất
        navLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("email");
            editor.remove("password");
            editor.remove("remember");
            editor.apply();

            startActivity(new Intent(this, DangNhapActivity.class));
            finish();
        });

        // Load admin info
        loadAdminInfo();

        // Đổi avatar
        avatarEditButton.setOnClickListener(v -> pickImageFromGallery());

        // Lưu thông tin
        btnSave.setOnClickListener(v -> saveAdminInfo());
    }

    // --- PICK IMAGE GALLERY ---
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        selectedAvatarUri = uri;
                        Glide.with(this).load(uri).into(imgAvatar);
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
            ActivityCompat.requestPermissions(this, new String[]{permission}, 101);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        } else if (requestCode == 101) {
            showToast("Bạn cần cấp quyền để chọn ảnh!");
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

        // Nếu admin có API riêng, thay bằng ApiService.apiService.uploadAdminAvatar(email, body)
        ApiService.apiService.uploadAdminAvatar(email, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showToast("Đổi ảnh đại diện thành công!");
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

    // --- LOAD ADMIN INFO ---
    private void loadAdminInfo() {
        // Nếu có API riêng cho admin, thay thế hàm gọi bên dưới
        ApiService.apiService.getAdminByEmail(email).enqueue(new Callback<GetAdminByEmailResponse>() {
            @Override
            public void onResponse(Call<GetAdminByEmailResponse> call, Response<GetAdminByEmailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetAdminByEmailResponse user = response.body();
                    edtFullName.setText(user.getHo_ten());
                    edtEmail.setText(user.getEmail());
                    edtPhone.setText(user.getSdt());
                    String avtPath = user.getAvt();
                    if (avtPath != null && !avtPath.isEmpty()) {
                        String fullAvtUrl = "http://221.132.33.173:3000" + avtPath;
                        Glide.with(Admin_EditThongTinActivity.this).load(fullAvtUrl)
                                .placeholder(R.drawable.default_avatar)
                                .into(imgAvatar);
                    } else {
                        imgAvatar.setImageResource(R.drawable.default_avatar);
                    }
                } else {
                    showToast("Lỗi tải thông tin admin!");
                }
            }

            @Override
            public void onFailure(Call<GetAdminByEmailResponse> call, Throwable t) {
                showToast("Lỗi kết nối!");
            }
        });
    }

    // --- LƯU ADMIN INFO ---
    private void saveAdminInfo() {
        String hoTen = edtFullName.getText().toString().trim();
        String emailStr = edtEmail.getText().toString().trim();
        String sdt = edtPhone.getText().toString().trim();

        // Nếu cần map sang model riêng, chỉnh lại cho phù hợp
        UpdateAdminRequest req = new UpdateAdminRequest(hoTen, sdt);

        ApiService.apiService.updateAdmin(email, req).enqueue(new Callback<ResponseBody>() {
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

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
