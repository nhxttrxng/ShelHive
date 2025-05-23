package com.nhom5.shelhive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.model.Motel;

import java.util.List;

public class MotelAdapter extends RecyclerView.Adapter<MotelAdapter.MotelViewHolder> {

    private Context context;
    private List<Motel> motelList;
    private OnItemClickListener itemClickListener;
    private OnItemActionListener actionListener;

    public interface OnItemClickListener {
        void onItemClick(int maDay);
    }

    public interface OnItemActionListener {
        void onEdit(Motel motel);
        void onDelete(Motel motel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.actionListener = listener;
    }

    public MotelAdapter(Context context, List<Motel> motelList) {
        this.context = context;
        this.motelList = motelList;
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

        holder.textName.setText(motel.getName());
        holder.textAddress.setText("Địa chỉ: " + motel.getAddress());

        // Thiết lập sự kiện click vào item (hiển thị danh sách phòng)
        holder.itemContainer.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(motel.getMaday());
            }
        });

        // Thiết lập nút Sửa
        holder.btnEdit.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEdit(motel);
            } else {
                Toast.makeText(context, "Edit clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Thiết lập nút Xoá
        holder.btnDelete.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDelete(motel);
            } else {
                Toast.makeText(context, "Delete clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Đảm bảo swipe layout được đóng lại nếu cần reset
        holder.swipeLayout.close();
    }

    @Override
    public int getItemCount() {
        return motelList.size();
    }

    public static class MotelViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textAddress;
        View btnEdit, btnDelete, itemContainer;
        SwipeLayout swipeLayout;

        public MotelViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            textName = itemView.findViewById(R.id.text_name);
            textAddress = itemView.findViewById(R.id.text_address);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            itemContainer = itemView.findViewById(R.id.item_container);
        }
    }
}
