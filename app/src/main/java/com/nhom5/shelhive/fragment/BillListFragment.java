package com.nhom5.shelhive.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.ui.common.adapter.BillAdapter;
import com.nhom5.shelhive.ui.model.Bill;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillListFragment extends Fragment {

    private RecyclerView recyclerViewBills;
    private LinearLayout emptyView;
    private BillAdapter billAdapter;

    public BillListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bill_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewBills = view.findViewById(R.id.recyclerViewBills);
        emptyView = view.findViewById(R.id.empty_view);

        recyclerViewBills.setLayoutManager(new LinearLayoutManager(getContext()));
        billAdapter = new BillAdapter(requireContext(), bill -> {
            Toast.makeText(getContext(), "Đã chọn hóa đơn: " + bill.getId(), Toast.LENGTH_SHORT).show();
            // Xử lý click mở chi tiết hóa đơn nếu muốn
        });
        recyclerViewBills.setAdapter(billAdapter);

        loadBillsFromAPI();
    }

    private void loadBillsFromAPI() {
        int roomId = 1;  // Thay bằng roomId thực tế (nên lấy từ arguments)
        ApiService.apiService.getInvoicesByRoom(roomId).enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Bill> bills = response.body();
                    if (!bills.isEmpty()) {
                        billAdapter.setBills(bills);
                        recyclerViewBills.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    } else {
                        recyclerViewBills.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                } else {
                    recyclerViewBills.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải hóa đơn: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                recyclerViewBills.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        });
    }
}
