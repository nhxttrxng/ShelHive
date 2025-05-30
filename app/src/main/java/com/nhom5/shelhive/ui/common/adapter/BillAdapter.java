package com.nhom5.shelhive.ui.common.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.model.Bill;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private Context context;
    private List<Bill> billList;
    private OnBillClickListener listener;

    public interface OnBillClickListener {
        void onBillClick(Bill bill);
    }

    public BillAdapter(Context context, OnBillClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.billList = new ArrayList<>();
    }

    public void setBills(List<Bill> bills) {
        this.billList = bills;
        notifyDataSetChanged();
    }

    // ------------------- BỔ SUNG: Filter theo tháng năm -----------------
    // Trả về list Bill đã filter theo tháng/năm (yyyy-MM)
    public List<Bill> filterByMonthYear(int month, int year) {
        List<Bill> filtered = new ArrayList<>();
        if (billList == null) return filtered;
        for (Bill bill : billList) {
            String billMonthYear = bill.getBillMonthYear(); // yyyy-MM
            if (billMonthYear != null && billMonthYear.length() >= 7) {
                try {
                    String[] arr = billMonthYear.split("-");
                    int billYear = Integer.parseInt(arr[0]);
                    int billMonth = Integer.parseInt(arr[1]);
                    if (billMonth == month && billYear == year) {
                        filtered.add(bill);
                    }
                } catch (Exception ignored) {}
            }
        }
        return filtered;
    }
    // --------------------------------------------------------------------

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = billList.get(position);

        // Hiển thị bill name: "Tháng MM/yyyy"
        holder.tvBillId.setText(formatMonthYear(bill.getBillMonthYear()));

        // Format số tiền
        holder.tvAmount.setText(formatCurrency(bill.getAmount()));

        // Nếu có extendedDueDate thì hiển thị extended, không thì dueDate
        String extendedDueDate = bill.getExtendedDueDate();
        String displayDueDate = (extendedDueDate != null && !extendedDueDate.trim().isEmpty())
                ? formatDate(extendedDueDate)
                : formatDate(bill.getDueDate());
        holder.tvDueDate.setText(displayDueDate);

        // Đổi màu chỉ cho trạng thái
        String status = bill.getStatus();
        holder.tvStatus.setText(status);

        if ("Trễ hạn".equalsIgnoreCase(status)) {
            holder.tvStatus.setTextColor(Color.RED);
        } else if ("Đã thanh toán".equalsIgnoreCase(status)) {
            holder.tvStatus.setTextColor(Color.parseColor("#388E3C"));
        } else {
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.darkbrown));
        }

        // Sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBillClick(bill);
            }
        });
    }

    @Override
    public int getItemCount() {
        return billList != null ? billList.size() : 0;
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tvBillId, tvAmount, tvStatus, tvDueDate;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBillId = itemView.findViewById(R.id.tvBillName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
        }
    }

    // ======== Các hàm tiện ích format ============

    // Format "yyyy-MM" thành "Tháng MM/yyyy"
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

    // Format ISO date (yyyy-MM-ddTHH:mm:ss) -> dd/MM/yyyy
    private String formatDate(String dateStr) {
        if (dateStr == null) return "";
        try {
            // Cắt lấy phần yyyy-MM-dd
            String day = dateStr.split("T")[0];
            SimpleDateFormat src = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat dest = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return dest.format(src.parse(day));
        } catch (Exception e) {
            return dateStr;
        }
    }

    // Format số tiền về dạng 1.000.000 đ
    private String formatCurrency(double amount) {
        NumberFormat vnFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        return vnFormat.format(amount) + " đ";
    }

}
