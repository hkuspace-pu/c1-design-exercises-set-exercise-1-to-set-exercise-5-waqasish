package com.example.restaurantmanagementapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private ArrayList<Reservation> list;
    private OnReservationActionListener listener;

    public interface OnReservationActionListener {
        void onDetailClick(Reservation reservation);
        void onEditClick(Reservation reservation);
        void onDeleteClick(Reservation reservation);
    }

    public ReservationAdapter(ArrayList<Reservation> list, OnReservationActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int p) {
        Reservation r = list.get(p);
        h.tvDateTime.setText(r.date + ", " + r.time);
        h.tvName.setText(r.name);
        h.tvTable.setText(r.table);

        h.tvDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDetailClick(r);
            }
        });

        h.tvEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(r);
            }
        });

        h.tvCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(r);
            }
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public void updateList(ArrayList<Reservation> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateTime, tvName, tvTable, tvDetail, tvEdit, tvCancel;
        
        public ViewHolder(@NonNull View v) {
            super(v);
            tvDateTime = v.findViewById(R.id.tvDateTime);
            tvName = v.findViewById(R.id.tvName);
            tvTable = v.findViewById(R.id.tvTable);
            tvDetail = v.findViewById(R.id.tvDetail);
            tvEdit = v.findViewById(R.id.tvEdit);
            tvCancel = v.findViewById(R.id.tvCancel);
        }
    }
}