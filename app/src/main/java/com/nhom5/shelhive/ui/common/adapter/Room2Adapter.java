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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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

    public Room2Adapter(Context context, List<Room2> roomList, OnItemClickListener listener) {
        this.context = context;
        this.roomList = roomList;
        this.listener = listener;
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

        // Hiển thị "Phòng XX"
        String maPhong = room.getMa_phong();
        String soPhongDisplay = "Phòng " + (maPhong.length() >= 2 ? maPhong.substring(maPhong.length() - 2) : maPhong);
        holder.soPhongTextView.setText(soPhongDisplay);

        // Ngày hết hạn, format dd/MM/yyyy
        holder.ngayKetThucTextView.setText("Ngày hết hạn: " +
                (room.getNgay_ket_thuc() != null && !room.getNgay_ket_thuc().isEmpty()
                        ? formatDateWithTrim(room.getNgay_ket_thuc())
                        : "Chưa xác định"));

        // Giá thuê đã được format
        holder.giaThueTextView.setText("Giá thuê: " + formatGiaThue(room.getGia_thue()));

        // Mặc định màu
        int defaultColor = context.getResources().getColor(R.color.darkbrown);
        int greenColor = Color.parseColor("#388E3C");

        if (room.getDa_thue() != null && room.getDa_thue()
                && room.getEmail_user() != null && !room.getEmail_user().isEmpty()) {
            // Có người thuê: Gọi API lấy họ tên
            ApiService.apiService.getUserByEmail(room.getEmail_user()).enqueue(new Callback<GetUserResponse>() {
                @Override
                public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String hoTen = response.body().getHo_ten();
                        holder.trangThaiTextView.setText((hoTen != null ? hoTen : room.getEmail_user()));
                    } else {
                        holder.trangThaiTextView.setText("Đã thuê");
                    }
                }
                @Override
                public void onFailure(Call<GetUserResponse> call, Throwable t) {
                    holder.trangThaiTextView.setText("Đã thuê");
                }
            });

            // Đổi màu xanh lá cho trạng thái đã thuê
            holder.soPhongTextView.setTextColor(greenColor);
            holder.trangThaiTextView.setTextColor(greenColor);
            holder.giaThueTextView.setTextColor(greenColor);
            holder.ngayKetThucTextView.setTextColor(greenColor);
            holder.imgRoom.setColorFilter(greenColor);

        } else {
            // Không có người thuê
            holder.trangThaiTextView.setText("Trống");
            holder.soPhongTextView.setTextColor(defaultColor);
            holder.trangThaiTextView.setTextColor(defaultColor);
            holder.giaThueTextView.setTextColor(defaultColor);
            holder.ngayKetThucTextView.setTextColor(defaultColor);
            holder.imgRoom.setColorFilter(null);
        }

        // Xử lý click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(room);
            }
        });
    }

    // Hàm format giá thuê (1.000.000 đ)
    private String formatGiaThue(Double giaThue) {
        if (giaThue == null) return "0 đ";
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formatted = formatter.format(giaThue.longValue()).replace(',', '.');
        return formatted + " đ";
    }

    // Hàm format ngày yyyy-MM-dd hoặc yyyy-MM-ddT... về dd/MM/yyyy
    private String formatDateWithTrim(String dateInput) {
        if (dateInput == null || dateInput.isEmpty()) return "";
        String isoDate = dateInput;
        int tIdx = isoDate.indexOf('T');
        if (tIdx > 0) {
            isoDate = isoDate.substring(0, tIdx);
        }
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat sdfOutput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdfOutput.format(sdfInput.parse(isoDate));
        } catch (ParseException e) {
            return isoDate;
        }
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
