package com.example.restaurantmanagementapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;

public class FoodMenuManagementActivity extends AppCompatActivity implements FoodMenuAdapter.OnFoodItemActionListener {

    private RecyclerView rvMenuItems;
    private FloatingActionButton fabAddItem;
    private DatabaseHelper dbHelper;
    private FoodMenuAdapter adapter;
    private ArrayList<FoodItem> foodItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu_management);

        rvMenuItems = findViewById(R.id.rvMenuItems);
        fabAddItem = findViewById(R.id.fabAddItem);
        dbHelper = new DatabaseHelper(this);

        rvMenuItems.setLayoutManager(new GridLayoutManager(this, 2));
        loadFoodItems();

        fabAddItem.setOnClickListener(v -> showAddFoodItemDialog(null));

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_food);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new android.content.Intent(this, StaffHomeActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            if (id == R.id.nav_food) {
                return true;
            }
            if (id == R.id.nav_reservations) {
                startActivity(new android.content.Intent(this, ReservationManagementActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            if (id == R.id.nav_settings) {
                startActivity(new android.content.Intent(this, SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }

            return false;
        });
    }

    private void loadFoodItems() {
        foodItems = dbHelper.getAllFoodItems();
        adapter = new FoodMenuAdapter(foodItems, this);
        rvMenuItems.setAdapter(adapter);
    }

    private void showAddFoodItemDialog(FoodItem foodItem) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_food_item, null);
        
        TextInputEditText etName = dialogView.findViewById(R.id.etFoodName);
        TextInputEditText etDescription = dialogView.findViewById(R.id.etFoodDescription);
        TextInputEditText etImageName = dialogView.findViewById(R.id.etFoodImageName);
        TextInputEditText etPrice = dialogView.findViewById(R.id.etFoodPrice);
        
        TextInputLayout tilName = dialogView.findViewById(R.id.tilFoodName);
        TextInputLayout tilDescription = dialogView.findViewById(R.id.tilFoodDescription);
        TextInputLayout tilImageName = dialogView.findViewById(R.id.tilFoodImageName);
        TextInputLayout tilPrice = dialogView.findViewById(R.id.tilFoodPrice);

        boolean isEdit = foodItem != null;
        String dialogTitle = isEdit ? "Edit Food Item" : "Add Food Item";
        
        if (isEdit) {
            etName.setText(foodItem.name);
            etDescription.setText(foodItem.description);
            etImageName.setText(foodItem.imageName);
            etPrice.setText(String.valueOf(foodItem.priceHKD));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle(dialogTitle);

        builder.setPositiveButton("Save", (d, which) -> {
            String name = etName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String imageName = etImageName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();

            // Validate inputs
            boolean isValid = true;
            if (name.isEmpty()) {
                tilName.setError("Food name is required");
                isValid = false;
            } else {
                tilName.setError(null);
            }

            if (description.isEmpty()) {
                tilDescription.setError("Description is required");
                isValid = false;
            } else {
                tilDescription.setError(null);
            }

            if (imageName.isEmpty()) {
                tilImageName.setError("Image name is required");
                isValid = false;
            } else {
                tilImageName.setError(null);
            }

            int price = 0;
            if (priceStr.isEmpty()) {
                tilPrice.setError("Price is required");
                isValid = false;
            } else {
                try {
                    price = Integer.parseInt(priceStr);
                    if (price <= 0) {
                        tilPrice.setError("Price must be greater than 0");
                        isValid = false;
                    } else {
                        tilPrice.setError(null);
                    }
                } catch (NumberFormatException e) {
                    tilPrice.setError("Invalid price");
                    isValid = false;
                }
            }

            if (isValid) {
                if (isEdit) {
                    if (dbHelper.updateFoodItem(foodItem.id, name, description, imageName, price)) {
                        Toast.makeText(this, "Food item updated successfully", Toast.LENGTH_SHORT).show();
                        loadFoodItems();
                    } else {
                        Toast.makeText(this, "Failed to update food item", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (dbHelper.addFoodItem(name, description, imageName, price) > 0) {
                        Toast.makeText(this, "Food item added successfully", Toast.LENGTH_SHORT).show();
                        loadFoodItems();
                    } else {
                        Toast.makeText(this, "Failed to add food item", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // Show dialog again with errors
                d.dismiss();
                showAddFoodItemDialog(foodItem);
            }
        });

        builder.setNegativeButton("Cancel", (d, which) -> d.dismiss());
        builder.create().show();
    }

    @Override
    public void onEditClick(FoodItem item) {
        showAddFoodItemDialog(item);
    }

    @Override
    public void onDeleteClick(FoodItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Food Item")
                .setMessage("Are you sure you want to delete " + item.name + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (dbHelper.deleteFoodItem(item.id)) {
                        Toast.makeText(this, "Food item deleted", Toast.LENGTH_SHORT).show();
                        loadFoodItems();
                    } else {
                        Toast.makeText(this, "Failed to delete food item", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFoodItems();
    }
}
