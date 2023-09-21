package com.example.h2h;

public class User {
    private String address;
    private String name;
    private String email;
    private double phone;
    Consignment consignment;

    // Default constructor
    public User() {
        // Default constructor required for Firebase Realtime Database
    }

    // Constructor with parameters
    public User(String address, String name, String email,double phone, Consignment consignment) {
        this.address = address;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.consignment = consignment;

    }
    public User(String address, String name, String email,double phone) {
        this.address = address;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getter for address
    public String getAddress() {
        return address;
    }

    // Setter for address
    public void setAddress(String address) {
        this.address = address;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    public void setPhone(double phone) {
        this.phone = phone;
    }

    public double getPhone() {
        return phone;
    }

    public Consignment getConsignment() {
        return consignment;
    }

    public void setConsignment(Consignment consignment) {
        this.consignment = consignment;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }
}
