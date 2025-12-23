package com.example.restaurantmanagementapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class GuestRegisterActivity extends AppCompatActivity {

    private TextInputLayout tilFirstName, tilLastName, tilAddress, tilPhone,
            tilEmail, tilUsername, tilPassword;
    private TextInputEditText etFirstName, etLastName, etAddress, etPhone,
            etEmail, etUsername, etPassword;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_register);

        dbHelper = new DatabaseHelper(this);

        tilFirstName = findViewById(R.id.tilFirstName);
        tilLastName = findViewById(R.id.tilLastName);
        tilAddress = findViewById(R.id.tilAddress);
        tilPhone = findViewById(R.id.tilPhone);
        tilEmail = findViewById(R.id.tilEmailRegister);
        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPasswordRegister);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmailRegister);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPasswordRegister);

        MaterialButton btnSubmit = findViewById(R.id.btnSubmitRegistration);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });
    }

    private void handleSubmit() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        clearErrors();

        boolean isValid = true;
        if (firstName.isEmpty()) {
            tilFirstName.setError("First name is required");
            isValid = false;
        }
        if (lastName.isEmpty()) {
            tilLastName.setError("Last name is required");
            isValid = false;
        }
        if (address.isEmpty()) {
            tilAddress.setError("Address is required");
            isValid = false;
        }
        if (phone.isEmpty()) {
            tilPhone.setError("Phone is required");
            isValid = false;
        }
        if (email.isEmpty()) {
            tilEmail.setError("Email is required");
            isValid = false;
        }
        if (username.isEmpty()) {
            tilUsername.setError("Username is required");
            isValid = false;
        }
        if (password.isEmpty()) {
            tilPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            tilPassword.setError("Password must be at least 6 characters");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        if (dbHelper.isEmailExists(email)) {
            tilEmail.setError("Email already registered");
            Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = dbHelper.registerGuestUser(firstName, lastName, address, phone, email, username, password);
        if (id > 0) {
            Toast.makeText(this, "Registration successful. Please login.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearErrors() {
        tilFirstName.setError(null);
        tilLastName.setError(null);
        tilAddress.setError(null);
        tilPhone.setError(null);
        tilEmail.setError(null);
        tilUsername.setError(null);
        tilPassword.setError(null);
    }
}


