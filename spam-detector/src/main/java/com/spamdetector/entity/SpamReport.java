
package com.spamdetector.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spam_reports")
public class SpamReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Reported phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,13}$", message = "Invalid phone number")
    @Column(nullable = false)
    private String reportedPhoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by_user_id", nullable = false)
    private User reportedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Reported phone number is required") @Pattern(regexp = "^[+]?[0-9]{10,13}$", message = "Invalid phone number") String getReportedPhoneNumber() {
        return reportedPhoneNumber;
    }

    public void setReportedPhoneNumber(@NotBlank(message = "Reported phone number is required") @Pattern(regexp = "^[+]?[0-9]{10,13}$", message = "Invalid phone number") String reportedPhoneNumber) {
        this.reportedPhoneNumber = reportedPhoneNumber;
    }

    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
    }
}