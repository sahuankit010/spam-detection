package com.spamdetector.service;

import com.spamdetector.dto.UserRegistrationDTO;
import com.spamdetector.dto.UserProfileDTO;

import java.util.List;

public interface UserService {
    UserProfileDTO registerUser(UserRegistrationDTO registrationDTO);
    UserProfileDTO getUserProfile(Long userId);
    UserProfileDTO updateUserProfile(Long userId, UserProfileDTO profileDTO);
    boolean checkPhoneNumberAvailability(String phoneNumber);
    Long getUserIdByPhoneNumber(String phoneNumber);
    List<UserProfileDTO> getAllUsers();
    void deleteAllUsers();
}