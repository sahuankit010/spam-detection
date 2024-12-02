package com.spamdetector.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SpamReportDTO {
    @NotBlank(message = "Reported phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,13}$", message = "Invalid phone number")
    private String reportedPhoneNumber;

    public @NotBlank(message = "Reported phone number is required") @Pattern(regexp = "^[+]?[0-9]{10,13}$", message = "Invalid phone number") String getReportedPhoneNumber() {
        return reportedPhoneNumber;
    }

    public void setReportedPhoneNumber(@NotBlank(message = "Reported phone number is required") @Pattern(regexp = "^[+]?[0-9]{10,13}$", message = "Invalid phone number") String reportedPhoneNumber) {
        this.reportedPhoneNumber = reportedPhoneNumber;
    }
}