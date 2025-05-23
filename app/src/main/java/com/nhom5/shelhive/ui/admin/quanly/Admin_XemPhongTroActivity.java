package com.nhom5.shelhive.ui.admin.quanly;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.ApiServiceWithNull;
import com.nhom5.shelhive.api.GetRoomInfoResponse;
import com.nhom5.shelhive.ui.model.Room2;
import com.nhom5.shelhive.ui.model.User;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_XemPhongTroActivity extends AppCompatActivity {

    private int maPhong;

    private EditText editEmail, editFullname, editBirthday, editHometown, editAddressPermanent, editPhone, editCCCD;
    private EditText editName, editAddress, editRoomCount;
    private ImageView createButton, deleteButton, backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanly_phongtro_view);

        maPhong = getIntent().getIntExtra("ma_phong", -1);
        if (maPhong == -1) {
            Toast.makeText(this, "Lỗi: Không có mã phòng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ánh xạ View
        editEmail = findViewById(R.id.editEmail);
        editFullname = findViewById(R.id.editFullname);
        editBirthday = findViewById(R.id.editBirthday);
        editHometown = findViewById(R.id.editHometown);
        editAddressPermanent = findViewById(R.id.editAddressPermanent);
        editPhone = findViewById(R.id.editPhone);
        editCCCD = findViewById(R.id.editCCCD);

        editName = findViewById(R.id.editName);
        editAddress = findViewById(R.id.editAddress);
        editRoomCount = findViewById(R.id.editRoomCount);

        createButton = findViewById(R.id.create_button);
        deleteButton = findViewById(R.id.delete_button);
        backButton = findViewById(R.id.back);

        // Load thông tin phòng
        loadRoomInfo();

        // Nút sửa
        createButton.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_XemPhongTroActivity.this, Admin_SuaPhongTroActivity.class);
            intent.putExtra("ma_phong", maPhong);
            startActivity(intent);
        });

        // Nút xóa (trả phòng)
        deleteButton.setOnClickListener(v -> {
            String currentEmail = editEmail.getText().toString().trim();
            if (currentEmail.isEmpty()) {
                Toast.makeText(Admin_XemPhongTroActivity.this, "Phòng này chưa có người thuê!", Toast.LENGTH_SHORT).show();
                return; // Không thực hiện xoá nữa
            }

            Map<String, Object> map = new HashMap<>();
            map.put("email_user", null);
            map.put("da_thue", false);
            map.put("ngay_bat_dau", null);
            map.put("ngay_ket_thuc", null);
            map.put("gia_thue", null);

            ApiServiceWithNull.apiServiceWithNull.updateRoom(maPhong, map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Admin_XemPhongTroActivity.this, "Đã xoá người thuê!", Toast.LENGTH_SHORT).show();
                        loadRoomInfo();
                    } else {
                        Toast.makeText(Admin_XemPhongTroActivity.this, "Lỗi xoá người thuê", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(Admin_XemPhongTroActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                }
            });
        });

        backButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRoomInfo();
    }

    private void loadRoomInfo() {
        ApiService.apiService.getRoomInfoByMaPhong(maPhong).enqueue(new Callback<GetRoomInfoResponse>() {
            @Override
            public void onResponse(Call<GetRoomInfoResponse> call, Response<GetRoomInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Room2 room = response.body().getRoom();
                    User user = response.body().getUser();

                    // Set dữ liệu phòng
                    if (room != null) {
                        editName.setText(room.getGia_thue() != null ? formatCurrency(room.getGia_thue()) + " đ" : "");
                        editAddress.setText(formatDateWithTrim(room.getNgay_bat_dau()));
                        editRoomCount.setText(formatDateWithTrim(room.getNgay_ket_thuc()));
                    }

                    // Set dữ liệu user nếu có
                    if (user != null) {
                        editEmail.setText(user.getEmail() != null ? user.getEmail() : "");
                        editFullname.setText(user.getHoTen() != null ? user.getHoTen() : "");
                        editBirthday.setText(formatDateWithTrim(user.getNgaySinh()));
                        editHometown.setText(user.getQueQuan() != null ? user.getQueQuan() : "");
                        editAddressPermanent.setText(user.getDiaChi() != null ? user.getDiaChi() : "");
                        editPhone.setText(user.getSdt() != null ? user.getSdt() : "");
                        editCCCD.setText(user.getCccd() != null ? user.getCccd() : "");
                    } else {
                        editEmail.setText("");
                        editFullname.setText("");
                        editBirthday.setText("");
                        editHometown.setText("");
                        editAddressPermanent.setText("");
                        editPhone.setText("");
                        editCCCD.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetRoomInfoResponse> call, Throwable t) {
                Toast.makeText(Admin_XemPhongTroActivity.this, "Lỗi tải thông tin phòng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Format yyyy-MM-dd hoặc yyyy-MM-ddTHH:mm:ssZ -> dd/MM/yyyy
    private String formatDateWithTrim(String dateInput) {
        if (dateInput == null || dateInput.isEmpty()) return "";
        String isoDate = dateInput;
        int tIdx = isoDate.indexOf('T');
        if (tIdx > 0) {
            isoDate = isoDate.substring(0, tIdx);
        }
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat sdfOutput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdfOutput.format(sdfInput.parse(isoDate));
        } catch (ParseException e) {
            return isoDate;
        }
    }

    // Format số tiền: 1000000 -> 1.000.000
    private String formatCurrency(Double amount) {
        if (amount == null) return "";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);
        return formatter.format(amount.longValue());
    }
}
