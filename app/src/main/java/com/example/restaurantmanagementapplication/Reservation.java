package com.example.restaurantmanagementapplication;

public class Reservation {
    public int id;
    public String dateTime, name, table;

    public Reservation(String dateTime, String name, String table) {
        this.dateTime = dateTime;
        this.name = name;
        this.table = table;
    }
}