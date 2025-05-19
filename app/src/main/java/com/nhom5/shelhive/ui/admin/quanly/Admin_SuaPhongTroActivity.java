package com.nhom5.shelhive.ui.admin.quanly;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetRoomInfoResponse;
import com.nhom5.shelhive.api.GetUserResponse;
import com.nhom5.shelhive.ui.model.Room2;
import com.nhom5.shelhive.ui.model.User;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_SuaPhongTroActivity extends AppCompatActivity {

    private int maPhong;
    private EditText editName, editAddress, editRoomCount, editEmail;
    private EditText editFullname, editBirthday, editHometown, editAddressPermanent, editPhone, editCCCD;
    private ImageView createButton, cancelButton, backButton;
    private TextView emailError;
    private boolean validUser = false;

    private Handler emailHandler = new Handler(Looper.getMainLooper());
    private Runnable emailRunnable;
    private static final int EMAIL_CHECK_DELAY = 600; // milliseconds

    private boolean isFormattingGiaThue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanly_phongtro_upd);

        maPhong = getIntent().getIntExtra("ma_phong", -1);
        if (maPhong == -1) {
            Toast.makeText(this, "Lỗi: Không có mã phòng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editName = findViewById(R.id.editName);
        editAddress = findViewById(R.id.editAddress);
        editRoomCount = findViewById(R.id.editRoomCount);

        editEmail = findViewById(R.id.editEmail);
        editFullname = findViewById(R.id.editFullname);
        editBirthday = findViewById(R.id.editBirthday);
        editHometown = findViewById(R.id.editHometown);
        editAddressPermanent = findViewById(R.id.editAddressPermanent);
        editPhone = findViewById(R.id.editPhone);
        editCCCD = findViewById(R.id.editCCCD);
        emailError = findViewById(R.id.email_error);

        createButton = findViewById(R.id.create_button);
        cancelButton = findViewById(R.id.cancel_button);
        backButton = findViewById(R.id.back);

        editName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (isFormattingGiaThue) return;
                isFormattingGiaThue = true;
                String raw = s.toString().replace(".", "").replace(" ", "").replace("đ", "");
                if (raw.isEmpty()) {
                    editName.setText("");
                    isFormattingGiaThue = false;
                    return;
                }
                try {
                    long value = Long.parseLong(raw);
                    String formatted = formatCurrency(value);
                    editName.removeTextChangedListener(this);
                    editName.setText(formatted);
                    editName.setSelection(formatted.length());
                    editName.addTextChangedListener(this);
                } catch (Exception ignored) {}
                isFormattingGiaThue = false;
            }
        });

        loadRoomInfo();

        editAddress.setOnClickListener(v -> showDatePickerDialog(editAddress));
        editRoomCount.setOnClickListener(v -> showDatePickerDialog(editRoomCount));

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailError.setVisibility(View.INVISIBLE);
                validUser = false;
                if (emailRunnable != null) {
                    emailHandler.removeCallbacks(emailRunnable);
                }
            }
            @Override public void afterTextChanged(Editable s) {
                String email = s.toString().trim();
                emailRunnable = () -> {
                    if (email.isEmpty()) {
                        clearUserFields();
                        editName.setText("");
                        editAddress.setText("");
                        editRoomCount.setText("");
                        emailError.setVisibility(View.INVISIBLE);
                        validUser = false;
                        return;
                    }
                    ApiService.apiService.getUserByEmail(email).enqueue(new Callback<GetUserResponse>() {
                        @Override
                        public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getHo_ten() != null) {
                                User user = new User(
                                        response.body().getEmail(),
                                        response.body().getHo_ten(),
                                        response.body().getDia_chi(),
                                        response.body().getGioi_tinh(),
                                        response.body().getQue_quan(),
                                        response.body().getNgay_sinh(),
                                        response.body().getSdt(),
                                        response.body().getCccd()
                                );
                                setUserFields(user);
                                emailError.setVisibility(View.INVISIBLE);
                                validUser = true;
                            } else {
                                clearUserFields();
                                emailError.setText("Email người dùng không tồn tại!");
                                emailError.setVisibility(View.VISIBLE);
                                validUser = false;
                            }
                        }
                        @Override
                        public void onFailure(Call<GetUserResponse> call, Throwable t) {
                            clearUserFields();
                            emailError.setText("Lỗi kết nối tới server");
                            emailError.setVisibility(View.VISIBLE);
                            validUser = false;
                        }
                    });
                };
                emailHandler.postDelayed(emailRunnable, EMAIL_CHECK_DELAY);
            }
        });

        createButton.setOnClickListener(v -> saveRoom());
        cancelButton.setOnClickListener(v -> finish());
        backButton.setOnClickListener(v -> finish());
    }

    private void showDatePickerDialog(EditText target) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String dateStr = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    target.setText(dateStr);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    private void setUserFields(User user) {
        editFullname.setText(user.getHoTen());
        editBirthday.setText(formatDateWithTrim(user.getNgaySinh()));
        editHometown.setText(user.getQueQuan());
        editAddressPermanent.setText(user.getDiaChi());
        editPhone.setText(user.getSdt());
        editCCCD.setText(user.getCccd());
    }

    private void clearUserFields() {
        editFullname.setText("");
        editBirthday.setText("");
        editHometown.setText("");
        editAddressPermanent.setText("");
        editPhone.setText("");
        editCCCD.setText("");
    }

    private void loadRoomInfo() {
        ApiService.apiService.getRoomInfoByMaPhong(maPhong).enqueue(new Callback<GetRoomInfoResponse>() {
            @Override
            public void onResponse(Call<GetRoomInfoResponse> call, Response<GetRoomInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Room2 room = response.body().getRoom();
                    User user = response.body().getUser();
                    if (room != null) {
                        editName.setText(room.getGia_thue() != null ? formatCurrency(room.getGia_thue().longValue()) : "");
                        editAddress.setText(formatDateWithTrim(room.getNgay_bat_dau()));
                        editRoomCount.setText(formatDateWithTrim(room.getNgay_ket_thuc()));
                        if (room.getEmail_user() != null) {
                            editEmail.setText(room.getEmail_user());
                        } else {
                            editEmail.setText("");
                        }
                    }
                    if (user != null) {
                        setUserFields(user);
                        emailError.setVisibility(View.INVISIBLE);
                        validUser = true;
                    } else {
                        clearUserFields();
                        validUser = false;
                    }
                }
            }
            @Override
            public void onFailure(Call<GetRoomInfoResponse> call, Throwable t) {
                Toast.makeText(Admin_SuaPhongTroActivity.this, "Lỗi tải thông tin phòng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertDateToIso(String dateInput) {
        if (TextUtils.isEmpty(dateInput)) return null;
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdfOutput.format(sdfInput.parse(dateInput));
        } catch (Exception e) {
            return null;
        }
    }

    private String formatDateWithTrim(String dateInput) {
        if (TextUtils.isEmpty(dateInput)) return "";
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

    private String formatCurrency(long amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);
        return formatter.format(amount);
    }

    private boolean isNgayBatDauValid(String ngayBatDau) {
        if (TextUtils.isEmpty(ngayBatDau)) return false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date startDate = sdf.parse(ngayBatDau);

            // Lấy ngày hiện tại, set giờ phút giây về 0 để so sánh đúng
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            return !startDate.before(today.getTime());
        } catch (Exception e) {
            return false;
        }
    }

    private void saveRoom() {
        String email = editEmail.getText().toString().trim();
        String giaThue = editName.getText().toString().replace(".", "").trim();
        String ngayThue = editAddress.getText().toString().trim();
        String ngayHetHan = editRoomCount.getText().toString().trim();

        // Nếu có email => bắt buộc nhập đủ các trường liên quan
        if (!TextUtils.isEmpty(email)) {
            if (!validUser) {
                emailError.setText("Email người dùng không tồn tại!");
                emailError.setVisibility(View.VISIBLE);
                return;
            }
            if (giaThue.isEmpty() || ngayThue.isEmpty() || ngayHetHan.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ giá thuê, ngày bắt đầu và ngày kết thúc!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isNgayBatDauValid(ngayThue)) {
                Toast.makeText(this, "Ngày bắt đầu phải từ hôm nay trở đi!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Map<String, Object> req = new HashMap<>();
        if (TextUtils.isEmpty(email)) {
            req.put("email_user", null);
            req.put("gia_thue", null);
            req.put("ngay_bat_dau", null);
            req.put("ngay_ket_thuc", null);
        } else {
            req.put("email_user", email);
            req.put("gia_thue", Double.valueOf(giaThue));
            req.put("ngay_bat_dau", convertDateToIso(ngayThue));
            req.put("ngay_ket_thuc", convertDateToIso(ngayHetHan));
        }

        Log.d("DEBUG_UPDATE_REQ", new Gson().toJson(req));

        ApiService.apiService.updateRoom(maPhong, req).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Admin_SuaPhongTroActivity.this, "Cập nhật phòng thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(Admin_SuaPhongTroActivity.this, "Lỗi cập nhật phòng!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Admin_SuaPhongTroActivity.this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
