package com.example.restaurantmanagementapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FoodMenuManagementActivity extends AppCompatActivity implements FoodMenuAdapter.OnFoodItemActionListener {

    private RecyclerView rvMenuItems;
    private FloatingActionButton fabAddItem;
    private DatabaseHelper dbHelper;
    private FoodMenuAdapter adapter;
    private ArrayList<FoodItem> foodItems;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private String selectedImagePath = null;
    private ImageView currentImagePreview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu_management);

        rvMenuItems = findViewById(R.id.rvMenuItems);
        fabAddItem = findViewById(R.id.fabAddItem);
        dbHelper = new DatabaseHelper(this);

        rvMenuItems.setLayoutManager(new GridLayoutManager(this, 2));
        loadFoodItems();

        fabAddItem.setOnClickListener(v -> showAddFoodItemDialog(null));

        // Initialize image picker launcher
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null && currentImagePreview != null) {
                        try {
                            // Save image to internal storage
                            selectedImagePath = saveImageToInternalStorage(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                            if (bitmap != null) {
                                currentImagePreview.setImageBitmap(bitmap);
                            }
                        } catch (IOException e) {
                            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        );

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_food);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new android.content.Intent(this, StaffHomeActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            if (id == R.id.nav_food) {
                return true;
            }
            if (id == R.id.nav_reservations) {
                startActivity(new android.content.Intent(this, ReservationManagementActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
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

    private String saveImageToInternalStorage(Uri imageUri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        if (inputStream == null) {
            throw new IOException("Cannot open input stream");
        }

        // Create images directory if it doesn't exist
        File imagesDir = new File(getFilesDir(), "food_images");
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }

        // Generate unique filename
        String fileName = "food_" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(imagesDir, fileName);

        // Copy image to internal storage
        FileOutputStream outputStream = new FileOutputStream(imageFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.close();
        inputStream.close();

        return imageFile.getAbsolutePath();
    }


    private void loadFoodItems() {
        foodItems = dbHelper.getAllFoodItems();
        adapter = new FoodMenuAdapter(foodItems, this);
        rvMenuItems.setAdapter(adapter);
    }

    private void showAddFoodItemDialog(FoodItem foodItem) {
        selectedImagePath = null;
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_food_item, null);
        
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        TextInputEditText etName = dialogView.findViewById(R.id.etFoodName);
        TextInputEditText etDescription = dialogView.findViewById(R.id.etFoodDescription);
        ImageView ivFoodImagePreview = dialogView.findViewById(R.id.ivFoodImagePreview);
        MaterialButton btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);
        TextInputEditText etPrice = dialogView.findViewById(R.id.etFoodPrice);
        
        TextInputLayout tilName = dialogView.findViewById(R.id.tilFoodName);
        TextInputLayout tilDescription = dialogView.findViewById(R.id.tilFoodDescription);
        TextInputLayout tilPrice = dialogView.findViewById(R.id.tilFoodPrice);

        MaterialButton btnCancel = dialogView.findViewById(R.id.btnCancelFoodItem);
        MaterialButton btnSave = dialogView.findViewById(R.id.btnSaveFoodItem);

        boolean isEdit = foodItem != null;
        String dialogTitle = isEdit ? "Edit Food Item" : "Add Food Item";
        tvDialogTitle.setText(dialogTitle);
        
        if (isEdit) {
            etName.setText(foodItem.name);
            etDescription.setText(foodItem.description);
            etPrice.setText(String.valueOf(foodItem.priceHKD));
            
            // Load existing image if available
            if (foodItem.imageName != null && !foodItem.imageName.isEmpty()) {
                // Check if it's a file path or drawable name
                if (foodItem.imageName.startsWith("/")) {
                    // It's a file path
                    File imageFile = new File(foodItem.imageName);
                    if (imageFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        if (bitmap != null) {
                            ivFoodImagePreview.setImageBitmap(bitmap);
                            selectedImagePath = foodItem.imageName;
                        }
                    }
                } else {
                    // It's a drawable name, try to load it
                    int imageRes = getResources().getIdentifier(foodItem.imageName, "drawable", getPackageName());
                    if (imageRes != 0) {
                        ivFoodImagePreview.setImageResource(imageRes);
                    }
                }
            }
        }

        // Store reference to image preview for launcher
        currentImagePreview = ivFoodImagePreview;

        // Image picker button
        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        // Cancel button
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Save button
        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();

            // Validate inputs
            boolean isValid = true;
            if (name.isEmpty()) {
                tilName.setError("Food name is required");
                isValid = false;
            } else {
                tilName.setError(null);
            }

            if (description.isEmpty()) {
                tilDescription.setError("Description is required");
                isValid = false;
            } else {
                tilDescription.setError(null);
            }

            int price = 0;
            if (priceStr.isEmpty()) {
                tilPrice.setError("Price is required");
                isValid = false;
            } else {
                try {
                    price = Integer.parseInt(priceStr);
                    if (price <= 0) {
                        tilPrice.setError("Price must be greater than 0");
                        isValid = false;
                    } else {
                        tilPrice.setError(null);
                    }
                } catch (NumberFormatException e) {
                    tilPrice.setError("Invalid price");
                    isValid = false;
                }
            }

            // Image is optional, but if not provided and it's a new item, use placeholder
            String imagePath = selectedImagePath;
            if (imagePath == null || imagePath.isEmpty()) {
                if (isEdit && foodItem != null && foodItem.imageName != null) {
                    // Keep existing image
                    imagePath = foodItem.imageName;
                } else {
                    // Use placeholder
                    imagePath = "placeholder_food";
                }
            }

            if (isValid) {
                dialog.dismiss();
                if (isEdit) {
                    if (dbHelper.updateFoodItem(foodItem.id, name, description, imagePath, price)) {
                        Toast.makeText(this, "Food item updated successfully", Toast.LENGTH_SHORT).show();
                        loadFoodItems();
                    } else {
                        Toast.makeText(this, "Failed to update food item", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (dbHelper.addFoodItem(name, description, imagePath, price) > 0) {
                        Toast.makeText(this, "Food item added successfully", Toast.LENGTH_SHORT).show();
                        loadFoodItems();
                    } else {
                        Toast.makeText(this, "Failed to add food item", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onEditClick(FoodItem item) {
        showAddFoodItemDialog(item);
    }

    @Override
    public void onDeleteClick(FoodItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Food Item")
                .setMessage("Are you sure you want to delete " + item.name + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (dbHelper.deleteFoodItem(item.id)) {
                        Toast.makeText(this, "Food item deleted", Toast.LENGTH_SHORT).show();
                        loadFoodItems();
                    } else {
                        Toast.makeText(this, "Failed to delete food item", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFoodItems();
    }
}
