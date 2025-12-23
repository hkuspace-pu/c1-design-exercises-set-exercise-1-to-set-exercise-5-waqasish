package com.example.restaurantmanagementapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class GuestForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilNewPassword, tilConfirmPassword;
    private TextInputEditText etEmail, etNewPassword, etConfirmPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_forgot_password);

        dbHelper = new DatabaseHelper(this);

        tilEmail = findViewById(R.id.tilEmailForgot);
        tilNewPassword = findViewById(R.id.tilNewPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        etEmail = findViewById(R.id.etEmailForgot);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        MaterialButton btnReset = findViewById(R.id.btnResetPassword);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReset();
            }
        });
    }

    private void handleReset() {
        String email = etEmail.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        tilEmail.setError(null);
        tilNewPassword.setError(null);
        tilConfirmPassword.setError(null);

        boolean isValid = true;
        if (email.isEmpty()) {
            tilEmail.setError("Email is required");
            isValid = false;
        }
        if (newPassword.isEmpty()) {
            tilNewPassword.setError("New password is required");
            isValid = false;
        } else if (newPassword.length() < 6) {
            tilNewPassword.setError("Password must be at least 6 characters");
            isValid = false;
        }
        if (confirmPassword.isEmpty()) {
            tilConfirmPassword.setError("Please confirm password");
            isValid = false;
        } else if (!confirmPassword.equals(newPassword)) {
            tilConfirmPassword.setError("Passwords do not match");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        if (!dbHelper.isEmailExists(email)) {
            tilEmail.setError("Email not found");
            Toast.makeText(this, "Email not found", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updated = dbHelper.updatePasswordByEmail(email, newPassword);
        if (updated) {
            Toast.makeText(this, "Password updated. Please login.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
        }
    }
}


