package com.example.restaurantmanagementapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class FoodMenuManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu_management);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_food);


        RecyclerView rv = findViewById(R.id.rvMenuItems);
        rv.setLayoutManager(new GridLayoutManager(this, 2));


        ArrayList<FoodItem> foodList = new ArrayList<>();
        foodList.add(new FoodItem("Grilled Salmon", "Fresh Atlantic salmon with lemon butter & herbs", "salmon", 268));
        foodList.add(new FoodItem("Wagyu Beef Burger", "A5 Japanese wagyu patty, truffle mayo", "wagyu_burger", 388));
        foodList.add(new FoodItem("Lobster Bisque", "Rich creamy soup with fresh lobster", "lobster_bisque", 198));
        foodList.add(new FoodItem("Truffle Pasta", "Handmade tagliatelle with black truffle", "truffle_pasta", 298));
        foodList.add(new FoodItem("Tiramisu", "Classic Italian dessert", "tiramisu", 98));
        foodList.add(new FoodItem("Caesar Salad", "Romaine lettuce, parmesan", "caesar_salad", 128));
        foodList.add(new FoodItem("Ribeye Steak 300g", "Australian grain-fed with pepper sauce", "ribeye_steak", 488));
        foodList.add(new FoodItem("Sashimi Platter", "Fresh tuna, salmon, hamachi", "sashimi", 458));

        rv.setAdapter(new FoodMenuAdapter(foodList));


        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, StaffHomeActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            if (id == R.id.nav_food) {
                startActivity(new Intent(this, FoodMenuManagementActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            if (id == R.id.nav_reservations) {
                startActivity(new Intent(this, ReservationManagementActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }

            return false;
        });
    }
}