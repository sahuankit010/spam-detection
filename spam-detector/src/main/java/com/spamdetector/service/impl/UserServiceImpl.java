package com.spamdetector.service.impl;

import com.spamdetector.dto.UserRegistrationDTO;
import com.spamdetector.dto.UserProfileDTO;
import com.spamdetector.entity.User;
import com.spamdetector.repository.UserRepository;
import com.spamdetector.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserProfileDTO registerUser(UserRegistrationDTO registrationDTO) {
        // Check if phone number already exists
        if (userRepository.existsByPhoneNumber(registrationDTO.getPhoneNumber())) {
            throw new RuntimeException("Phone number already registered");
        }

        // Create new user
        User user = new User();
        user.setName(registrationDTO.getName());
        user.setPhoneNumber(registrationDTO.getPhoneNumber());
        user.setEmail(registrationDTO.getEmail());

        // Encode password
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        // Save user
        User savedUser = userRepository.save(user);

        // Convert to DTO
        return convertToProfileDTO(savedUser);
    }

    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToProfileDTO(user);
    }

    @Override
    @Transactional
    public UserProfileDTO updateUserProfile(Long userId, UserProfileDTO profileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(profileDTO.getName());
        user.setEmail(profileDTO.getEmail());

        User updatedUser = userRepository.save(user);
        return convertToProfileDTO(updatedUser);
    }

    @Override
    public boolean checkPhoneNumberAvailability(String phoneNumber) {
        return !userRepository.existsByPhoneNumber(phoneNumber);
    }

    // Helper method to convert User to UserProfileDTO
    private UserProfileDTO convertToProfileDTO(User user) {
        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setId(user.getId());
        profileDTO.setName(user.getName());
        profileDTO.setPhoneNumber(user.getPhoneNumber());
        profileDTO.setEmail(user.getEmail());
        return profileDTO;
    }

    @Override
    public Long getUserIdByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserProfileDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToProfileDTO)
                .collect(Collectors.toList());
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }


}