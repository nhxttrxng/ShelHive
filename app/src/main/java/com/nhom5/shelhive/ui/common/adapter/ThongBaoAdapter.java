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

import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHolder> {
    private List<ThongBao> thongBaoList;
    private final Context context;
    private final ItemClickListener itemClickListener;

    // Interface để lắng nghe sự kiện click, trả về đối tượng ThongBao
    public interface ItemClickListener {
        void onItemClick(ThongBao thongBao);
    }

    public ThongBaoAdapter(List<ThongBao> thongBaoList, Context context, ItemClickListener itemClickListener) {
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
        ThongBao thongBao = thongBaoList.get(position);
        holder.textView.setText(thongBao.getNoiDung());

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(thongBao);
            }
        });
    }


    @Override
    public int getItemCount() {
        return thongBaoList.size();
    }

    public void capNhatDuLieu(List<ThongBao> newThongBaoList) {
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
