package com.example.restaurantmanagementapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;

public class GuestSettingsActivity extends AppCompatActivity {

    private MaterialSwitch switchNewReservation;
    private MaterialSwitch switchUpdateReservation;
    private MaterialSwitch switchCancelledReservation;
    private MaterialSwitch switchNewMenuItem;
    private MaterialButton btnLogout;
    private SharedPreferences sharedPreferences;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_settings);

        // Get user email from intent
        userEmail = getIntent().getStringExtra("user_email");

        // Initialize SharedPreferences for storing notification settings
        sharedPreferences = getSharedPreferences("GuestNotificationSettings", MODE_PRIVATE);

        // Initialize views
        switchNewReservation = findViewById(R.id.switchNewReservation);
        switchUpdateReservation = findViewById(R.id.switchUpdateReservation);
        switchCancelledReservation = findViewById(R.id.switchCancelledReservation);
        switchNewMenuItem = findViewById(R.id.switchNewMenuItem);
        btnLogout = findViewById(R.id.btnLogout);

        // Load saved notification preferences
        loadNotificationSettings();

        // Save settings when switches are toggled
        switchNewReservation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveNotificationSetting("new_reservation", isChecked);
        });

        switchUpdateReservation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveNotificationSetting("update_reservation", isChecked);
        });

        switchCancelledReservation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveNotificationSetting("cancelled_reservation", isChecked);
        });

        switchNewMenuItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveNotificationSetting("new_menu_item", isChecked);
        });

        // Logout button
        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Intent intent = new Intent(GuestSettingsActivity.this, GuestLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_setting);

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
                return true;
            }
            return false;
        });
    }

    private void loadNotificationSettings() {
        // Load saved preferences, default to true if not set
        switchNewReservation.setChecked(sharedPreferences.getBoolean("new_reservation", true));
        switchUpdateReservation.setChecked(sharedPreferences.getBoolean("update_reservation", true));
        switchCancelledReservation.setChecked(sharedPreferences.getBoolean("cancelled_reservation", true));
        switchNewMenuItem.setChecked(sharedPreferences.getBoolean("new_menu_item", true));
    }

    private void saveNotificationSetting(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}

