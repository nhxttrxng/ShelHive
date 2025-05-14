package com.nhom5.shelhive.ui.admin.quanly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetDayTroResponse;
import com.nhom5.shelhive.ui.common.adapter.MotelAdapter;
import com.nhom5.shelhive.ui.model.Motel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_QuanLyActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMotels;
    private MotelAdapter motelAdapter;
    private List<Motel> motelList;
    private String finalEmail;
    private ImageView noneMotel;

    private static final int REQUEST_EDIT_MOTEL = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanly_nhatro);

        String email = getIntent().getStringExtra("email");
        if (email == null) {
            SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
            email = prefs.getString("email", null);
        }

        finalEmail = email;

        Log.d("Admin_QuanLy", "Email sử dụng: " + finalEmail);

        recyclerViewMotels = findViewById(R.id.recyclerViewMotels);
        recyclerViewMotels.setLayoutManager(new LinearLayoutManager(this));

        noneMotel = findViewById(R.id.none_motel);
        ImageView noneMotelSearch = findViewById(R.id.none_motel_search);
        EditText editSearchMotel = findViewById(R.id.editSearchMotel);

        motelList = new ArrayList<>();
        motelAdapter = new MotelAdapter(this, motelList);
        recyclerViewMotels.setAdapter(motelAdapter);

        if (finalEmail != null) {
            loadMotelsFromApi(finalEmail, noneMotel);
        }

        editSearchMotel.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMotels(s.toString(), noneMotelSearch);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        ImageView btnBack = findViewById(R.id.mingcute_arrow_up);
        btnBack.setOnClickListener(v -> finish());

        ImageView btnAddMotel = findViewById(R.id.add_motel_button);
        btnAddMotel.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_QuanLyActivity.this, Admin_ThemNhaTroActivity.class);
            intent.putExtra("email", finalEmail);
            startActivity(intent);
        });

        View popupDelete = findViewById(R.id.popup_deletemotel);
        popupDelete.setVisibility(View.GONE);

        ImageView yesButton = popupDelete.findViewById(R.id.yes_button);
        ImageView closeButton = popupDelete.findViewById(R.id.close_button);

        motelAdapter.setOnActionClickListener(new MotelAdapter.OnActionClickListener() {
            @Override
            public void onEditClick(Motel motel, int position) {
                Intent intent = new Intent(Admin_QuanLyActivity.this, Admin_SuaNhaTroActivity.class);
                intent.putExtra("ma_day", motel.getMaday());
                intent.putExtra("ten_tro", motel.getName());
                intent.putExtra("dia_chi", motel.getAddress());
                intent.putExtra("gia_dien", motel.getGia_dien());
                intent.putExtra("gia_nuoc", motel.getGia_nuoc());
                intent.putExtra("email", finalEmail);
                startActivityForResult(intent, REQUEST_EDIT_MOTEL); // Sửa ở đây
            }

            @Override
            public void onDeleteClick(int ma_day) {
                popupDelete.setVisibility(View.VISIBLE);

                yesButton.setOnClickListener(v -> {
                    ApiService.apiService.deleteDayTro(ma_day).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                loadMotelsFromApi(finalEmail, noneMotel);
                            } else {
                                Log.e("DELETE", "Xoá thất bại: " + response.code());
                            }
                            popupDelete.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("DELETE", "Lỗi API xoá: " + t.getMessage());
                            popupDelete.setVisibility(View.GONE);
                        }
                    });
                });

                closeButton.setOnClickListener(v -> popupDelete.setVisibility(View.GONE));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_MOTEL && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("updated", false)) {
                loadMotelsFromApi(finalEmail, noneMotel);
            }
        }
    }

    private void filterMotels(String text, ImageView noneMotelSearch) {
        List<Motel> filteredList = new ArrayList<>();
        for (Motel motel : motelList) {
            if (motel.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(motel);
            }
        }

        if (filteredList.isEmpty()) {
            recyclerViewMotels.setVisibility(View.GONE);
            noneMotelSearch.setVisibility(View.VISIBLE);
        } else {
            recyclerViewMotels.setVisibility(View.VISIBLE);
            noneMotelSearch.setVisibility(View.GONE);
        }

        motelAdapter.setFilteredList(filteredList);
    }

    private void loadMotelsFromApi(String email, ImageView noneMotel) {
        Log.d("API_CALL", "Gọi API với email: " + email);
        ApiService.apiService.getDayTroByAdminEmail(email)
                .enqueue(new Callback<List<GetDayTroResponse>>() {
                    @Override
                    public void onResponse(Call<List<GetDayTroResponse>> call, Response<List<GetDayTroResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<GetDayTroResponse> responseList = response.body();

                            Log.d("API_RESPONSE", "Dữ liệu nhận được: " + responseList.toString());

                            motelList.clear();
                            for (GetDayTroResponse dto : responseList) {
                                motelList.add(new Motel(
                                        dto.getMa_day(),
                                        dto.getTen_tro(),
                                        dto.getDia_chi(),
                                        dto.getSo_phong(),
                                        dto.getGia_dien(),
                                        dto.getGia_nuoc()
                                ));
                            }

                            motelAdapter.setFilteredList(motelList);
                            motelAdapter.notifyDataSetChanged();

                            if (motelList.isEmpty()) {
                                recyclerViewMotels.setVisibility(View.GONE);
                                noneMotel.setVisibility(View.VISIBLE);
                            } else {
                                recyclerViewMotels.setVisibility(View.VISIBLE);
                                noneMotel.setVisibility(View.GONE);
                            }
                        } else {
                            Log.d("API_RESPONSE", "API trả về null hoặc thất bại");
                            recyclerViewMotels.setVisibility(View.GONE);
                            noneMotel.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GetDayTroResponse>> call, Throwable t) {
                        Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                        recyclerViewMotels.setVisibility(View.GONE);
                        noneMotel.setVisibility(View.VISIBLE);
                    }
                });
    }
}
