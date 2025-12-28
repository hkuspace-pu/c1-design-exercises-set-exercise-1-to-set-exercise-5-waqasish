package com.example.restaurantmanagementapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class GuestFoodMenuActivity extends AppCompatActivity {

    private RecyclerView rvGuestMenuItems;
    private DatabaseHelper dbHelper;
    private GuestFoodMenuAdapter adapter;
    private ArrayList<FoodItem> foodItems;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_food_menu);

        // Get user email from intent (if passed)
        userEmail = getIntent().getStringExtra("user_email");

        rvGuestMenuItems = findViewById(R.id.rvGuestMenuItems);
        dbHelper = new DatabaseHelper(this);

        rvGuestMenuItems.setLayoutManager(new GridLayoutManager(this, 2));
        loadFoodItems();

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_food_menu);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_food_menu) {
                // Already on this screen
                return true;
            }
            if (id == R.id.nav_make_reservation) {
                Intent intent = new Intent(this, GuestMakeReservationActivity.class);
                if (userEmail != null) {
                    intent.putExtra("user_email", userEmail);
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            if (id == R.id.nav_manage_reservation) {
                Intent intent = new Intent(this, GuestHomeActivity.class);
                if (userEmail != null) {
                    intent.putExtra("user_email", userEmail);
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            if (id == R.id.nav_setting) {
                // TODO: Navigate to Settings screen
                android.widget.Toast.makeText(this, "Settings - Coming soon", android.widget.Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });
    }

    private void loadFoodItems() {
        // Use the same database method as staff - they share the same data
        foodItems = dbHelper.getAllFoodItems();
        adapter = new GuestFoodMenuAdapter(foodItems);
        rvGuestMenuItems.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload to get any updates from staff
        loadFoodItems();
    }
}

