package com.example.restaurantmanagementapplication;

public class FoodItem {
    public String name, description, imageName;
    public int priceHKD;

    public FoodItem(String name, String description, String imageName, int priceHKD) {
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        this.priceHKD = priceHKD;
    }
}