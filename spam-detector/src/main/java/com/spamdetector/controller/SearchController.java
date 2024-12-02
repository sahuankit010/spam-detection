package com.spamdetector.controller;

import com.spamdetector.dto.SearchResultDTO;
import com.spamdetector.service.SpamReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SpamReportService spamReportService;

    @Autowired
    public SearchController(SpamReportService spamReportService) {
        this.spamReportService = spamReportService;
    }

    @GetMapping("/name")
    public List<SearchResultDTO> searchByName(@Valid  @RequestParam String query, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return spamReportService.searchByName(userId, query);
    }

    @GetMapping("/phone")
    public List<SearchResultDTO> searchByPhoneNumber(@Valid @RequestParam String phoneNumber, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return spamReportService.searchByPhoneNumber(userId, phoneNumber);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        String phoneNumber = authentication.getName();
        return spamReportService.getUserIdByPhoneNumber(phoneNumber);
    }
}
