package com.example.restaurantmanagementapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "restaurant_db";
    // Bump version when changing schema
    private static final int DATABASE_VERSION = 4;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_USER_EMAIL = "email";
    private static final String COL_USER_PASSWORD = "password";
    private static final String COL_USER_NAME = "name";
    private static final String COL_USER_FIRST_NAME = "first_name";
    private static final String COL_USER_LAST_NAME = "last_name";
    private static final String COL_USER_ADDRESS = "address";
    private static final String COL_USER_PHONE = "phone";
    private static final String COL_USER_USERNAME = "username";
    private static final String COL_USER_ROLE = "role";

    // Reservations table
    private static final String TABLE_RESERVATIONS = "reservations";
    private static final String COL_RESERVATION_ID = "reservation_id";
    private static final String COL_RESERVATION_DATE = "reservation_date";
    private static final String COL_RESERVATION_TIME = "reservation_time";
    private static final String COL_RESERVATION_NAME = "customer_name";
    private static final String COL_RESERVATION_TABLE = "table_number";
    private static final String COL_RESERVATION_GUESTS = "number_of_guests";
    private static final String COL_RESERVATION_SPECIAL_REQUEST = "special_request";
    private static final String COL_RESERVATION_USER_EMAIL = "user_email";

    // Food Items table
    private static final String TABLE_FOOD_ITEMS = "food_items";
    private static final String COL_FOOD_ID = "food_id";
    private static final String COL_FOOD_NAME = "name";
    private static final String COL_FOOD_DESCRIPTION = "description";
    private static final String COL_FOOD_IMAGE_NAME = "image_name";
    private static final String COL_FOOD_PRICE = "price_hkd";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COL_USER_PASSWORD + " TEXT NOT NULL, " +
                COL_USER_NAME + " TEXT NOT NULL, " +
                COL_USER_FIRST_NAME + " TEXT, " +
                COL_USER_LAST_NAME + " TEXT, " +
                COL_USER_ADDRESS + " TEXT, " +
                COL_USER_PHONE + " TEXT, " +
                COL_USER_USERNAME + " TEXT UNIQUE, " +
                COL_USER_ROLE + " TEXT NOT NULL)";

        // Create reservations table
        String createReservationsTable = "CREATE TABLE " + TABLE_RESERVATIONS + " (" +
                COL_RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RESERVATION_DATE + " TEXT NOT NULL, " +
                COL_RESERVATION_TIME + " TEXT NOT NULL, " +
                COL_RESERVATION_NAME + " TEXT NOT NULL, " +
                COL_RESERVATION_TABLE + " TEXT NOT NULL, " +
                COL_RESERVATION_GUESTS + " INTEGER, " +
                COL_RESERVATION_SPECIAL_REQUEST + " TEXT, " +
                COL_RESERVATION_USER_EMAIL + " TEXT)";

        // Create food items table
        String createFoodItemsTable = "CREATE TABLE " + TABLE_FOOD_ITEMS + " (" +
                COL_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FOOD_NAME + " TEXT NOT NULL, " +
                COL_FOOD_DESCRIPTION + " TEXT, " +
                COL_FOOD_IMAGE_NAME + " TEXT, " +
                COL_FOOD_PRICE + " INTEGER NOT NULL)";

        db.execSQL(createUsersTable);
        db.execSQL(createReservationsTable);
        db.execSQL(createFoodItemsTable);

        // Insert default staff user
        ContentValues userValues = new ContentValues();
        userValues.put(COL_USER_EMAIL, "staff@bellavista.com");
        userValues.put(COL_USER_PASSWORD, "staff123");
        userValues.put(COL_USER_NAME, "Staff Member");
        userValues.put(COL_USER_FIRST_NAME, "Staff");
        userValues.put(COL_USER_LAST_NAME, "Member");
        userValues.put(COL_USER_ADDRESS, "");
        userValues.put(COL_USER_PHONE, "");
        userValues.put(COL_USER_USERNAME, "staff");
        userValues.put(COL_USER_ROLE, "staff");
        db.insert(TABLE_USERS, null, userValues);

        // Don't insert sample reservations - start with empty reservations

        // Insert sample food items
        insertSampleFoodItems(db);
    }

    private void insertSampleReservations(SQLiteDatabase db) {
        String[][] reservations = {
            {"16 Nov, 2025", "7:30 PM", "Ahmed Khan", "Table 3"},
            {"16 Nov, 2025", "8:00 PM", "Emily Wong", "Table 8"},
            {"16 Nov, 2025", "9:15 PM", "David Chen", "Table 12"},
            {"17 Nov, 2025", "6:30 PM", "Sarah Lim", "Table 5"},
            {"17 Nov, 2025", "8:45 PM", "Michael Lau", "Table 10"}
        };

        for (String[] res : reservations) {
            ContentValues values = new ContentValues();
            values.put(COL_RESERVATION_DATE, res[0]);
            values.put(COL_RESERVATION_TIME, res[1]);
            values.put(COL_RESERVATION_NAME, res[2]);
            values.put(COL_RESERVATION_TABLE, res[3]);
            db.insert(TABLE_RESERVATIONS, null, values);
        }
    }

    private void insertSampleFoodItems(SQLiteDatabase db) {
        String[][] foodItems = {
            {"Grilled Salmon", "Fresh Atlantic salmon with lemon butter & herbs", "salmon", "268"},
            {"Wagyu Beef Burger", "A5 Japanese wagyu patty, truffle mayo", "wagyu_burger", "388"},
            {"Lobster Bisque", "Rich creamy soup with fresh lobster", "lobster_bisque", "198"},
            {"Truffle Pasta", "Handmade tagliatelle with black truffle", "truffle_pasta", "298"},
            {"Tiramisu", "Classic Italian dessert", "tiramisu", "98"},
            {"Caesar Salad", "Romaine lettuce, parmesan", "caesar_salad", "128"},
            {"Ribeye Steak 300g", "Australian grain-fed with pepper sauce", "ribeye_steak", "488"},
            {"Sashimi Platter", "Fresh tuna, salmon, hamachi", "sashimi", "458"}
        };

        for (String[] item : foodItems) {
            ContentValues values = new ContentValues();
            values.put(COL_FOOD_NAME, item[0]);
            values.put(COL_FOOD_DESCRIPTION, item[1]);
            values.put(COL_FOOD_IMAGE_NAME, item[2]);
            values.put(COL_FOOD_PRICE, Integer.parseInt(item[3]));
            db.insert(TABLE_FOOD_ITEMS, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            // Add new columns to reservations table
            try {
                db.execSQL("ALTER TABLE " + TABLE_RESERVATIONS + " ADD COLUMN " + COL_RESERVATION_GUESTS + " INTEGER");
            } catch (Exception e) {
                // Column might already exist
            }
            try {
                db.execSQL("ALTER TABLE " + TABLE_RESERVATIONS + " ADD COLUMN " + COL_RESERVATION_SPECIAL_REQUEST + " TEXT");
            } catch (Exception e) {
                // Column might already exist
            }
            // Clear sample reservations
            db.execSQL("DELETE FROM " + TABLE_RESERVATIONS);
        }
        if (oldVersion < 4) {
            // Add user_email column to reservations table
            try {
                db.execSQL("ALTER TABLE " + TABLE_RESERVATIONS + " ADD COLUMN " + COL_RESERVATION_USER_EMAIL + " TEXT");
            } catch (Exception e) {
                // Column might already exist
            }
        }
    }

    // User authentication methods
    public boolean authenticateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_USER_EMAIL + " = ? AND " + COL_USER_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    // Check if a user with this email already exists
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_USER_ID + " FROM " + TABLE_USERS + " WHERE " + COL_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // Register a new guest user
    public long registerGuestUser(String firstName,
                                  String lastName,
                                  String address,
                                  String phone,
                                  String email,
                                  String username,
                                  String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String fullName = (firstName + " " + lastName).trim();

        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password);
        values.put(COL_USER_NAME, fullName);
        values.put(COL_USER_FIRST_NAME, firstName);
        values.put(COL_USER_LAST_NAME, lastName);
        values.put(COL_USER_ADDRESS, address);
        values.put(COL_USER_PHONE, phone);
        values.put(COL_USER_USERNAME, username);
        values.put(COL_USER_ROLE, "guest");

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    // Update password for a user identified by email
    public boolean updatePasswordByEmail(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_PASSWORD, newPassword);
        int rows = db.update(TABLE_USERS, values, COL_USER_EMAIL + " = ?", new String[]{email});
        db.close();
        return rows > 0;
    }

    // Reservation methods
    public long addReservation(String date, String time, String name, String table, int numberOfGuests, String specialRequest) {
        return addReservation(date, time, name, table, numberOfGuests, specialRequest, null);
    }

    public long addReservation(String date, String time, String name, String table, int numberOfGuests, String specialRequest, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RESERVATION_DATE, date);
        values.put(COL_RESERVATION_TIME, time);
        values.put(COL_RESERVATION_NAME, name);
        values.put(COL_RESERVATION_TABLE, table);
        values.put(COL_RESERVATION_GUESTS, numberOfGuests);
        values.put(COL_RESERVATION_SPECIAL_REQUEST, specialRequest);
        if (userEmail != null) {
            values.put(COL_RESERVATION_USER_EMAIL, userEmail);
        }
        long id = db.insert(TABLE_RESERVATIONS, null, values);
        db.close();
        return id;
    }

    // Overloaded method for backward compatibility
    public long addReservation(String date, String time, String name, String table) {
        return addReservation(date, time, name, table, 0, "");
    }

    // Check if a table is available for a given date and time (with 1-hour occupancy window)
    public boolean isTableAvailable(String date, String time, String tableNumber) {
        return isTableAvailable(date, time, tableNumber, -1);
    }

    // Check if a table is available for a given date and time (with 1-hour occupancy window)
    // excludeReservationId: ID of reservation to exclude from check (useful when editing)
    public boolean isTableAvailable(String date, String time, String tableNumber, int excludeReservationId) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        // Parse the requested time to get hour and minute
        TimeSlot requestedSlot = parseTimeSlot(time);
        if (requestedSlot == null) {
            // If time parsing fails, fall back to exact match
            String query = "SELECT * FROM " + TABLE_RESERVATIONS + " WHERE " +
                    COL_RESERVATION_DATE + " = ? AND " + COL_RESERVATION_TIME + " = ? AND " +
                    COL_RESERVATION_TABLE + " = ?";
            if (excludeReservationId > 0) {
                query += " AND " + COL_RESERVATION_ID + " != ?";
                Cursor cursor = db.rawQuery(query, new String[]{date, time, tableNumber, String.valueOf(excludeReservationId)});
                boolean isAvailable = cursor.getCount() == 0;
                cursor.close();
                db.close();
                return isAvailable;
            } else {
                Cursor cursor = db.rawQuery(query, new String[]{date, time, tableNumber});
                boolean isAvailable = cursor.getCount() == 0;
                cursor.close();
                db.close();
                return isAvailable;
            }
        }

        // Get all reservations for this table on this date
        String query = "SELECT " + COL_RESERVATION_TIME + ", " + COL_RESERVATION_ID + " FROM " + TABLE_RESERVATIONS +
                " WHERE " + COL_RESERVATION_DATE + " = ? AND " + COL_RESERVATION_TABLE + " = ?";
        if (excludeReservationId > 0) {
            query += " AND " + COL_RESERVATION_ID + " != ?";
        }
        String[] args = excludeReservationId > 0 
                ? new String[]{date, tableNumber, String.valueOf(excludeReservationId)}
                : new String[]{date, tableNumber};
        
        Cursor cursor = db.rawQuery(query, args);
        
        boolean isAvailable = true;
        if (cursor.moveToFirst()) {
            do {
                String existingTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TIME));
                TimeSlot existingSlot = parseTimeSlot(existingTime);
                if (existingSlot != null) {
                    // Check if time slots overlap (1-hour window)
                    // Requested time: requestedSlot.start to requestedSlot.start + 1 hour
                    // Existing time: existingSlot.start to existingSlot.start + 1 hour
                    // They overlap if: requestedSlot.start < existingSlot.end && existingSlot.start < requestedSlot.end
                    if (requestedSlot.startMinutes < existingSlot.endMinutes && 
                        existingSlot.startMinutes < requestedSlot.endMinutes) {
                        isAvailable = false;
                        break;
                    }
                }
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return isAvailable;
    }

    // Helper class to represent a time slot
    private static class TimeSlot {
        int startMinutes; // Minutes since midnight
        int endMinutes;   // Minutes since midnight (start + 60)
        
        TimeSlot(int hour, int minute) {
            this.startMinutes = hour * 60 + minute;
            this.endMinutes = this.startMinutes + 60; // 1 hour later
        }
    }

    // Parse time string (e.g., "7:15 PM" or "7:15PM") to TimeSlot
    private TimeSlot parseTimeSlot(String timeStr) {
        try {
            // Remove spaces and convert to uppercase
            String time = timeStr.trim().replaceAll("\\s+", "").toUpperCase();
            
            // Check if it's AM or PM
            boolean isPM = time.contains("PM");
            time = time.replace("AM", "").replace("PM", "");
            
            // Split by colon
            String[] parts = time.split(":");
            if (parts.length != 2) {
                return null;
            }
            
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            
            // Convert to 24-hour format
            if (isPM && hour != 12) {
                hour += 12;
            } else if (!isPM && hour == 12) {
                hour = 0;
            }
            
            return new TimeSlot(hour, minute);
        } catch (Exception e) {
            return null;
        }
    }

    // Get available tables for a given date and time (with 1-hour occupancy window)
    public ArrayList<Integer> getAvailableTables(String date, String time) {
        return getAvailableTables(date, time, -1);
    }

    // Get available tables for a given date and time (with 1-hour occupancy window)
    // excludeReservationId: ID of reservation to exclude from check (useful when editing)
    public ArrayList<Integer> getAvailableTables(String date, String time, int excludeReservationId) {
        ArrayList<Integer> allTables = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            allTables.add(i);
        }

        // Parse the requested time to get hour and minute
        TimeSlot requestedSlot = parseTimeSlot(time);
        if (requestedSlot == null) {
            // If time parsing fails, fall back to exact match
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT " + COL_RESERVATION_TABLE + " FROM " + TABLE_RESERVATIONS +
                    " WHERE " + COL_RESERVATION_DATE + " = ? AND " + COL_RESERVATION_TIME + " = ?";
            if (excludeReservationId > 0) {
                query += " AND " + COL_RESERVATION_ID + " != ?";
            }
            String[] args = excludeReservationId > 0 
                    ? new String[]{date, time, String.valueOf(excludeReservationId)}
                    : new String[]{date, time};
            Cursor cursor = db.rawQuery(query, args);

            ArrayList<Integer> bookedTables = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    String tableStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TABLE));
                    // Extract number from "Table X" format
                    try {
                        String numberStr = tableStr.replace("Table ", "").trim();
                        int tableNum = Integer.parseInt(numberStr);
                        bookedTables.add(tableNum);
                    } catch (NumberFormatException e) {
                        // Skip invalid table numbers
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();

            // Remove booked tables from available list
            allTables.removeAll(bookedTables);
            return allTables;
        }

        // Get all reservations for this date
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_RESERVATION_TABLE + ", " + COL_RESERVATION_TIME + ", " + COL_RESERVATION_ID +
                " FROM " + TABLE_RESERVATIONS + " WHERE " + COL_RESERVATION_DATE + " = ?";
        if (excludeReservationId > 0) {
            query += " AND " + COL_RESERVATION_ID + " != ?";
        }
        String[] args = excludeReservationId > 0 
                ? new String[]{date, String.valueOf(excludeReservationId)}
                : new String[]{date};
        Cursor cursor = db.rawQuery(query, args);

        ArrayList<Integer> bookedTables = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String tableStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TABLE));
                String existingTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TIME));
                TimeSlot existingSlot = parseTimeSlot(existingTime);
                
                if (existingSlot != null) {
                    // Check if time slots overlap (1-hour window)
                    if (requestedSlot.startMinutes < existingSlot.endMinutes && 
                        existingSlot.startMinutes < requestedSlot.endMinutes) {
                        // Extract number from "Table X" format
                        try {
                            String numberStr = tableStr.replace("Table ", "").trim();
                            int tableNum = Integer.parseInt(numberStr);
                            if (!bookedTables.contains(tableNum)) {
                                bookedTables.add(tableNum);
                            }
                        } catch (NumberFormatException e) {
                            // Skip invalid table numbers
                        }
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Remove booked tables from available list
        allTables.removeAll(bookedTables);
        return allTables;
    }

    public ArrayList<Reservation> getAllReservations() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RESERVATIONS + " ORDER BY " +
                COL_RESERVATION_DATE + ", " + COL_RESERVATION_TIME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TIME));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_NAME));
                String table = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TABLE));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_RESERVATION_ID));
                
                int numberOfGuests = 0;
                try {
                    int columnIndex = cursor.getColumnIndex(COL_RESERVATION_GUESTS);
                    if (columnIndex >= 0 && !cursor.isNull(columnIndex)) {
                        numberOfGuests = cursor.getInt(columnIndex);
                    }
                } catch (Exception e) {
                    // Column might not exist
                }
                
                String specialRequest = "";
                try {
                    int columnIndex = cursor.getColumnIndex(COL_RESERVATION_SPECIAL_REQUEST);
                    if (columnIndex >= 0 && !cursor.isNull(columnIndex)) {
                        specialRequest = cursor.getString(columnIndex);
                        if (specialRequest == null) specialRequest = "";
                    }
                } catch (Exception e) {
                    // Column might not exist
                }
                
                Reservation reservation = new Reservation(date, time, name, table, numberOfGuests, specialRequest);
                reservation.id = id;
                reservations.add(reservation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reservations;
    }

    public ArrayList<Reservation> getTodayReservations() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        String today = sdf.format(new Date());
        
        String query = "SELECT * FROM " + TABLE_RESERVATIONS + " WHERE " +
                COL_RESERVATION_DATE + " LIKE ? ORDER BY " + COL_RESERVATION_TIME;
        Cursor cursor = db.rawQuery(query, new String[]{today + "%"});

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TIME));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_NAME));
                String table = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TABLE));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_RESERVATION_ID));
                
                int numberOfGuests = 0;
                try {
                    int columnIndex = cursor.getColumnIndex(COL_RESERVATION_GUESTS);
                    if (columnIndex >= 0 && !cursor.isNull(columnIndex)) {
                        numberOfGuests = cursor.getInt(columnIndex);
                    }
                } catch (Exception e) {
                    // Column might not exist
                }
                
                String specialRequest = "";
                try {
                    int columnIndex = cursor.getColumnIndex(COL_RESERVATION_SPECIAL_REQUEST);
                    if (columnIndex >= 0 && !cursor.isNull(columnIndex)) {
                        specialRequest = cursor.getString(columnIndex);
                        if (specialRequest == null) specialRequest = "";
                    }
                } catch (Exception e) {
                    // Column might not exist
                }
                
                Reservation reservation = new Reservation(date, time, name, table, numberOfGuests, specialRequest);
                reservation.id = id;
                reservations.add(reservation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reservations;
    }

    public boolean updateReservation(int id, String date, String time, String name, String table) {
        return updateReservation(id, date, time, name, table, 0, "");
    }

    public boolean updateReservation(int id, String date, String time, String name, String table, int numberOfGuests, String specialRequest) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RESERVATION_DATE, date);
        values.put(COL_RESERVATION_TIME, time);
        values.put(COL_RESERVATION_NAME, name);
        values.put(COL_RESERVATION_TABLE, table);
        values.put(COL_RESERVATION_GUESTS, numberOfGuests);
        values.put(COL_RESERVATION_SPECIAL_REQUEST, specialRequest);
        int rowsAffected = db.update(TABLE_RESERVATIONS, values, COL_RESERVATION_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteReservation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_RESERVATIONS, COL_RESERVATION_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public Reservation getReservationById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RESERVATIONS + " WHERE " +
                COL_RESERVATION_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        Reservation reservation = null;
        if (cursor.moveToFirst()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_DATE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TIME));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_NAME));
            String table = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TABLE));
            
            int numberOfGuests = 0;
            try {
                int columnIndex = cursor.getColumnIndex(COL_RESERVATION_GUESTS);
                if (columnIndex >= 0 && !cursor.isNull(columnIndex)) {
                    numberOfGuests = cursor.getInt(columnIndex);
                }
            } catch (Exception e) {
                // Column might not exist in older versions
            }
            
            String specialRequest = "";
            try {
                int columnIndex = cursor.getColumnIndex(COL_RESERVATION_SPECIAL_REQUEST);
                if (columnIndex >= 0 && !cursor.isNull(columnIndex)) {
                    specialRequest = cursor.getString(columnIndex);
                    if (specialRequest == null) specialRequest = "";
                }
            } catch (Exception e) {
                // Column might not exist in older versions
            }
            
            reservation = new Reservation(date, time, name, table, numberOfGuests, specialRequest);
            reservation.id = id;
        }
        cursor.close();
        db.close();
        return reservation;
    }

    // Get reservations by customer name
    // Get reservations by user email (for finding all reservations by logged-in user)
    public ArrayList<Reservation> getReservationsByUserEmail(String userEmail) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        if (userEmail == null || userEmail.trim().isEmpty()) {
            return reservations;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RESERVATIONS + " WHERE " +
                COL_RESERVATION_USER_EMAIL + " = ? ORDER BY " + COL_RESERVATION_DATE + ", " + COL_RESERVATION_TIME;
        Cursor cursor = db.rawQuery(query, new String[]{userEmail.trim()});

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TIME));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_NAME));
                String table = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TABLE));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_RESERVATION_ID));
                
                int numberOfGuests = 0;
                try {
                    int columnIndex = cursor.getColumnIndex(COL_RESERVATION_GUESTS);
                    if (columnIndex >= 0 && !cursor.isNull(columnIndex)) {
                        numberOfGuests = cursor.getInt(columnIndex);
                    }
                } catch (Exception e) {
                    // Column might not exist
                }
                
                String specialRequest = "";
                try {
                    int columnIndex = cursor.getColumnIndex(COL_RESERVATION_SPECIAL_REQUEST);
                    if (columnIndex >= 0 && !cursor.isNull(columnIndex)) {
                        specialRequest = cursor.getString(columnIndex);
                        if (specialRequest == null) specialRequest = "";
                    }
                } catch (Exception e) {
                    // Column might not exist
                }
                
                Reservation reservation = new Reservation(date, time, name, table, numberOfGuests, specialRequest);
                reservation.id = id;
                reservations.add(reservation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reservations;
    }

    public ArrayList<Reservation> getReservationsByCustomerName(String customerName) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        if (customerName == null || customerName.trim().isEmpty()) {
            return reservations;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        // Trim the customer name for comparison
        String trimmedName = customerName.trim();
        // Get all reservations and filter by name (handles case and whitespace differences)
        String query = "SELECT * FROM " + TABLE_RESERVATIONS + " ORDER BY " + COL_RESERVATION_DATE + ", " + COL_RESERVATION_TIME;
        Cursor cursor = db.rawQuery(query, null);
        
        // Filter in Java to handle case-insensitive and whitespace differences
        if (cursor.moveToFirst()) {
            do {
                String reservationName = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_NAME));
                // Compare trimmed and case-insensitive
                if (reservationName != null && reservationName.trim().equalsIgnoreCase(trimmedName)) {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_DATE));
                    String time = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TIME));
                    String table = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESERVATION_TABLE));
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_RESERVATION_ID));
                    
                    int numberOfGuests = 0;
                    try {
                        int columnIndex = cursor.getColumnIndex(COL_RESERVATION_GUESTS);
                        if (columnIndex >= 0 && !cursor.isNull(columnIndex)) {
                            numberOfGuests = cursor.getInt(columnIndex);
                        }
                    } catch (Exception e) {
                        // Column might not exist
                    }
                    
                    String specialRequest = "";
                    try {
                        int columnIndex = cursor.getColumnIndex(COL_RESERVATION_SPECIAL_REQUEST);
                        if (columnIndex >= 0 && !cursor.isNull(columnIndex)) {
                            specialRequest = cursor.getString(columnIndex);
                            if (specialRequest == null) specialRequest = "";
                        }
                    } catch (Exception e) {
                        // Column might not exist
                    }
                    
                    Reservation reservation = new Reservation(date, time, reservationName, table, numberOfGuests, specialRequest);
                    reservation.id = id;
                    reservations.add(reservation);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reservations;
    }

    // Get user name by email
    public String getUserNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_USER_NAME + " FROM " + TABLE_USERS + " WHERE " + COL_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        String name = null;
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME));
        }
        cursor.close();
        db.close();
        return name;
    }

    // Food Item methods
    public long addFoodItem(String name, String description, String imageName, int price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FOOD_NAME, name);
        values.put(COL_FOOD_DESCRIPTION, description);
        values.put(COL_FOOD_IMAGE_NAME, imageName);
        values.put(COL_FOOD_PRICE, price);
        long id = db.insert(TABLE_FOOD_ITEMS, null, values);
        db.close();
        return id;
    }

    public ArrayList<FoodItem> getAllFoodItems() {
        ArrayList<FoodItem> foodItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_FOOD_ITEMS + " ORDER BY " + COL_FOOD_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_FOOD_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_FOOD_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_FOOD_DESCRIPTION));
                String imageName = cursor.getString(cursor.getColumnIndexOrThrow(COL_FOOD_IMAGE_NAME));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(COL_FOOD_PRICE));
                FoodItem item = new FoodItem(name, description, imageName, price);
                item.id = id;
                foodItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodItems;
    }

    public boolean updateFoodItem(int id, String name, String description, String imageName, int price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FOOD_NAME, name);
        values.put(COL_FOOD_DESCRIPTION, description);
        values.put(COL_FOOD_IMAGE_NAME, imageName);
        values.put(COL_FOOD_PRICE, price);
        int rowsAffected = db.update(TABLE_FOOD_ITEMS, values, COL_FOOD_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteFoodItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_FOOD_ITEMS, COL_FOOD_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public FoodItem getFoodItemById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_FOOD_ITEMS + " WHERE " + COL_FOOD_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        FoodItem item = null;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_FOOD_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_FOOD_DESCRIPTION));
            String imageName = cursor.getString(cursor.getColumnIndexOrThrow(COL_FOOD_IMAGE_NAME));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow(COL_FOOD_PRICE));
            item = new FoodItem(name, description, imageName, price);
            item.id = id;
        }
        cursor.close();
        db.close();
        return item;
    }
}

