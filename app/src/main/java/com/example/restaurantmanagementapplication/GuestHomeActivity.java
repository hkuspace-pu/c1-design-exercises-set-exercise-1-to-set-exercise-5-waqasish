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

public class GuestHomeActivity extends AppCompatActivity implements GuestReservationTableAdapter.OnReservationActionListener {

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
        // Initialize adapter with empty list to ensure it's ready
        adapter = new GuestReservationTableAdapter(new ArrayList<>(), this);
        rvGuestReservations.setAdapter(adapter);
        loadReservations();

        btnMakeReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestHomeActivity.this, GuestMakeReservationActivity.class);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
                Intent intent = new Intent(this, GuestMakeReservationActivity.class);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
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
        if (userEmail == null) {
            tvEmptyReservations.setVisibility(View.VISIBLE);
            rvGuestReservations.setVisibility(View.GONE);
            return;
        }

        // Get reservations by user email (to find all reservations for logged-in user)
        // The name displayed will be whatever name was entered during reservation
        ArrayList<Reservation> reservations = dbHelper.getReservationsByUserEmail(userEmail);
        
        if (reservations.isEmpty()) {
            tvEmptyReservations.setVisibility(View.VISIBLE);
            rvGuestReservations.setVisibility(View.GONE);
        } else {
            tvEmptyReservations.setVisibility(View.GONE);
            rvGuestReservations.setVisibility(View.VISIBLE);
        }
        // Always update the adapter to ensure it reflects current data
        adapter.updateList(reservations);
    }

    @Override
    public void onEditClick(Reservation reservation) {
        // Navigate to make reservation page with pre-filled data
        Intent intent = new Intent(this, GuestMakeReservationActivity.class);
        intent.putExtra("user_email", userEmail);
        intent.putExtra("edit_reservation_id", reservation.id);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onCancelClick(Reservation reservation) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Cancel Reservation")
                .setMessage("Are you sure you want to cancel this reservation?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (dbHelper.deleteReservation(reservation.id)) {
                        Toast.makeText(this, "Reservation cancelled", Toast.LENGTH_SHORT).show();
                        loadReservations();
                    } else {
                        Toast.makeText(this, "Failed to cancel reservation", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReservations();
    }
}
