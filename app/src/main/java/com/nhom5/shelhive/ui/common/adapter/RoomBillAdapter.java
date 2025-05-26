package com.nhom5.shelhive.ui.common.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.model.Room;

import java.util.List;

public class RoomBillAdapter extends RecyclerView.Adapter<RoomBillAdapter.RoomViewHolder> {

    private Context context;
    private List<Room> roomList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(Room room);
    }

    public RoomBillAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Room> newList) {
        this.roomList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);

        // Hiển thị mã phòng (2 số cuối)
        String shortMaPhong = room.getMa_phong();
        if (shortMaPhong.length() >= 2)
            shortMaPhong = shortMaPhong.substring(shortMaPhong.length() - 2);
        holder.tvRoomNumber.setText("Phòng " + shortMaPhong);

        // Reset về màu mặc định
        int colorDefault = ContextCompat.getColor(context, R.color.darkbrown);
        int colorRed = Color.parseColor("#D70000");
        int colorGreen = Color.parseColor("#2E8B57");

        if (room.getDa_thue() != null && room.getDa_thue()) {
            holder.tvTenantName.setText(
                    room.getTenNguoiThue() != null && !room.getTenNguoiThue().isEmpty()
                            ? room.getTenNguoiThue() : "Đã thuê"
            );
            holder.tvUnpaidBills.setVisibility(View.VISIBLE);
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvUnpaidBills.setText("Chưa thanh toán: " + room.getUnpaidBills() + " hóa đơn");
            holder.tvStatus.setText(room.getPayStatus() != null ? room.getPayStatus() : "");

            // Mặc định màu text
            int colorToSet = colorDefault;
            int colorImg = colorDefault;

            if ("Trễ hạn".equalsIgnoreCase(room.getPayStatus())) {
                colorToSet = colorRed;
                colorImg = colorRed;
            } else if ("Đã đóng".equalsIgnoreCase(room.getPayStatus())) {
                colorToSet = colorGreen;
                colorImg = colorGreen;
            }

            holder.tvRoomNumber.setTextColor(colorToSet);
            holder.tvTenantName.setTextColor(colorToSet);
            holder.tvUnpaidBills.setTextColor(colorToSet);
            holder.tvStatus.setTextColor(colorToSet);

            if (holder.imgRoom != null) {
                holder.imgRoom.setColorFilter(colorImg);
            }

            // Enable & click
            holder.itemView.setEnabled(true);
            holder.itemView.setAlpha(1.0f);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onClick(room);
            });
        } else {
            // Không có người thuê
            holder.tvTenantName.setText("Chưa có người thuê");
            holder.tvUnpaidBills.setVisibility(View.GONE);
            holder.tvStatus.setVisibility(View.GONE);

            // Set màu mặc định
            holder.tvRoomNumber.setTextColor(colorDefault);
            holder.tvTenantName.setTextColor(colorDefault);
            if (holder.imgRoom != null) {
                holder.imgRoom.setColorFilter(colorDefault);
            }

            // Disable click
            holder.itemView.setOnClickListener(null);
            holder.itemView.setEnabled(false);
            holder.itemView.setAlpha(0.6f); // mờ item cho khác biệt
        }
    }

    @Override
    public int getItemCount() {
        return roomList != null ? roomList.size() : 0;
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomNumber, tvUnpaidBills, tvTenantName, tvStatus;
        ImageView imgRoom;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomNumber = itemView.findViewById(R.id.tv_room_number);
            tvUnpaidBills = itemView.findViewById(R.id.tv_unpaid_bills);
            tvTenantName = itemView.findViewById(R.id.tv_tenant_name);
            tvStatus = itemView.findViewById(R.id.tv_status);
            imgRoom = itemView.findViewById(R.id.img_room);
        }
    }
}
