package com.example;

public class Household {
    private String id;
    private String ownerName;
    private String address;

    public Household(String id, String ownerName, String address) {
        this.id = id;
        this.ownerName = ownerName;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getAddress() {
        return address;
    }
}
