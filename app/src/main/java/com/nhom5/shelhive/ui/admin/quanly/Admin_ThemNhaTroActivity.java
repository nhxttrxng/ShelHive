package com.nhom5.shelhive.ui.admin.quanly;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.CreateDayTroRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_ThemNhaTroActivity extends AppCompatActivity {

    private EditText editName, editAddress, editRoomCount, editElectric, editWater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanly_nhatro_add);

        // Ánh xạ view
        editName = findViewById(R.id.editName);
        editAddress = findViewById(R.id.editAddress);
        editRoomCount = findViewById(R.id.editRoomCount);
        editElectric = findViewById(R.id.editElectric);
        editWater = findViewById(R.id.editWater);
        ImageView createButton = findViewById(R.id.create_button);
        ImageView backButton = findViewById(R.id.back); // <-- ImageView back từ layout

        // Lấy email từ Intent hoặc SharedPreferences
        String email = getIntent().getStringExtra("email");
        if (email == null) {
            SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
            email = prefs.getString("email", null);
        }

        final String finalEmail = email;

        // Quay lại khi nhấn nút back
        backButton.setOnClickListener(v -> finish());

        // Gắn validate khi mất focus
        setupFocusValidation(editName, "Tên nhà trọ không được để trống");
        setupFocusValidation(editAddress, "Địa chỉ không được để trống");
        setupFocusValidation(editRoomCount, "Số phòng không được để trống");
        setupFocusValidation(editElectric, "Giá điện không được để trống");
        setupFocusValidation(editWater, "Giá nước không được để trống");

        // Gọi API khi nhấn nút tạo
        createButton.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String address = editAddress.getText().toString().trim();
            String roomCountStr = editRoomCount.getText().toString().trim();
            String electricStr = editElectric.getText().toString().trim();
            String waterStr = editWater.getText().toString().trim();

            if (name.isEmpty()) {
                editName.setError("Vui lòng nhập tên nhà trọ");
                return;
            }
            if (address.isEmpty()) {
                editAddress.setError("Vui lòng nhập địa chỉ");
                return;
            }
            if (roomCountStr.isEmpty()) {
                editRoomCount.setError("Vui lòng nhập số phòng");
                return;
            }
            if (electricStr.isEmpty()) {
                editElectric.setError("Vui lòng nhập giá điện");
                return;
            }
            if (waterStr.isEmpty()) {
                editWater.setError("Vui lòng nhập giá nước");
                return;
            }

            try {
                int roomCount = Integer.parseInt(roomCountStr);
                double giaDien = Double.parseDouble(electricStr);
                double giaNuoc = Double.parseDouble(waterStr);

                CreateDayTroRequest request = new CreateDayTroRequest(
                        finalEmail, name, address, roomCount, giaDien, giaNuoc
                );

                ApiService.apiService.createDayTro(request).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(Admin_ThemNhaTroActivity.this, "Tạo nhà trọ thành công", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK); // Để màn trước biết reload nếu cần
                            finish(); // Quay lại không khởi tạo Intent mới
                        } else {
                            Toast.makeText(Admin_ThemNhaTroActivity.this, "Tạo thất bại: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(Admin_ThemNhaTroActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số phòng/giá điện/nước không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupFocusValidation(EditText editText, String errorMsg) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && editText.getText().toString().trim().isEmpty()) {
                editText.setError(errorMsg);
            }
        });
    }
}
