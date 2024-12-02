package com.spamdetector.dto;

import lombok.Data;

@Data
public class SearchResultDTO {
    private String name;
    private String phoneNumber;
    private double spamLikelihood;
    private boolean isRegisteredUser;
    private String email; // Add this field

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getSpamLikelihood() {
        return spamLikelihood;
    }

    public void setSpamLikelihood(double spamLikelihood) {
        this.spamLikelihood = spamLikelihood;
    }

    public boolean isRegisteredUser() {
        return isRegisteredUser;
    }

    public void setRegisteredUser(boolean registeredUser) {
        isRegisteredUser = registeredUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
