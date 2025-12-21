package com.example.restaurantmanagementapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ReservationHomeAdapter extends RecyclerView.Adapter<ReservationHomeAdapter.ViewHolder> {

    private ArrayList<Reservation> list;

    public ReservationHomeAdapter(ArrayList<Reservation> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation_home, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int p) {
        Reservation r = list.get(p);
        h.tvName.setText(r.name);
        h.tvTable.setText(r.table);
        
        // Extract time from dateTime string (format: "16 Nov, 2025, 7:30 PM")
        String[] parts = r.dateTime.split(", ");
        if (parts.length >= 2) {
            h.tvTime.setText(parts[parts.length - 1]); // Get the last part which is the time
        } else {
            h.tvTime.setText(r.dateTime);
        }
    }

    @Override
    public int getItemCount() { 
        return list.size(); 
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvTable;
        
        public ViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvTime = v.findViewById(R.id.tvTime);
            tvTable = v.findViewById(R.id.tvTable);
        }
    }
}

