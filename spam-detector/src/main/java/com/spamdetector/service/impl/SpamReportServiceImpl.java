package com.spamdetector.service.impl;

import com.spamdetector.dto.SpamReportDTO;
import com.spamdetector.dto.SearchResultDTO;
import com.spamdetector.entity.Contact;
import com.spamdetector.entity.SpamReport;
import com.spamdetector.entity.User;
import com.spamdetector.repository.ContactRepository;
import com.spamdetector.repository.SpamReportRepository;
import com.spamdetector.repository.UserRepository;
import com.spamdetector.service.SpamReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpamReportServiceImpl implements SpamReportService {
    private final SpamReportRepository spamReportRepository;
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;

    @Autowired
    public SpamReportServiceImpl(SpamReportRepository spamReportRepository, UserRepository userRepository, ContactRepository contactRepository) {
        this.spamReportRepository = spamReportRepository;
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
    }

    @Override
    @Transactional
    public void reportSpam(Long userId, SpamReportDTO spamReportDTO) {
        User reportingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the user has already reported this number as spam
        boolean alreadyReported = spamReportRepository.existsByReportedPhoneNumberAndReportedBy_Id(
                spamReportDTO.getReportedPhoneNumber(), userId);

        if (alreadyReported) {
            throw new RuntimeException("You have already reported this number as spam.");
        }

        SpamReport spamReport = new SpamReport();
        spamReport.setReportedPhoneNumber(spamReportDTO.getReportedPhoneNumber());
        spamReport.setReportedBy(reportingUser);

        spamReportRepository.save(spamReport);
    }

    @Override
    public double getSpamLikelihood(String phoneNumber) {
        long totalReports = spamReportRepository.count();
        if (totalReports == 0) {
            return 0.0;
        }

        long phoneNumberReports = spamReportRepository.countByReportedPhoneNumber(phoneNumber);

        // Calculate the spam likelihood as a percentage
        double spamLikelihood = ((double) phoneNumberReports / totalReports) * 100.0;
        return spamLikelihood;
    }

    @Override
    public List<SearchResultDTO> searchByName(Long userId, String query) {
        // Fetch all users and contacts matching the query
        List<SearchResultDTO> results = new ArrayList<>();

        // Search registered users whose names start with the query
        List<User> usersStartingWith = userRepository.findByNameStartingWithIgnoreCase(query);
        results.addAll(usersToSearchResultDTOs(usersStartingWith, userId));

        // Search contacts whose names start with the query
        List<Contact> contactsStartingWith = contactRepository.findByNameStartingWithIgnoreCase(query);
        results.addAll(contactsToSearchResultDTOs(contactsStartingWith));

        // Search registered users whose names contain the query but don't start with it
        List<User> usersContaining = userRepository.findByNameContainingIgnoreCase(query);
        usersContaining.removeAll(usersStartingWith); // Remove duplicates
        results.addAll(usersToSearchResultDTOs(usersContaining, userId));

        // Search contacts whose names contain the query but don't start with it
        List<Contact> contactsContaining = contactRepository.findByNameContainingIgnoreCase(query);
        contactsContaining.removeAll(contactsStartingWith); // Remove duplicates
        results.addAll(contactsToSearchResultDTOs(contactsContaining));

        return results;
    }

    @Override
    public List<SearchResultDTO> searchByPhoneNumber(Long userId, String phoneNumber) {
        List<SearchResultDTO> results = new ArrayList<>();

        // Check if there is a registered user with the phone number
        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            results.add(userToSearchResultDTO(user, userId));
            return results;
        }

        // If not a registered user, search contacts
        List<Contact> contacts = contactRepository.findByPhoneNumber(phoneNumber);
        results.addAll(contactsToSearchResultDTOs(contacts));

        return results;
    }

    // Helper methods
    private List<SearchResultDTO> usersToSearchResultDTOs(List<User> users, Long userId) {
        return users.stream()
                .map(user -> userToSearchResultDTO(user, userId))
                .collect(Collectors.toList());
    }

    private SearchResultDTO userToSearchResultDTO(User user, Long searchingUserId) {
        SearchResultDTO result = new SearchResultDTO();
        result.setName(user.getName());
        result.setPhoneNumber(user.getPhoneNumber());
        result.setSpamLikelihood(getSpamLikelihood(user.getPhoneNumber()));
        result.setRegisteredUser(true);

        // Check if the searching user is in the user's contact list
        boolean isInContactList = contactRepository.existsByUser_IdAndPhoneNumber(user.getId(), getUserPhoneNumber(searchingUserId));
        if (isInContactList) {
            result.setEmail(user.getEmail()); // Only set email if in contact list
        } else {
            result.setEmail(null);
        }

        return result;
    }

    // Helper method to get user's phone number
    private String getUserPhoneNumber(Long userId) {
        return userRepository.findById(userId)
                .map(User::getPhoneNumber)
                .orElse("");
    }

    private List<SearchResultDTO> contactsToSearchResultDTOs(List<Contact> contacts) {
        return contacts.stream()
                .map(contact -> {
                    SearchResultDTO result = new SearchResultDTO();
                    result.setName(contact.getName());
                    result.setPhoneNumber(contact.getPhoneNumber());
                    result.setSpamLikelihood(getSpamLikelihood(contact.getPhoneNumber()));
                    result.setRegisteredUser(false);
                    result.setEmail(null);
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Long getUserIdByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteAllSpamReports() {
        spamReportRepository.deleteAll();
    }

}
