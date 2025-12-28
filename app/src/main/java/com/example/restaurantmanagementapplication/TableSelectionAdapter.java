package com.example.restaurantmanagementapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;

public class TableSelectionAdapter extends RecyclerView.Adapter<TableSelectionAdapter.ViewHolder> {

    private ArrayList<TableItem> tables;
    private OnTableSelectedListener listener;
    private int selectedTable = -1;

    public interface OnTableSelectedListener {
        void onTableSelected(int tableNumber);
    }

    public TableSelectionAdapter(ArrayList<TableItem> tables, OnTableSelectedListener listener) {
        this.tables = tables;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_table_selection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TableItem table = tables.get(position);
        holder.tvTableNumber.setText(String.valueOf(table.tableNumber));
        
        if (table.isAvailable) {
            holder.tvTableStatus.setText("Available");
            holder.tvTableStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.darker_gray));
            holder.cardTable.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.white));
            holder.cardTable.setStrokeColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.darker_gray));
        } else {
            holder.tvTableStatus.setText("Booked");
            holder.tvTableStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_dark));
            holder.cardTable.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.darker_gray));
            holder.cardTable.setStrokeColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_dark));
        }

        // Highlight selected table
        if (selectedTable == table.tableNumber) {
            holder.cardTable.setStrokeColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.orange_main));
            holder.cardTable.setStrokeWidth(4);
            holder.cardTable.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.light_orange));
        } else {
            if (table.isAvailable) {
                holder.cardTable.setStrokeWidth(2);
            }
        }

        holder.cardTable.setOnClickListener(v -> {
            if (table.isAvailable) {
                selectedTable = table.tableNumber;
                notifyDataSetChanged();
                if (listener != null) {
                    listener.onTableSelected(table.tableNumber);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public void updateTables(ArrayList<TableItem> newTables) {
        this.tables = newTables;
        notifyDataSetChanged();
    }

    public void setSelectedTable(int tableNumber) {
        this.selectedTable = tableNumber;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardTable;
        TextView tvTableNumber, tvTableStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTable = itemView.findViewById(R.id.cardTable);
            tvTableNumber = itemView.findViewById(R.id.tvTableNumber);
            tvTableStatus = itemView.findViewById(R.id.tvTableStatus);
        }
    }
}

