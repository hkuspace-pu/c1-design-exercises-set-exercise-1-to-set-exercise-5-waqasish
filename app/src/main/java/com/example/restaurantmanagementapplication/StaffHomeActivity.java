package com.example.restaurantmanagementapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StaffHomeActivity extends AppCompatActivity {

    private RecyclerView rvTodayReservations;
    private TextView tvDate;
    private DatabaseHelper dbHelper;
    private ReservationHomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        rvTodayReservations = findViewById(R.id.rvTodayReservations);
        tvDate = findViewById(R.id.tvDate);
        dbHelper = new DatabaseHelper(this);

        // Set current date
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(new Date()));

        // Setup RecyclerView
        rvTodayReservations.setLayoutManager(new LinearLayoutManager(this));
        loadTodayReservations();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
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

    private void loadTodayReservations() {
        ArrayList<Reservation> reservations = dbHelper.getTodayReservations();
        adapter = new ReservationHomeAdapter(reservations);
        rvTodayReservations.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTodayReservations();
    }
}