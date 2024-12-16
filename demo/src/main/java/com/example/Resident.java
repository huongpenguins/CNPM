package com.example;

public class Resident {
    private String id;
    private String householdId;
    private String name;
    private String identityCard;
    private String dateOfBirth;
    private String placeOfBirth;
    private String ethnicity;
    private String occupation;
    private String relationship;

    public Resident(String id, String householdId, String name, String identityCard, String dateOfBirth,
                    String placeOfBirth, String ethnicity, String occupation, String relationship) {
        this.id = id;
        this.householdId = householdId;
        this.name = name;
        this.identityCard = identityCard;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.ethnicity = ethnicity;
        this.occupation = occupation;
        this.relationship = relationship;
    }

    public String getId() {
        return id;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public String getName() {
        return name;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getRelationship() {
        return relationship;
    }
}
