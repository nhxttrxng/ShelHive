package com.nhom5.shelhive.ui.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout; // Import SwipeLayout
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.model.Motel;

import java.util.ArrayList;
import java.util.List;

public class MotelAdapter extends RecyclerView.Adapter<MotelAdapter.MotelViewHolder> {

    private List<Motel> motelList;
    private Context context;
    private OnItemClickListener itemClickListener;
    private OnActionClickListener actionClickListener;

    public interface OnItemClickListener {
        void onItemClick(int maDay);  // truyền ma_day
    }

    public interface OnActionClickListener {
        void onEditClick(Motel motel, int position);
        void onDeleteClick(int maDay); // Chỉ truyền maDay thay vì Motel
    }

    public MotelAdapter(Context context, List<Motel> motelList) {
        this.context = context;
        this.motelList = motelList != null ? motelList : new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnActionClickListener(OnActionClickListener listener) {
        this.actionClickListener = listener;
    }

    public void setFilteredList(List<Motel> filteredList) {
        this.motelList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_motel, parent, false);
        return new MotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MotelViewHolder holder, int position) {
        Motel motel = motelList.get(position);
        holder.nameTextView.setText(motel.getName());
        holder.addressTextView.setText(motel.getAddress());

        // Click vào item tổng thể
        holder.itemContainer.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(motel.getMaday());
            }
        });

        // Click nút sửa
        holder.btnEdit.setOnClickListener(v -> {
            if (actionClickListener != null) {
                actionClickListener.onEditClick(motel, position);
                holder.swipeLayout.close(true); // Đóng SwipeLayout sau khi click
            }
        });

        // Click nút xoá
        holder.btnDelete.setOnClickListener(v -> {
            if (actionClickListener != null) {
                // Truyền ma_day vào phương thức onDeleteClick
                actionClickListener.onDeleteClick(motel.getMaday());
                holder.swipeLayout.close(true); // Đóng SwipeLayout sau khi click
            }
        });
    }

    @Override
    public int getItemCount() {
        return motelList != null ? motelList.size() : 0;
    }

    public static class MotelViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView nameTextView, addressTextView;
        AppCompatButton btnEdit, btnDelete;
        View itemContainer; // THÊM DÒNG NÀY

        public MotelViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            nameTextView = itemView.findViewById(R.id.text_name);
            addressTextView = itemView.findViewById(R.id.text_address);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            itemContainer = itemView.findViewById(R.id.item_container); // THÊM DÒNG NÀY
        }
    }
}
