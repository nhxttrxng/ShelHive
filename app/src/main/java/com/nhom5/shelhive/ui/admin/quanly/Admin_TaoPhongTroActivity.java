package com.nhom5.shelhive.ui.admin.quanly;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.CreateRoomRequest;
import com.nhom5.shelhive.api.GetUserResponse;
import com.nhom5.shelhive.ui.model.User;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_TaoPhongTroActivity extends AppCompatActivity {

    private int maDay;
    private EditText editName, editAddress, editRoomCount, editEmail;
    private EditText editFullname, editBirthday, editHometown, editAddressPermanent, editPhone, editCCCD;
    private ImageView createButton, backButton;
    private TextView emailError, nameError, addressError, roomCountError;
    private boolean validUser = false;
    private Handler emailHandler = new Handler(Looper.getMainLooper());
    private Runnable emailRunnable;
    private static final int EMAIL_CHECK_DELAY = 600;
    private boolean isFormattingGiaThue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanly_phongtro_add);

        maDay = getIntent().getIntExtra("ma_day", -1);
        if (maDay == -1) {
            Toast.makeText(this, "Không có mã dãy trọ!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ánh xạ view
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
        nameError = findViewById(R.id.name_error);
        addressError = findViewById(R.id.address_error);
        roomCountError = findViewById(R.id.roomcount_error);

        createButton = findViewById(R.id.create_button);
        backButton = findViewById(R.id.back);

        // Format giá thuê khi nhập
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

        // Date picker cho ngày thuê/ngày hết hạn
        editAddress.setOnClickListener(v -> showDatePickerDialog(editAddress));
        editRoomCount.setOnClickListener(v -> showDatePickerDialog(editRoomCount));

        // Validate email user, fill info
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailError.setVisibility(View.INVISIBLE);
                validUser = false;
                if (emailRunnable != null) emailHandler.removeCallbacks(emailRunnable);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();
                emailRunnable = () -> {
                    if (email.isEmpty()) {
                        clearUserFields();
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

        // Nút tạo phòng
        createButton.setOnClickListener(v -> createRoom());

        // Nút back
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

    // Hàm format số tiền: 1000000 -> 1.000.000
    private String formatCurrency(long amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);
        return formatter.format(amount);
    }

    // Convert dd/MM/yyyy -> yyyy-MM-dd
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

    // Convert yyyy-MM-dd hoặc yyyy-MM-ddTHH:mm:ssZ về dd/MM/yyyy
    private String formatDateWithTrim(String dateInput) {
        if (TextUtils.isEmpty(dateInput)) return "";
        String isoDate = dateInput;
        int tIdx = isoDate.indexOf('T');
        if (tIdx > 0) isoDate = isoDate.substring(0, tIdx);
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat sdfOutput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdfOutput.format(sdfInput.parse(isoDate));
        } catch (ParseException e) {
            return isoDate;
        }
    }

    private void createRoom() {
        // Lấy dữ liệu form
        String giaThueStr = editName.getText().toString().replace(".", "").trim();
        String ngayBatDau = editAddress.getText().toString().trim();
        String ngayKetThuc = editRoomCount.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        // Validate: nếu có email thì các trường không được để trống
        boolean hasEmail = !email.isEmpty();
        boolean valid = true;

        // Xóa error trước
        nameError.setVisibility(View.INVISIBLE);
        addressError.setVisibility(View.INVISIBLE);
        roomCountError.setVisibility(View.INVISIBLE);
        emailError.setVisibility(View.INVISIBLE);

        if (hasEmail && !validUser) {
            emailError.setText("Email người dùng không tồn tại!");
            emailError.setVisibility(View.VISIBLE);
            valid = false;
        }

        if (hasEmail) {
            if (giaThueStr.isEmpty()) {
                nameError.setText("Vui lòng nhập giá thuê!");
                nameError.setVisibility(View.VISIBLE);
                valid = false;
            }
            if (ngayBatDau.isEmpty()) {
                addressError.setText("Vui lòng chọn ngày thuê!");
                addressError.setVisibility(View.VISIBLE);
                valid = false;
            }
            if (ngayKetThuc.isEmpty()) {
                roomCountError.setText("Vui lòng chọn ngày hết hạn!");
                roomCountError.setVisibility(View.VISIBLE);
                valid = false;
            }
            // Ngày bắt đầu phải từ hôm nay trở đi
            if (!ngayBatDau.isEmpty() && !isDateTodayOrAfter(ngayBatDau)) {
                addressError.setText("Ngày thuê phải từ hôm nay trở đi!");
                addressError.setVisibility(View.VISIBLE);
                valid = false;
            }
        }

        if (!valid) return;

        Double giaThue = null;
        if (!giaThueStr.isEmpty()) {
            try {
                giaThue = Double.valueOf(giaThueStr);
            } catch (Exception ignored) {}
        }

        String isoNgayBatDau = ngayBatDau.isEmpty() ? null : convertDateToIso(ngayBatDau);
        String isoNgayKetThuc = ngayKetThuc.isEmpty() ? null : convertDateToIso(ngayKetThuc);

        // Khi không có email: mấy trường này để null
        if (!hasEmail) {
            giaThue = null;
            isoNgayBatDau = null;
            isoNgayKetThuc = null;
        }

        // ma_phong sẽ được backend tự sinh (không cần gửi), nên truyền null/"" cho maPhong.
        CreateRoomRequest req = new CreateRoomRequest(
                null, // ma_phong, server sẽ tự sinh
                maDay,
                hasEmail ? email : null,
                isoNgayBatDau,
                isoNgayKetThuc,
                giaThue
        );

        ApiService.apiService.createRoom(req).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Admin_TaoPhongTroActivity.this, "Tạo phòng thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(Admin_TaoPhongTroActivity.this, "Lỗi tạo phòng!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Admin_TaoPhongTroActivity.this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isDateTodayOrAfter(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar inputCal = Calendar.getInstance();
            inputCal.setTime(sdf.parse(dateStr));
            Calendar today = Calendar.getInstance();
            // Xóa phần giờ phút để so sánh theo ngày
            inputCal.set(Calendar.HOUR_OF_DAY, 0);
            inputCal.set(Calendar.MINUTE, 0);
            inputCal.set(Calendar.SECOND, 0);
            inputCal.set(Calendar.MILLISECOND, 0);
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            return !inputCal.before(today); // input >= today
        } catch (Exception e) {
            return false;
        }
    }
}
