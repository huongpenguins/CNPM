package com.example;

public class Resident {
    private String id;
    private String name;
    private String dateOfBirth;
    private String phone;
    private String address;

    public Resident(String id, String name, String dateOfBirth, String phone, String address) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.address = address;
    }

    // Getter và Setter
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
}
