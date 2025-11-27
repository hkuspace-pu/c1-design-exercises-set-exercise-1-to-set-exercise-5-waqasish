package com.example.restaurantmanagementapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class ReservationManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_management);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_reservations);

        RecyclerView rv = findViewById(R.id.rvReservations);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Reservation> list = new ArrayList<>();
        list.add(new Reservation("16 Nov, 7:30 PM", "Ahmed Khan", "Table 3"));
        list.add(new Reservation("16 Nov, 8:00 PM", "Emily Wong", "Table 8"));
        list.add(new Reservation("16 Nov, 9:15 PM", "David Chen", "Table 12"));
        list.add(new Reservation("17 Nov, 6:30 PM", "Sarah Lim", "Table 5"));
        list.add(new Reservation("17 Nov, 8:45 PM", "Michael Lau", "Table 10"));

        rv.setAdapter(new ReservationAdapter(list));

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