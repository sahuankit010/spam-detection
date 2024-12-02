package com.spamdetector.service;

import com.spamdetector.dto.SpamReportDTO;
import com.spamdetector.dto.SearchResultDTO;
import java.util.List;

public interface SpamReportService {
    void reportSpam(Long userId, SpamReportDTO spamReportDTO);
    double getSpamLikelihood(String phoneNumber);
    List<SearchResultDTO> searchByName(Long userId, String query);
    List<SearchResultDTO> searchByPhoneNumber(Long userId, String phoneNumber);
    Long getUserIdByPhoneNumber(String phoneNumber);
    void deleteAllSpamReports();
}
