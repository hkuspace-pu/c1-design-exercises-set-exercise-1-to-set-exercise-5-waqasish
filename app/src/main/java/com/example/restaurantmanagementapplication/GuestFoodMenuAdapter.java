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

public class GuestFoodMenuAdapter extends RecyclerView.Adapter<GuestFoodMenuAdapter.ViewHolder> {

    private ArrayList<FoodItem> list;

    public GuestFoodMenuAdapter(ArrayList<FoodItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_guest_food_menu, parent, false);
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
    }

    @Override
    public int getItemCount() { 
        return list.size(); 
    }

    public void updateList(ArrayList<FoodItem> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvPrice;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivFoodImage);
            tvName = itemView.findViewById(R.id.tvFoodName);
            tvDesc = itemView.findViewById(R.id.tvFoodDescription);
            tvPrice = itemView.findViewById(R.id.tvFoodPrice);
        }
    }
}

