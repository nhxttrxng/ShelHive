package com.nhom5.shelhive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.GetRoomInfoResponse;
import com.nhom5.shelhive.ui.model.Room2;
import com.nhom5.shelhive.ui.model.User;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private Context context;
    private List<GetRoomInfoResponse> roomList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(GetRoomInfoResponse room);
    }

    public RoomAdapter(Context context, List<GetRoomInfoResponse> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<GetRoomInfoResponse> newList) {
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
        GetRoomInfoResponse roomInfo = roomList.get(position);
        Room2 room = roomInfo.getRoom();
        User user = roomInfo.getUser();

        // ✅ Lấy 2 chữ số cuối của mã phòng
        String maPhong = room.getMa_phong();
        String shortMaPhong;
        try {
            int phongSo = Integer.parseInt(maPhong);
            shortMaPhong = String.format("%02d", phongSo % 100); // lấy 2 số cuối
        } catch (NumberFormatException e) {
            shortMaPhong = maPhong; // fallback nếu không phải số
        }

        holder.tvRoomNumber.setText("Phòng " + shortMaPhong);

        if (user != null) {
            holder.tvTenantName.setText(user.getHoTen());
            holder.tvStatus.setText("Đã thuê");
            holder.tvBadge.setVisibility(View.VISIBLE);
            holder.tvUnpaidBills.setText("Chưa thanh toán: 1 hoá đơn"); // bạn có thể thay bằng dữ liệu thật
        } else {
            holder.tvTenantName.setText("Chưa có người thuê");
            holder.tvStatus.setText("Trống");
            holder.tvBadge.setVisibility(View.GONE);
            holder.tvUnpaidBills.setText("Không có hoá đơn");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(roomInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList != null ? roomList.size() : 0;
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomNumber, tvUnpaidBills, tvTenantName, tvStatus, tvBadge;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomNumber = itemView.findViewById(R.id.tv_room_number);
            tvUnpaidBills = itemView.findViewById(R.id.tv_unpaid_bills);
            tvTenantName = itemView.findViewById(R.id.tv_tenant_name);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvBadge = itemView.findViewById(R.id.tv_badge);
        }
    }
}
