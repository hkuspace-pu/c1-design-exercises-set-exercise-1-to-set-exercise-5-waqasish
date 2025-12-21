package com.example.restaurantmanagementapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;

public class ReservationManagementActivity extends AppCompatActivity implements ReservationAdapter.OnReservationActionListener {

    private RecyclerView rvReservations;
    private FloatingActionButton fabAddReservation;
    private DatabaseHelper dbHelper;
    private ReservationAdapter adapter;
    private ArrayList<Reservation> reservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_management);

        rvReservations = findViewById(R.id.rvReservations);
        fabAddReservation = findViewById(R.id.fabAddReservation);
        dbHelper = new DatabaseHelper(this);

        rvReservations.setLayoutManager(new LinearLayoutManager(this));
        loadReservations();

        fabAddReservation.setOnClickListener(v -> showAddReservationDialog(null));

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_reservations);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new android.content.Intent(this, StaffHomeActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            if (id == R.id.nav_food) {
                startActivity(new android.content.Intent(this, FoodMenuManagementActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            if (id == R.id.nav_reservations) {
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

    private void loadReservations() {
        reservations = dbHelper.getAllReservations();
        adapter = new ReservationAdapter(reservations, this);
        rvReservations.setAdapter(adapter);
    }

    private void showAddReservationDialog(Reservation reservation) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_reservation, null);
        
        TextInputEditText etDate = dialogView.findViewById(R.id.etReservationDate);
        TextInputEditText etTime = dialogView.findViewById(R.id.etReservationTime);
        TextInputEditText etName = dialogView.findViewById(R.id.etCustomerName);
        TextInputEditText etTable = dialogView.findViewById(R.id.etTableNumber);
        
        TextInputLayout tilDate = dialogView.findViewById(R.id.tilReservationDate);
        TextInputLayout tilTime = dialogView.findViewById(R.id.tilReservationTime);
        TextInputLayout tilName = dialogView.findViewById(R.id.tilCustomerName);
        TextInputLayout tilTable = dialogView.findViewById(R.id.tilTableNumber);

        boolean isEdit = reservation != null;
        String dialogTitle = isEdit ? "Edit Reservation" : "Add Reservation";
        
        if (isEdit) {
            // Parse dateTime to separate date and time
            String[] parts = reservation.dateTime.split(", ");
            if (parts.length >= 2) {
                etDate.setText(parts[0] + ", " + parts[1]);
                etTime.setText(parts[parts.length - 1]);
            }
            etName.setText(reservation.name);
            etTable.setText(reservation.table);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle(dialogTitle);

        builder.setPositiveButton("Save", (d, which) -> {
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String table = etTable.getText().toString().trim();

            // Validate inputs
            boolean isValid = true;
            if (date.isEmpty()) {
                tilDate.setError("Date is required");
                isValid = false;
            } else {
                tilDate.setError(null);
            }

            if (time.isEmpty()) {
                tilTime.setError("Time is required");
                isValid = false;
            } else {
                tilTime.setError(null);
            }

            if (name.isEmpty()) {
                tilName.setError("Customer name is required");
                isValid = false;
            } else {
                tilName.setError(null);
            }

            if (table.isEmpty()) {
                tilTable.setError("Table number is required");
                isValid = false;
            } else {
                tilTable.setError(null);
            }

            if (isValid) {
                if (isEdit) {
                    if (dbHelper.updateReservation(reservation.id, date, time, name, table)) {
                        Toast.makeText(this, "Reservation updated successfully", Toast.LENGTH_SHORT).show();
                        loadReservations();
                    } else {
                        Toast.makeText(this, "Failed to update reservation", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (dbHelper.addReservation(date, time, name, table) > 0) {
                        Toast.makeText(this, "Reservation added successfully", Toast.LENGTH_SHORT).show();
                        loadReservations();
                    } else {
                        Toast.makeText(this, "Failed to add reservation", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // Show dialog again with errors
                d.dismiss();
                showAddReservationDialog(reservation);
            }
        });

        builder.setNegativeButton("Cancel", (d, which) -> d.dismiss());
        builder.create().show();
    }

    @Override
    public void onEditClick(Reservation reservation) {
        showAddReservationDialog(reservation);
    }

    @Override
    public void onDeleteClick(Reservation reservation) {
        new AlertDialog.Builder(this)
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
