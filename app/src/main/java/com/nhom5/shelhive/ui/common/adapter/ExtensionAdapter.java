package com.nhom5.shelhive.ui.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.model.Bill;
import com.nhom5.shelhive.ui.model.Extension;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExtensionAdapter extends RecyclerView.Adapter<ExtensionAdapter.ExtensionViewHolder> {

    private Context context;
    private List<Extension> extensionList;
    private List<Bill> billList; // Bill tương ứng từng extension
    private OnExtensionClickListener listener; // BỔ SUNG callback

    // Định nghĩa interface callback
    public interface OnExtensionClickListener {
        void onExtensionClick(Extension extension, Bill bill);
    }

    // Constructor CHUẨN
    public ExtensionAdapter(Context context, OnExtensionClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.extensionList = new ArrayList<>();
        this.billList = new ArrayList<>();
    }

    public void setExtensions(List<Extension> extensions, List<Bill> bills) {
        this.extensionList = extensions != null ? extensions : new ArrayList<>();
        this.billList = bills != null ? bills : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExtensionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_extension, parent, false);
        return new ExtensionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtensionViewHolder holder, int position) {
        Extension ext = extensionList.get(position);
        Bill bill = (billList != null && billList.size() > position) ? billList.get(position) : null;

        if (bill != null) {
            holder.tvBillId.setText(formatMonthYear(bill.getBillMonthYear()));
            double tongTien = bill.getElectricityAmount() + bill.getWaterAmount() + bill.getRoomAmount() + ext.getExpectedInterest();
            holder.tvAmount.setText(formatCurrency(tongTien));
            holder.tvDueDate.setText(formatDate(ext.getNewDueDate()));
            String status = bill.getStatus();
            holder.tvStatus.setText(status);

            if ("Trễ hạn".equalsIgnoreCase(status)) {
                holder.tvStatus.setTextColor(android.graphics.Color.RED);
            } else if ("Đã thanh toán".equalsIgnoreCase(status)) {
                holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#388E3C"));
            } else {
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.darkbrown));
            }
        } else {
            holder.tvBillId.setText("Không rõ tháng");
            holder.tvAmount.setText(formatCurrency(ext.getExpectedInterest()));
            holder.tvDueDate.setText(formatDate(ext.getNewDueDate()));
            holder.tvStatus.setText("?");
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.darkbrown));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onExtensionClick(ext, bill);
        });
    }

    @Override
    public int getItemCount() {
        return extensionList != null ? extensionList.size() : 0;
    }

    public static class ExtensionViewHolder extends RecyclerView.ViewHolder {
        TextView tvBillId, tvAmount, tvStatus, tvDueDate;
        public ExtensionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBillId = itemView.findViewById(R.id.tvBillName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
        }
    }

    // FORMAT SUPPORT
    private String formatMonthYear(String thangNam) {
        if (thangNam == null) return "Tháng ?";
        try {
            SimpleDateFormat src = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
            SimpleDateFormat dest = new SimpleDateFormat("'Tháng' MM/yyyy", Locale.getDefault());
            return dest.format(src.parse(thangNam));
        } catch (Exception e) {
            return "Tháng " + thangNam;
        }
    }
    private String formatDate(String isoDate) {
        if (isoDate == null) return "";
        try {
            String s = isoDate.split("T")[0];
            SimpleDateFormat src = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat dest = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return dest.format(src.parse(s));
        } catch (Exception e) {
            return isoDate;
        }
    }
    private String formatCurrency(double amount) {
        NumberFormat vnFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        return vnFormat.format(amount) + " đ";
    }
}
