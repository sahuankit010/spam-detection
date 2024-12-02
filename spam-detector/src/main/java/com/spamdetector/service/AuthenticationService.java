package com.spamdetector.service;

import com.spamdetector.dto.UserProfileDTO;
import com.spamdetector.dto.UserRegistrationDTO;

public interface AuthenticationService {
    String login(String phoneNumber, String password);
    void logout(String token);
    UserProfileDTO getCurrentUserProfile();
    UserProfileDTO registerUser(UserRegistrationDTO registrationDTO);
}
