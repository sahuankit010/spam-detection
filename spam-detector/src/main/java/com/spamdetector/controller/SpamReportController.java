package com.spamdetector.controller;

import com.spamdetector.dto.SpamReportDTO;
import com.spamdetector.service.SpamReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spam")
public class SpamReportController {

    private final SpamReportService spamReportService;

    public SpamReportController(SpamReportService spamReportService) {
        this.spamReportService = spamReportService;
    }

    @PostMapping("/report")
    public ResponseEntity<String> reportSpam(@Valid @RequestBody SpamReportDTO spamReportDTO, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        spamReportService.reportSpam(userId, spamReportDTO);
        return ResponseEntity.ok("Spam report submitted successfully.");
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        String phoneNumber = authentication.getName();
        return spamReportService.getUserIdByPhoneNumber(phoneNumber);
    }

    @GetMapping("/likelihood")
    public ResponseEntity<Double> getSpamLikelihood(@Valid @RequestParam String phoneNumber) {
        double likelihood = spamReportService.getSpamLikelihood(phoneNumber);
        return ResponseEntity.ok(likelihood);
    }


}
