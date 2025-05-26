package com.nhom5.shelhive.ui.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ThongBaoDonGian;

import java.util.List;

public class ThongBaoDonGianAdapter extends RecyclerView.Adapter<ThongBaoDonGianAdapter.ThongBaoViewHolder> {

    private List<ThongBaoDonGian> danhSach;
    private Context context;
    private OnThongBaoClickListener listener;

    public interface OnThongBaoClickListener {
        void onClick(ThongBaoDonGian thongBao);
    }

    public ThongBaoDonGianAdapter(List<ThongBaoDonGian> danhSach, Context context, OnThongBaoClickListener listener) {
        this.danhSach = danhSach;
        this.context = context;
        this.listener = listener;
    }

    public void capNhatDuLieu(List<ThongBaoDonGian> danhSachMoi) {
        this.danhSach = danhSachMoi;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ThongBaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thong_bao, parent, false);
        return new ThongBaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoViewHolder holder, int position) {
        ThongBaoDonGian thongBao = danhSach.get(position);
        holder.noiDungText.setText(thongBao.getNoi_dung());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(thongBao);
        });
    }

    @Override
    public int getItemCount() {
        return danhSach.size();
    }

    static class ThongBaoViewHolder extends RecyclerView.ViewHolder {
        TextView noiDungText;

        public ThongBaoViewHolder(@NonNull View itemView) {
            super(itemView);
            noiDungText = itemView.findViewById(R.id.text_thong_bao);
        }
    }
}
