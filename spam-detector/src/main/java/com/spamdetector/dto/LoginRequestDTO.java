package com.spamdetector.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    @NotBlank(message = "Password is required")
    private String password;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }
}
