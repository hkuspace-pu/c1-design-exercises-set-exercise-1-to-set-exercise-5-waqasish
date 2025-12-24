package com.example.restaurantmanagementapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class GuestReservationTableAdapter extends RecyclerView.Adapter<GuestReservationTableAdapter.ViewHolder> {

    private ArrayList<Reservation> list;

    public GuestReservationTableAdapter(ArrayList<Reservation> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_guest_reservation_table, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int p) {
        Reservation r = list.get(p);
        
        // Parse dateTime to extract date and time separately
        String[] parts = r.dateTime.split(", ");
        if (parts.length >= 2) {
            // Date is first part (e.g., "16 Nov, 2025")
            h.tvDate.setText(parts[0] + ", " + parts[1]);
            // Time is last part (e.g., "7:30 PM")
            h.tvTime.setText(parts[parts.length - 1]);
        } else {
            h.tvDate.setText(r.dateTime);
            h.tvTime.setText("");
        }
        
        h.tvTableNo.setText(r.table);
    }

    @Override
    public int getItemCount() { 
        return list.size(); 
    }

    public void updateList(ArrayList<Reservation> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvTableNo;
        
        public ViewHolder(@NonNull View v) {
            super(v);
            tvDate = v.findViewById(R.id.tvDate);
            tvTime = v.findViewById(R.id.tvTime);
            tvTableNo = v.findViewById(R.id.tvTableNo);
        }
    }
}

