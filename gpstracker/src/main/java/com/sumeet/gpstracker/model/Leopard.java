package com.sumeet.gpstracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Leopard {
    @Id
    private String callerId;
    private String name;
    private double latitude;
    private double longitude;
    private boolean shocked;
    private int shockCount;
    private boolean crossedOuterFence; // New field
    private double maxFood = 10.0; // Maximum food in kg
    private double foodEaten = 0.0; // Amount of food eaten

    // Getters and setters for all fields
    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isShocked() {
        return shocked;
    }

    public void setShocked(boolean shocked) {
        this.shocked = shocked;
    }

    public int getShockCount() {
        return shockCount;
    }

    public void setShockCount(int shockCount) {
        this.shockCount = shockCount;
    }

    public boolean isCrossedOuterFence() {
        return crossedOuterFence;
    }

    public void setCrossedOuterFence(boolean crossedOuterFence) {
        this.crossedOuterFence = crossedOuterFence;
    }

    public double getMaxFood() {
        return maxFood;
    }

    public double getFoodEaten() {
        return foodEaten;
    }

    public double getSpaceLeft() {
        return maxFood - foodEaten;
    }

    public void addFood(double amount) {
        if (amount + foodEaten <= maxFood) {
            foodEaten += amount;
        }
    }

    public void setFoodEaten(double foodEaten) {
        this.foodEaten = foodEaten;
    }

    public void setMaxFood(double maxFood) {
        this.maxFood = maxFood;
    }
}
