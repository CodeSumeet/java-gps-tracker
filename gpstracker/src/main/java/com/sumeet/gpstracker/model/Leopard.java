package com.sumeet.gpstracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Leopard {
    @Id
    private String callerId;
    private String name;
    private int age;
    private double latitude;
    private double longitude;

    // Constructors, Getters, and Setters
    public Leopard() {}

    public Leopard(String callerId, String name, int age, double latitude, double longitude) {
        this.callerId = callerId;
        this.name = name;
        this.age = age;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
}