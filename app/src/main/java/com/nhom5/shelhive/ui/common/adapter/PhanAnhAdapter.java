package com.nhom5.shelhive.ui.common.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.model.PhanAnh;

import java.util.List;

public class PhanAnhAdapter extends RecyclerView.Adapter<PhanAnhAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PhanAnh phanAnh);
    }

    private final List<PhanAnh> list;
    private OnItemClickListener listener;

    public PhanAnhAdapter(List<PhanAnh> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhanAnhAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_phan_anh, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhanAnhAdapter.ViewHolder holder, int position) {

        PhanAnh item = list.get(position);
        holder.tvMaPhong.setText("Phòng: " + item.getMaPhong());
        holder.tvTieuDe.setText("Tiêu đề: " + item.getTieuDe());
        holder.tvTinhTrang.setText("Tình trạng: " + item.getTinhTrang());
        Log.d("AdapterDebug", "TinhTrang: " + item.getTinhTrang());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaPhong, tvTieuDe, tvTinhTrang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaPhong = itemView.findViewById(R.id.tv_room);
            tvTieuDe = itemView.findViewById(R.id.tv_content);
            tvTinhTrang = itemView.findViewById(R.id.tv_status);
        }
    }
}
