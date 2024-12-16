package com.example;

public class Household {
    private String householdId;
    private String apartmentId;
    private String residentId;
    private String vehicleId;
    private String issueDate;
    private String ownerName;
    private String phoneNumber;

    public Household(String householdId, String apartmentId, String residentId, String vehicleId, String issueDate, String ownerName, String phoneNumber) {
        this.householdId = householdId;
        this.apartmentId = apartmentId;
        this.residentId = residentId;
        this.vehicleId = vehicleId;
        this.issueDate = issueDate;
        this.ownerName = ownerName;
        this.phoneNumber = phoneNumber;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public String getResidentId() {
        return residentId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
