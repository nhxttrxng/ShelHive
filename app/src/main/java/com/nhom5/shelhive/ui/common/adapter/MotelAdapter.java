package com.nhom5.shelhive.ui.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.model.Motel;

import java.util.ArrayList;
import java.util.List;

public class MotelAdapter extends RecyclerView.Adapter<MotelAdapter.MotelViewHolder> {

    private List<Motel> motelList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Motel motel, int position);
    }

    public MotelAdapter(Context context, List<Motel> motelList) {
        this.context = context;
        this.motelList = motelList != null ? motelList : new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_motel, parent, false);
        return new MotelViewHolder(view);
    }
    public void setFilteredList(List<Motel> filteredList) {
        this.motelList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MotelViewHolder holder, int position) {
        Motel motel = motelList.get(position);
        holder.nameTextView.setText(motel.getName());
        holder.addressTextView.setText(motel.getAddress());

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(motel, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return motelList != null ? motelList.size() : 0;
    }

    public static class MotelViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, addressTextView;

        public MotelViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_name);
            addressTextView = itemView.findViewById(R.id.text_address);
        }
    }
}
