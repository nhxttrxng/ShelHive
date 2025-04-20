package com.nhom5.shelhive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.model.Motel;

import java.util.List;

public class MotelAdapter extends RecyclerView.Adapter<MotelAdapter.MotelViewHolder> {

    private List<Motel> motelList;
    private Context context;

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
        holder.nameTextView.setText(motel.getName());
        holder.addressTextView.setText(motel.getAddress());

        int badgeCount = motel.getBadgeCount();

        if (badgeCount > 0) {
            holder.badgeContainer.setVisibility(View.VISIBLE);
            holder.badgeTextView.setText(String.valueOf(badgeCount));
        } else {
            holder.badgeContainer.setVisibility(View.GONE); // hoặc INVISIBLE nếu muốn giữ chỗ
        }
    }

    @Override
    public int getItemCount() {
        return motelList.size();
    }

    public static class MotelViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, addressTextView, badgeTextView;
        FrameLayout badgeContainer;

        public MotelViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_name);
            addressTextView = itemView.findViewById(R.id.text_address);
            badgeTextView = itemView.findViewById(R.id.text_badge);
            badgeContainer = itemView.findViewById(R.id.badge_container);
        }
    }
}
