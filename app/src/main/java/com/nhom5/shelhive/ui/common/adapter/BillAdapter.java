package com.nhom5.shelhive.ui.common.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.model.Bill;

import java.util.ArrayList;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Bill bill);
    }

    private List<Bill> billList = new ArrayList<>();
    private final Context context;
    private final OnItemClickListener listener;

    public BillAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setBills(List<Bill> bills) {
        this.billList = bills;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = billList.get(position);
        holder.tvBillName.setText("Hóa đơn " + bill.getId());
        holder.tvAmount.setText(String.format("%,.0f VNĐ", bill.getAmount()));
        holder.tvDueDate.setText(bill.getDueDate());
        holder.tvStatus.setText(bill.getStatus().equalsIgnoreCase("Đã đóng") ? "Đã đóng" : "Chưa đóng");

        holder.itemView.setOnClickListener(v -> listener.onItemClick(bill));
    }

    @Override
    public int getItemCount() {
        return billList != null ? billList.size() : 0;
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tvBillName, tvAmount, tvDueDate, tvStatus;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBillName = itemView.findViewById(R.id.tvBillName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
