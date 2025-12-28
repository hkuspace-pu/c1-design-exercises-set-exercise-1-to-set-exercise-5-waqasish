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
    private OnReservationActionListener listener;

    public interface OnReservationActionListener {
        void onEditClick(Reservation reservation);
        void onCancelClick(Reservation reservation);
    }

    public GuestReservationTableAdapter(ArrayList<Reservation> list, OnReservationActionListener listener) {
        this.list = list;
        this.listener = listener;
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
        
        // Use separate date and time fields
        h.tvDate.setText(r.date);
        h.tvTime.setText(r.time);
        h.tvTableNo.setText(r.table);

        h.tvEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(r);
            }
        });

        h.tvCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancelClick(r);
            }
        });
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
        TextView tvDate, tvTime, tvTableNo, tvEdit, tvCancel;
        
        public ViewHolder(@NonNull View v) {
            super(v);
            tvDate = v.findViewById(R.id.tvDate);
            tvTime = v.findViewById(R.id.tvTime);
            tvTableNo = v.findViewById(R.id.tvTableNo);
            tvEdit = v.findViewById(R.id.tvEditGuestReservation);
            tvCancel = v.findViewById(R.id.tvCancelGuestReservation);
        }
    }
}

