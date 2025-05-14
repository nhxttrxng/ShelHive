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
import com.nhom5.shelhive.ui.common.adapter.BillAdapter;
import com.nhom5.shelhive.ui.model.Bill;

import java.util.ArrayList;
import java.util.List;

public class BillListFragment extends Fragment {
    private RecyclerView recyclerViewBills;
    private LinearLayout emptyView;
    private BillAdapter billAdapter;
    private List<Bill> billList;

    public BillListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ view
        recyclerViewBills = view.findViewById(R.id.recyclerViewBills);
        emptyView = view.findViewById(R.id.empty_view);

        // Thiết lập RecyclerView
        recyclerViewBills.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Khởi tạo dữ liệu
        billList = new ArrayList<>();
        
        // Tạo Adapter - Sử dụng constructor mới
        billAdapter = new BillAdapter(getContext());
        recyclerViewBills.setAdapter(billAdapter);
        
        // Thiết lập sự kiện click
        billAdapter.setOnItemClickListener(bill -> {
            Toast.makeText(getContext(), "Đã chọn hóa đơn: " + bill.getName(), Toast.LENGTH_SHORT).show();
            // Xử lý khi chọn hóa đơn ở đây
        });
        
        // Tải dữ liệu
        loadBillData();
    }
    
    private void loadBillData() {
        // Mô phỏng dữ liệu mẫu
        billList = new ArrayList<>();
        
        // Tạo dữ liệu mẫu
        billList.add(new Bill("1", "1/2025", "07/04/2025", 1877750, false, "1", "Phòng 1", false));
        billList.add(new Bill("2", "2/2025", "07/04/2025", 1674750, false, "1", "Phòng 1", false));
        billList.add(new Bill("3", "2/2025", "07/05/2025", 1595000, false, "1", "Phòng 1", true));
        billList.add(new Bill("4", "12/2024", "07/01/2025", 1450000, true, "1", "Phòng 1", false));
        
        // Cập nhật adapter với danh sách mới
        if (billAdapter != null) {
            billAdapter.setBills(billList);
        }
        
        // Hiển thị empty view nếu không có dữ liệu
        if (billList.isEmpty()) {
            if (recyclerViewBills != null) recyclerViewBills.setVisibility(View.GONE);
            if (emptyView != null) emptyView.setVisibility(View.VISIBLE);
            
            // Tìm và cập nhật TextView hiển thị thông báo trống
            TextView tvEmptyMessage = emptyView.findViewById(R.id.tv_empty_message);
            if (tvEmptyMessage != null) {
                tvEmptyMessage.setText("Không có hóa đơn nào!");
            }
        } else {
            if (recyclerViewBills != null) recyclerViewBills.setVisibility(View.VISIBLE);
            if (emptyView != null) emptyView.setVisibility(View.GONE);
        }
    }
    
    // Phương thức để cập nhật danh sách hóa đơn từ bên ngoài
    public void updateBillList(List<Bill> newBills) {
        if (billList != null) {
            billList.clear();
            if (newBills != null) {
                billList.addAll(newBills);
            }
        } else {
            billList = new ArrayList<>();
            if (newBills != null) {
                billList.addAll(newBills);
            }
        }
        
        // Cập nhật adapter với danh sách mới
        if (billAdapter != null) {
            billAdapter.setBills(billList);
        }
        
        // Cập nhật giao diện
        if (billList == null || billList.isEmpty()) {
            if (recyclerViewBills != null) recyclerViewBills.setVisibility(View.GONE);
            if (emptyView != null) emptyView.setVisibility(View.VISIBLE);
            
            // Tìm và cập nhật TextView hiển thị thông báo trống
            if (emptyView != null) {
                TextView tvEmptyMessage = emptyView.findViewById(R.id.tv_empty_message);
                if (tvEmptyMessage != null) {
                    tvEmptyMessage.setText("Không có hóa đơn nào!");
                }
            }
        } else {
            if (recyclerViewBills != null) recyclerViewBills.setVisibility(View.VISIBLE);
            if (emptyView != null) emptyView.setVisibility(View.GONE);
        }
    }
} 