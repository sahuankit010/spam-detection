package com.spamdetector.repository;

import com.spamdetector.entity.SpamReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpamReportRepository extends JpaRepository<SpamReport, Long> {
    List<SpamReport> findByReportedPhoneNumber(String phoneNumber);
    long countByReportedPhoneNumber(String phoneNumber);
    long count(); // Total number of spam reports

    boolean existsByReportedPhoneNumberAndReportedBy_Id(String phoneNumber, Long userId);
}
