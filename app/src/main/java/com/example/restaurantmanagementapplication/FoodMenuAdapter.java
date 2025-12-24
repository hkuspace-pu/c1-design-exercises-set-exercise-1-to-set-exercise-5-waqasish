package com.example.restaurantmanagementapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.ViewHolder> {

    private ArrayList<FoodItem> list;
    private OnFoodItemActionListener listener;

    public interface OnFoodItemActionListener {
        void onEditClick(FoodItem item);
        void onDeleteClick(FoodItem item);
    }

    public FoodMenuAdapter(ArrayList<FoodItem> list, OnFoodItemActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem item = list.get(position);
        holder.tvName.setText(item.name);
        holder.tvDesc.setText(item.description);
        holder.tvPrice.setText("HK$ " + item.priceHKD);

        // Load image - check if it's a file path or drawable name
        if (item.imageName != null && !item.imageName.isEmpty()) {
            if (item.imageName.startsWith("/")) {
                // It's a file path
                File imageFile = new File(item.imageName);
                if (imageFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    if (bitmap != null) {
                        holder.ivImage.setImageBitmap(bitmap);
                    } else {
                        holder.ivImage.setImageResource(R.drawable.placeholder_food);
                    }
                } else {
                    holder.ivImage.setImageResource(R.drawable.placeholder_food);
                }
            } else {
                // It's a drawable name
                int imageRes = holder.itemView.getContext().getResources()
                        .getIdentifier(item.imageName, "drawable", holder.itemView.getContext().getPackageName());
                if (imageRes != 0) {
                    holder.ivImage.setImageResource(imageRes);
                } else {
                    holder.ivImage.setImageResource(R.drawable.placeholder_food);
                }
            }
        } else {
            holder.ivImage.setImageResource(R.drawable.placeholder_food);
        }

        holder.tvEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(item);
            }
        });

        holder.tvDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(item);
            }
        });
    }

    public void updateList(ArrayList<FoodItem> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvPrice, tvEdit, tvDelete;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivFoodImage);
            tvName = itemView.findViewById(R.id.tvFoodName);
            tvDesc = itemView.findViewById(R.id.tvFoodDescription);
            tvPrice = itemView.findViewById(R.id.tvFoodPrice);
            tvEdit = itemView.findViewById(R.id.tvEdit);
            tvDelete = itemView.findViewById(R.id.tvDelete);
        }
    }
}