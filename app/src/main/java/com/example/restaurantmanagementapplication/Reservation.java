package com.example.restaurantmanagementapplication;

public class Reservation {
    public int id;
    public String date, time, name, table;
    public int numberOfGuests;
    public String specialRequest;

    public Reservation(String date, String time, String name, String table) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.table = table;
    }

    public Reservation(String date, String time, String name, String table, int numberOfGuests, String specialRequest) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.table = table;
        this.numberOfGuests = numberOfGuests;
        this.specialRequest = specialRequest;
    }
}