package com.nhom5.shelhive.ui.common.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.admin.thongbao.Admin_XoaThongBaoActivity;
import com.nhom5.shelhive.ui.model.ThongBao;
import com.nhom5.shelhive.ui.model.ThongBaoHoaDon;

import java.util.List;

public class ThongBaoHDAdapter extends RecyclerView.Adapter<ThongBaoHDAdapter.ViewHolder> {
    private List<ThongBaoHoaDon> thongBaoHoaDonList;
    private final Context context;
    private final ItemClickListener itemClickListener;

    // Interface để lắng nghe sự kiện click, trả về đối tượng ThongBao
    public interface ItemClickListener {
        void onItemClick(ThongBaoHoaDon thongBao);
    }

    public ThongBaoHDAdapter(List<ThongBaoHoaDon> thongBaoList, Context context, ItemClickListener itemClickListener) {
        this.thongBaoHoaDonList = thongBaoList;
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
        ThongBaoHoaDon thongBao = thongBaoHoaDonList.get(position);
        holder.textView.setText(thongBao.getNoi_dung());

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(thongBao);
            }
        });
    }


    @Override
    public int getItemCount() {
        return thongBaoHoaDonList.size();
    }

    public void capNhatDuLieu(List<ThongBaoHoaDon> newThongBaoList) {
        this.thongBaoHoaDonList = newThongBaoList;
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
