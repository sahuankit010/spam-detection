package com.spamdetector.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ContactDTO {
    private Long id;

    @NotBlank(message = "Contact name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,13}$", message = "Invalid phone number")
    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Contact name is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Contact name is required") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Phone number is required") @Pattern(regexp = "^[+]?[0-9]{10,13}$", message = "Invalid phone number") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank(message = "Phone number is required") @Pattern(regexp = "^[+]?[0-9]{10,13}$", message = "Invalid phone number") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}