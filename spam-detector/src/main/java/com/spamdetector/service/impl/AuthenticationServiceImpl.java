package com.spamdetector.service.impl;

import com.spamdetector.dto.UserProfileDTO;
import com.spamdetector.dto.UserRegistrationDTO;
import com.spamdetector.entity.User;
import com.spamdetector.repository.UserRepository;
import com.spamdetector.security.JwtTokenUtil;
import com.spamdetector.service.AuthenticationService;
import com.spamdetector.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service

public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil; // Dependency for JWT generation
    private final UserService userService; // Inject the UserService

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }


    @Override
    public String login(String phoneNumber, String password) {
        // Perform authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(phoneNumber, password)
        );

        // Set authentication in context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token upon successful authentication
        return jwtTokenUtil.generateToken(phoneNumber);
    }

    @Override
    public UserProfileDTO registerUser(UserRegistrationDTO registrationDTO) {
        return userService.registerUser(registrationDTO); // Delegate to UserService
    }

    @Override
    public void logout(String token) {
        // Invalidate session context
        SecurityContextHolder.clearContext();
        // Additional token invalidation logic can be added if needed
    }

    @Override
    public UserProfileDTO getCurrentUserProfile() {
        // Fetch current authenticated user's phone number
        String phoneNumber = getAuthenticatedUserPhoneNumber();

        // Retrieve user details from database
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Convert user entity to UserProfileDTO
        return convertToUserProfileDTO(user);
    }

    /**
     * Helper method to perform user authentication.
     */
    private Authentication authenticate(String phoneNumber, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(phoneNumber, password)
        );
    }

    /**
     * Helper method to get the phone number of the authenticated user.
     */
    private String getAuthenticatedUserPhoneNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // Return the authenticated user's phone number
    }

    /**
     * Convert User entity to UserProfileDTO.
     */
    private UserProfileDTO convertToUserProfileDTO(User user) {
        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setId(user.getId());
        profileDTO.setName(user.getName());
        profileDTO.setPhoneNumber(user.getPhoneNumber());
        profileDTO.setEmail(user.getEmail());

        return profileDTO;
    }
}
