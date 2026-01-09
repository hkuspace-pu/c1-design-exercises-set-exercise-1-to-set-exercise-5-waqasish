package com.example.restaurantmanagementapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class ReservationConfirmationActivity extends AppCompatActivity {

    private TextView tvConfirmationName, tvConfirmationDate, tvConfirmationTime, 
                     tvConfirmationTable, tvConfirmationGuests, tvConfirmationSpecialRequest;
    private LinearLayout llSpecialRequest;
    private MaterialButton btnViewReservations, btnMakeAnother;
    private String userEmail;
    private String reservationName, reservationDate, reservationTime, reservationTable;
    private int reservationGuests;
    private String reservationSpecialRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_confirmation);

        // Get reservation details from intent
        reservationName = getIntent().getStringExtra("reservation_name");
        reservationDate = getIntent().getStringExtra("reservation_date");
        reservationTime = getIntent().getStringExtra("reservation_time");
        reservationTable = getIntent().getStringExtra("reservation_table");
        reservationGuests = getIntent().getIntExtra("reservation_guests", 0);
        reservationSpecialRequest = getIntent().getStringExtra("reservation_special_request");
        userEmail = getIntent().getStringExtra("user_email");

        // Initialize views
        tvConfirmationName = findViewById(R.id.tvConfirmationName);
        tvConfirmationDate = findViewById(R.id.tvConfirmationDate);
        tvConfirmationTime = findViewById(R.id.tvConfirmationTime);
        tvConfirmationTable = findViewById(R.id.tvConfirmationTable);
        tvConfirmationGuests = findViewById(R.id.tvConfirmationGuests);
        tvConfirmationSpecialRequest = findViewById(R.id.tvConfirmationSpecialRequest);
        llSpecialRequest = findViewById(R.id.llSpecialRequest);
        btnViewReservations = findViewById(R.id.btnViewReservations);
        btnMakeAnother = findViewById(R.id.btnMakeAnother);

        // Set reservation details
        tvConfirmationName.setText(reservationName != null ? reservationName : "");
        tvConfirmationDate.setText(reservationDate != null ? reservationDate : "");
        tvConfirmationTime.setText(reservationTime != null ? reservationTime : "");
        tvConfirmationTable.setText(reservationTable != null ? reservationTable : "");
        tvConfirmationGuests.setText(String.valueOf(reservationGuests));

        // Show special request only if it exists
        if (reservationSpecialRequest != null && !reservationSpecialRequest.trim().isEmpty()) {
            llSpecialRequest.setVisibility(View.VISIBLE);
            tvConfirmationSpecialRequest.setText(reservationSpecialRequest);
        } else {
            llSpecialRequest.setVisibility(View.GONE);
        }

        // View Reservations button
        btnViewReservations.setOnClickListener(v -> {
            Intent intent = new Intent(this, GuestHomeActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        // Make Another Reservation button
        btnMakeAnother.setOnClickListener(v -> {
            Intent intent = new Intent(this, GuestMakeReservationActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_make_reservation);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_food_menu) {
                Intent intent = new Intent(this, GuestFoodMenuActivity.class);
                if (userEmail != null) {
                    intent.putExtra("user_email", userEmail);
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
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
                Intent intent = new Intent(this, GuestSettingsActivity.class);
                if (userEmail != null) {
                    intent.putExtra("user_email", userEmail);
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });
    }
}

