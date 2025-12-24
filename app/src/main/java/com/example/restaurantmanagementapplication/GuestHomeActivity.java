package com.example.restaurantmanagementapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class GuestHomeActivity extends AppCompatActivity {

    private RecyclerView rvGuestReservations;
    private TextView tvEmptyReservations;
    private MaterialButton btnMakeReservation;
    private ImageButton btnMenu;
    private DatabaseHelper dbHelper;
    private GuestReservationTableAdapter adapter;
    private String userEmail;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);

        // Get user email from intent
        userEmail = getIntent().getStringExtra("user_email");
        if (userEmail == null) {
            Toast.makeText(this, "User information not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        userName = dbHelper.getUserNameByEmail(userEmail);

        rvGuestReservations = findViewById(R.id.rvGuestReservations);
        tvEmptyReservations = findViewById(R.id.tvEmptyReservations);
        btnMakeReservation = findViewById(R.id.btnMakeReservation);
        btnMenu = findViewById(R.id.btnMenu);

        rvGuestReservations.setLayoutManager(new LinearLayoutManager(this));
        loadReservations();

        btnMakeReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to make reservation screen
                Toast.makeText(GuestHomeActivity.this, "Make Reservation - Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Open side menu or options
                Toast.makeText(GuestHomeActivity.this, "Menu - Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_manage_reservation);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_food_menu) {
                Intent intent = new Intent(this, GuestFoodMenuActivity.class);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            if (id == R.id.nav_make_reservation) {
                // TODO: Navigate to Make Reservation screen
                Toast.makeText(this, "Make Reservation - Coming soon", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id == R.id.nav_manage_reservation) {
                // Already on this screen
                return true;
            }
            if (id == R.id.nav_setting) {
                // TODO: Navigate to Settings screen
                Toast.makeText(this, "Settings - Coming soon", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });
    }

    private void loadReservations() {
        if (userName == null) {
            tvEmptyReservations.setVisibility(View.VISIBLE);
            rvGuestReservations.setVisibility(View.GONE);
            return;
        }

        ArrayList<Reservation> reservations = dbHelper.getReservationsByCustomerName(userName);
        
        if (reservations.isEmpty()) {
            tvEmptyReservations.setVisibility(View.VISIBLE);
            rvGuestReservations.setVisibility(View.GONE);
        } else {
            tvEmptyReservations.setVisibility(View.GONE);
            rvGuestReservations.setVisibility(View.VISIBLE);
            adapter = new GuestReservationTableAdapter(reservations);
            rvGuestReservations.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReservations();
    }
}
