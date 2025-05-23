package com.nhom5.shelhive.ui.user.xacthuc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.UpdateUserRequest;
import com.nhom5.shelhive.ui.user.User_TrangChuActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public class User_KiemTraThongTinActivity extends AppCompatActivity {

    private TextInputEditText edtCCCD, edtName, edtBirthday, edtSex, edtQueQuan, edtAddress;
    private MaterialButton btnRecapture, btnNext;
    private String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_idcard2);

        // Ánh xạ view
        edtCCCD = findViewById(R.id.cccd);
        edtName = findViewById(R.id.name);
        edtBirthday = findViewById(R.id.birthday);
        edtSex = findViewById(R.id.sex);
        edtQueQuan = findViewById(R.id.quequan);
        edtAddress = findViewById(R.id.address);
        btnRecapture = findViewById(R.id.recapture);
        btnNext = findViewById(R.id.next);

        // Nhận thông tin truyền từ Activity trước
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        HashMap<String, String> info = (HashMap<String, String>) intent.getSerializableExtra("info");

        if (info != null) {
            edtCCCD.setText(info.get("cccd"));
            edtName.setText(info.get("name"));
            edtBirthday.setText(info.get("birthday"));
            edtSex.setText(info.get("sex"));
            edtQueQuan.setText(info.get("quequan"));
            edtAddress.setText(info.get("address"));
        }

        btnRecapture.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> {
            String cccd = edtCCCD.getText().toString().trim();
            String name = capitalizeWords(edtName.getText().toString().trim());
            String birthdayInput = edtBirthday.getText().toString().trim();
            String sex = edtSex.getText().toString().trim();
            String quequan = edtQueQuan.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (cccd.isEmpty() || name.isEmpty() || birthdayInput.isEmpty() ||
                    sex.isEmpty() || quequan.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển đổi ngày sinh về yyyy-MM-dd
            String birthday = convertDateFormat(birthdayInput);

            if (birthday == null) {
                Toast.makeText(this, "Định dạng ngày sinh không hợp lệ! Đúng dạng: dd/MM/yyyy", Toast.LENGTH_SHORT).show();
                return;
            }

            UpdateUserRequest req = new UpdateUserRequest();
            req.setCccd(cccd);
            req.setHo_ten(name);
            req.setNgay_sinh(birthday);
            req.setGioi_tinh(sex);
            req.setQue_quan(quequan);
            req.setDia_chi(address);

            ApiService.apiService.updateUser(email, req).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Intent i = new Intent(User_KiemTraThongTinActivity.this, User_TrangChuActivity.class);
                        i.putExtra("email", email);
                        startActivity(i);
                        finishAffinity();
                    } else {
                        Toast.makeText(User_KiemTraThongTinActivity.this, "Cập nhật thông tin thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(User_KiemTraThongTinActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Hàm chuyển họ tên về dạng chỉ viết hoa chữ cái đầu mỗi từ
    private String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) return input;
        String[] words = input.toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return sb.toString().trim();
    }

    // Chuyển "16/10/2004" -> "2004-10-16"
    private String convertDateFormat(String input) {
        try {
            SimpleDateFormat fromFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return toFormat.format(fromFormat.parse(input));
        } catch (ParseException e) {
            return null;
        }
    }
}
