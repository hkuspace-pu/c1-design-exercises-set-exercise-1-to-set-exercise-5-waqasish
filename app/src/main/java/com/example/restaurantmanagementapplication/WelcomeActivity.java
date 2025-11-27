package com.example.restaurantmanagementapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Staff Button → Go to your existing Staff Login
        findViewById(R.id.btnStaff).setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        });

        // Guest Button → Temporary message (no error!)
        findViewById(R.id.btnGuest).setOnClickListener(v -> {
            Toast.makeText(this, "Guest Login Coming Soon!", Toast.LENGTH_LONG).show();

        });
    }
}