package com.nhom5.shelhive.ui.admin.quanly;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.UpdateMotelRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_SuaNhaTroActivity extends AppCompatActivity {

    private EditText editName, editAddress, editElectric, editWater;
    private int maDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanly_nhatro_upd);

        // Ánh xạ view
        editName = findViewById(R.id.editName);
        editAddress = findViewById(R.id.editAddress);
        editElectric = findViewById(R.id.editElectric);
        editWater = findViewById(R.id.editWater);
        ImageView saveButton = findViewById(R.id.save_button);
        ImageView cancelButton = findViewById(R.id.cancel_button);
        ImageView backButton = findViewById(R.id.back); // hoặc R.id.back nếu bạn đặt tên khác

        // Nhận dữ liệu từ Intent
        maDay = getIntent().getIntExtra("ma_day", -1);
        String name = getIntent().getStringExtra("ten_tro");
        String address = getIntent().getStringExtra("dia_chi");
        double giaDien = getIntent().getDoubleExtra("gia_dien", 0);
        double giaNuoc = getIntent().getDoubleExtra("gia_nuoc", 0);

        // Hiển thị dữ liệu cũ lên form
        editName.setText(name);
        editAddress.setText(address);
        editElectric.setText(String.valueOf(giaDien));
        editWater.setText(String.valueOf(giaNuoc));

        // Nút quay lại hoặc hủy
        ImageView.OnClickListener backHandler = v -> finish();
        cancelButton.setOnClickListener(backHandler);
        backButton.setOnClickListener(backHandler);

        // Lưu
        saveButton.setOnClickListener(v -> {
            String newName = editName.getText().toString().trim();
            String newAddress = editAddress.getText().toString().trim();
            double newGiaDien = Double.parseDouble(editElectric.getText().toString().trim());
            double newGiaNuoc = Double.parseDouble(editWater.getText().toString().trim());

            UpdateMotelRequest request = new UpdateMotelRequest(newName, newAddress, newGiaDien, newGiaNuoc);

            ApiService.apiService.updateDayTro(maDay, request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Admin_SuaNhaTroActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                        // Trả kết quả về để Admin_QuanLyActivity reload danh sách
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updated", true);
                        setResult(RESULT_OK, resultIntent);

                        finish();
                    } else {
                        Toast.makeText(Admin_SuaNhaTroActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(Admin_SuaNhaTroActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
