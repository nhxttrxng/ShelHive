package com.nhom5.shelhive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.model.Bill;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    private List<Bill> bills;
    private Context context;
    private OnItemClickListener listener;
    private SimpleDateFormat dateFormat;
    private NumberFormat currencyFormat;

    public interface OnItemClickListener {
        void onItemClick(Bill bill);
    }

    public BillAdapter(Context context) {
        this.context = context;
        this.bills = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills != null ? bills : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bill bill = bills.get(position);
        holder.tvBillName.setText(bill.getName());
        holder.tvAmount.setText(currencyFormat.format(bill.getAmount()));
        
        // Kiểm tra null để tránh lỗi NullPointerException
        if (bill.getDueDateAsDate() != null) {
            holder.tvDueDate.setText(dateFormat.format(bill.getDueDateAsDate()));
        } else {
            holder.tvDueDate.setText("Chưa có hạn");
        }
        
        // Cập nhật trạng thái của hóa đơn
        if (bill.isPaid()) {
            holder.tvStatus.setText("Đã đóng");
            holder.tvStatus.setBackgroundResource(R.drawable.rounded_corner_bg_green);
        } else {
            // Kiểm tra nếu hóa đơn quá hạn
            if (bill.isOverdue()) {
                holder.tvStatus.setText("Quá hạn");
                holder.tvStatus.setBackgroundResource(R.drawable.rounded_corner_bg_red);
            } else {
                holder.tvStatus.setText("Chưa đóng");
                holder.tvStatus.setBackgroundResource(R.drawable.rounded_corner_bg);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(bill);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bills != null ? bills.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBillName, tvAmount, tvDueDate, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBillName = itemView.findViewById(R.id.tvBillName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
} 