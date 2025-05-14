package com.nhom5.shelhive.ui.common.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private Context context;
    private List<Room> roomList;
    private OnRoomClickListener listener;

    public interface OnRoomClickListener {
        void onRoomClick(Room room, int position);
    }

    public RoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList != null ? roomList : new ArrayList<>();
    }

    public void setOnRoomClickListener(OnRoomClickListener listener) {
        this.listener = listener;
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

        // Thiết lập số phòng
        holder.tvRoomNumber.setText("Phòng " + room.getRoomNumber());

        // Thiết lập thông tin hóa đơn chưa thanh toán
        holder.tvUnpaidBills.setText("Chưa thanh toán: " + room.getUnpaidBills() + " hoá đơn");

        // Thiết lập tên người thuê
        holder.tvTenantName.setText(room.getTenantName());

        // Thiết lập trạng thái
        holder.tvStatus.setText(room.getStatus().getLabel());

        // Thiết lập màu sắc dựa trên trạng thái
        int color = Color.parseColor(room.getStatus().getColor());
        holder.tvRoomNumber.setTextColor(color);
        holder.tvUnpaidBills.setTextColor(color);
        holder.tvTenantName.setTextColor(color);
        holder.tvStatus.setTextColor(color);

        // Thiết lập icon phòng
        int iconResId = 0;
        switch(room.getStatus().getIconName()) {
            case "ic_room_red":
                iconResId = R.drawable.ic_room_red;
                break;
            case "ic_room_brown":
                iconResId = R.drawable.ic_room_brown;
                break;
            case "ic_room_green":
                iconResId = R.drawable.ic_room_green;
                break;
        }
        if (iconResId != 0) {
            holder.imgRoom.setImageResource(iconResId);
        }

        // Hiển thị badge nếu có hóa đơn trễ hạn
        if (room.getOverdueCount() > 0) {
            holder.tvBadge.setVisibility(View.VISIBLE);
            holder.tvBadge.setText(String.valueOf(room.getOverdueCount()));
        } else {
            holder.tvBadge.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRoomClick(room, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList != null ? roomList.size() : 0;
    }

    public void updateRoomList(List<Room> newRoomList) {
        this.roomList = newRoomList != null ? newRoomList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomNumber, tvUnpaidBills, tvTenantName, tvStatus, tvBadge;
        ImageView imgRoom;
        LinearLayout statusContainer;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRoom = itemView.findViewById(R.id.img_room);
            tvRoomNumber = itemView.findViewById(R.id.tv_room_number);
            tvUnpaidBills = itemView.findViewById(R.id.tv_unpaid_bills);
            tvTenantName = itemView.findViewById(R.id.tv_tenant_name);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvBadge = itemView.findViewById(R.id.tv_badge);
            statusContainer = itemView.findViewById(R.id.status_container);
        }
    }
}