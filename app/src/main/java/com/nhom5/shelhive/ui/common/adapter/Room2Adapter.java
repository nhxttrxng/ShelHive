package com.nhom5.shelhive.ui.common.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.api.ApiService;
import com.nhom5.shelhive.api.GetUserResponse;
import com.nhom5.shelhive.ui.model.Room2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Room2Adapter extends RecyclerView.Adapter<Room2Adapter.Room2ViewHolder> {

    private Context context;
    private List<Room2> roomList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Room2 room);
    }

    public Room2Adapter(Context context, List<Room2> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<Room2> newList) {
        this.roomList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Room2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room2, parent, false);
        return new Room2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Room2ViewHolder holder, int position) {
        Room2 room = roomList.get(position);

        // Hiển thị "Phòng XX" – 2 số cuối của mã phòng
        String maPhong = room.getMa_phong();
        String soPhongDisplay = "Phòng " + (maPhong.length() >= 2 ? maPhong.substring(maPhong.length() - 2) : maPhong);
        holder.soPhongTextView.setText(soPhongDisplay);

        // Ngày kết thúc
        holder.ngayKetThucTextView.setText("Ngày hết hạn: " +
                (room.getNgay_ket_thuc() != null ? room.getNgay_ket_thuc() : "Chưa xác định"));

        // Giá thuê
        holder.giaThueTextView.setText("Giá thuê: " +
                (room.getGia_thue() != null ? room.getGia_thue() + " đ" : "0 đ"));

        // Mặc định màu
        int defaultColor = context.getResources().getColor(R.color.darkbrown);
        int greenColor = Color.parseColor("#388E3C"); // màu xanh lá đậm

        if (room.getEmail_user() != null && !room.getEmail_user().isEmpty()) {
            // Gọi API để lấy tên người dùng
            ApiService.apiService.getUserByEmail(room.getEmail_user()).enqueue(new Callback<GetUserResponse>() {
                @Override
                public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        holder.trangThaiTextView.setText(response.body().getHo_ten());
                    } else {
                        holder.trangThaiTextView.setText(room.getTrang_thai_phong());
                    }
                }

                @Override
                public void onFailure(Call<GetUserResponse> call, Throwable t) {
                    holder.trangThaiTextView.setText(room.getTrang_thai_phong());
                }
            });

            // Đổi màu các text & icon nếu có người thuê
            holder.soPhongTextView.setTextColor(greenColor);
            holder.trangThaiTextView.setTextColor(greenColor);
            holder.giaThueTextView.setTextColor(greenColor);
            holder.ngayKetThucTextView.setTextColor(greenColor);
            holder.imgRoom.setColorFilter(greenColor);
        } else {
            // Không có người thuê
            holder.trangThaiTextView.setText(room.getTrang_thai_phong());
            holder.soPhongTextView.setTextColor(defaultColor);
            holder.trangThaiTextView.setTextColor(defaultColor);
            holder.giaThueTextView.setTextColor(defaultColor);
            holder.ngayKetThucTextView.setTextColor(defaultColor);
            holder.imgRoom.setColorFilter(null); // Không áp tint
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(room);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList != null ? roomList.size() : 0;
    }

    public static class Room2ViewHolder extends RecyclerView.ViewHolder {
        TextView soPhongTextView, ngayKetThucTextView, trangThaiTextView, giaThueTextView;
        ImageView imgRoom;

        public Room2ViewHolder(@NonNull View itemView) {
            super(itemView);
            soPhongTextView = itemView.findViewById(R.id.so_phong);
            ngayKetThucTextView = itemView.findViewById(R.id.ngay_ket_thuc);
            trangThaiTextView = itemView.findViewById(R.id.trang_thai);
            giaThueTextView = itemView.findViewById(R.id.gia_thue);
            imgRoom = itemView.findViewById(R.id.img_room);
        }
    }
}
