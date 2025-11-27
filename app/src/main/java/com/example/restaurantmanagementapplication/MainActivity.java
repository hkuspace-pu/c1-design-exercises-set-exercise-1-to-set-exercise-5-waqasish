package com.example.restaurantmanagementapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);   // ← This shows your welcome screen
    }


    public void openStaffLogin(View view) {
        startActivity(new Intent(MainActivity.this, StaffLoginActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public void openStaffHome(View view) {
        startActivity(new Intent(MainActivity.this, StaffHomeActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}