package com.nhom5.shelhive.ui.user.hoadon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetMotelByIdResponse;
import com.nhom5.shelhive.ui.model.Bill;
import java.text.SimpleDateFormat;
import java.util.*;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_ViewBillDetailActivity extends AppCompatActivity {
    private int billId;
    private int maDay = -1;
    private int roomId = -1;
    private TextView tvRoomNumber, tvElectricityTotal, tvWaterTotal, tvServiceTotal, tvInterestTotal, tvTotal;
    private EditText edMonth, edOriginalDueDate, edNewDueDate, edElectricityOld, edElectricityNew, edWaterOld, edWaterNew, edInterestRate;
    private TextView edElectricityPrice, edWaterPrice;
    private CheckBox cbElectricity, cbWater, cbRoom, cbInterest, cbLateFee;
    private Button btnRemind, btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_xem_hdp);

        billId = getIntent().getIntExtra("BILL_ID", -1);
        maDay = getIntent().getIntExtra("MA_DAY", -1);
        initViews();

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // CHỈNH ĐÚNG ĐOẠN NÀY: btnRemind chuyển sang User_GiaHanActivity, chỉ truyền billId
        btnRemind.setOnClickListener(v -> {
            Intent intent = new Intent(User_ViewBillDetailActivity.this, User_GiaHanActivity.class);
            intent.putExtra("billId", billId);
            startActivity(intent);
        });

        btnPay.setOnClickListener(v -> {
            double amount = 0;
            try {
                amount = Double.parseDouble(tvTotal.getText().toString().replace(".", "").replace(" đ", ""));
            } catch (Exception e) {
                Log.e("PAY_DEBUG", "Lỗi parse tiền: " + e.getMessage());
            }
            if (amount <= 0 || billId <= 0) {
                Toast.makeText(this, "Thiếu thông tin hóa đơn!", Toast.LENGTH_SHORT).show();
                return;
            }
            Map<String, Object> body = new HashMap<>();
            body.put("amount", amount);
            body.put("ma_hoa_don", billId);
            body.put("orderInfo", "Thanh toán hóa đơn phòng");
            body.put("returnUrl", "shelhive://vnpay_return");

            Log.d("PAY_DEBUG", "Gửi lên body: " + body);

            ApiService.apiService.createPayment(body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String paymentUrl = response.body().string().trim();
                            if (paymentUrl.contains("https://")) {
                                int idx = paymentUrl.indexOf("https://");
                                paymentUrl = paymentUrl.substring(idx);
                            }
                            if (paymentUrl.endsWith("\"")) paymentUrl = paymentUrl.substring(0, paymentUrl.length() - 1);
                            Log.d("PAY_DEBUG", "Link thanh toán: " + paymentUrl);

                            if (!paymentUrl.startsWith("https://")) {
                                Toast.makeText(User_ViewBillDetailActivity.this, "Link trả về không hợp lệ!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Intent intent = new Intent(User_ViewBillDetailActivity.this, VnpayWebViewActivity.class);
                            intent.putExtra("url", paymentUrl);
                            startActivity(intent);

                        } catch (Exception e) {
                            Log.e("PAY_DEBUG", "Exception: " + e.getMessage(), e);
                            Toast.makeText(User_ViewBillDetailActivity.this, "Không mở được link thanh toán!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("PAY_DEBUG", "API call không thành công! Code: " + response.code());
                        Toast.makeText(User_ViewBillDetailActivity.this, "Gọi API thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("PAY_DEBUG", "API lỗi: " + t.getMessage(), t);
                    Toast.makeText(User_ViewBillDetailActivity.this, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        loadBillDetail(billId);
        loadMotelInfo(maDay);
        handleVnpayDeepLink(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleVnpayDeepLink(intent);
    }

    private void handleVnpayDeepLink(Intent intent) {
        if (intent == null) return;
        if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null) {
            Uri data = intent.getData();
            Log.d("PAY_DEBUG", "DEEPLINK DATA: " + data);
            Set<String> keys = data.getQueryParameterNames();
            for (String key : keys) {
                Log.d("PAY_DEBUG", "DEEPLINK PARAM: " + key + "=" + data.getQueryParameter(key));
            }
            String responseCode = data.getQueryParameter("vnp_ResponseCode");
            String billIdStr = data.getQueryParameter("vnp_TxnRef");
            Log.d("PAY_DEBUG", "ResponseCode: " + responseCode + ", TxnRef: " + billIdStr);

            if ("00".equals(responseCode) && billIdStr != null) {
                int billIdFromDeepLink = Integer.parseInt(billIdStr);
                Map<String, String> body = new HashMap<>();
                body.put("trang_thai", "đã thanh toán");
                ApiService.apiService.updateInvoiceStatus(billIdFromDeepLink, body)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(User_ViewBillDetailActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                                Log.d("PAY_DEBUG", "Đã update trạng thái bill thành công");
                                loadBillDetail(billIdFromDeepLink);
                                btnRemind.setVisibility(View.GONE);
                                btnPay.setVisibility(View.GONE);
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("PAY_DEBUG", "Lỗi cập nhật trạng thái hóa đơn: " + t.getMessage(), t);
                                Toast.makeText(User_ViewBillDetailActivity.this, "Lỗi cập nhật trạng thái hóa đơn!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Thanh toán thất bại hoặc bị hủy!", Toast.LENGTH_LONG).show();
                Log.e("PAY_DEBUG", "Deeplink trả về thất bại/hủy");
            }
        }
    }

    private void initViews() {
        tvRoomNumber = findViewById(R.id.tv_room_number);
        tvElectricityTotal = findViewById(R.id.tv_electricity_total);
        tvWaterTotal = findViewById(R.id.tv_water_total);
        tvServiceTotal = findViewById(R.id.tv_service_total);
        tvInterestTotal = findViewById(R.id.tv_interest_total);
        tvTotal = findViewById(R.id.tv_total);

        edMonth = findViewById(R.id.ed_month);
        edOriginalDueDate = findViewById(R.id.ed_original_due_date);
        edNewDueDate = findViewById(R.id.ed_new_due_date);
        edElectricityOld = findViewById(R.id.ed_electricity_old);
        edElectricityNew = findViewById(R.id.ed_electricity_new);
        edWaterOld = findViewById(R.id.ed_water_old);
        edWaterNew = findViewById(R.id.ed_water_new);
        edInterestRate = findViewById(R.id.ed_interest_rate);

        edElectricityPrice = findViewById(R.id.ed_electricity_price);
        edWaterPrice = findViewById(R.id.ed_water_price);

        cbElectricity = findViewById(R.id.cb_electricity);
        cbWater = findViewById(R.id.cb_water);
        cbRoom = findViewById(R.id.cb_room);
        cbInterest = findViewById(R.id.cb_interest);
        cbLateFee = findViewById(R.id.cb_late_fee);

        btnRemind = findViewById(R.id.btn_remind);
        btnPay = findViewById(R.id.btn_extend);
    }

    private void loadBillDetail(int billId) {
        ApiService.apiService.getInvoiceById(billId).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bill bill = response.body();
                    roomId = bill.getRoomId();
                    populateBillDetails(bill);
                } else {
                    Log.e("API_ERROR", "Không tải được chi tiết hóa đơn");
                }
            }
            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi khi tải hóa đơn: " + t.getMessage());
            }
        });
    }

    private void loadMotelInfo(int maDay) {
        if (maDay == -1) return;
        ApiService.apiService.getMotelById(maDay).enqueue(new Callback<GetMotelByIdResponse>() {
            @Override
            public void onResponse(Call<GetMotelByIdResponse> call, Response<GetMotelByIdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetMotelByIdResponse motel = response.body();
                    edElectricityPrice.setText(formatCurrency(motel.getGiaDien()) + "/kwh");
                    edWaterPrice.setText(formatCurrency(motel.getGiaNuoc()) + "/m³");
                }
            }
            @Override
            public void onFailure(Call<GetMotelByIdResponse> call, Throwable t) {
                Log.e("API_ERROR", "Không lấy được giá điện/nước: " + t.getMessage());
            }
        });
    }

    private void populateBillDetails(Bill bill) {
        String roomCode = String.valueOf(bill.getRoomId());
        String roomShort = roomCode.length() > 2 ? roomCode.substring(roomCode.length() - 2) : roomCode;
        tvRoomNumber.setText("Phòng " + roomShort);

        edMonth.setText(formatMonthYear(bill.getBillMonthYear()));
        edOriginalDueDate.setText(formatDate(bill.getDueDate()));
        String newDueDate = bill.getExtendedDueDate();
        edNewDueDate.setText(formatDate(newDueDate));
        edElectricityOld.setText(String.valueOf(bill.getElectricityOldIndex()));
        edElectricityNew.setText(String.valueOf(bill.getElectricityNewIndex()));
        edWaterOld.setText(String.valueOf(bill.getWaterOldIndex()));
        edWaterNew.setText(String.valueOf(bill.getWaterNewIndex()));
        edInterestRate.setText("0.5");

        tvElectricityTotal.setText(formatCurrency(bill.getElectricityAmount()));
        tvWaterTotal.setText(formatCurrency(bill.getWaterAmount()));
        tvServiceTotal.setText(formatCurrency(bill.getRoomAmount()));
        tvInterestTotal.setText(formatCurrency(bill.getExtensionFee()));
        tvTotal.setText(formatCurrency(bill.getAmount()));

        cbElectricity.setChecked(bill.getElectricityAmount() > 0);
        cbWater.setChecked(bill.getWaterAmount() > 0);
        cbRoom.setChecked(bill.getRoomAmount() > 0);
        boolean hasExtension = (newDueDate != null && !newDueDate.trim().isEmpty());
        cbInterest.setChecked(hasExtension);
        cbLateFee.setChecked(hasExtension);

        if (bill.getStatus() != null && bill.getStatus().trim().equalsIgnoreCase("Đã thanh toán")) {
            btnRemind.setVisibility(View.GONE);
            btnPay.setVisibility(View.GONE);
        } else {
            btnRemind.setVisibility(View.VISIBLE);
            btnPay.setVisibility(View.VISIBLE);
        }
    }

    private String formatMonthYear(String iso) {
        if (iso == null || iso.isEmpty()) return "";
        try {
            if (iso.length() >= 7) {
                String[] arr = iso.split("-");
                if (arr.length >= 2) {
                    return arr[1] + "/" + arr[0];
                }
            }
        } catch (Exception e) { }
        return iso;
    }

    private String formatDate(String iso) {
        if (iso == null || iso.isEmpty()) return "";
        try {
            SimpleDateFormat src = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date d = src.parse(iso.substring(0, 10));
            SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return out.format(d);
        } catch (Exception e) {
            return iso;
        }
    }

    private String formatCurrency(double amount) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,### đ");
        java.text.DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        return df.format(amount);
    }
}
