package com.example.restaurantmanagementapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class GuestMakeReservationActivity extends AppCompatActivity implements TableSelectionAdapter.OnTableSelectedListener {

    private TextInputEditText etSelectDate, etTime, etNumberOfGuests, etSpecialRequest;
    private TextInputLayout tilSelectDate, tilTime, tilNumberOfGuests;
    private RecyclerView rvTables;
    private MaterialButton btnReserveTable;
    private DatabaseHelper dbHelper;
    private TableSelectionAdapter tableAdapter;
    private String userEmail;
    private String userName;
    private int selectedTableNumber = -1;
    private Calendar selectedDate = Calendar.getInstance();
    private String selectedTime = "";
    private int editReservationId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_make_reservation);

        userEmail = getIntent().getStringExtra("user_email");
        if (userEmail == null) {
            Toast.makeText(this, "User information not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        userName = dbHelper.getUserNameByEmail(userEmail);

        // Check if editing existing reservation
        editReservationId = getIntent().getIntExtra("edit_reservation_id", -1);
        isEditMode = editReservationId != -1;

        etSelectDate = findViewById(R.id.etSelectDate);
        etTime = findViewById(R.id.etTime);
        etNumberOfGuests = findViewById(R.id.etNumberOfGuests);
        etSpecialRequest = findViewById(R.id.etSpecialRequest);
        tilSelectDate = findViewById(R.id.tilSelectDate);
        tilTime = findViewById(R.id.tilTime);
        tilNumberOfGuests = findViewById(R.id.tilNumberOfGuests);
        rvTables = findViewById(R.id.rvTables);
        btnReserveTable = findViewById(R.id.btnReserveTable);

        rvTables.setLayoutManager(new GridLayoutManager(this, 4));

        // Date picker
        etSelectDate.setOnClickListener(v -> showDatePicker());
        etSelectDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePicker();
            }
        });

        // Time picker
        etTime.setOnClickListener(v -> showTimePicker());
        etTime.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showTimePicker();
            }
        });

        // Reserve button
        btnReserveTable.setOnClickListener(v -> makeReservation());

        // Load existing reservation data if editing
        if (isEditMode) {
            Reservation existingReservation = dbHelper.getReservationById(editReservationId);
            if (existingReservation != null) {
                // Use separate date and time fields
                etSelectDate.setText(existingReservation.date);
                etTime.setText(existingReservation.time);
                selectedTime = existingReservation.time;
                if (existingReservation.numberOfGuests > 0) {
                    etNumberOfGuests.setText(String.valueOf(existingReservation.numberOfGuests));
                }
                if (existingReservation.specialRequest != null && !existingReservation.specialRequest.isEmpty()) {
                    etSpecialRequest.setText(existingReservation.specialRequest);
                }
                // Extract table number
                String tableStr = existingReservation.table.replace("Table ", "").trim();
                try {
                    selectedTableNumber = Integer.parseInt(tableStr);
                } catch (NumberFormatException e) {
                    selectedTableNumber = -1;
                }
                // Update button text
                btnReserveTable.setText("UPDATE RESERVATION");
                // Update available tables after a short delay to ensure adapter is ready
                rvTables.post(() -> updateAvailableTables());
            }
        }

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_make_reservation);

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
                return true;
            }
            if (id == R.id.nav_manage_reservation) {
                Intent intent = new Intent(this, GuestHomeActivity.class);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            if (id == R.id.nav_setting) {
                android.widget.Toast.makeText(this, "Settings - Coming soon", android.widget.Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                selectedDate.set(year, month, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                etSelectDate.setText(sdf.format(selectedDate.getTime()));
                tilSelectDate.setError(null);
                updateAvailableTables();
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar currentTime = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
            this,
            (view, hourOfDay, minute) -> {
                Calendar time = Calendar.getInstance();
                time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time.set(Calendar.MINUTE, minute);
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
                selectedTime = sdf.format(time.getTime());
                etTime.setText(selectedTime);
                tilTime.setError(null);
                updateAvailableTables();
            },
            currentTime.get(Calendar.HOUR_OF_DAY),
            currentTime.get(Calendar.MINUTE),
            false
        );
        timePickerDialog.show();
    }

    private void updateAvailableTables() {
        if (etSelectDate.getText().toString().isEmpty() || etTime.getText().toString().isEmpty()) {
            // Show all tables as available if date/time not selected
            ArrayList<TableItem> allTables = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                allTables.add(new TableItem(i, true));
            }
            tableAdapter = new TableSelectionAdapter(allTables, this);
            rvTables.setAdapter(tableAdapter);
            // If editing, pre-select the current table
            if (isEditMode && selectedTableNumber > 0) {
                tableAdapter.setSelectedTable(selectedTableNumber);
            }
            return;
        }

        String date = etSelectDate.getText().toString();
        String time = etTime.getText().toString();
        ArrayList<Integer> availableTables = dbHelper.getAvailableTables(date, time);

        ArrayList<TableItem> tables = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            // If editing and this is the current table, make it available
            boolean isAvailable = availableTables.contains(i);
            if (isEditMode && selectedTableNumber == i) {
                isAvailable = true; // Current table should be available for editing
            }
            tables.add(new TableItem(i, isAvailable));
        }

        tableAdapter = new TableSelectionAdapter(tables, this);
        rvTables.setAdapter(tableAdapter);
        
        // If editing, pre-select the current table
        if (isEditMode && selectedTableNumber > 0) {
            tableAdapter.setSelectedTable(selectedTableNumber);
        }
    }

    @Override
    public void onTableSelected(int tableNumber) {
        selectedTableNumber = tableNumber;
    }

    private void makeReservation() {
        String date = etSelectDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String numberOfGuestsStr = etNumberOfGuests.getText().toString().trim();
        String specialRequest = etSpecialRequest.getText().toString().trim();

        // Validate inputs
        boolean isValid = true;
        if (date.isEmpty()) {
            tilSelectDate.setError("Date is required");
            isValid = false;
        } else {
            tilSelectDate.setError(null);
        }

        if (time.isEmpty()) {
            tilTime.setError("Time is required");
            isValid = false;
        } else {
            tilTime.setError(null);
        }

        int numberOfGuests = 0;
        if (numberOfGuestsStr.isEmpty()) {
            tilNumberOfGuests.setError("Number of guests is required");
            isValid = false;
        } else {
            try {
                numberOfGuests = Integer.parseInt(numberOfGuestsStr);
                if (numberOfGuests <= 0) {
                    tilNumberOfGuests.setError("Number of guests must be greater than 0");
                    isValid = false;
                } else {
                    tilNumberOfGuests.setError(null);
                }
            } catch (NumberFormatException e) {
                tilNumberOfGuests.setError("Invalid number");
                isValid = false;
            }
        }

        if (selectedTableNumber == -1) {
            Toast.makeText(this, "Please select a table", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (isValid) {
            // Check if table is still available
            String tableStr = "Table " + selectedTableNumber;
            if (!dbHelper.isTableAvailable(date, time, tableStr)) {
                Toast.makeText(this, "This table is no longer available. Please select another table.", Toast.LENGTH_SHORT).show();
                updateAvailableTables();
                return;
            }

            // Create or update reservation
            boolean success;
            if (isEditMode) {
                success = dbHelper.updateReservation(editReservationId, date, time, userName, tableStr, numberOfGuests, specialRequest);
                if (success) {
                    Toast.makeText(this, "Reservation updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to update reservation", Toast.LENGTH_SHORT).show();
                }
            } else {
                long reservationId = dbHelper.addReservation(date, time, userName, tableStr, numberOfGuests, specialRequest);
                success = reservationId > 0;
                if (success) {
                    Toast.makeText(this, "Reservation made successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to make reservation", Toast.LENGTH_SHORT).show();
                }
            }

            if (success) {
                // Navigate back to manage reservations
                Intent intent = new Intent(this, GuestHomeActivity.class);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }
    }
}

