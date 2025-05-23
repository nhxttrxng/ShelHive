package com.nhom5.shelhive.ui.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;

import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHolder> {
    private List<String> thongBaoList;
    private final Context context;
    private final ItemClickListener itemClickListener;

    // Interface để lắng nghe sự kiện click
    public interface ItemClickListener {
        void onItemClick(String noiDung);
    }

    public ThongBaoAdapter(List<String> thongBaoList, Context context, ItemClickListener itemClickListener) {
        this.thongBaoList = thongBaoList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thong_bao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String thongBao = thongBaoList.get(position);
        holder.textView.setText(thongBao);

        // Xử lý sự kiện click trên item
        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(thongBao));
    }

    @Override
    public int getItemCount() {
        return thongBaoList.size();
    }

    public void capNhatDuLieu(List<String> newThongBaoList) {
        this.thongBaoList = newThongBaoList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_thong_bao);
        }
    }
}
